package nika.ngipro;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.Locale;
import nika.ngipro.HexAdapter;

/* loaded from: classes4.dex */
public class HexEditorActivity extends AppCompatActivity {
    public static final String EXTRA_FILE_PATH = "file_path";
    private static final long MAX_IN_MEMORY_SIZE = 20971520;
    private HexAdapter adapter;
    private byte[] fileData;
    private String filePath;
    private TextView inspectorText;
    private byte[] lastSearchPattern;
    private RecyclerView recycler;
    private TextView statusText;
    private boolean dirty = false;
    private int lastFoundOffset = -1;
    private EditSnapshot lastEdit = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class EditSnapshot {
        int offset;
        byte[] oldBytes;

        private EditSnapshot() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.attach(newBase));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_editor);
        this.filePath = getIntent().getStringExtra("file_path");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHex);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HexEditorActivity.this.m1740lambda$onCreate$0$nikangiproHexEditorActivity(view);
            }
        });
        toolbar.setTitle(this.filePath != null ? new File(this.filePath).getName() : "Hex Editor");
        this.recycler = (RecyclerView) findViewById(R.id.hexRecycler);
        this.statusText = (TextView) findViewById(R.id.hexStatusText);
        this.inspectorText = (TextView) findViewById(R.id.inspectorText);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btnGoto).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HexEditorActivity.this.m1741lambda$onCreate$1$nikangiproHexEditorActivity(view);
            }
        });
        findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HexEditorActivity.this.m1742lambda$onCreate$2$nikangiproHexEditorActivity(view);
            }
        });
        findViewById(R.id.btnFindNext).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HexEditorActivity.this.m1743lambda$onCreate$3$nikangiproHexEditorActivity(view);
            }
        });
        findViewById(R.id.btnUndo).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HexEditorActivity.this.m1744lambda$onCreate$4$nikangiproHexEditorActivity(view);
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HexEditorActivity.this.m1745lambda$onCreate$5$nikangiproHexEditorActivity(view);
            }
        });
        loadFile();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1740lambda$onCreate$0$nikangiproHexEditorActivity(View v) {
        confirmExitIfDirty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1741lambda$onCreate$1$nikangiproHexEditorActivity(View v) {
        showGotoDialog();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$2$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1742lambda$onCreate$2$nikangiproHexEditorActivity(View v) {
        showSearchDialog();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$3$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1743lambda$onCreate$3$nikangiproHexEditorActivity(View v) {
        findNext();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$4$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1744lambda$onCreate$4$nikangiproHexEditorActivity(View v) {
        undoLastEdit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$5$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1745lambda$onCreate$5$nikangiproHexEditorActivity(View v) {
        saveChanges();
    }

    private void loadFile() {
        if (this.filePath == null) {
            Toast.makeText(this, "No file provided", 0).show();
            finish();
            return;
        }
        final File f = new File(this.filePath);
        if (!f.exists()) {
            Toast.makeText(this, "File not found", 0).show();
            finish();
        } else if (f.length() > MAX_IN_MEMORY_SIZE) {
            Toast.makeText(this, "File too large for this editor (" + ((f.length() / 1024) / 1024) + "MB, max 20MB)", 1).show();
            finish();
        } else {
            new Thread(new Runnable() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    HexEditorActivity.this.m1739lambda$loadFile$8$nikangiproHexEditorActivity(f);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$loadFile$8$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1739lambda$loadFile$8$nikangiproHexEditorActivity(File f) {
        int read;
        try {
            FileInputStream fis = new FileInputStream(f);
            try {
                this.fileData = new byte[(int) f.length()];
                int off = 0;
                while (true) {
                    byte[] bArr = this.fileData;
                    if (off >= bArr.length || (read = fis.read(bArr, off, bArr.length - off)) <= 0) {
                        break;
                    } else {
                        off += read;
                    }
                }
                runOnUiThread(new Runnable() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        HexEditorActivity.this.m1737lambda$loadFile$6$nikangiproHexEditorActivity();
                    }
                });
                fis.close();
            } finally {
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HexEditorActivity.this.m1738lambda$loadFile$7$nikangiproHexEditorActivity(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$loadFile$6$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1737lambda$loadFile$6$nikangiproHexEditorActivity() {
        HexAdapter hexAdapter = new HexAdapter(this.fileData, new HexAdapter.OnRowClick() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda2
            @Override // nika.ngipro.HexAdapter.OnRowClick
            public final void onClick(int i) {
                HexEditorActivity.this.openRowEditor(i);
            }
        });
        this.adapter = hexAdapter;
        this.recycler.setAdapter(hexAdapter);
        updateStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$loadFile$7$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1738lambda$loadFile$7$nikangiproHexEditorActivity(Exception e) {
        Toast.makeText(this, "Error reading file: " + e.getMessage(), 1).show();
        finish();
    }

    private void updateStatus() {
        String sizeStr = this.fileData.length + " bytes";
        this.statusText.setText(sizeStr + (this.dirty ? "  •  unsaved changes" : ""));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openRowEditor(final int rowIndex) {
        final int start = rowIndex * 16;
        final int end = Math.min(start + 16, this.fileData.length);
        updateInspector(start);
        StringBuilder current = new StringBuilder();
        for (int i = start; i < end; i++) {
            current.append(String.format("%02X ", Integer.valueOf(this.fileData[i] & 255)));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme.DeviceDefault.Dialog.Alert);
        builder.setTitle(String.format(Locale.US, "Edit offset 0x%08X", Integer.valueOf(start)));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(1);
        l.setPadding(40, 20, 40, 20);
        TextView hint = new TextView(this);
        hint.setText("Hex bytes, space-separated:");
        hint.setTextSize(12.0f);
        l.addView(hint);
        final EditText input = new EditText(this);
        input.setText(current.toString().trim());
        l.addView(input);
        builder.setView(l);
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda7
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                HexEditorActivity.this.m1746lambda$openRowEditor$9$nikangiproHexEditorActivity(input, end, start, rowIndex, dialogInterface, i2);
            }
        });
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) null);
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$openRowEditor$9$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1746lambda$openRowEditor$9$nikangiproHexEditorActivity(EditText input, int end, int start, int rowIndex, DialogInterface d, int w) {
        try {
            String[] parts = input.getText().toString().trim().split("\\s+");
            int count = Math.min(parts.length, end - start);
            EditSnapshot snap = new EditSnapshot();
            snap.offset = start;
            snap.oldBytes = new byte[count];
            System.arraycopy(this.fileData, start, snap.oldBytes, 0, count);
            this.lastEdit = snap;
            for (int i = 0; i < count; i++) {
                int value = Integer.parseInt(parts[i], 16) & 255;
                this.fileData[start + i] = (byte) value;
            }
            this.dirty = true;
            this.adapter.notifyItemChanged(rowIndex);
            updateStatus();
            updateInspector(start);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid hex format", 0).show();
        }
    }

    private void undoLastEdit() {
        EditSnapshot editSnapshot = this.lastEdit;
        if (editSnapshot == null) {
            Toast.makeText(this, "Nothing to undo", 0).show();
            return;
        }
        System.arraycopy(editSnapshot.oldBytes, 0, this.fileData, this.lastEdit.offset, this.lastEdit.oldBytes.length);
        int row = this.lastEdit.offset / 16;
        this.adapter.notifyItemChanged(row);
        updateInspector(this.lastEdit.offset);
        this.recycler.scrollToPosition(row);
        Toast.makeText(this, "Reverted last edit", 0).show();
        this.lastEdit = null;
        this.dirty = true;
        updateStatus();
    }

    private void updateInspector(int offset) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "@0x%08X  ", Integer.valueOf(offset)));
        int avail = this.fileData.length - offset;
        if (avail >= 1) {
            sb.append("i8=").append((int) this.fileData[offset]).append("  ");
        }
        if (avail >= 1) {
            sb.append("u8=").append(this.fileData[offset] & 255).append("  ");
        }
        if (avail >= 2) {
            sb.append("i16=").append(readLE(offset, 2, true)).append("  ");
        }
        if (avail >= 4) {
            sb.append("i32=").append(readLE(offset, 4, true)).append("  ");
        }
        if (avail >= 4) {
            sb.append("f32=").append(Float.intBitsToFloat((int) readLE(offset, 4, false))).append("  ");
        }
        if (avail >= 8) {
            sb.append("i64=").append(readLE(offset, 8, true));
        }
        this.inspectorText.setText(sb.toString());
    }

    private long readLE(int offset, int size, boolean signExtend) {
        long value = 0;
        for (int i = 0; i < size; i++) {
            value |= (this.fileData[offset + i] & 255) << (i * 8);
        }
        if (signExtend && size < 8) {
            long signBit = 1 << ((size * 8) - 1);
            if ((value & signBit) != 0) {
                return value - (1 << (size * 8));
            }
            return value;
        }
        return value;
    }

    private void findNext() {
        byte[] bArr = this.lastSearchPattern;
        if (bArr == null) {
            Toast.makeText(this, "Search for something first", 0).show();
            return;
        }
        int searchFrom = this.lastFoundOffset + 1;
        byte[] bArr2 = this.fileData;
        if (searchFrom >= bArr2.length) {
            Toast.makeText(this, "Reached end of file", 0).show();
            return;
        }
        int foundAt = indexOf(bArr2, bArr, searchFrom);
        if (foundAt < 0) {
            Toast.makeText(this, "No more matches", 0).show();
            return;
        }
        this.lastFoundOffset = foundAt;
        int row = foundAt / 16;
        this.recycler.scrollToPosition(row);
        this.adapter.highlightRow(row);
        updateInspector(foundAt);
        Toast.makeText(this, String.format(Locale.US, "Found at 0x%08X", Integer.valueOf(foundAt)), 0).show();
    }

    private void showGotoDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme.DeviceDefault.Dialog.Alert);
        b.setTitle("Goto Offset");
        final EditText input = new EditText(this);
        input.setHint("Hex offset (e.g. 1A0)");
        b.setView(input);
        b.setPositiveButton("Go", new DialogInterface.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                HexEditorActivity.this.m1750lambda$showGotoDialog$10$nikangiproHexEditorActivity(input, dialogInterface, i);
            }
        });
        b.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showGotoDialog$10$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1750lambda$showGotoDialog$10$nikangiproHexEditorActivity(EditText input, DialogInterface d, int w) {
        try {
            long offset = Long.parseLong(input.getText().toString().trim(), 16);
            if (offset >= 0 && offset < this.fileData.length) {
                int row = (int) (offset / 16);
                this.recycler.scrollToPosition(row);
                this.adapter.highlightRow(row);
                return;
            }
            Toast.makeText(this, "Offset out of range", 0).show();
        } catch (Exception e) {
            Toast.makeText(this, "Invalid offset", 0).show();
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme.DeviceDefault.Dialog.Alert);
        b.setTitle("Search (hex bytes or text)");
        final EditText input = new EditText(this);
        input.setHint("e.g. 'AA BB CC' or 'hello'");
        b.setView(input);
        b.setPositiveButton("Find", new DialogInterface.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                HexEditorActivity.this.m1751lambda$showSearchDialog$11$nikangiproHexEditorActivity(input, dialogInterface, i);
            }
        });
        b.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showSearchDialog$11$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1751lambda$showSearchDialog$11$nikangiproHexEditorActivity(EditText input, DialogInterface d, int w) {
        String query = input.getText().toString().trim();
        byte[] pattern = parseSearchQuery(query);
        if (pattern == null || pattern.length == 0) {
            Toast.makeText(this, "Invalid search query", 0).show();
            return;
        }
        int foundAt = indexOf(this.fileData, pattern, 0);
        this.lastSearchPattern = pattern;
        if (foundAt < 0) {
            this.lastFoundOffset = -1;
            Toast.makeText(this, "Not found", 0).show();
            return;
        }
        this.lastFoundOffset = foundAt;
        int row = foundAt / 16;
        this.recycler.scrollToPosition(row);
        this.adapter.highlightRow(row);
        updateInspector(foundAt);
        Toast.makeText(this, String.format(Locale.US, "Found at 0x%08X", Integer.valueOf(foundAt)), 0).show();
    }

    private byte[] parseSearchQuery(String query) {
        if (query.isEmpty()) {
            return null;
        }
        boolean looksHex = query.matches("^[0-9A-Fa-f\\s]+$") && query.replace(" ", "").length() % 2 == 0;
        if (looksHex) {
            try {
                String[] parts = query.trim().split("\\s+");
                byte[] result = new byte[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    result[i] = (byte) (Integer.parseInt(parts[i], 16) & 255);
                }
                return result;
            } catch (Exception e) {
            }
        }
        return query.getBytes();
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0016, code lost:

        r0 = r0 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int indexOf(byte[] data, byte[] pattern, int fromIndex) {
        int i = Math.max(0, fromIndex);
        while (i <= data.length - pattern.length) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    break;
                }
            }
            return i;
        }
        return -1;
    }

    private void saveChanges() {
        if (!this.dirty) {
            Toast.makeText(this, "No changes to save", 0).show();
        } else {
            new Thread(new Runnable() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    HexEditorActivity.this.m1749lambda$saveChanges$14$nikangiproHexEditorActivity();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$saveChanges$14$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1749lambda$saveChanges$14$nikangiproHexEditorActivity() {
        try {
            RandomAccessFile raf = new RandomAccessFile(this.filePath, "rw");
            try {
                raf.seek(0L);
                raf.write(this.fileData);
                runOnUiThread(new Runnable() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        HexEditorActivity.this.m1747lambda$saveChanges$12$nikangiproHexEditorActivity();
                    }
                });
                raf.close();
            } finally {
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    HexEditorActivity.this.m1748lambda$saveChanges$13$nikangiproHexEditorActivity(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$saveChanges$12$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1747lambda$saveChanges$12$nikangiproHexEditorActivity() {
        this.dirty = false;
        updateStatus();
        Toast.makeText(this, "Saved", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$saveChanges$13$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1748lambda$saveChanges$13$nikangiproHexEditorActivity(Exception e) {
        Toast.makeText(this, "Save failed: " + e.getMessage(), 1).show();
    }

    private void confirmExitIfDirty() {
        if (this.dirty) {
            new AlertDialog.Builder(this, android.R.style.Theme.DeviceDefault.Dialog.Alert).setTitle("Unsaved changes").setMessage("Discard changes and exit?").setPositiveButton("Discard", new DialogInterface.OnClickListener() { // from class: nika.ngipro.HexEditorActivity$$ExternalSyntheticLambda8
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    HexEditorActivity.this.m1736lambda$confirmExitIfDirty$15$nikangiproHexEditorActivity(dialogInterface, i);
                }
            }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
        } else {
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$confirmExitIfDirty$15$nika-ngipro-HexEditorActivity, reason: not valid java name */
    public /* synthetic */ void m1736lambda$confirmExitIfDirty$15$nikangiproHexEditorActivity(DialogInterface d, int w) {
        finish();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        confirmExitIfDirty();
    }
}
