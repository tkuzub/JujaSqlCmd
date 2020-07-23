package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connectToDB();
    }

    private void connectToDB() {
        view.write("Hello user!!!");
        view.write("Please enter the database name, username and password in the format databaseName|userName|password");

        while (true) {
            String string = view.read();
            String[] data = string.split("\\|");

            String databaseName = data[0];
            String userName = data[1];
            String password = data[2];

            try {
                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                String massage = e.getMessage();
                if (e.getCause() != null) {
                    massage += " " + e.getCause().getMessage();
                }
                view.write("FAIL for a cause " + massage);
                view.write("Try again!!!");
            }
        }
        view.write("Success!!!");
    }
}
