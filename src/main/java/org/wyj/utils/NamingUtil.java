package org.wyj.utils;

import java.util.Locale;

public class NamingUtil {
    private static final String UNDER_LINE = "_";

    public static String underlineToCamel(String name) {
        if (name == null || "".equals(name)) {
            return "";
        }
        name = name.toLowerCase(Locale.ROOT);

        String[] subStringArr = name.split(UNDER_LINE);
        if (subStringArr.length == 1) {
            return subStringArr[0];
        }

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(subStringArr[0]);
        for (int i = 1; i < subStringArr.length; i++) {
            String subStr = subStringArr[i];
            sBuilder.append(subStr.substring(0, 1).toUpperCase())
                    .append(subStr.substring(1));
        }
        return sBuilder.toString();
    }
}
