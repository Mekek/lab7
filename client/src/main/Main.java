package main;

import commandManager.CommandDescriptionHolder;
import commandManager.CommandExecutor;
import commandManager.CommandLoaderUtility;
import commandManager.CommandMode;
import exceptions.CommandsNotLoadedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverLogic.*;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Program entry point class. Contains main() method.
 * This program is managing collection of type <code>HashSet&#8249;Route></code>
 *
 * @author Mekek
 * @since 1.0
 */
public class Main {
    public static final int PORT = 50457;
    private static final Logger logger = LogManager.getLogger("lab6");

    /**
     * Program entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // server connecting
        try {
            //ServerConnection connection = new UdpServerConnectionFactory().openConnection(InetAddress.getByName(HOST_ADDRESS), PORT);
            ServerConnection connection = new UDPConnectionBlockDecorator((UDPServerConnection) new UdpServerConnectionFactory().openConnection(InetAddress.getLocalHost(), PORT), true);
            ServerConnectionHandler.setServerConnection(connection);
            connection.openConnection();

            // request commands
            boolean commandsNotLoaded = true;
            int waitingCount = 4000;
            while (commandsNotLoaded) {
                try {
                    CommandLoaderUtility.initializeCommands();
                    CommandExecutor executor = new CommandExecutor(CommandDescriptionHolder.getInstance().getCommands(), System.in, CommandMode.CLI_UserMode);
                    commandsNotLoaded = false;

                    // start executing
                    System.out.println("Connected to the server.");
                    System.out.println("Please, enter any command.");
                    executor.startExecuting();
                } catch (CommandsNotLoadedException e) {
                    logger.warn("Trying to get commands from the server...");

                    AtomicInteger secondsRemained = new AtomicInteger(waitingCount / 1000 - 1);

                    javax.swing.Timer timer = new Timer(1000, (x) -> logger.info("Retry after " + secondsRemained.getAndDecrement() + "seconds. You can abort the wait by pressing Enter."));

                    timer.start();

                    CountDownLatch latch = new CountDownLatch(1);

                    int finalWaitingCount = waitingCount;
                    Runnable wait = () -> {
                        try {
                            Thread.sleep(finalWaitingCount);
                            latch.countDown();
                        } catch (InterruptedException ex) {
                            logger.info("Connecting...");
                        }
                    };

                    /*Runnable forceInterrupt = () -> {
                        try {
                            int ignored = System.in.read();
                            latch.countDown();
                        } catch (IOException ex) {
                            logger.error("Something went wrong during i/o.");
                        }
                    };*/

                    Thread tWait = new Thread(wait);
                    //Thread tForceInt = new Thread(forceInterrupt);

                    tWait.start();
                    //tForceInt.start();

                    try {
                        latch.await();
                        timer.stop();
                        tWait.interrupt();
                    } catch (InterruptedException ex) {
                        logger.info("interrupted");
                    }

                    waitingCount += 2000;
                }
            }
        } catch (UnknownHostException ex) {
            logger.fatal("Cant find host");
        } catch (IOException ex) {
            logger.error("Something went wrong during IO operation.");
        }
    }
}