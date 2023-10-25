package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.exception.ParseError;

import java.util.List;

public record FlowError(String sourceType, String source, List<ParseError> errors) {
}
