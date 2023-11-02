package info.colinhan.sisyphus.server.service.impl;

import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.tartarus.model.ValueSource;
import info.colinhan.sisyphus.tartarus.runtime.ExecutionContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowBuildContext implements ExecutionContext {
    // variableName -> variableType -> formName list
    Map<String, Map<VariableType, List<String>>> variableUsedByForms = new HashMap<>();

    @Override
    public String getUser(ValueSource source) {
        return null;
    }
}
