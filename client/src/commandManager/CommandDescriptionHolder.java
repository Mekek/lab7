package commandManager;

import commandLogic.CommandDescription_;

import java.util.ArrayList;

public class CommandDescriptionHolder {

    private static CommandDescriptionHolder instance;
    ArrayList<CommandDescription_> commands;

    private CommandDescriptionHolder(ArrayList<CommandDescription_> commands) {
        this.commands = commands;
    }

    public static void initialize(ArrayList<CommandDescription_> commands) {
        instance = new CommandDescriptionHolder(commands);
    }

    public static CommandDescriptionHolder getInstance() {
        return instance;
    }

    public ArrayList<CommandDescription_> getCommands() {
        return commands;
    }
}
