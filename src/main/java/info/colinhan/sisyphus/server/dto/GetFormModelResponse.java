package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.jacal.model.Form;

public class GetFormModelResponse {
    private final Form model;
    private final String error;

    public GetFormModelResponse(Form model, String error) {
        this.model = model;
        this.error = error;
    }

    public Form getModel() {
        return model;
    }

    public String getError() {
        return error;
    }
}
