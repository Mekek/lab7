package commandManager.commands;

import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;


/**
 * Saves collection to file.
 *
 * @author Mekek
 * @since 1.0
 */
public class Save implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.save");
    private CommandStatusResponse_ response;

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
        TicketHandler_ writer = TicketHandler_.getInstance();
        response = CommandStatusResponse_.ofString("[Server] Collection saving executing...\nsize of collection to write: " + writer.getCollection().size());
        logger.info(response.getResponse());
        writer.writeCollection();
        response = CommandStatusResponse_.ofString("[Server] Collection saving executed.\nsize of written collection: " + writer.getCollection().size());
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
