package clientLogic;

import exceptions.NotAvailableException_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.CallerBack_;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientHandler_ implements ActionListener {
    private static final Logger logger = LogManager.getLogger("com.github.Mekek.lab6");
    private static ClientHandler_ instance;
    private final javax.swing.Timer timer;
    boolean availability = true;
    private CallerBack_ callerBack;

    private ClientHandler_() {
        timer = new Timer(300000, this);
    }

    public static ClientHandler_ getInstance() {
        if (instance == null)
            instance = new ClientHandler_();
        return instance;
    }

    public void approveCallerBack(CallerBack_ callerBack) throws NotAvailableException_ {
        if (availability || this.callerBack.equals(callerBack)) {
            this.callerBack = callerBack;
            availability = true;
            timer.start();
        } else throw new NotAvailableException_(callerBack);
    }

    public boolean isAvailability() {
        return availability;
    }

    public void allowNewCallerBack() {
        availability = true;
    }

    public void restartTimer() {
        logger.info("Timer restarted!");
        this.timer.restart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug("Allowed new connection.");
        allowNewCallerBack();
    }
}
