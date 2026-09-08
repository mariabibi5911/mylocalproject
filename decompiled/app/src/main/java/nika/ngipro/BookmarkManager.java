package nika.ngipro;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public final class BookmarkManager {
    private static final String PREFS_NAME = "NGI_BOOKMARKS";

    /* loaded from: classes4.dex */
    public static class Bookmark {
        public long address;
        public String label;
        public String note;
    }

    private BookmarkManager() {
    }

    private static String keyFor(String fileName) {
        return "bm_" + fileName;
    }

    public static List<Bookmark> load(Context context, String fileName) {
        List<Bookmark> result = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String json = prefs.getString(keyFor(fileName), "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Bookmark b = new Bookmark();
                b.address = o.optLong("address");
                b.label = o.optString("label");
                b.note = o.optString("note");
                result.add(b);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static void add(Context context, String fileName, long address, String label, String note) {
        List<Bookmark> bookmarks = load(context, fileName);
        Bookmark b = new Bookmark();
        b.address = address;
        b.label = label;
        b.note = note;
        bookmarks.add(b);
        save(context, fileName, bookmarks);
    }

    public static void remove(Context context, String fileName, int index) {
        List<Bookmark> bookmarks = load(context, fileName);
        if (index >= 0 && index < bookmarks.size()) {
            bookmarks.remove(index);
            save(context, fileName, bookmarks);
        }
    }

    private static void save(Context context, String fileName, List<Bookmark> bookmarks) {
        JSONArray arr = new JSONArray();
        try {
            for (Bookmark b : bookmarks) {
                JSONObject o = new JSONObject();
                o.put("address", b.address);
                o.put("label", b.label);
                o.put("note", b.note);
                arr.put(o);
            }
        } catch (Exception e) {
        }
        context.getSharedPreferences(PREFS_NAME, 0).edit().putString(keyFor(fileName), arr.toString()).apply();
    }
}
