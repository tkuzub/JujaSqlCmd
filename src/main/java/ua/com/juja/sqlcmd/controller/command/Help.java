package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

public class Help implements Command {
    private final View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Existing teams: ");

        view.write("\tconnect|databaseName|userName|password");
        view.write("\t\t-for connect to database");

        view.write("\thelp");
        view.write("\t\t-for to display all existing commands on the screen");

        view.write("\ttables");
        view.write("\t\t-show a list of all tables of the database to which they are connected");

        view.write("\tcreate|tableName|column1|column2|...|columnN");
        view.write("\t\t-the command creates a new table with the given fields");

        view.write("\tfind|tableName");
        view.write("\t\t-to get the contents of the table 'tableName'");

        view.write("\tinsert|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\t-to insert one row into a given 'tableName'");

        view.write("\tupdate|tableName|column1|value1|column2|value2");
        view.write("\t\t-to update the entry in the specified table by setting the value");

        view.write("\tdelete|tableName|column|value");
        view.write("\t\t-to deletes one or more records in the table for which the column = value condition is met");

        view.write("\tclear|tableName");
        view.write("\t\t-to clear all data from the 'tableName'");

        view.write("\tdrop|tableName");
        view.write("\t\t-to drop a 'tableName'");

        view.write("\texit");
        view.write("\t\t-for exit with database");
    }
}
