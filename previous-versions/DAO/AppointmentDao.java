package DAO;

import DAO.POJO.Appointment;
import MVC.model.CalendarMonthModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Database.DBUtils;
import utils.Database.QueryUtils;
import utils.DateTime.DateTimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class AppointmentDao {
    // TODO: TEST APPOINTMENT CLASS
    // flag for active XXX
    private static boolean activeBool;
    private static String sqlStatement;
    private int month;
    private int year;

    // methods for dynamic creation of TableViews and args for DaoMethods.add()
    public static ObservableList<String> getAppointmentColumns() throws SQLException, ClassNotFoundException {
        sqlStatement = "SELECT * FROM appointment";
        try {
            return DAO.getColumnNames(sqlStatement);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<String> getAppointmentColumnValues(String query) throws SQLException, ClassNotFoundException {
        sqlStatement = "SELECT * FROM appointment";

        try {
            return DAO.getColumnValues(sqlStatement);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // get, update, delete, add
    public static Appointment getAppointment(int appointmentId) throws ClassNotFoundException, SQLException, ParseException {
        String sqlStatement = "select * FROM appointment WHERE appointmentId  = '" + appointmentId + "'";

        Appointment appointmentResult;
        ResultSet result = DAO.getResultSet(sqlStatement);
        while (result.next()) {
            int appointmnetIdG = result.getInt("appointmentId");
            int customerId  = result.getInt("customerId");
            int userId = result.getInt("userId");
            String title = result.getString("title");
            String description = result.getString("description");
            String location = result.getString("location");
            String contact = result.getString("contact");
            String type = result.getString("type");
            String url = result.getString("url");
            String start = result.getString("start");
            String end = result.getString("end");
            String createDate = result.getString("createDate");
            String createdBy = result.getString("createdBy");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");

            Calendar startCalendar = DateTimeUtils.stringToCalendar(start);
            Calendar endCalendar = DateTimeUtils.stringToCalendar(end);
            Calendar createDateCalendar = DateTimeUtils.stringToCalendar(createDate);
            Calendar lastUpdateCalendar = DateTimeUtils.stringToCalendar(lastUpdate);
            appointmentResult = new Appointment(appointmnetIdG, customerId, userId, title, description, location, contact, type, url, startCalendar, endCalendar, createDateCalendar, createdBy, lastUpdateCalendar, lastUpdateby);
            return appointmentResult;
        }
        DBUtils.closeConnection();
        return null;
    }

    public static ObservableList<Appointment> getAllAppointments() throws ClassNotFoundException, SQLException, ParseException {
        sqlStatement = "select * from appointment";

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet result = DAO.getResultSet(sqlStatement);
        while (result.next()) {
            int appointmnetIdG = result.getInt("appointmentId");
            int customerId  = result.getInt("customerId");
            int userId = result.getInt("userId");
            String title = result.getString("title");
            String description = result.getString("description");
            String location = result.getString("location");
            String contact = result.getString("contact");
            String type = result.getString("type");
            String url = result.getString("url");
            String start = result.getString("start");
            String end = result.getString("end");

            String createDate = result.getString("createDate");
            String createdBy = result.getString("createdBy");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");

            Calendar startCalendar = DateTimeUtils.stringToCalendar(start);
            Calendar endCalendar = DateTimeUtils.stringToCalendar(end);
            Calendar createDateCalendar = DateTimeUtils.stringToCalendar(createDate);
            Calendar lastUpdateCalendar = DateTimeUtils.stringToCalendar(lastUpdate);
            Appointment a = new Appointment(appointmnetIdG, customerId, userId, title, description, location, contact, type, url, startCalendar, endCalendar, createDateCalendar, createdBy, lastUpdateCalendar, lastUpdateby);
            allAppointments.add(a);
        }
        DBUtils.closeConnection();
        return allAppointments;
    }


    public static ObservableList<Appointment> getAppointmentsWithinMonth(int year, int month) throws SQLException, ParseException {
        String yearString = String.valueOf(year);
        String monthString = String.format("%02d", month + 1);

        sqlStatement = "SELECT * FROM appointment WHERE start >= " + "'" + yearString + "-" + monthString + "-01 00:00:00' AND end <= " + "'" + yearString + "-" + monthString + "-30 12:59:59'";
        ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
        ResultSet result = null;
        try {
            result = DAO.getResultSet(sqlStatement);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assert result != null;
        while (result.next()) {
            int appointmnetIdG = result.getInt("appointmentId");
            int customerId = result.getInt("customerId");
            int userId = result.getInt("userId");
            String title = result.getString("title");
            String description = result.getString("description");
            String location = result.getString("location");
            String contact = result.getString("contact");
            String type = result.getString("type");
            String url = result.getString("url");
            String start = result.getString("start");
            String end = result.getString("end");

            String createDate = result.getString("createDate");
            String createdBy = result.getString("createdBy");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");

            Calendar startCalendar = DateTimeUtils.stringToCalendar(start);
            Calendar endCalendar = DateTimeUtils.stringToCalendar(end);
            Calendar createDateCalendar = DateTimeUtils.stringToCalendar(createDate);
            Calendar lastUpdateCalendar = DateTimeUtils.stringToCalendar(lastUpdate);
            Appointment a = new Appointment(appointmnetIdG, customerId, userId, title, description, location, contact, type, url, startCalendar, endCalendar, createDateCalendar, createdBy, lastUpdateCalendar, lastUpdateby);
            monthAppointments.add(a);
//            Calendar startDate = a.getStart();
//            int startMonth = startDate.get(Calendar.DAY_OF_MONTH);
//            int startDay = startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);
//            System.out.println("a start: " + startMonth + startDay);
        }
        return monthAppointments;
    }
    public static ObservableList<Appointment> getAppointmentsWithinMonth(CalendarMonthModel calendar) throws SQLException, ParseException {
        String year = String.valueOf(calendar.getCurrentYear());
        int month = calendar.getCurrentMonth();
        String monthString = String.format("%02d", month + 1);

        sqlStatement = "SELECT * FROM appointment WHERE start >= " + "'" + year + "-" + monthString + "-01 00:00:00' AND end <= " + "'" + year + "-" + monthString + "-30 12:59:59'";
        ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
        ResultSet result = null;
        try {
            result = DAO.getResultSet(sqlStatement);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assert result != null;
        while (result.next()) {
            int appointmnetIdG = result.getInt("appointmentId");
            int customerId = result.getInt("customerId");
            int userId = result.getInt("userId");
            String title = result.getString("title");
            String description = result.getString("description");
            String location = result.getString("location");
            String contact = result.getString("contact");
            String type = result.getString("type");
            String url = result.getString("url");
            String start = result.getString("start");
            String end = result.getString("end");

            String createDate = result.getString("createDate");
            String createdBy = result.getString("createdBy");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");

            Calendar startCalendar = DateTimeUtils.stringToCalendar(start);
            Calendar endCalendar = DateTimeUtils.stringToCalendar(end);
            Calendar createDateCalendar = DateTimeUtils.stringToCalendar(createDate);
            Calendar lastUpdateCalendar = DateTimeUtils.stringToCalendar(lastUpdate);
            Appointment a = new Appointment(appointmnetIdG, customerId, userId, title, description, location, contact, type, url, startCalendar, endCalendar, createDateCalendar, createdBy, lastUpdateCalendar, lastUpdateby);
            monthAppointments.add(a);
//            Calendar startDate = a.getStart();
//            int startMonth = startDate.get(Calendar.DAY_OF_MONTH);
//            int startDay = startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);
//            System.out.println("a start: " + startMonth + startDay);
        }
        return monthAppointments;
    }

    public static void updateAppointmentWithString(String updateCol, String setColValue, int rowId) throws ClassNotFoundException {
        try {
            // TODO format date strings to Calendar
            // UPDATE `table_name` SET `column_name` = `new_value' [WHERE condition];
            // TODO: check colVal type....if int without single quotes, else with single quotes (like below)
            sqlStatement = "UPDATE appointment SET " + updateCol + " = '" + setColValue + "' WHERE appointmentId = " + rowId;
            DBUtils.startConnection();
            QueryUtils.createQuery(sqlStatement);
            ResultSet result = QueryUtils.getResult();
        } catch (ClassNotFoundException e) {
            System.out.println("ApptDao UPDATE CLASS NOT FOUND");
            e.getException();
        }
    }

    public static void updateAppointmentDate(String updateCol, String setColValue, int rowId) throws SQLException {
        try {
            // TODO format date strings to Calendar

//            Calendar colValue = DateTimeUtils.stringToCalendar(setColValue);
            // UPDATE `table_name` SET `column_name` = `new_value' [WHERE condition];
            // TODO: check colVal type....if int without single quotes, else with single quotes (like below)
            sqlStatement = "UPDATE appointment SET " + updateCol + " = '" + setColValue + "' WHERE appointmentId = " + rowId;
            DBUtils.startConnection();
            //execute query
            QueryUtils.createQuery(sqlStatement);
            // test print results
            System.out.println(Objects.requireNonNull(getAppointment(rowId)).getStart().getTime());
        } catch (ClassNotFoundException e) {
            System.out.println("ApptDao UPDATE CLASS NOT FOUND");
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(int rowId) {
        // Todo: POPUP "Are you sure? This is permanent."
        //DELETE FROM `table_name` [WHERE condition];
        sqlStatement = "DELETE FROM appointment WHERE appointmentId = " + rowId;
    }

    public static void addAppointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, String startDate, String endDate,  String createdBy,String lastUpdate, String lastUpdateBy) throws ClassNotFoundException, SQLException {
        // INSERT INTO `table_name`(column_1,column_2,...) VALUES (value_1,value_2,...);
        sqlStatement = "INSERT INTO appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy) " +
                "VALUES (" + appointmentId + ", " + customerId + ", " +  userId + ", '" + title + "' , '" + description + "' , '" + location + "' , '" + contact + "' , '" + type + "' , '" + url + "' , " + startDate + ", " + endDate + ", '" + createdBy + "' , " + lastUpdate + ", '" + lastUpdateBy + "')";
        System.out.println(sqlStatement);
        ResultSet result = DAO.getResultSet(sqlStatement);
    }
}
