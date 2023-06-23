package commandManager;

import commandLogic.CommandDescription_;
import commandManager.commands.*;
import exceptions.UnknownCommandException_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Command Manager for interactive collection manage.
 *
 * @author Mekek
 * @since 1.0
 */
public class ServerCommandManager_ {

    private static final Logger logger = LogManager.getLogger("com.github.Mekek.lab6");
    final LinkedHashMap<String, Command> serverCommands;

    /**
     * Setup command manager and all of its commands.
     */
    public ServerCommandManager_() {
        serverCommands = new LinkedHashMap<>();

        serverCommands.put("save", new Save());
    }

    /**
     * Get all commands from manager.
     *
     * @return Map of loaded commands
     */
    public LinkedHashMap<String, Command> getServerCommands() {
        return serverCommands;
    }

    /**
     * Universe method for command executing.
     *
     * @param args request
     */
    public void executeCommand(String[] args) {
        try {
            Command baseCommand = Optional.ofNullable(serverCommands.get(args[0])).orElseThrow(() -> new UnknownCommandException_("Указанная команда не была обнаружена"));
            baseCommand.execute(args);
            System.out.println(baseCommand.getResponse().getResponse());
        } catch (Exception e) {
            logger.fatal("Something went wrong!");
        }
    }

    public Command fromDescription(CommandDescription_ description) {
        return serverCommands.get(description.getName());
    }
}
