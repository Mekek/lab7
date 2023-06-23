package commandManager.commands;

import responses.CommandStatusResponse_;

public class ExecuteScript implements Command {
    private CommandStatusResponse_ response;
    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescr() {
        return "Reads and executes script from file.";
    }

    @Override
    public String getArgs() {
        return "file_path";
    }

    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        response = CommandStatusResponse_.ofString("Server is alive and ready for command executing!");
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
