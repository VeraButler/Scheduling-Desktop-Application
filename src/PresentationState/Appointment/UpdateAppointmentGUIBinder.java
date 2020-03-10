package PresentationState.Appointment;


//import org.svenehrke.javafxdemos.PresentationState.infra.JavaFxWidgetBindings;

import PresentationState.infra.JavaFxWidgetBindings;

import java.sql.SQLException;
import java.text.ParseException;

public class UpdateAppointmentGUIBinder {

	private final UpdateAppointmentController controller;
	private final UpdateAppointmentPresentationState presentationState;

	public UpdateAppointmentGUIBinder(UpdateAppointmentController controller, UpdateAppointmentPresentationState presentationState) {
		this.controller = controller;
		this.presentationState = presentationState;
	}

	void bindAndInitialize() throws ParseException, SQLException, ClassNotFoundException {
		presentationState.initBinding();
		initWidgetBinding();
		initActionHandlers();
		presentationState.initData(1);
	}

	private void initWidgetBinding() {
		JavaFxWidgetBindings.bindTextField(controller.idTxt, presentationState.id);
		JavaFxWidgetBindings.bindTextField(controller.customerTxt, presentationState.customer);
		JavaFxWidgetBindings.bindTextField(controller.userIdTxt, presentationState.userId);
		JavaFxWidgetBindings.bindTextField(controller.titleTxt, presentationState.title);
		JavaFxWidgetBindings.bindTextField(controller.descriptionTxt, presentationState.description);
		JavaFxWidgetBindings.bindTextField(controller.locationTxt, presentationState.location);
		JavaFxWidgetBindings.bindTextField(controller.contactTxt, presentationState.customerContact);
		JavaFxWidgetBindings.bindTextField(controller.typeTxt, presentationState.type);
		JavaFxWidgetBindings.bindTextField(controller.urlTxt, presentationState.url);
		JavaFxWidgetBindings.bindTextField(controller.startDateTxt, presentationState.startTime);
		JavaFxWidgetBindings.bindTextField(controller.endDateTxt, presentationState.endTime);
//		JavaFxWidgetBindings.bindLabel(controller.greetingLabel, presentationState.greeting);
	}

	private void initActionHandlers() {
		JavaFxWidgetBindings.bindButton(controller.updateButton, UpdateAppointmentActionHandlers.updateButtonHandler(presentationState));
	}
}
