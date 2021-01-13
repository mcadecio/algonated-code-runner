package com.daio.fyp.verifier;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ImportVerifier implements Verifier {

    private static final Pattern IMPORT_PATTERN = Pattern.compile("import (.)*;");

    @Override
    public List<String> verify(String code) {

        final Matcher matcher = IMPORT_PATTERN.matcher(code);

        return matcher.results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
    }
}
