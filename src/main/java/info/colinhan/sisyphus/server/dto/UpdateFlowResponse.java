package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.tartarus.parser.ParseError;

import java.util.List;

public class UpdateFlowResponse {
    private final boolean success;
    private final FlowEntityDto flow;
    private final List<ParseError> errors;

    public UpdateFlowResponse(FlowEntityDto flow) {
        this.success = true;
        this.flow = flow;
        this.errors = null;
    }

    public UpdateFlowResponse(List<ParseError> errors) {
        this.success = false;
        this.flow = null;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public FlowEntityDto getFlow() {
        return flow;
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}
