package me.elordenador.Utilities;

import java.util.List;

public class StringUtils {
    public boolean isPresent(String item, List<String> list) {
        for (String i : list) {
            if (i.equals(item)) {
                return true;
            }
        }
        return false;
    }
}
