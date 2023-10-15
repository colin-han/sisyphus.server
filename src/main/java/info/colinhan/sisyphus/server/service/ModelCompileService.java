package info.colinhan.sisyphus.server.service;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.tartarus.exceptions.TartarusParserException;

public interface ModelCompileService {
    void compileFlow(FlowVersionEntity version) throws TartarusParserException;

    String generateFlowSVG(String code, Long flowId) throws TartarusParserException;
}
