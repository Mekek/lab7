package commandManager.externalRecievers;

import commandLogic.CommandDescription_;
import commandLogic.commandReceiverLogic.receivers.ExternalArgumentReceiver;
import exceptions.BuildObjectException;
import models.Ticket;
import models.handlers.ModeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requestLogic.requestSenders.ArgumentRequestSender;
import responses.CommandStatusResponse_;
import serverLogic.ServerConnectionHandler;

public class ArgumentTicketCommandReceiver implements ExternalArgumentReceiver<Ticket> {

    private static final Logger logger = LogManager.getLogger("com.github.Mekek.lab6");
    ModeManager<Ticket> modeManager;
    Ticket ticket;

    {
        ticket = new Ticket();
    }

    public ArgumentTicketCommandReceiver(ModeManager<Ticket> modeManager) {
        this.modeManager = modeManager;
    }

    @Override
    public boolean receiveCommand(CommandDescription_ command, String[] args) throws BuildObjectException {
        ticket = modeManager.buildObject();
        CommandStatusResponse_ response = new ArgumentRequestSender<Ticket>().sendCommand(command, args, ticket, ServerConnectionHandler.getCurrentConnection());
        if (response != null) {
            logger.info("Status code: " + response.getStatusCode());
            logger.info("Response: \n" + response.getResponse());
            return true;
        }
        return false;
    }

    @Override
    public Ticket getArguemnt() {
        return ticket;
    }
}
