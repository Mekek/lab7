package commandManager.commands;

import commandManager.CommandManager_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.util.Optional;

/**
 * Shows reference about available commands.
 *
 * @author Mekek
 * @since 1.0
 */
public class Help implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.help");
    private CommandStatusResponse_ response;

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescr() {
        return "Shows reference about available commands.";
    }

    /**
     * Prints information about the available commands in the program to the console.
     * Overrides execute() method of the Command class.
     */
    @Override
    public void execute(String[] args) {
        CommandManager_ manager = new CommandManager_();

        StringBuilder sb = new StringBuilder();

        if (args.length == 1) {
            manager.getCommands().forEach((name, command) -> sb.append(name).append(" ").append(command.getArgs()).append(" - ").append(command.getDescr()).append('\n'));
        } else {
            for (int i = 1; i < args.length; i++) {
                var command = manager.getCommands().get(args[i]);
                sb.append(args[i]).append(" : ").append(Optional.ofNullable(command).map(Command::getDescr).orElse("This command is not found in manager")).append('\n');
            }
        }

        response = CommandStatusResponse_.ofString(sb.toString());
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
