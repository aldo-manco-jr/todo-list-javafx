package org.aldomancojr.todolist.data_model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

/**
 * Created by unieuro on 7/22/2019.
 */
public class ToDoData {
    private static ToDoData singleton = new ToDoData();
    private ObservableList<ToDoItem> listToDoItems;
    private String filename = "ToDoListItems.txt";
    private DateTimeFormatter datetime;

    private ToDoData() {
        datetime = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static ToDoData getSingleton() {
        return singleton;
    }

    public ObservableList<ToDoItem> getListToDoItems() {
        return listToDoItems;
    }

    /*public void setListToDoItems(List<ToDoItem> listToDoItems) {
        this.listToDoItems = listToDoItems;
    }*/

    public void loadToDoItems() throws IOException{
        listToDoItems = FXCollections.observableArrayList();
        Path filepath = Paths.get(filename);
        BufferedReader buffer = Files.newBufferedReader(filepath);
        String input;

        try {
            while ((input = buffer.readLine()) != null){
                String[] data = input.split("\t");
                ToDoItem tmp = new ToDoItem(data[0], data[1], LocalDate.parse(data[2], datetime));
                listToDoItems.add(tmp);
            }
        }finally {
            if (buffer!=null){
                buffer.close();
            }
        }
    }

    public void storeToDoItems() throws IOException{
        Path filepath = Paths.get(filename);
        BufferedWriter buffer = Files.newBufferedWriter(filepath);

        try {
            Iterator<ToDoItem> iterator = listToDoItems.iterator();
            while (iterator.hasNext()){
                ToDoItem tmp = iterator.next();
                buffer.write(String.format("%s\t%s\t%s", tmp.getShortDescription(), tmp.getDetails(), tmp.getDeadline().format(datetime)));
                buffer.newLine();
            }
        }finally {
            if (buffer!=null){
                buffer.close();
            }
        }
    }

    public void addToDoItem(ToDoItem item){
        listToDoItems.add(item);
    }

    public void deleteToDoItem(ToDoItem item){
        listToDoItems.remove(item);
    }
}
