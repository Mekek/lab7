package models.requestLogic.requestWorkers;

import exceptions.UnsupportedRequestException_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import models.requestLogic.requests.ServerRequest_;
import requests.ArgumentCommandClientRequest_;
import requests.BaseRequest_;
import requests.CommandClientRequest_;
import requests.CommandDescriptionsRequest_;

import java.util.LinkedHashMap;
import java.util.Optional;

public class RequestWorkerManager_ {

    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    private final LinkedHashMap<Class<?>, RequestWorker_> workers = new LinkedHashMap<>();

    public RequestWorkerManager_() {
        workers.put(BaseRequest_.class, new BaseRequestWorker_());
        workers.put(CommandClientRequest_.class, new CommandClientRequestWorker_());
        workers.put(ArgumentCommandClientRequest_.class, new ArgumentCommandClientRequestWorker_<>());
        workers.put(CommandDescriptionsRequest_.class, new CommandConfigureRequestWorker_());
    }

    public void workWithRequest(ServerRequest_ request) {
        try {
            Optional.ofNullable(workers.get(request.getUserRequest().getClass())).orElseThrow(() -> new UnsupportedRequestException_("Указанный запрос не может быть обработан")).workWithRequest(request);
        } catch (UnsupportedRequestException_ ex) {
            logger.error("Got an invalid request.");
        }
    }
}
