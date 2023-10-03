package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.jacal.model.Form;

public class GetFormModelResponse {
    private final Form form;
    private final String error;

    public GetFormModelResponse(Form form, String error) {
        this.form = form;
        this.error = error;
    }

    public Form getForm() {
        return form;
    }

    public String getError() {
        return error;
    }
}
