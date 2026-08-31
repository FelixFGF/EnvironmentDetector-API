package de.felixfgf.environmentdetector.placeholder;

import de.felixfgf.environmentdetector.EnvironmentDetectorAPI;
import de.felixfgf.environmentdetector.EnvironmentInfo;
import de.felixfgf.environmentdetector.Environmentdetector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PlaceholderManager {
    private static final Map<String, Function<EnvironmentInfo, String>> PLACEHOLDERS = new HashMap<>();
    private static final String PREFIX = "${environmentdetector:";
    private static final String SUFFIX = "}";

    static {
        PLACEHOLDERS.put("minecraft_version", EnvironmentInfo::minecraftVersion);
        PLACEHOLDERS.put("loader", EnvironmentInfo::loader);
        PLACEHOLDERS.put("loader_version", EnvironmentInfo::loaderVersion);
        PLACEHOLDERS.put("launcher", EnvironmentInfo::launcher);
        PLACEHOLDERS.put("platform", EnvironmentInfo::platform);
        PLACEHOLDERS.put("pack_name", EnvironmentInfo::packName);
        PLACEHOLDERS.put("environment", EnvironmentInfo::environment);
        PLACEHOLDERS.put("os", EnvironmentInfo::operatingSystem);
    }

    public static String resolve(String text) {
        if (text == null || !text.contains(PREFIX)) {
            return text;
        }

        try {
            EnvironmentInfo info = EnvironmentDetectorAPI.getEnvironment();
            String result = text;

            for (Map.Entry<String, Function<EnvironmentInfo, String>> entry : PLACEHOLDERS.entrySet()) {
                String placeholder = PREFIX + entry.getKey() + SUFFIX;
                if (result.contains(placeholder)) {
                    String value = entry.getValue().apply(info);
                    result = result.replace(placeholder, value != null ? value : "unknown");
                }
            }
            return result;
        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Error resolving placeholders in text: {}", text, e);
            return text;
        }
    }
}
