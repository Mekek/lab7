package commandLogic.commandReceiverLogic.receivers;

import commandLogic.CommandDescription_;

public interface ExternalBaseReceiver {
    boolean receiveCommand(CommandDescription_ command, String[] args) throws Exception;
}
