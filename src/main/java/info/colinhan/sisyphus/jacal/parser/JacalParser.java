package info.colinhan.sisyphus.jacal.parser;

import info.colinhan.sisyphus.jacal.model.Form;
import info.colinhan.sisyphus.jacal.model.FormItem;
import info.colinhan.sisyphus.jacal.model.FormItemType;
import info.colinhan.sisyphus.jacal.model.FormItemTypes;
import info.colinhan.sisyphus.exception.ParserException;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JacalParser {
    public static Form parse(String code) throws ParserException {
        return new Form(
                Arrays.stream(code.split("\n"))
                        .map(JacalParser::parseLine)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    private static Pattern parseRegex = Pattern.compile("^(?<req>\\*)?\\s*(?<name>\\w+)\\s+(?<type>\\w+)(\\s+(?<title>.*))?$");

    public static FormItem parseLine(String line) {
        if (Strings.isBlank(line)) {
            return null; // empty line
        }
        if (line.startsWith("#")) {
            return null; // comments
        }
        Matcher matcher = parseRegex.matcher(line);
        if (!matcher.find()) {
            throw new RuntimeException("Jacal parse failed!");
        }

        String name = matcher.group("name");
        String type = matcher.group("type");
        String title = matcher.group("title");
        boolean required = matcher.group("req") != null;

        FormItemType type1 = FormItemTypes.get(type);
        return new FormItem(name, type1, title, required);
    }
}
