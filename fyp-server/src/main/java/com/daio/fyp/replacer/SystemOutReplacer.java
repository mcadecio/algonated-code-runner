package com.daio.fyp.replacer;

import java.util.regex.Pattern;

public class SystemOutReplacer {

    private static final Pattern SYSTEM_OUT_REGEX = Pattern.compile("(System\\.out\\.)");

    public String replace(String code) {
        return code.replaceAll("(System\\.out\\.)", "consoleLogger.");
    }
}
