package ua.com.juja.sqlcmd.controller.launcher;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.controller.exception.ExitException;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private final View view;
    private final Command[] commands;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
                new Create(manager, view),
                new Insert(manager, view),
                new Update(manager, view),
                new Clear(manager, view),
                new Tables(manager, view),
                new Find(manager, view),
                new Drop(manager, view),
                new Delete(manager, view),
                new Unsupported(view)};
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }

    public void doWork() {
        view.write("Hello user!!!");
        view.write("Please enter the database name, " +
                "username and password in the format connect|databaseName|userName|password");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw e;
                    }
                    printError(e);
                    break;
                }
            }
            view.write("Enter an existing command (or command 'help' for help)");
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += " " + e.getCause().getMessage();
        }
        view.write("Failure for a reason: " + message);
        view.write("Try again!!!");
    }
}
