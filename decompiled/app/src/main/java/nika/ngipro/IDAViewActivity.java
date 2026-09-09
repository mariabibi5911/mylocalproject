package nika.ngipro;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import nika.ngipro.FunctionAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class IDAViewActivity extends AppCompatActivity {
    public static final String EXTRA_FILE_PATH = "file_path";
    private View detailContainer;
    private TextView detailText;
    private String filePath;
    private TextView functionCountText;
    private View functionListContainer;
    private RecyclerView functionRecycler;
    private TextView tabDisasm;
    private TextView tabPseudo;
    private Toolbar toolbarDetail;
    private String currentDisasm = "";
    private String currentPseudo = "";
    private boolean showingDisasm = true;

    public native String buildCfgForRange(String str, long j, int i);

    public native String decompileFunction(String str, long j, int i);

    public native String disassembleARM64(byte[] bArr, long j);

    public native String listFunctions(String str);

    public native String scanFunctions(String str, int i);

    public native String trackReferences(String str, long j, int i);

    static {
        System.loadLibrary("ngi_pro_engine");
        System.loadLibrary("sdkgen");
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
        setContentView(R.layout.activity_ida_view);
        this.filePath = getIntent().getStringExtra("file_path");
        this.functionListContainer = findViewById(R.id.functionListContainer);
        this.detailContainer = findViewById(R.id.detailContainer);
        this.functionRecycler = (RecyclerView) findViewById(R.id.functionRecycler);
        this.functionCountText = (TextView) findViewById(R.id.functionCountText);
        this.detailText = (TextView) findViewById(R.id.detailText);
        this.tabDisasm = (TextView) findViewById(R.id.tabDisasm);
        this.tabPseudo = (TextView) findViewById(R.id.tabPseudo);
        this.toolbarDetail = (Toolbar) findViewById(R.id.toolbarDetail);
        Toolbar toolbarList = (Toolbar) findViewById(R.id.toolbarList);
        toolbarList.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IDAViewActivity.this.m1755lambda$onCreate$0$nikangiproIDAViewActivity(view);
            }
        });
        this.toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IDAViewActivity.this.m1756lambda$onCreate$1$nikangiproIDAViewActivity(view);
            }
        });
        this.functionRecycler.setLayoutManager(new LinearLayoutManager(this));
        this.tabDisasm.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IDAViewActivity.this.m1757lambda$onCreate$2$nikangiproIDAViewActivity(view);
            }
        });
        this.tabPseudo.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IDAViewActivity.this.m1758lambda$onCreate$3$nikangiproIDAViewActivity(view);
            }
        });
        loadFunctions();
    }

    /* renamed from: lambda$onCreate$0$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1755lambda$onCreate$0$nikangiproIDAViewActivity(View v) {
        finish();
    }

    /* renamed from: lambda$onCreate$1$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1756lambda$onCreate$1$nikangiproIDAViewActivity(View v) {
        showFunctionList();
    }

    /* renamed from: lambda$onCreate$2$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1757lambda$onCreate$2$nikangiproIDAViewActivity(View v) {
        switchTab(true);
    }

    /* renamed from: lambda$onCreate$3$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1758lambda$onCreate$3$nikangiproIDAViewActivity(View v) {
        switchTab(false);
    }

    private void loadFunctions() {
        String str = this.filePath;
        if (str == null || str.isEmpty()) {
            Toast.makeText(this, "No file loaded", 0).show();
            finish();
        } else {
            new Thread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    IDAViewActivity.this.m1754lambda$loadFunctions$7$nikangiproIDAViewActivity();
                }
            }).start();
        }
    }

    /* renamed from: lambda$loadFunctions$7$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1754lambda$loadFunctions$7$nikangiproIDAViewActivity() {
        IDAViewActivity iDAViewActivity;
        final List<FunctionAdapter.FunctionEntry> entries = new ArrayList<>();
        try {
            Map<Long, FunctionAdapter.FunctionEntry> byAddress = new LinkedHashMap<>();
            String json = listFunctions(this.filePath);
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    FunctionAdapter.FunctionEntry e = new FunctionAdapter.FunctionEntry();
                    e.name = o.optString("name", "sub_unknown");
                    e.address = o.optLong("address");
                    e.size = o.optLong("size");
                    if (e.size > 0) {
                        byAddress.put(Long.valueOf(e.address), e);
                    }
                }
            } catch (Exception e2) {
            }
            String scanResult = scanFunctions(this.filePath, 400);
            int codeOnlyCount = 0;
            if (scanResult != null) {
                try {
                    if (!scanResult.isEmpty()) {
                        String[] split = scanResult.split("\n");
                        int length = split.length;
                        char c = 0;
                        int codeOnlyCount2 = 0;
                        int i2 = 0;
                        while (i2 < length) {
                            String line = split[i2];
                            String[] parts = line.split("\\|");
                            if (parts.length == 2) {
                                try {
                                    long addr = Long.parseLong(parts[c].replace("0x", ""), 16);
                                    long size = Long.parseLong(parts[1]);
                                    if (!byAddress.containsKey(Long.valueOf(addr))) {
                                        FunctionAdapter.FunctionEntry e3 = new FunctionAdapter.FunctionEntry();
                                        e3.address = addr;
                                        e3.size = size;
                                        Object[] objArr = new Object[1];
                                        try {
                                            objArr[0] = Long.valueOf(addr);
                                            e3.name = String.format("sub_%X [code]", objArr);
                                            byAddress.put(Long.valueOf(addr), e3);
                                            codeOnlyCount2++;
                                        } catch (Exception e4) {
                                        }
                                    }
                                } catch (Exception e5) {
                                }
                            }
                            i2++;
                            c = 0;
                        }
                        codeOnlyCount = codeOnlyCount2;
                    }
                } catch (Throwable th) {
                    iDAViewActivity = this;
                    final String errorMsg = "Error scanning functions: " + th.getClass().getSimpleName();
                    iDAViewActivity.runOnUiThread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            IDAViewActivity.this.m1753lambda$loadFunctions$6$nikangiproIDAViewActivity(errorMsg, entries);
                        }
                    });
                }
            }
            entries.addAll(byAddress.values());
            entries.sort(new Comparator() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda6
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Long.compare(((FunctionAdapter.FunctionEntry) obj).address, ((FunctionAdapter.FunctionEntry) obj2).address);
                    return compare;
                }
            });
            final int finalCodeOnlyCount = codeOnlyCount;
            final int symbolCount = entries.size() - codeOnlyCount;
            iDAViewActivity = this;
            try {
                iDAViewActivity.runOnUiThread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        IDAViewActivity.this.m1752lambda$loadFunctions$5$nikangiproIDAViewActivity(entries, symbolCount, finalCodeOnlyCount);
                    }
                });
            } catch (Throwable th2) {
                final String errorMsg2 = "Error scanning functions: " + th2.getClass().getSimpleName();
                iDAViewActivity.runOnUiThread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        IDAViewActivity.this.m1753lambda$loadFunctions$6$nikangiproIDAViewActivity(errorMsg2, entries);
                    }
                });
            }
        } catch (Throwable th3) {
            iDAViewActivity = this;
        }
    }

    /* renamed from: lambda$loadFunctions$5$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1752lambda$loadFunctions$5$nikangiproIDAViewActivity(List entries, int symbolCount, int finalCodeOnlyCount) {
        this.functionCountText.setText(entries.size() + " functions  (" + symbolCount + " from symbols, " + finalCodeOnlyCount + " from code analysis)");
        this.functionRecycler.setAdapter(new FunctionAdapter(entries, new IDAViewActivity$$ExternalSyntheticLambda5(this)));
        if (entries.isEmpty()) {
            this.functionCountText.setText("No functions found");
        }
    }

    /* renamed from: lambda$loadFunctions$6$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1753lambda$loadFunctions$6$nikangiproIDAViewActivity(String finalError, List entries) {
        this.functionCountText.setText(finalError);
        this.functionRecycler.setAdapter(new FunctionAdapter(entries, new IDAViewActivity$$ExternalSyntheticLambda5(this)));
    }

    public void openFunction(final FunctionAdapter.FunctionEntry entry) {
        this.toolbarDetail.setTitle(entry.name);
        this.detailText.setText("Loading…");
        this.functionListContainer.setVisibility(8);
        this.detailContainer.setVisibility(0);
        this.showingDisasm = true;
        updateTabStyles();
        new Thread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                IDAViewActivity.this.m1760lambda$openFunction$11$nikangiproIDAViewActivity(entry);
            }
        }).start();
    }

    /* renamed from: lambda$openFunction$11$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1760lambda$openFunction$11$nikangiproIDAViewActivity(FunctionAdapter.FunctionEntry entry) {
        try {
            int len = (int) Math.min(entry.size, 4096L);
            if (len <= 0) {
                runOnUiThread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        IDAViewActivity.this.m1761lambda$openFunction$8$nikangiproIDAViewActivity();
                    }
                });
                return;
            }
            byte[] buf = new byte[len];
            RandomAccessFile raf = new RandomAccessFile(this.filePath, "r");
            try {
                raf.seek(entry.address);
                raf.readFully(buf, 0, len);
                raf.close();
                this.currentDisasm = disassembleARM64(buf, entry.address);
                this.currentPseudo = decompileFunction(this.filePath, entry.address, len);
                runOnUiThread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        IDAViewActivity.this.m1762lambda$openFunction$9$nikangiproIDAViewActivity();
                    }
                });
            } finally {
            }
        } catch (Throwable t) {
            final String msg = "[-] Error: " + t.getClass().getSimpleName() + (t.getMessage() != null ? " — " + t.getMessage() : "");
            runOnUiThread(new Runnable() { // from class: nika.ngipro.IDAViewActivity$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    IDAViewActivity.this.m1759lambda$openFunction$10$nikangiproIDAViewActivity(msg);
                }
            });
        }
    }

    /* renamed from: lambda$openFunction$8$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1761lambda$openFunction$8$nikangiproIDAViewActivity() {
        this.detailText.setText("[-] Invalid function size");
    }

    /* renamed from: lambda$openFunction$9$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1762lambda$openFunction$9$nikangiproIDAViewActivity() {
        this.detailText.setText(this.currentDisasm);
    }

    /* renamed from: lambda$openFunction$10$nika-ngipro-IDAViewActivity */
    public /* synthetic */ void m1759lambda$openFunction$10$nikangiproIDAViewActivity(String msg) {
        this.detailText.setText(msg);
    }

    private void switchTab(boolean disasm) {
        this.showingDisasm = disasm;
        this.detailText.setText(disasm ? this.currentDisasm : this.currentPseudo);
        updateTabStyles();
    }

    private void updateTabStyles() {
        int accent = ThemeManager.getAccent(this);
        this.tabDisasm.setTextColor(this.showingDisasm ? accent : -9737350);
        this.tabPseudo.setTextColor(this.showingDisasm ? -9737350 : accent);
    }

    private void showFunctionList() {
        this.detailContainer.setVisibility(8);
        this.functionListContainer.setVisibility(0);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.detailContainer.getVisibility() == 0) {
            showFunctionList();
        } else {
            super.onBackPressed();
        }
    }
}
