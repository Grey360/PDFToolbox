package main.java.main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/pdf.fxml"));
        primaryStage.setTitle("Hello world !");
        primaryStage.setScene(new Scene(root,1024,768));
        primaryStage.setResizable(false);
        primaryStage.show();
        /*
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
            }
        });*/
    }



    public static void main(String[] args) {

        launch(args);
    }
}
