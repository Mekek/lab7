package commandLogic.commandReceiverLogic.handlers;

import commandLogic.commandReceiverLogic.receivers.ExternalBaseReceiver;

import java.util.ArrayList;

public class NonArgReceiversHandler_ extends ReceiverHandler_ {

    private final ArrayList<ExternalBaseReceiver> receivers;

    {
        receivers = new ArrayList<>();
    }

    @Override
    public void addReceiver(ExternalBaseReceiver receiver) {
        receivers.add(receiver);
    }

    @Override
    public ArrayList<ExternalBaseReceiver> getReceivers() {
        return receivers;
    }
}
