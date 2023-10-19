package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.exception.ParseError;

import java.util.List;

public class GetFlowSvgResponse {
    private final boolean success;
    private final String svg;
    private final List<ParseError> errors;

    public GetFlowSvgResponse(String svg) {
        this.success = true;
        this.svg = svg;
        this.errors = null;
    }

    public GetFlowSvgResponse(List<ParseError> errors) {
        this.success = false;
        this.svg = null;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSvg() {
        return svg;
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}
