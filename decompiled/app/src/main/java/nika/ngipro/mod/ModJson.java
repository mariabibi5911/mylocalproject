package nika.ngipro.mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

public final class ModJson {
    private ModJson() {
    }

    public static String[] names(JSONObject object) {
        if (object == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) names.add(keys.next());
        return names.toArray(new String[0]);
    }
}