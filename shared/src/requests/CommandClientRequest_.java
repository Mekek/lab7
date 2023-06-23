package requests;

import commandLogic.CommandDescription_;

public class CommandClientRequest_ extends BaseRequest_ {
    private final CommandDescription_ command;
    private final String[] lineArgs;

    public CommandClientRequest_(CommandDescription_ command, String[] lineArgs) {
        this.command = command;
        this.lineArgs = lineArgs;
    }

    public CommandDescription_ getCommandDescription() {
        return command;
    }

    public String[] getLineArgs() {
        return lineArgs;
    }
}
