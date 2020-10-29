package lv.theironminerlv.sidesurvivalportals.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.ConfigurationSection;

public class Messages {
    private static Map<String, String> messageMap = new HashMap<String, String>();
    private static ConfigurationSection configuration = null;

    public void load(ConfigurationSection messages) {
        configuration = messages;
        Collection<String> keys = messages.getKeys(true);
        for (String key : keys) {
            if (messages.isString(key)) {
                String value = messages.getString(key);
                value = ConvertUtils.color(StringEscapeUtils.unescapeHtml(value));
                messageMap.put(key, value);
            }
        }
    }

    public static String get(String key) {
        if (messageMap.containsKey(key)) {
            return messageMap.get(key);
        }
        return "";
    }

    public static List<String> getList(String key) {
		if (configuration.contains(key) && !configuration.getString(key).equals("")) {
            return ConvertUtils.color(Arrays.asList(configuration.getString(key).split("\n")));
        }
        return null;
	}

    public static String getParam(String key, String paramName, String paramValue) {
        return get(key).replace(paramName, paramValue);
    }

    public String getParam(String key, String paramName1, String paramValue1, String paramName2, String paramValue2) {
        return get(key).replace(paramName1, paramValue1).replace(paramName2, paramValue2);
    }

	public static List<String> getListParam(String key, String paramName, String paramValue) {
        if (configuration.contains(key) && !configuration.getString(key).equals("")) {
            return ConvertUtils.color(Arrays.asList(configuration.getString(key).replace(paramName, paramValue).split("\n")));
        }
        return null;
	}
}