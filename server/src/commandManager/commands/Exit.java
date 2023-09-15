package commandManager.commands;

import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

/**
 * Terminates the application (without saving collection).
 *
 * @author Mekek
 * @since 1.0
 */
public class Exit implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.exit");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescr() {
        return "Terminates the application. Invoke server-side collection saving.";
    }

    @Override
    public void execute(String[] args) {
        logger.trace("Invoked exit command. Saving a collection...");
        logger.info("Someone is disconnected... Saving a collection...");
        Save saveCommand = new Save();
        saveCommand.execute(new String[0]);
        response = CommandStatusResponse.ofString("Exiting the program!");
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
