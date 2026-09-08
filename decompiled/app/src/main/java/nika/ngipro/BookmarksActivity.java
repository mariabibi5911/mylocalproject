package nika.ngipro;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.EnvironmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;
import nika.ngipro.BookmarkAdapter;
import nika.ngipro.BookmarkManager;

/* loaded from: classes4.dex */
public class BookmarksActivity extends AppCompatActivity {
    public static final String EXTRA_FILE_PATH = "file_path";
    private BookmarkAdapter adapter;
    private List<BookmarkManager.Bookmark> bookmarks;
    private View emptyText;
    private String fileName;
    private String filePath;
    private RecyclerView recycler;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.attach(newBase));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        String stringExtra = getIntent().getStringExtra("file_path");
        this.filePath = stringExtra;
        this.fileName = stringExtra != null ? new File(this.filePath).getName() : EnvironmentCompat.MEDIA_UNKNOWN;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBookmarks);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.BookmarksActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BookmarksActivity.this.m1724lambda$onCreate$0$nikangiproBookmarksActivity(view);
            }
        });
        toolbar.setSubtitle(this.fileName);
        this.recycler = (RecyclerView) findViewById(R.id.bookmarksRecycler);
        this.emptyText = findViewById(R.id.bookmarksEmpty);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btnAddBookmark).setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.BookmarksActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BookmarksActivity.this.m1725lambda$onCreate$1$nikangiproBookmarksActivity(view);
            }
        });
        reload();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-BookmarksActivity, reason: not valid java name */
    public /* synthetic */ void m1724lambda$onCreate$0$nikangiproBookmarksActivity(View v) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$nika-ngipro-BookmarksActivity, reason: not valid java name */
    public /* synthetic */ void m1725lambda$onCreate$1$nikangiproBookmarksActivity(View v) {
        showAddDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reload() {
        this.bookmarks = BookmarkManager.load(this, this.fileName);
        BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(this.bookmarks, new BookmarkAdapter.OnClick() { // from class: nika.ngipro.BookmarksActivity.1
            @Override // nika.ngipro.BookmarkAdapter.OnClick
            public void onOpen(int index) {
                BookmarkManager.Bookmark b = (BookmarkManager.Bookmark) BookmarksActivity.this.bookmarks.get(index);
                Toast.makeText(BookmarksActivity.this, String.format("0x%08X — %s", Long.valueOf(b.address), b.label), 1).show();
            }

            @Override // nika.ngipro.BookmarkAdapter.OnClick
            public void onDelete(int index) {
                BookmarksActivity bookmarksActivity = BookmarksActivity.this;
                BookmarkManager.remove(bookmarksActivity, bookmarksActivity.fileName, index);
                BookmarksActivity.this.reload();
            }
        });
        this.adapter = bookmarkAdapter;
        this.recycler.setAdapter(bookmarkAdapter);
        this.emptyText.setVisibility(this.bookmarks.isEmpty() ? 0 : 8);
    }

    private void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(1);
        l.setPadding(40, 20, 40, 20);
        final EditText addr = new EditText(this);
        addr.setHint("Hex address (e.g. 1A0)");
        final EditText label = new EditText(this);
        label.setHint("Label (e.g. \"login check\")");
        final EditText note = new EditText(this);
        note.setHint("Note (optional)");
        l.addView(addr);
        l.addView(label);
        l.addView(note);
        new AlertDialog.Builder(this, android.R.style.Theme.DeviceDefault.Dialog.Alert).setTitle("➕ New Bookmark").setView(l).setPositiveButton("Save", new DialogInterface.OnClickListener() { // from class: nika.ngipro.BookmarksActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                BookmarksActivity.this.m1726lambda$showAddDialog$2$nikangiproBookmarksActivity(addr, label, note, dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showAddDialog$2$nika-ngipro-BookmarksActivity, reason: not valid java name */
    public /* synthetic */ void m1726lambda$showAddDialog$2$nikangiproBookmarksActivity(EditText addr, EditText label, EditText note, DialogInterface d, int w) {
        try {
            long address = Long.parseLong(addr.getText().toString().trim(), 16);
            String labelText = label.getText().toString().trim();
            String noteText = note.getText().toString().trim();
            BookmarkManager.add(this, this.fileName, address, labelText.isEmpty() ? "Bookmark" : labelText, noteText);
            reload();
        } catch (Exception e) {
            Toast.makeText(this, "Invalid address format", 0).show();
        }
    }
}
