package info.colinhan.sisyphus.jacal.parser;

import info.colinhan.sisyphus.jacal.model.Form;
import info.colinhan.sisyphus.jacal.model.FormItem;
import info.colinhan.sisyphus.jacal.model.FormItemType;
import info.colinhan.sisyphus.jacal.model.FormItemTypes;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JacalParser {
    public static Form parse(String code) {
        return new Form(
                Arrays.stream(code.split("\n"))
                        .map(JacalParser::parseLine)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    private static Pattern parseRegex = Pattern.compile("^(\\w+)\\s+(\\w+)\\s+(.*)$");

    public static FormItem parseLine(String line) {
        if (line.startsWith("#")) {
            return null; // comments
        }
        Matcher matcher = parseRegex.matcher(line);
        if (!matcher.find()) {
            throw new RuntimeException("Jacal parse failed!");
        }

        String name = matcher.group(1);
        String type = matcher.group(2);
        String title = matcher.group(3);

        FormItemType type1 = FormItemTypes.get(type);
        return new FormItem(name, type1, title);
    }
}
