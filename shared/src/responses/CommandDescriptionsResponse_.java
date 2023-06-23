package responses;

import commandLogic.CommandDescription_;

import java.util.ArrayList;

public class CommandDescriptionsResponse_ extends BaseResponse_ {
    private final ArrayList<CommandDescription_> commands;

    public CommandDescriptionsResponse_(ArrayList<CommandDescription_> commands) {
        this.commands = commands;
    }

    public ArrayList<CommandDescription_> getCommands() {
        return commands;
    }
}
