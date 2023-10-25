package info.colinhan.sisyphus.jacal.parser;

import info.colinhan.sisyphus.exception.ParseError;
import info.colinhan.sisyphus.jacal.model.Form;
import info.colinhan.sisyphus.jacal.model.FormItem;
import info.colinhan.sisyphus.jacal.model.FormItemType;
import info.colinhan.sisyphus.jacal.model.FormItemTypes;
import info.colinhan.sisyphus.exception.ParseException;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JacalParser {
    public static Form parse(String code) throws ParseException {
        List<FormItem> list = new ArrayList<>();
        List<ParseError> errors = new ArrayList<>();
        String[] split = code.split("\n");
        JacalParseContext parseContext = new JacalParseContext(errors);
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            FormItem formItem = parseLine(parseContext, i, s);
            if (formItem != null) {
                list.add(formItem);
            }
        }
        if (!errors.isEmpty()) {
            throw new ParseException(errors);
        }
        return new Form(list);
    }

    private static Pattern parseRegex = Pattern.compile("^(?<req>\\*)?\\s*(?<name>\\w+)\\s+(?<type>\\w+)(\\s+(?<title>.*))?$");

    public static FormItem parseLine(JacalParseContext jacalParseContext, int lineIndex, String line) {
        if (Strings.isBlank(line)) {
            return null; // empty line
        }
        if (line.startsWith("#")) {
            return null; // comments
        }
        Matcher matcher = parseRegex.matcher(line);
        if (!matcher.find()) {
            jacalParseContext.addError(new ParseError(lineIndex + 1, 0, line.length(), "Invalid line", null));
            return null;
        }

        String name = matcher.group("name");
        String type = matcher.group("type");
        String title = matcher.group("title");
        boolean required = matcher.group("req") != null;

        if (validName(jacalParseContext, lineIndex, name, matcher)) return null;

        FormItemType type1 = parseType(jacalParseContext, lineIndex, type, matcher);
        if (type1 == null) return null;

        return new FormItem(name, type1, title, required);
    }

    private static FormItemType parseType(JacalParseContext jacalParseContext, int lineIndex, String type, Matcher matcher) {
        FormItemType type1 = FormItemTypes.get(type);
        if (type1 == null) {
            int typeStart = matcher.start("type");
            int typeEnd = matcher.end("type");
            jacalParseContext.addError(new ParseError(lineIndex + 1, typeStart, typeEnd - typeStart, "Invalid type", null));
            return null;
        }
        return type1;
    }

    private static boolean validName(JacalParseContext jacalParseContext, int lineIndex, String name, Matcher matcher) {
        if (jacalParseContext.hasName(name)) {
            int nameStart = matcher.start("name");
            int nameEnd = matcher.end("name");
            jacalParseContext.addError(new ParseError(lineIndex + 1, nameStart, nameEnd - nameStart, "Duplicate name", null));
            return true;
        }
        jacalParseContext.addName(name);
        return false;
    }
}
