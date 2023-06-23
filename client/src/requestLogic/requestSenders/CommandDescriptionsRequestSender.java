package requestLogic.requestSenders;

import commandLogic.CommandDescription_;
import exceptions.NotAvailableServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.CommandDescriptionsRequest_;
import responses.CommandDescriptionsResponse_;
import serverLogic.ServerConnectionHandler;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.ArrayList;

public class CommandDescriptionsRequestSender {

    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    public ArrayList<CommandDescription_> sendRequestAndGetCommands() {
        ArrayList<CommandDescription_> result = null;

        CommandDescriptionsRequest_ request = new CommandDescriptionsRequest_();

        try {
            CommandDescriptionsResponse_ response = (CommandDescriptionsResponse_) new RequestSender().sendRequest(request, ServerConnectionHandler.getCurrentConnection());
            result = response.getCommands();
        } catch (PortUnreachableException e) {
            logger.fatal("Server is unavailable. Please, wait until server will came back.");
            logger.fatal("We can't get available commands because the server is unavailable.");
        } catch (NotAvailableServerException e) {
            logger.fatal("Server is busy. Please, wait until server will came back.");
            logger.fatal("We can't get available commands because the server is busy.");
        } catch (IOException e) {
            logger.error("Something went wrong during I/O operations.");
        }

        return result;
    }
}
