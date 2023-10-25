package info.colinhan.sisyphus.server.service;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.exception.ParseException;

public interface ModelCompileService {
    void compileFlow(FlowVersionEntity version) throws ParseException;

    String generateFlowSVG(String code, Long flowId) throws ParseException;
}
