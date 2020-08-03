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
        view.write("\tfind|tableName");
        view.write("\t\t-to get the contents of the table 'tableName'");
        view.write("\texit");
        view.write("\t\t-for exit with database");
    }
}
