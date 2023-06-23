package main;

import commandManager.ServerCommandManager_;
import commandManager.commands.Save;
import models.Ticket;
import models.handlers.CollectionHandler_;
import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.RequestReader_;
import models.requestLogic.StatusRequest_;
import models.requestLogic.requestWorkers.RequestWorkerManager_;
import models.requestLogic.requests.ServerRequest_;
import requests.BaseRequest_;

import java.io.*;
import java.net.SocketTimeoutException;

import serverLogic.DatagramServerConnectionFactory_;
import serverLogic.ServerConnection_;

import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

@SuppressWarnings("InfiniteLoopStatement")
public class Main {
    public static final int PORT = 50457;
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");
    private static final Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }

    /**
     * Environment key to CSV file for store collection.
     */
    public static final String ENV_KEY = "lab6";

    public static void main(String[] args) {
        CollectionHandler_<Vector<Ticket>, Ticket> handler = TicketHandler_.getInstance();
        TicketHandler_ loader = TicketHandler_.getInstance();
//        HistoryCommand.initializeCommandsHistoryQueue();

        logger.trace("This is a server!");

        // setup background tasks
        var timer = new Timer(600000, event -> new Save().execute(new String[0]));
        timer.start();

        // load collection
        try {
            loader.loadCollection(ENV_KEY);
            logger.info("Loaded elements: " + handler.getCollection().size());
            logger.info(" ");

            // commands
            logger.info("You are operating with collection of \n  type: " + handler.getCollection().getClass().getName() + ", \n  filled with elements of type: " + handler.getFirstOrNew().getClass().getName());
            logger.info("Server is listening a requests.");
        } catch (IllegalArgumentException e) {
            logger.info("Something went wrong! Collection can't be loaded from file due to some troubles: \n");
            logger.info(e.getMessage());
        }

        // connection
        logger.info("Connection...");
        ServerConnection_ connection = new DatagramServerConnectionFactory_().initializeServer(PORT);
        while (true) {
            try {
                StatusRequest_ rq = connection.listenAndGetData();
                if (rq.getCode() < 0) {
                    logger.debug("Status code: " + rq.getCode());
                    continue;
                }

                RequestReader_ rqReader = new RequestReader_(rq.getInputStream());
                BaseRequest_ baseRequest = rqReader.readObject();
                var request = new ServerRequest_(baseRequest, rq.getCallerBack(), connection);
                RequestWorkerManager_ worker = new RequestWorkerManager_();
                worker.workWithRequest(request);
            } catch (SocketTimeoutException e) {
                // Check if there's any input available in System.in
                try {
                    if (System.in.available() > 0) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        String line = reader.readLine();
                        if (line != null) {
                            ServerCommandManager_ manager = new ServerCommandManager_();
                            manager.executeCommand(line.split(" "));
                        }
                    }
                } catch (IOException e1) {
                    logger.error("Something went wrong during I/O", e1);
                }
            } catch (IOException e) {
                logger.error("Something went wrong during I/O", e);
            } catch (ClassNotFoundException e) {
                logger.error("Class not found", e);
            } catch (RuntimeException e) {
                logger.fatal(e);
            }
        }
    }
}