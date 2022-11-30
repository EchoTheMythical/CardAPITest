package com.example.apitest;
import javafx.application.Application;
import static com.example.apitest.HelloController.*;
import static functions.functions.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        String usrIn = getStr();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < usrIn.length(); i++) {
            if (usrIn.charAt(i) == ' ') temp.append("%20");
            else temp.append(usrIn.charAt(i));
        }
        requestCard(temp.toString()).printList();
        launch();
    }
}