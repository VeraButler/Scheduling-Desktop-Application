package iluwatar.Interface;

import iluwatar.POJO.CustomerDetails;

import java.util.Optional;
import java.util.stream.Stream;

public interface CustomerDetailsDao {
    /**
     * In an application the Data Access Object (DAO) is a part of Data access layer. It is an object
     * that provides an interface to some type of persistence mechanism. By mapping application calls to
     * the persistence layer, DAO provides some specific data operations without exposing details of the
     * database. This isolation supports the Single responsibility principle. It separates what data
     * accesses the application needs, in terms of domain-specific objects and data types (the public
     * interface of the DAO), from how these needs can be satisfied with a specific DBMS, database
     * schema, etc.
     *
     * <p>Any change in the way data is stored and retrieved will not change the client code as the
     * client will be using interface and need not worry about exact source.
     *
     *  @see InMemoryCustomerDetailsDao
     *  @see DbCustomerDetailsDao
     **/

    /**
     * Get all customersDetails
     *
     * @return all the customers as a stream. The stream may be lazily or eagerly evaluated based on the implementation.
     *         The stream must be closed after use.
     * @throws Exception if any error occurs.
     */
    Stream<CustomerDetails> getAll() throws Exception;

    /**
     * Get customer as Optional by id
     *
     * @param id identifier of the customer.
     * @return an optional with customer if an customer with unique identifier customerId exists, empty optional otherwise.
     * @throws Exception if any error occurs
     */
    Optional<CustomerDetails> getById(int id) throws Exception;

//    /**
//     * Add an customer.
//     *
//     * @param customer the customer to by updated.
//     * @return true if customer exists and is successfully updated, false otherwise.
//     * @throws Exception if any error occurs.
//     */
//    boolean add(Customer customer) throws Exception;
//
//    /**
//     * Update an customer.
//     *
//     * @param customer the customer to be updated
//     * @return true if customer exists and is successfully updated, false otherwise.
//     * @throws Exception if an error occurs
//     */
//    boolean update(Customer customer) throws Exception;
//
//    /**
//     * Delete a customer.
//     *
//     * @param customer the customer to be deleted.
//     * @return true if customer exists and is successfully deleted, false otherwise.
//     * @throws Exception if any error occurs.
//     */
//    boolean delete(Customer customer) throws Exception;
}
