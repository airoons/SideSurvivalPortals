package lv.sidesurvival.utils;

import lv.side.lang.api.LangAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Messages {

    public static String getPlayerLanguage(Player player) {
        if (player == null)
            return LangAPI.getDefaultLang();

        String lang = LangAPI.getPlayerSelected(player.getName());
        if (lang == null)
            lang = LangAPI.getDefaultLang();

        return lang;
    }

    public static String get(Player player, String key) {
        key = "s-portals." + key;
        String r = LangAPI.localize(getPlayerLanguage(player), key);
        if (r.equalsIgnoreCase("???"))
            System.out.println("Missing LangAPI key " + key);

        return ConvertUtils.color(r);
    }

    public static List<String> getList(Player player, String key) {
        return ConvertUtils.color(
                new ArrayList<>(Arrays.asList(
                        get(player, key).split("\n")
                ))
        );
    }

    public static String paramsReplace(String msg, String[] paramsName, String[] paramsValue) {
        for (int i = 0; i < paramsValue.length; i++) {
            if (i < paramsName.length)
                msg = msg.replace(paramsName[i], paramsValue[i]);
        }

        return msg;
    }

    public static List<String> paramsReplace(List<String> msgList, String[] paramsName, String[] paramsValue) {
        for (int i = 0; i < msgList.size(); i++) {
            msgList.set(i, paramsReplace(msgList.get(i), paramsName, paramsValue));
        }

        return msgList;
    }

    public static String getParam(Player player, String key, String paramName, String paramValue) {
        return ConvertUtils.color(get(player, key).replace(paramName, paramValue));
    }

    public static String getParam(Player player, String key, String[] paramsName, String[] paramsValue) {
        return ConvertUtils.color(paramsReplace(get(player, key), paramsName, paramsValue));
    }

    public static List<String> getListParam(Player player, String key, String paramName, String paramValue) {
        return ConvertUtils.color(
                new ArrayList<>(Arrays.asList(
                        get(player, key).replace(paramName, paramValue).split("\n")
                ))
        );
    }

    public static List<String> getListParam(Player player, String key, String[] paramsName, String[] paramsValue) {
        return ConvertUtils.color(
                new ArrayList<>(Arrays.asList(
                        paramsReplace(get(player, key), paramsName, paramsValue).split("\n")
                ))
        );
    }
}