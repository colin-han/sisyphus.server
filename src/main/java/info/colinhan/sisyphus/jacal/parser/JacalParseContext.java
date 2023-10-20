package info.colinhan.sisyphus.jacal.parser;

import com.alibaba.fastjson2.JSONPath;
import info.colinhan.sisyphus.exception.ParseError;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class JacalParseContext {
    private final List<ParseError> errors;
    private final Set<String> names = new HashSet<>();

    public JacalParseContext(List<ParseError> errors) {
        this.errors = errors;
    }

    public List<ParseError> errors() {
        return errors;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (JacalParseContext) obj;
        return Objects.equals(this.errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }

    @Override
    public String toString() {
        return "JacalParseContext[" +
                "errors=" + errors + ']';
    }

    void addError(ParseError error) {
        errors().add(error);
    }

    public boolean hasName(String name) {
        return this.names.contains(name);
    }

    public void addName(String name) {
        this.names.add(name);
    }
}