package com.dercio.algonated_code_runner.verifier;

import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IllegalMethodVerifier implements Verifier {

    private final List<String> illegalMethods;

    public IllegalMethodVerifier(List<String> illegalMethods) {
        this.illegalMethods = illegalMethods;
    }

    @Override
    public List<String> verify(String code) {
        if (illegalMethods.isEmpty()) {
            return Collections.emptyList();
        }

        final String regex = String.format("(%s)", String.join("|", illegalMethods));

        return Pattern.compile(regex)
                .matcher(code)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
    }
}
