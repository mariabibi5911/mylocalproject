package nika.ngipro.mod;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

/** Versioned, atomic JSON persistence for NGI PRO configuration. */
public final class ModStore {
    private static final String DIRECTORY = "ngi-pro";
    private static final String FILE_NAME = "mod-manager.json";
    private static final String BACKUP_NAME = "mod-manager.backup.json";
    private final File directory;

    public ModStore(Context context) {
        directory = new File(context.getFilesDir(), DIRECTORY);
    }

    public ModManagerState load() {
        File file = new File(directory, FILE_NAME);
        if (!file.exists()) return ModManagerState.defaults();
        try {
            return ModManagerState.fromJson(new JSONObject(read(file)));
        } catch (Exception ignored) {
            return ModManagerState.defaults();
        }
    }

    public void save(ModManagerState state) throws Exception {
        if (!directory.exists() && !directory.mkdirs()) throw new IllegalStateException("Unable to create configuration directory");
        File target = new File(directory, FILE_NAME);
        File temp = new File(directory, FILE_NAME + ".tmp");
        write(temp, state.toJson().toString());
        if (target.exists() && !target.delete()) throw new IllegalStateException("Unable to replace configuration");
        if (!temp.renameTo(target)) throw new IllegalStateException("Unable to commit configuration");
    }

    public void backup() throws Exception {
        File target = new File(directory, FILE_NAME);
        if (target.exists()) copy(target, new File(directory, BACKUP_NAME));
    }

    public boolean restoreBackup() throws Exception {
        File backup = new File(directory, BACKUP_NAME);
        if (!backup.exists()) return false;
        copy(backup, new File(directory, FILE_NAME));
        return true;
    }

    public String exportJson(ModManagerState state) throws Exception { return state.toJson().toString(2); }

    public ModManagerState importJson(String json) throws Exception { return ModManagerState.fromJson(new JSONObject(json)); }

    public void reset() throws Exception { save(ModManagerState.defaults()); }

    private static String read(File file) throws Exception {
        FileInputStream input = new FileInputStream(file);
        try {
            byte[] bytes = new byte[(int) file.length()];
            int offset = 0;
            int count;
            while (offset < bytes.length && (count = input.read(bytes, offset, bytes.length - offset)) > 0) offset += count;
            return new String(bytes, 0, offset, StandardCharsets.UTF_8);
        } finally { input.close(); }
    }

    private static void write(File file, String value) throws Exception {
        FileOutputStream output = new FileOutputStream(file, false);
        try { output.write(value.getBytes(StandardCharsets.UTF_8)); } finally { output.close(); }
    }

    private static void copy(File from, File to) throws Exception {
        FileInputStream input = new FileInputStream(from);
        FileOutputStream output = new FileOutputStream(to, false);
        try {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = input.read(buffer)) > 0) output.write(buffer, 0, count);
        } finally { input.close(); output.close(); }
    }
}
