package info.colinhan.sisyphus.server.service.impl;

import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.service.ModelCompileService;
import info.colinhan.sisyphus.tartarus.TartarusService;
import info.colinhan.sisyphus.tartarus.model.Flow;
import org.springframework.stereotype.Service;

@Service
public class ModelCompileServiceImpl implements ModelCompileService {
    @Override
    public void compile(FlowVersionEntity version) {
        Flow flow = TartarusService.parseFlow(version.getCode());

        version.setModel(flow);
    }
}
