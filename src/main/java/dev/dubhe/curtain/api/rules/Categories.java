package dev.dubhe.curtain.api.rules;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Categories {
    public static final String FEATURE = "feature";
    public static final String BOT = "bot";
    public static final String CREATIVE = "creative";
    public static final String COMMAND = "command";
    public static final String SURVIVAL = "survival";
    public static final String BUGFIX = "bugfix";
    public static final String CLIENT = "client";
    public static final String TNT = "tnt";

    public static List<String> getCategories() {
        ArrayList<String> rt = new ArrayList<>();
        Field[] fields = Categories.class.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            try {
                String value = (String) field.get(name);
                rt.add(value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return rt;
    }
}
