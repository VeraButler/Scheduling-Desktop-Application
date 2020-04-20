package PresentationState.Appointment;

import DAO.AppointmentDao;
import DAO.CustomerDao;
import DAO.POJO.Appointment;
import DAO.POJO.Customer;
import iluwatar.DbDao.DbAppointmentDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Database.DBUtils;
import utils.DateTime.DateTimeUtils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * PresentationState cannot only hold real data but also meta data used for the presentation.
 * For example it could hold information about validation results, mandatory, enabled or disabled fields etc.
 * depending on certain rules. The PresentationState then will contain the result of applying these rules.
 */

public class UpdateAppointmentPresentationState {
	//get appointment information
	Appointment appointment;

	public final StringProperty id = new SimpleStringProperty();
	public final StringProperty customer = new SimpleStringProperty();
	public final StringProperty customerContact = new SimpleStringProperty();

	public final StringProperty userId = new SimpleStringProperty();
	public final StringProperty title = new SimpleStringProperty();
	public final StringProperty description = new SimpleStringProperty();
	public final StringProperty location = new SimpleStringProperty();
	public final StringProperty type = new SimpleStringProperty();
	public final StringProperty url = new SimpleStringProperty();
	public final StringProperty startTime = new SimpleStringProperty();
	public final StringProperty startDate = new SimpleStringProperty();
	public final StringProperty endDate = new SimpleStringProperty();
	public final StringProperty endTime = new SimpleStringProperty();

	public ObservableList<Integer> ids = FXCollections.observableArrayList();

	public void initBinding() {
		/**
		 * The purpose of initBinding() is to bind internal fields of PresentationState to each other
		 * (e.g. customerName automatically changes when appointmentId changes).
		 */
		startDate.addListener((observable, oldValue, newValue) -> {
//			startDate.getValue();
//			datePicker.getEditor().getText();
			System.out.println(oldValue);
			System.out.println(newValue);
		});


	}

	/**
	 * USE - initialize the presentation state
	 *
	 * @param appointmentId - passed from GUIBinder bindAndInitialize
	 * @throws ParseException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	public void initData(int appointmentId) throws Exception {
		if(ids.isEmpty()) {
			setIds();
		}
		appointment = AppointmentDao.getAppointment(appointmentId);
		// get customerName
		assert appointment != null;
		Customer cust = CustomerDao.getCustomerById(appointment.getCustomerId());

		id.setValue(String.valueOf(appointment.getAppointmentId()));

		assert cust != null;
		customer.setValue(String.valueOf(cust.getName()));
		userId.setValue(String.valueOf(appointment.getUserId()));
		customerContact.setValue(String.valueOf(appointment.getContact()));
		title.setValue(String.valueOf(appointment.getTitle()));
		description.setValue(String.valueOf(appointment.getDescription()));
		location.setValue(String.valueOf(appointment.getLocation()));
		url.setValue(String.valueOf(appointment.getUrl()));

		// DateTime elements
		// format YYYY/MM/DD

		Calendar startCal = appointment.getStart();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(startCal.getTime());
		startDate.setValue(dateStr);

		Calendar endCal = appointment.getEnd();
		dateStr = format.format(endCal.getTime());
		endDate.setValue(dateStr);
		// format HH:mm
		startTime.setValue(DateTimeUtils.getHoursAndMinutes(appointment.getStart()));
		endTime.setValue(DateTimeUtils.getHoursAndMinutes(appointment.getEnd()));

	}


	private void newAppointment() {
		customer.setValue("");
		customerContact.setValue("");
		userId.setValue("1");
		title.setValue("");
		description.setValue("");
		location.setValue("");
		type.setValue("");
		url.setValue("");


		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		startTime.setValue(dtf.format(now));
		endTime.setValue(dtf.format(now.plusHours(1)));

		dtf = DateTimeFormatter.ofPattern("YYYY-mm-dd");

		startDate.setValue(dtf.format(now));
		endDate.setValue(dtf.format(now));
	}

	private void setIds() throws Exception {
		Stream<iluwatar.POJO.Appointment> appointmentStream = new DbAppointmentDao(DBUtils.getMySQLDataSource()).getAll();
		appointmentStream.forEach(a->{
			ids.add(a.getId());
		});
	}
}
