package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.exception.ExitException;
import ua.com.juja.sqlcmd.view.View;

public class Exit implements Command {

    private final View view;

    public Exit(View view) {
        this.view = view;
    }

    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    public void process(String command) {
        view.write("Good bay!!!");
        throw new ExitException();
    }
}
