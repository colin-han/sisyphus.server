package info.colinhan.sisyphus.server.service.impl;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.service.ModelCompileService;
import info.colinhan.sisyphus.tartarus.ModelParseContext;
import info.colinhan.sisyphus.tartarus.TartarusService;
import info.colinhan.sisyphus.tartarus.model.Flow;
import org.springframework.stereotype.Service;

@Service
public class ModelCompileServiceImpl implements ModelCompileService {
    private ModelParseContext getModelParseContext(long flowId) {
        return null;
    }

    @Override
    public void compile(FlowVersionEntity version) {
        Flow flow = TartarusService.parseFlow(version.getCode(), getModelParseContext(version.getFlowId()));

        version.setModel(flow);
    }

    @Override
    public String generateFlowSVG(String code, Long flowId) {
        return TartarusService.generateSVG(code, getModelParseContext(flowId));
    }
}
