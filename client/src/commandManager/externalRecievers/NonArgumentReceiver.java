package commandManager.externalRecievers;

import commandLogic.CommandDescription_;
import commandLogic.commandReceiverLogic.receivers.ExternalBaseReceiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requestLogic.requestSenders.CommandRequestSender;
import responses.CommandStatusResponse_;
import serverLogic.ServerConnectionHandler;

public class NonArgumentReceiver implements ExternalBaseReceiver {

    private static final Logger logger = LogManager.getLogger("com.github.Mekek.lab6");

    @Override
    public boolean receiveCommand(CommandDescription_ command, String[] args) {
        CommandStatusResponse_ response = new CommandRequestSender().sendCommand(command, args, ServerConnectionHandler.getCurrentConnection());
        if (response != null) {
            logger.info("Status code: " + response.getStatusCode());
            logger.info("Response: \n" + response.getResponse());
            return true;
        }
        return false;
    }
}
