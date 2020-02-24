package MVC.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class MainScreenController {
    // Stage & Scene
    Stage stage;
    Parent scene;

    // Event Handlers

    public TabPane mainTabPane;

    public MainScreenController() throws Exception {
    }

    private void setTabs() throws ParseException, SQLException, ClassNotFoundException, IOException {
        Tab calendar = new Tab("Calendar");
        calendar.setContent(FXMLLoader.load(this.getClass().getResource("/MVC/view/calendarMonthView.fxml")));

        // Weekly View on My Week Tab
        Tab weekly = new Tab("My Week");
        weekly.setContent(FXMLLoader.load(this.getClass().getResource("/MVC/view/weeklyView.fxml")));

        // Customer View on Customers Tab
        Tab customers = new Tab("Customers");

        mainTabPane.getTabs().addAll(calendar, weekly, customers);
    }

//    @FXML
//    private void onActionExit(ActionEvent actionEvent) {
//        System.exit(0);
//    }

    @FXML
    public void initialize() throws Exception {
        setTabs();
    }
}
