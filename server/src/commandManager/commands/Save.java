package commandManager.commands;

import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;


/**
 * Saves collection to file.
 *
 * @author Mekek
 * @since 1.0
 */
public class Save implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.save");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescr() {
        return "Deprecated (for server-use only).";
    }

    @Override
    public void execute(String[] args) {
        logger.trace("Saving...");
        TicketHandler writer = TicketHandler.getInstance();
        int collectionSize = writer.getCollection().size();

        response = CommandStatusResponse.ofString("[Server] Collection saving executing...\nsize of collection to write: " + collectionSize);
        logger.info(response.getResponse());

        writer.writeCollectionToDatabase();

        response = CommandStatusResponse.ofString("[Server] Collection saving executed.\nsize of written collection: " + collectionSize);
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
