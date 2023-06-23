package commandManager;

import commandLogic.CommandDescription_;
import requestLogic.requestSenders.CommandDescriptionsRequestSender;

import java.util.ArrayList;

public class CommandLoaderUtility {
    public static void initializeCommands() {
        ArrayList<CommandDescription_> commands = new CommandDescriptionsRequestSender().sendRequestAndGetCommands();
        CommandDescriptionHolder.initialize(commands);
    }
}
