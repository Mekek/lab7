package commandManager;

import commandLogic.CommandDescription;
import commandManager.commands.*;
import exceptions.UnknownCommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requestLogic.CallerBack;
import requests.CommandClientRequest;
import responseLogic.responseSenders.CommandResponseSender;
import responses.CommandStatusResponse;
import serverLogic.ServerConnection;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Command Manager for interactive collection manage.
 *
 * @author Mekek
 * @since 1.0
 */
public class CommandManager {

    private static final Logger logger = LogManager.getLogger("com.github.Mekek.lab6");
    final LinkedHashMap<String, Command> commands;

    /**
     * Setup command manager and all of its commands.
     */
    public CommandManager() {
        commands = new LinkedHashMap<>();

        commands.put("help", new Help());
        commands.put("info", new Info());
        commands.put("show", new Show());
        commands.put("update", new Update());
        commands.put("clear", new ClearCommand());
        commands.put("save", new Save());
        commands.put("execute_script", new ExecuteScript());
        commands.put("exit", new Exit());
        commands.put("print_field_ascending_event", new PrintFieldAscendingEvent());
        commands.put("remove_by_id", new RemoveById());
        commands.put("remove_at", new RemoveAt());
        commands.put("remove_all_by_event_name", new RemoveAllByEventName());
        commands.put("add", new Add());
        commands.put("add_if_min", new AddIfMin());
        commands.put("add_if_max", new AddIfMax());
        commands.put("count_by_price", new CountByPrice_());

    }

    /**
     * Get all commands from manager.
     *
     * @return Map of loaded commands
     */
    public LinkedHashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * Universe method for command executing.
     *
     * @param command request
     */
    public void executeCommand(CommandClientRequest command, CallerBack requester, ServerConnection answerConnection) {
        CommandStatusResponse response;
        try {
            Command baseCommand = Optional.ofNullable(commands.get(command.getCommandDescription().getName())).orElseThrow(() -> new UnknownCommandException("Такой команды не существует"));
//            HistoryCommand.addToCommandsHistoryQueue(baseCommand.getName());
            baseCommand.execute(command.getLineArgs());
            response = baseCommand.getResponse();
        } catch (IllegalArgumentException | NullPointerException e) {
            response = new CommandStatusResponse("Неверные аргументы команды. Команда будет пропущена. (" + e.getMessage() + ")", -90);
            logger.fatal(response.getResponse(), e);
        } catch (IndexOutOfBoundsException e) {
            response = new CommandStatusResponse("Неверное количество аргументов команды. Возможно, вам нужно обновить клиент", -91);
            logger.fatal(response.getResponse(), e);
        } catch (Exception e) {
            response = new CommandStatusResponse("В менеджере команд произошла непредвиденная ошибка!", -92);
            logger.fatal(response.getResponse(), e);
        }
        CommandResponseSender.sendResponse(response, answerConnection, requester);
    }

    public Command fromDescription(CommandDescription description) {
        return commands.get(description.getName());
    }
}
