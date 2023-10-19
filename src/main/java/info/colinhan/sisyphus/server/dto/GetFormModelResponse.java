package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.jacal.model.Form;
import info.colinhan.sisyphus.exception.ParseError;

import java.util.List;

public class GetFormModelResponse {
    private final boolean success;
    private final Form model;
    private final List<ParseError> errors;

    public GetFormModelResponse(Form model) {
        this.success = true;
        this.model = model;
        this.errors = null;
    }

    public GetFormModelResponse(List<ParseError> errors) {
        this.success = false;
        this.model = null;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public Form getModel() {
        return model;
    }

    public List<ParseError> getErrors() {
        return errors;
    }
}
