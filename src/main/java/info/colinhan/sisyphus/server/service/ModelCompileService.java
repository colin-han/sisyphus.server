package info.colinhan.sisyphus.server.service;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;

public interface ModelCompileService {
    void compile(FlowVersionEntity version);
}
