package commandManager;

import commandLogic.CommandDescription;
import commandLogic.commandReceiverLogic.callers.ExternalArgumentReceiverCaller;
import commandLogic.commandReceiverLogic.callers.ExternalBaseReceiverCaller;
import models.Event;
import models.Ticket;

import java.util.ArrayList;

public class CommandExporter {
    public static ArrayList<CommandDescription> getCommandsToExport() {
        ArrayList<CommandDescription> commands = new ArrayList<>();
        commands.add(new CommandDescription("help", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("info", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("show", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("update", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("clear", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("count_by_price", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("execute_script", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("exit", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("print_field_ascending_event", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("remove_by_id", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("remove_at", new ExternalBaseReceiverCaller()));
        commands.add(new CommandDescription("add", new ExternalArgumentReceiverCaller<Ticket>()));
        commands.add(new CommandDescription("add_if_min", new ExternalArgumentReceiverCaller<Ticket>()));
        commands.add(new CommandDescription("add_if_max", new ExternalArgumentReceiverCaller<Ticket>()));
        commands.add(new CommandDescription("remove_all_by_event_name", new ExternalArgumentReceiverCaller<Event>()));

        return commands;
    }
}
