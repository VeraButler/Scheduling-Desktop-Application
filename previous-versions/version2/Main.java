package version2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Database.DBUtils;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
//        Font.loadFont(getClass().getResourceAsStream("../CSS/Fonts/LeagueSpartan-Bold.otf"), 35);
        Parent root = FXMLLoader.load(getClass().getResource(".../view/loginScreenView.fxml"));
        root.getStylesheets().addAll("/previous-versions/CSS/calendarPane.css", "/previous-versions/CSS/loginStyle.css", "/previous-versions/resources/app.css");
        primaryStage.setTitle("ACMECo Scheduling System");
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws ClassNotFoundException {
        // connect to Database
        try {
        DBUtils.startConnection();
        // TODO load all data to master lists
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // launch app
        launch(args);

        // close Database connection
        DBUtils.closeConnection();
    }
}
