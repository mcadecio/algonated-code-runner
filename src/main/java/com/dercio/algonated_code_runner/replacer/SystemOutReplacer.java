package com.dercio.algonated_code_runner.replacer;

import java.util.regex.Pattern;

public class SystemOutReplacer {

    private static final Pattern SYSTEM_OUT_REGEX = Pattern.compile("(System\\.out\\.)");

    public String replace(String code) {
        return code.replaceAll(SYSTEM_OUT_REGEX.pattern(), "consoleLogger.");
    }
}
