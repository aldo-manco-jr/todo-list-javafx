package org.aldomancojr.todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.aldomancojr.todolist.data_model.ToDoData;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        primaryStage.setTitle("ToDo List");
        primaryStage.setScene(new Scene(root, 900, 504));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        try {
            ToDoData.getSingleton().loadToDoItems();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        try {
            ToDoData.getSingleton().storeToDoItems();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
