package util;

public class JsonUtil {

    public static String getKey(String line) {

        String lineWithoutSpaceAndDoubleQuotation = line.replaceAll("[\\s\"]", "");

        int semiColonIndex = lineWithoutSpaceAndDoubleQuotation.indexOf(':');

        if (semiColonIndex == -1) return null;

        return lineWithoutSpaceAndDoubleQuotation.substring(0, semiColonIndex);

    }

    public static String getValue(String line) {
        String lineWithoutSpaceAndDoubleQuotation = line.replaceAll("[\\s\"]", "");
        int semiColonIndex = lineWithoutSpaceAndDoubleQuotation.indexOf(':');
        if (semiColonIndex == -1) {
            if (line.contains("],")) {
                return "],";
            }
            if (line.contains("]")) {
                return "]";
            }
            if (line.contains("},")) {
                return "},";
            }
            if (line.contains("}")) {
                return "}";
            }
            if (line.contains("{")) {
                return "{";
            }
            if (line.contains("[")) {
                return "[";
            }
        }

        String subString = lineWithoutSpaceAndDoubleQuotation.substring(semiColonIndex + 1);

        String value = null;

        if (subString.contains(",")) {
            value = subString.substring(0, subString.length() - 1);
        } else {
            value = subString;
        }
        return value;
    }


}
