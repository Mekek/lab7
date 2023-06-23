package commandManager;

import commandLogic.CommandDescription_;
import commandLogic.commandReceiverLogic.callers.ExternalArgumentReceiverCaller_;
import commandLogic.commandReceiverLogic.callers.ExternalBaseReceiverCaller_;
import models.Event;
import models.Ticket;

import java.util.ArrayList;

public class CommandExporter_ {
    public static ArrayList<CommandDescription_> getCommandsToExport() {
        ArrayList<CommandDescription_> commands = new ArrayList<>();
        commands.add(new CommandDescription_("help", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("info", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("show", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("update", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("clear", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("count_by_price", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("execute_script", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("exit", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("print_field_ascending_event", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("remove_by_id", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("remove_at", new ExternalBaseReceiverCaller_()));
        commands.add(new CommandDescription_("add", new ExternalArgumentReceiverCaller_<Ticket>()));
        commands.add(new CommandDescription_("add_if_min", new ExternalArgumentReceiverCaller_<Ticket>()));
        commands.add(new CommandDescription_("add_if_max", new ExternalArgumentReceiverCaller_<Ticket>()));
        commands.add(new CommandDescription_("remove_all_by_event_name", new ExternalArgumentReceiverCaller_<Event>()));

        return commands;
    }
}
