package info.colinhan.sisyphus.jacal.model;

import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.model.VariableTypes;

import java.util.HashMap;
import java.util.Map;

public class FormItemTypes {
    private FormItemTypes() {}

    private static class TheType implements FormItemType {
        private final String name;
        private final VariableType valueType;

        public TheType(String name, VariableType valueType) {
            this.name = name;
            this.valueType = valueType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public VariableType getValueType() {
            return valueType;
        }
    }
    private static Map<String, FormItemType> types = new HashMap<>();

    public static FormItemType get(String name) {
        return types.get(name.toUpperCase());
    }

    private static TheType createType(String name, VariableType valueType) {
        TheType value = new TheType(name, valueType);
        types.put(name, value);
        return value;
    }

    public final static FormItemType TEXT = createType("TEXT", VariableTypes.STRING);
    public final static FormItemType NUMBER = createType("NUMBER", VariableTypes.NUMBER);
    public final static FormItemType TOGGLE = createType("TOGGLE", VariableTypes.BOOLEAN);
}
