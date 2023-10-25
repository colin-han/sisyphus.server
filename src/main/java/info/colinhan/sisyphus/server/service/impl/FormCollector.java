package info.colinhan.sisyphus.server.service.impl;

import info.colinhan.sisyphus.tartarus.action.BuiltInActions;
import info.colinhan.sisyphus.tartarus.model.AbstractModelVisitor;
import info.colinhan.sisyphus.tartarus.model.Action;

import java.util.ArrayList;
import java.util.List;

public class FormCollector extends AbstractModelVisitor<Void> {
    private final List<Action> forms = new ArrayList<>();

    @Override
    public Void visitAction(Action action) {
        if (action.getName().equals(BuiltInActions.ACTION_NAME_FILL_FORM)) {
            forms.add(action);
        }
        return super.visitAction(action);
    }

    public List<Action> getForms() {
        return forms;
    }
}
