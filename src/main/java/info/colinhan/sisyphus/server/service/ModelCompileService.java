package info.colinhan.sisyphus.server.service;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.exception.ParserException;

public interface ModelCompileService {
    void compileFlow(FlowVersionEntity version) throws ParserException;

    String generateFlowSVG(String code, Long flowId) throws ParserException;
}
