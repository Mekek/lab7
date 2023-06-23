package models.requestLogic.requestWorkers;

import models.requestLogic.requests.ServerRequest_;

public interface RequestWorker_ {
    void workWithRequest(ServerRequest_ request);
}
