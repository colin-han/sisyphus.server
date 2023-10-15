package info.colinhan.sisyphus.server.service.impl;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.service.ModelCompileService;
import info.colinhan.sisyphus.tartarus.ModelParseContext;
import info.colinhan.sisyphus.tartarus.TartarusService;
import info.colinhan.sisyphus.tartarus.exceptions.TartarusParserException;
import info.colinhan.sisyphus.tartarus.model.Flow;
import org.springframework.stereotype.Service;

@Service
public class ModelCompileServiceImpl implements ModelCompileService {
    private ModelParseContext getModelParseContext(long flowId) {
        return new MockModelParseContext();
    }

    @Override
    public void compileFlow(FlowVersionEntity version) throws TartarusParserException {
        Flow flow = TartarusService.parseFlow(version.getCode(), getModelParseContext(version.getFlowId()));

        version.setModel(flow);
    }

    @Override
    public String generateFlowSVG(String code, Long flowId) throws TartarusParserException {
        return TartarusService.generateSVG(code, getModelParseContext(flowId));
    }
}
