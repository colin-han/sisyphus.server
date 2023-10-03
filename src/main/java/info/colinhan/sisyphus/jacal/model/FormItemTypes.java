package info.colinhan.sisyphus.jacal.model;

import java.util.HashMap;
import java.util.Map;

public class FormItemTypes {
    private FormItemTypes() {}

    private static class TheType implements FormItemType {
        private final String name;
        private final FormItemValueType valueType;

        public TheType(String name, FormItemValueType valueType) {
            this.name = name;
            this.valueType = valueType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public FormItemValueType getValueType() {
            return valueType;
        }
    }
    private static Map<String, FormItemType> types = new HashMap<>();

    public static FormItemType get(String name) {
        return types.get(name);
    }

    private static TheType createType(String name, FormItemValueType valueType) {
        TheType value = new TheType(name, valueType);
        types.put(name, value);
        return value;
    }

    public final static FormItemType TEXT = createType("TEXT", FormItemValueType.STRING);
    public final static FormItemType NUMBER = createType("NUMBER", FormItemValueType.NUMBER);
    public final static FormItemType TOGGLE = createType("TOGGLE", FormItemValueType.BOOLEAN);
}
