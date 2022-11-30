package com.example.apitest;
import javafx.application.Application;
import static functions.functions.*;
import static com.example.apitest.HelloController.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.gson.*;
import java.net.http.*;
import java.net.URI;
import java.util.*;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
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
        CardInfo testcard = requestCard(temp.toString());
        for(int i  = 0; i < testcard.data.size(); i++) print(testcard.data.get(i).id + "\n" + testcard.data.get(i).name + "\n" + testcard.data.get(i).type + "\n" + testcard.data.get(i).desc + "\n" + testcard.data.get(i).race + "\n" + testcard.data.get(i).attribute + "\n");
        launch();
    }
}