package iluwatar.DbDao;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import iluwatar.CustomException;
import iluwatar.Interface.CustomerDao;
import iluwatar.POJO.Customer;
import iluwatar.POJO.CustomerDetails;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An implementation of {@link CustomerDao} that persists customers in RDBMS.
 */
public class DbCustomerDao implements CustomerDao {
//TODO fix logger using Logger.util class
//    private static final Logger LOGGER = LoggerFactory.getLogger(DbCustomerDao.class);

    private final DataSource dataSource;

    /**
     * Creates an instance of {@link DbCustomerDao} which uses provided dataSource to
     * store and retrieve customer information.
     *
     * @param dataSource a non-null dataSource.
     */
    public DbCustomerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Get all customers as Java Stream.
     *
     * @return a lazily populated stream of customers. Note the stream returned must be closed to free
     *     all the acquired resources. The stream keeps an open connection to the database till it is
     *     complete or is closed manually.
     */
    @Override
    public Stream<Customer> getAll() throws Exception {
        try {
            var connection = getConnection();
            PreparedStatement statement = (PreparedStatement) connection.prepareStatement("SELECT * FROM customer");
            var resultSet = statement.executeQuery(); // NOSONAR
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<Customer>(Long.MAX_VALUE,
                    Spliterator.ORDERED) {

                @Override
                public boolean tryAdvance(Consumer<? super Customer> action) {
                    try {
                        if (!resultSet.next()) {
                            return false;
                        }
                        action.accept(createCustomer(resultSet));
                        return true;
                    } catch (SQLException e) {
                        throw new RuntimeException(e); // NOSONAR
                    }
                }
            }, false).onClose(() -> mutedClose(connection, statement, resultSet));
        } catch (SQLException e) {
            throw new CustomException(e.getMessage(), e);
        }
    }

        private Connection getConnection() throws SQLException {
            return (Connection) dataSource.getConnection();
        }

    private void mutedClose(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            //TODO fix logger
//            LOGGER.info("Exception thrown " + e.getMessage());
        }
    }

    private Customer createCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(resultSet.getInt("customerId"),
                resultSet.getString("customerName"),
                resultSet.getInt("addressId"),
                resultSet.getBoolean("active"),
                String.valueOf(resultSet.getDate("createDate")),
                resultSet.getString("createdBy"),
                String.valueOf(resultSet.getDate("lastUpdate")),
                resultSet.getString("lastUpdateBy"));
    }

    public int maxId() throws CustomException, SQLException {
        ResultSet resultSet = null;

        try (var connection = getConnection();
             var statement = connection.prepareStatement("SELECT max(customerId) FROM customer")) {
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            throw new CustomException(ex.getMessage(), ex);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> getById(int id) throws Exception {

        ResultSet resultSet = null;

        try (var connection = getConnection();
             var statement = connection.prepareStatement("SELECT * FROM customer WHERE customerId = ?")) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createCustomer(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new CustomException(ex.getMessage(), ex);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Customer customer) throws Exception {
        if (getById(customer.getId()).isPresent()) {
            return false;
        }
        try (var connection = getConnection();
             // int id, String customerName, int addressId, boolean active, String createDate,
             // String createdBy, String lastUpdate, String lastUpdateBy
             //-- CALL new_customer('name', 'address1', 'address2', 'Quebec', '88888', '555-5555', 1);
             var statement = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?,?,?,?)")) {
            // dates to strings
            // TODO fix types for calendar - must be in 'YYYY-MM-DD 00:00:00'
            // set customerId to null bc of auto_increment on table
            statement.setInt(1, customer.getId());
            statement.setString(2, customer.getCustomerName());
            statement.setInt(3, customer.getAddressId());
            if (customer.isActive()) {
                statement.setInt(4, 1);
            } else {
                statement.setInt(4, 0);
            }
            statement.setString(5, "NOW()");
            statement.setString(6, "test");
            statement.setString(7, "NOW()");
            statement.setString(8, "test");

            // customer isActive is always set to 1
            statement.execute();
            return true;
        } catch (SQLException ex) {
            throw new CustomException(ex.getMessage(), ex);
        }
    }

    // new customer with customer details view
        public boolean add(CustomerDetails customer) throws Exception {
            if (getById(customer.getId()).isPresent()) {
                return false;
            }
            try (var connection = getConnection();
                 // var statement = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?,?,?,?)"))
                 //-- CALL new_customer('name', 'address1', 'address2', 'Quebec', '88888', '555-5555', 1);
                 var statement = connection.prepareStatement("CALL new_customer(?, ?, ?, ?, ?, ?, 1)")){
                // dates to strings
                // TODO fix types for calendar - must be in 'YYYY-MM-DD 00:00:00'
                // set customerId to null bc of auto_increment on table
                statement.setString(1, customer.getName());
                statement.setString(2, customer.getAddress1());
                statement.setString(3, customer.getAddress2());
                statement.setString(4, customer.getCity());
                statement.setString(5, customer.getPostalCode());
                statement.setString(6, customer.getPhone());
                // customer isActive is always set to 1
                statement.execute();
                return true;
            } catch (SQLException ex) {
                throw new CustomException(ex.getMessage(), ex);
            }
    }

    /**
     * {@inheritDoc}
     */
    // overloaded function for customerDetails
    public boolean update(CustomerDetails customer) throws CustomException, SQLException {
        try (var connection = getConnection();
             CallableStatement statement =
                     connection
                             .prepareCall("{call update_customer(?,?,?,?,?,?,?,?)}")) {
            // INT:customer id
            statement.setInt(1, customer.getId());
            // STRING:name
            statement.setString(2, customer.getName());
            //STRING:address1
            statement.setString(3, customer.getAddress1());
            //STRING:address2
            statement.setString(4,customer.getAddress2());
            //STRING:city
            statement.setString(5, customer.getCity());
            //STRING:postal code
            statement.setString(6, customer.getPostalCode());
            //STRING:phone
            statement.setString(7, customer.getPostalCode());
            //INT:active
            statement.setInt(8, 1);
            statement.closeOnCompletion();
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new CustomException(ex.getMessage(), ex);
        }
    }
    @Override
    public boolean update(Customer customer) throws Exception {
        try (var connection = getConnection();
             var statement =
                     connection
                             .prepareStatement("UPDATE customer SET " +
                                     "customerName = ? " +
                                     "addressId = ?" +
                                     "active = ?" +
                                     "WHERE customerId = ?")) {
            //TODO add all updates you would like to make based on UI
            statement.setString(1, customer.getCustomerName());
            statement.setInt(2, customer.getAddressId());
            statement.setInt(3, 1);
            statement.setInt(4, customer.getId());
            statement.closeOnCompletion();
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new CustomException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Customer customer) throws Exception {
        try (var connection = getConnection();
             var statement = connection.prepareStatement("DELETE FROM customer WHERE customerId = ?")) {
            statement.setInt(1, customer.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new CustomException(ex.getMessage(), ex);
        }
    }

    private int getAddressId(CustomerDetails customer) throws SQLException {
        var connection = getConnection();
        var addressIdStatement = connection.prepareStatement("SELECT addressId from address WHERE address = ? AND address2 = ? limit 1");
        addressIdStatement.setString(1, customer.getAddress1());
        addressIdStatement.setString(2, customer.getAddress2());
        ResultSet rs = addressIdStatement.executeQuery();
        if(rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }


    private int getCityId(CustomerDetails customer) throws SQLException {
        var connection = getConnection();
        var addressIdStatement = connection.prepareStatement("SELECT cityId from city WHERE city = " + customer.getCity() +
                "AND address2 = " + customer.getAddress2());
        ResultSet rs = addressIdStatement.executeQuery();
        if(rs.next()) {
            return rs.getInt(0);
        }
        return -1;
    }

}
