package nika.ngipro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.neomods.libdumper.jni.NativeLibWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nika.ngipro.ToolsAdapter;
import nika.ngipro.UpdateChecker;
import nika.ngipro.databinding.ActivityMainBinding;
import nika.ngipro.auth.SessionStore;
import nika.ngipro.mod.ModManagerActivity;

/* loaded from: classes4.dex */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TOOL_AUTO_ANALYSIS = 1;
    private static final int TOOL_BOOKMARKS = 18;
    private static final int TOOL_CFG = 11;
    private static final int TOOL_DECOMPILER = 6;
    private static final int TOOL_DISASM_ARM64 = 4;
    private static final int TOOL_DISASM_DEX = 5;
    private static final int TOOL_FUNCTIONS = 2;
    private static final int TOOL_FUNC_EXPLORER = 17;
    private static final int TOOL_HEX_EDITOR = 14;
    private static final int TOOL_HEX_PATCHER = 7;
    private static final int TOOL_IDA_VIEW = 15;
    private static final int TOOL_IMPORTS = 10;
    private static final int TOOL_LIBDUMPER = 13;
    private static final int TOOL_SDK_GEN = 12;
    private static final int TOOL_SEARCH = 8;
    private static final int TOOL_SECTIONS = 9;
    private static final int TOOL_STRINGS = 3;
    private static final int TOOL_XREFS = 16;
    private static final int TOOL_MOD_MANAGER = 19;
    private ActivityMainBinding binding;
    private String currentFilePath = "";
    private ResultsAdapter resultsAdapter;

    public native String analyzeElfFile(String str);

    public native String autoAnalyze(String str);

    public native String buildCfgJson(String str);

    public native String decompileFunction(String str, long j, int i);

    public native String disassembleARM64(byte[] bArr, long j);

    public native String dumpFunctionBytes(String str, long j);

    public native String findCallers(String str, long j);

    public native String findReferences(String str, long j);

    public native String fullDumpToDir(String str, String str2);

    public native String generateSdk(String str, String str2);

    public native String huntFunctions(String str);

    public native String listFunctions(String str);

    public native String listSections(String str);

    public native boolean patchFile(String str, long j, String str2);

    public native String resolveImports(String str);

    public native String scanFunctions(String str, int i);

    public native String searchInBinary(String str, String str2);

    public native String smartScanner(String str);

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
        ActivityMainBinding inflate = ActivityMainBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        setSupportActionBar(this.binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.binding.drawerLayout, this.binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        this.binding.navView.setNavigationItemSelectedListener(this);
        setupToolsGrid();
        setupResultsList();
        updateHomeProfileStrip();
        this.binding.profileStrip.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m1782lambda$onCreate$0$nikangiproMainActivity(view);
            }
        });
        this.binding.btnSelect.setOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m1783lambda$onCreate$2$nikangiproMainActivity(view);
            }
        });
        UpdateChecker.checkSilently(this, new UpdateChecker.Callback() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda22
            @Override // nika.ngipro.UpdateChecker.Callback
            public final void onResult(UpdateChecker.UpdateInfo updateInfo) {
                MainActivity.this.m1784lambda$onCreate$3$nikangiproMainActivity(updateInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1782lambda$onCreate$0$nikangiproMainActivity(View v) {
        openScreen(new Intent(this, (Class<?>) ProfileActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$2$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1783lambda$onCreate$2$nikangiproMainActivity(final View v) {
        v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80L).withEndAction(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(120L).start();
            }
        }).start();
        Intent intent = new Intent("android.intent.action.GET_CONTENT").setType("*/*");
        startActivityForResult(intent, TypedValues.TYPE_TARGET);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$3$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1784lambda$onCreate$3$nikangiproMainActivity(UpdateChecker.UpdateInfo info) {
        if (info != null) {
            showUpdateDialog(info);
        }
    }

    private void showUpdateDialog(final UpdateChecker.UpdateInfo info) {
        String message = "Version " + info.latestVersion + " is available.\n\n" + ((info.changelog == null || info.changelog.isEmpty()) ? "See the update page for details." : info.changelog);
        new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert).setTitle("⚡ Update available").setMessage(message).setPositiveButton("Update Now", new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda10
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1803lambda$showUpdateDialog$4$nikangiproMainActivity(info, dialogInterface, i);
            }
        }).setNegativeButton("Later", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showUpdateDialog$4$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1803lambda$showUpdateDialog$4$nikangiproMainActivity(UpdateChecker.UpdateInfo info, DialogInterface d, int w) {
        UpdateChecker.openUpdateLink(this, info.libUrl);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        applyAccentColor();
        updateHomeProfileStrip();
    }

    private void applyAccentColor() {
        int accent = ThemeManager.getAccent(this);
        this.binding.toolbar.setTitleTextColor(accent);
        this.binding.btnSelect.setBackgroundTintList(ColorStateList.valueOf(accent));
    }

    private void updateHomeProfileStrip() {
        SharedPreferences prefs = getSharedPreferences("NGI_PREFS", 0);
        String avatarUrl = XorUtil.deobfuscate(prefs.getString("github_avatar_x", ""));
        String login = XorUtil.deobfuscate(prefs.getString("github_login_x", null));
        nika.ngipro.auth.AuthSession session = new SessionStore(this).currentSession();
        if ((login == null || login.isEmpty()) && session != null) login = session.username;
        if ((avatarUrl == null || avatarUrl.isEmpty()) && session != null) avatarUrl = session.avatarUrl;
        if (login == null) {
            login = "NGI User";
        }
        this.binding.homeUserName.setText(login);
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with((FragmentActivity) this).load(avatarUrl).circleCrop().into(this.binding.homeAvatar);
        }
    }

    private void setupResultsList() {
        this.resultsAdapter = new ResultsAdapter();
        this.binding.resultsList.setLayoutManager(new LinearLayoutManager(this));
        this.binding.resultsList.setAdapter(this.resultsAdapter);
        this.binding.emptyState.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: showResults, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void m1807lambda$showXrefsDialog$18$nikangiproMainActivity(String text) {
        hideLoading();
        this.resultsAdapter.submit(text);
        this.binding.emptyState.setVisibility(this.resultsAdapter.isEmpty() ? 0 : 8);
        this.binding.resultsList.setAlpha(0.3f);
        this.binding.resultsList.animate().alpha(1.0f).setDuration(250L).start();
    }

    private void showLoading(String label) {
        this.binding.emptyState.setVisibility(0);
        this.binding.emptyStateIcon.setVisibility(8);
        this.binding.emptyStateProgress.setVisibility(0);
        this.binding.emptyStateText.setText(label);
    }

    private void hideLoading() {
        this.binding.emptyStateIcon.setVisibility(0);
        this.binding.emptyStateProgress.setVisibility(8);
        this.binding.emptyStateText.setText(R.string.results_empty_state);
    }

    private void setupToolsGrid() {
        List<ToolsAdapter.ToolItem> tools = new ArrayList<>();
        tools.add(new ToolsAdapter.ToolItem(1, android.R.drawable.ic_menu_view, getString(R.string.nav_auto_analysis)));
        tools.add(new ToolsAdapter.ToolItem(2, android.R.drawable.ic_menu_agenda, getString(R.string.nav_functions)));
        tools.add(new ToolsAdapter.ToolItem(3, android.R.drawable.ic_menu_edit, getString(R.string.nav_strings)));
        tools.add(new ToolsAdapter.ToolItem(4, android.R.drawable.ic_menu_manage, getString(R.string.nav_disasm_arm64)));
        tools.add(new ToolsAdapter.ToolItem(5, android.R.drawable.ic_menu_sort_by_size, getString(R.string.nav_disasm_dex)));
        tools.add(new ToolsAdapter.ToolItem(6, android.R.drawable.ic_menu_slideshow, getString(R.string.nav_decompiler)));
        tools.add(new ToolsAdapter.ToolItem(7, android.R.drawable.ic_menu_edit, getString(R.string.nav_hex_patcher)));
        tools.add(new ToolsAdapter.ToolItem(8, android.R.drawable.ic_menu_search, getString(R.string.nav_search)));
        tools.add(new ToolsAdapter.ToolItem(9, android.R.drawable.ic_menu_sort_alphabetically, getString(R.string.nav_sections)));
        tools.add(new ToolsAdapter.ToolItem(10, android.R.drawable.ic_menu_share, getString(R.string.nav_imports)));
        tools.add(new ToolsAdapter.ToolItem(11, android.R.drawable.ic_menu_mapmode, getString(R.string.nav_cfg)));
        tools.add(new ToolsAdapter.ToolItem(12, android.R.drawable.ic_menu_save, getString(R.string.nav_sdk_gen)));
        tools.add(new ToolsAdapter.ToolItem(13, android.R.drawable.ic_menu_compass, getString(R.string.nav_libdumper)));
        tools.add(new ToolsAdapter.ToolItem(14, android.R.drawable.ic_menu_edit, getString(R.string.nav_hex_editor)));
        tools.add(new ToolsAdapter.ToolItem(15, android.R.drawable.ic_menu_zoom, getString(R.string.nav_ida_view)));
        tools.add(new ToolsAdapter.ToolItem(16, android.R.drawable.ic_menu_myplaces, getString(R.string.nav_xrefs)));
        tools.add(new ToolsAdapter.ToolItem(17, android.R.drawable.ic_menu_recent_history, getString(R.string.nav_func_explorer)));
        tools.add(new ToolsAdapter.ToolItem(18, android.R.drawable.ic_menu_agenda, getString(R.string.nav_bookmarks)));
        tools.add(new ToolsAdapter.ToolItem(19, android.R.drawable.ic_menu_manage, getString(R.string.nav_mod_manager)));
        tools.add(new ToolsAdapter.ToolItem(20, android.R.drawable.ic_menu_sort_by_size, getString(R.string.nav_batch_processor)));
        tools.add(new ToolsAdapter.ToolItem(21, android.R.drawable.ic_menu_search, getString(R.string.nav_secret_scanner)));
        this.binding.toolsGrid.setLayoutManager(new GridLayoutManager(this, 3));
        ToolsAdapter toolsAdapter = new ToolsAdapter(tools, new ToolsAdapter.OnToolClick() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda26
            @Override // nika.ngipro.ToolsAdapter.OnToolClick
            public final void onClick(int i) {
                MainActivity.this.runTool(i);
            }
        });
        this.binding.toolsGrid.setAdapter(toolsAdapter);
        animateGridEntrance();
    }

    private void animateGridEntrance() {
        this.binding.toolsGrid.post(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m1778lambda$animateGridEntrance$5$nikangiproMainActivity();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$animateGridEntrance$5$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1778lambda$animateGridEntrance$5$nikangiproMainActivity() {
        int childCount = this.binding.toolsGrid.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.binding.toolsGrid.getChildAt(i);
            if (child != null) {
                child.setAlpha(0.0f);
                child.setScaleX(0.85f);
                child.setScaleY(0.85f);
                child.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setStartDelay(i * 30).setDuration(220L).setInterpolator(new DecelerateInterpolator()).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runTool(final int toolId) {
        if (this.currentFilePath.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_select_file), 0).show();
            return;
        }
        if (toolId == 7) {
            showPatchDialog();
            return;
        }
        if (toolId == 8) {
            showSearchDialog();
            return;
        }
        if (toolId == 4) {
            startDisassemblyDialog();
            return;
        }
        if (toolId == 5) {
            startDexDisassembler();
            return;
        }
        if (toolId == 6) {
            runDecompilerTool();
            return;
        }
        if (toolId == 13) {
            runLibDumperTool();
        }
        if (toolId == 13) {
            runLibDumperTool();
            return;
        }
        if (toolId == 14) {
            Intent intent = new Intent(this, (Class<?>) HexEditorActivity.class);
            intent.putExtra("file_path", this.currentFilePath);
            openScreen(intent);
            return;
        }
        if (toolId == 15) {
            Intent intent2 = new Intent(this, (Class<?>) IDAViewActivity.class);
            intent2.putExtra("file_path", this.currentFilePath);
            openScreen(intent2);
        } else {
            if (toolId == 16) {
                showXrefsDialog();
                return;
            }
            if (toolId == 17) {
                runFunctionExplorer();
                return;
            }
            if (toolId == 18) {
                Intent intent3 = new Intent(this, (Class<?>) BookmarksActivity.class);
                intent3.putExtra("file_path", this.currentFilePath);
                openScreen(intent3);
            } else if (toolId == 19) {
                Intent intent4 = new Intent(this, (Class<?>) nika.ngipro.mod.ModManagerActivity.class);
                intent4.putExtra("file_path", this.currentFilePath);
                openScreen(intent4);
            } else if (toolId == 20) {
                Intent intent5 = new Intent(this, (Class<?>) BatchProcessorActivity.class);
                openScreen(intent5);
            } else if (toolId == 21) {
                Intent intent6 = new Intent(this, (Class<?>) SecretScannerActivity.class);
                openScreen(intent6);
            } else {
                showLoading("Running…");
                new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.m1799lambda$runTool$7$nikangiproMainActivity(toolId);
                    }
                }).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runTool$7$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1799lambda$runTool$7$nikangiproMainActivity(int toolId) {
        String result = "";
        try {
        } catch (Throwable t) {
            result = "[-] Unexpected error in tool: " + t.getClass().getSimpleName() + (t.getMessage() != null ? " — " + t.getMessage() : "");
        }
        if (toolId == 1) {
            result = autoAnalyze(this.currentFilePath);
        } else if (toolId == 2) {
            result = huntFunctions(this.currentFilePath);
        } else if (toolId == 3) {
            result = smartScanner(this.currentFilePath);
        } else if (toolId == 9) {
            result = listSections(this.currentFilePath);
        } else if (toolId == 10) {
            result = resolveImports(this.currentFilePath);
        } else {
            if (toolId != 11) {
                if (toolId == 12) {
                    String outDir = getExternalFilesDir(null) + "/SDK_Output";
                    result = generateSdk(this.currentFilePath, outDir);
                }
                final String finalResult = result;
                runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda29
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.m1798lambda$runTool$6$nikangiproMainActivity(finalResult);
                    }
                });
            }
            result = buildCfgJson(this.currentFilePath);
        }
        final String finalResult2 = result;
        runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m1798lambda$runTool$6$nikangiproMainActivity(finalResult2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runTool$6$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1798lambda$runTool$6$nikangiproMainActivity(String finalResult) {
        if (finalResult.isEmpty()) {
            return;
        }
        m1807lambda$showXrefsDialog$18$nikangiproMainActivity(finalResult);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null) {
            new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1779lambda$onActivityResult$10$nikangiproMainActivity(data);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$10$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1779lambda$onActivityResult$10$nikangiproMainActivity(Intent data) {
        try {
            final File f = new File(getFilesDir(), "target.so");
            InputStream is = getContentResolver().openInputStream(data.getData());
            FileOutputStream os = new FileOutputStream(f);
            byte[] b = new byte[8192];
            while (true) {
                int l = is.read(b);
                if (l <= 0) {
                    os.close();
                    is.close();
                    this.currentFilePath = f.getAbsolutePath();
                    incrementFilesAnalyzedCount();
                    final String result = analyzeElfFile(this.currentFilePath);
                    runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainActivity.this.m1780lambda$onActivityResult$8$nikangiproMainActivity(f, result);
                        }
                    });
                    return;
                }
                os.write(b, 0, l);
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1781lambda$onActivityResult$9$nikangiproMainActivity(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$8$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1780lambda$onActivityResult$8$nikangiproMainActivity(File f, String result) {
        this.binding.statusFileText.setText(f.getName());
        m1807lambda$showXrefsDialog$18$nikangiproMainActivity(">>> " + getString(R.string.file_loaded) + ": " + f.getName() + "\n" + result);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onActivityResult$9$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1781lambda$onActivityResult$9$nikangiproMainActivity(Exception e) {
        m1807lambda$showXrefsDialog$18$nikangiproMainActivity("[-] Error: " + e.getMessage());
    }

    private void incrementFilesAnalyzedCount() {
        SharedPreferences prefs = getSharedPreferences("NGI_PREFS", 0);
        int count = prefs.getInt("files_analyzed_count", 0);
        prefs.edit().putInt("files_analyzed_count", count + 1).apply();
    }

    @Override // com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        if (id == R.id.nav_profile) {
            openScreen(new Intent(this, (Class<?>) ProfileActivity.class));
            return true;
        }
        if (id == R.id.nav_settings) {
            openScreen(new Intent(this, (Class<?>) SettingsActivity.class));
            return true;
        }
        if (id == R.id.nav_language) {
            showLanguageDialog();
            return true;
        }
        if (id != R.id.nav_logout) {
            return false;
        }
        performLogout();
        return true;
    }

    private void runLibDumperTool() {
        if (!NativeLibWrapper.Companion.isNativeAvailable()) {
            m1807lambda$showXrefsDialog$18$nikangiproMainActivity("[-] libNeoLibDumper.so not found.\nBuild it via GitHub Actions and place it under:\napp/src/main/jniLibs/<abi>/libNeoLibDumper.so");
        } else {
            showLoading("Analyzing with Rust engine…");
            new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1797lambda$runLibDumperTool$14$nikangiproMainActivity();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runLibDumperTool$14$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1797lambda$runLibDumperTool$14$nikangiproMainActivity() {
        try {
            NativeLibWrapper.DumpConfig config = new NativeLibWrapper.DumpConfig();
            if (!NativeLibWrapper.Companion.validateElf(this.currentFilePath)) {
                runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.m1794lambda$runLibDumperTool$11$nikangiproMainActivity();
                    }
                });
                return;
            }
            NativeLibWrapper.ElfInfo info = NativeLibWrapper.Companion.loadElf(this.currentFilePath);
            int symCount = NativeLibWrapper.Companion.extractSymbols(this.currentFilePath, config);
            int classCount = NativeLibWrapper.Companion.reconstructClasses(config);
            int nsCount = NativeLibWrapper.Companion.detectNamespaces(config);
            String outDir = getExternalFilesDir(null) + "/LibDumper_Output";
            new File(outDir).mkdirs();
            String dumpCpp = NativeLibWrapper.Companion.generateDumpCpp(config);
            writeFile(outDir + "/Dump.cpp", dumpCpp);
            String symbolTable = NativeLibWrapper.Companion.generateSymbolTable(config);
            writeFile(outDir + "/SymbolTable.txt", symbolTable);
            final StringBuilder sb = new StringBuilder();
            sb.append("[+] LibDumper (Rust engine v").append(NativeLibWrapper.Companion.getVersion()).append(")\n");
            if (info != null) {
                sb.append(info.fileName).append(" (").append(info.architecture).append(", ").append(info.bitWidth).append("-bit)\n");
            }
            sb.append("Symbols: ").append(symCount).append("\n");
            sb.append("Classes reconstructed: ").append(classCount).append("\n");
            sb.append("Namespaces: ").append(nsCount).append("\n");
            sb.append("Saved: Dump.cpp + SymbolTable.txt\n");
            sb.append("Location: ").append(outDir);
            runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1795lambda$runLibDumperTool$12$nikangiproMainActivity(sb);
                }
            });
        } catch (Throwable t) {
            runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1796lambda$runLibDumperTool$13$nikangiproMainActivity(t);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runLibDumperTool$11$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1794lambda$runLibDumperTool$11$nikangiproMainActivity() {
        m1807lambda$showXrefsDialog$18$nikangiproMainActivity("[-] Not a valid ELF file");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runLibDumperTool$12$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1795lambda$runLibDumperTool$12$nikangiproMainActivity(StringBuilder sb) {
        m1807lambda$showXrefsDialog$18$nikangiproMainActivity(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runLibDumperTool$13$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1796lambda$runLibDumperTool$13$nikangiproMainActivity(Throwable t) {
        m1807lambda$showXrefsDialog$18$nikangiproMainActivity("[-] LibDumper error: " + t.getClass().getSimpleName() + (t.getMessage() != null ? " — " + t.getMessage() : ""));
    }

    private void writeFile(String path, String content) {
        try {
            FileWriter fw = new FileWriter(path);
            try {
                fw.write(content);
                fw.close();
            } finally {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showXrefsDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        b.setTitle("🔗 CROSS REFERENCES / CALL GRAPH");
        final EditText input = new EditText(this);
        input.setHint(getString(R.string.dlg_hex_offset_hint));
        b.setView(input);
        b.setPositiveButton("DATA REFS", new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda21
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1806lambda$showXrefsDialog$17$nikangiproMainActivity(input, dialogInterface, i);
            }
        });
        b.setNeutralButton("CALLERS (BL)", new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda23
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1809lambda$showXrefsDialog$20$nikangiproMainActivity(input, dialogInterface, i);
            }
        });
        b.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showXrefsDialog$17$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1806lambda$showXrefsDialog$17$nikangiproMainActivity(EditText input, DialogInterface d, int w) {
        try {
            final long addr = Long.parseLong(input.getText().toString().trim(), 16);
            new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1805lambda$showXrefsDialog$16$nikangiproMainActivity(addr);
                }
            }).start();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.dlg_invalid_format), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showXrefsDialog$16$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1805lambda$showXrefsDialog$16$nikangiproMainActivity(long addr) {
        String result;
        try {
            result = findReferences(this.currentFilePath, addr);
        } catch (Throwable t) {
            result = "[-] Xrefs error: " + t.getClass().getSimpleName();
        }
        final String finalResult = result;
        runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m1807lambda$showXrefsDialog$18$nikangiproMainActivity(finalResult);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showXrefsDialog$20$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1809lambda$showXrefsDialog$20$nikangiproMainActivity(EditText input, DialogInterface d, int w) {
        try {
            final long addr = Long.parseLong(input.getText().toString().trim(), 16);
            new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1808lambda$showXrefsDialog$19$nikangiproMainActivity(addr);
                }
            }).start();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.dlg_invalid_format), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showXrefsDialog$19$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1808lambda$showXrefsDialog$19$nikangiproMainActivity(long addr) {
        String result;
        try {
            result = findCallers(this.currentFilePath, addr);
        } catch (Throwable t) {
            result = "[-] Call graph error: " + t.getClass().getSimpleName();
        }
        final String finalResult = result;
        runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m1807lambda$showXrefsDialog$18$nikangiproMainActivity(finalResult);
            }
        });
    }

    private void showPatchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle(getString(R.string.dlg_hex_patcher_title));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(1);
        l.setPadding(40, 20, 40, 20);
        final EditText off = new EditText(this);
        off.setHint(getString(R.string.dlg_offset_hint));
        final EditText hex = new EditText(this);
        hex.setHint(getString(R.string.dlg_bytes_hint));
        l.addView(off);
        l.addView(hex);
        builder.setView(l);
        builder.setPositiveButton(getString(R.string.dlg_patch_action), new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda27
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1801lambda$showPatchDialog$21$nikangiproMainActivity(off, hex, dialogInterface, i);
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showPatchDialog$21$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1801lambda$showPatchDialog$21$nikangiproMainActivity(EditText off, EditText hex, DialogInterface di, int wh) {
        try {
            long o = Long.parseLong(off.getText().toString().trim(), 16);
            if (patchFile(this.currentFilePath, o, hex.getText().toString())) {
                m1807lambda$showXrefsDialog$18$nikangiproMainActivity("[+] " + getString(R.string.dlg_patch_success) + " 0x" + Long.toHexString(o).toUpperCase());
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.dlg_invalid_format), 0).show();
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        b.setTitle(getString(R.string.dlg_search_title));
        final EditText input = new EditText(this);
        input.setHint(getString(R.string.dlg_search_hint));
        b.setView(input);
        b.setPositiveButton(getString(R.string.dlg_search_action), new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1802lambda$showSearchDialog$22$nikangiproMainActivity(input, dialogInterface, i);
            }
        });
        b.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showSearchDialog$22$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1802lambda$showSearchDialog$22$nikangiproMainActivity(EditText input, DialogInterface d, int w) {
        String q = input.getText().toString();
        m1807lambda$showXrefsDialog$18$nikangiproMainActivity(getString(R.string.dlg_searching_for) + ": " + q + "\n" + searchInBinary(this.currentFilePath, q));
    }

    private void startDisassemblyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle(getString(R.string.dlg_disasm_title));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(1);
        final EditText off = new EditText(this);
        off.setHint(getString(R.string.dlg_hex_offset_hint));
        final EditText len = new EditText(this);
        len.setHint(getString(R.string.dlg_byte_count_hint));
        l.addView(off);
        l.addView(len);
        builder.setView(l);
        builder.setPositiveButton(getString(R.string.dlg_disassemble_action), new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda20
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1810lambda$startDisassemblyDialog$23$nikangiproMainActivity(off, len, dialogInterface, i);
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$startDisassemblyDialog$23$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1810lambda$startDisassemblyDialog$23$nikangiproMainActivity(EditText off, EditText len, DialogInterface d, int w) {
        try {
            long o = Long.parseLong(off.getText().toString().trim(), 16);
            int n = Integer.parseInt(len.getText().toString().trim());
            RandomAccessFile raf = new RandomAccessFile(this.currentFilePath, "r");
            raf.seek(o);
            byte[] buffer = new byte[n];
            raf.read(buffer);
            raf.close();
            m1807lambda$showXrefsDialog$18$nikangiproMainActivity(disassembleARM64(buffer, o));
        } catch (Exception e) {
            m1807lambda$showXrefsDialog$18$nikangiproMainActivity("[-] Error: " + e.getMessage());
        }
    }

    private void startDexDisassembler() {
        Intent intent = new Intent(this, (Class<?>) DexDisassemblerActivity.class);
        intent.putExtra("file_path", this.currentFilePath);
        openScreen(intent);
    }

    private void runDecompilerTool() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("🧠 " + getString(R.string.nav_decompiler));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(1);
        final EditText off = new EditText(this);
        off.setHint(getString(R.string.dlg_hex_offset_hint));
        final EditText len = new EditText(this);
        len.setHint(getString(R.string.dlg_byte_count_hint));
        l.addView(off);
        l.addView(len);
        builder.setView(l);
        builder.setPositiveButton(getString(R.string.dlg_decompile_action), new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda31
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1787lambda$runDecompilerTool$26$nikangiproMainActivity(off, len, dialogInterface, i);
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runDecompilerTool$26$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1787lambda$runDecompilerTool$26$nikangiproMainActivity(EditText off, EditText len, DialogInterface d, int w) {
        try {
            final long o = Long.parseLong(off.getText().toString().trim(), 16);
            final int n = Integer.parseInt(len.getText().toString().trim());
            new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1786lambda$runDecompilerTool$25$nikangiproMainActivity(o, n);
                }
            }).start();
        } catch (Exception e) {
            m1807lambda$showXrefsDialog$18$nikangiproMainActivity("[-] Error: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runDecompilerTool$25$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1786lambda$runDecompilerTool$25$nikangiproMainActivity(long o, int n) {
        final String pseudo = decompileFunction(this.currentFilePath, o, n);
        runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m1807lambda$showXrefsDialog$18$nikangiproMainActivity(pseudo);
            }
        });
    }

    private void runFunctionExplorer() {
        if (this.currentFilePath.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_select_file), 0).show();
        } else {
            new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1793lambda$runFunctionExplorer$32$nikangiproMainActivity();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runFunctionExplorer$32$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1793lambda$runFunctionExplorer$32$nikangiproMainActivity() {
        int i;
        String[] strArr;
        long addr = 0L;
        try {
            Map<Long, String> namesByAddr = new LinkedHashMap<>();
            Map<Long, Integer> sizesByAddr = new LinkedHashMap<>();
            String raw = huntFunctions(this.currentFilePath);
            Pattern p = Pattern.compile("\\[FUNC]\\[(\\w+)] (\\S+) @ 0x([0-9a-fA-F]+) \\(size (\\d+)\\)");
            Matcher m = p.matcher(raw);
            while (true) {
                i = 2;
                if (!m.find()) {
                    break;
                }
                String name = m.group(2);
                long addr2 = Long.parseLong(m.group(3), 16);
                int size = Integer.parseInt(m.group(4));
                if (size != 0) {
                    namesByAddr.put(Long.valueOf(addr2), name);
                    sizesByAddr.put(Long.valueOf(addr2), Integer.valueOf(size));
                }
            }
            final int symbolCount = namesByAddr.size();
            String scanResult = scanFunctions(this.currentFilePath, 400);
            if (scanResult != null && !scanResult.isEmpty()) {
                String[] split = scanResult.split("\n");
                int length = split.length;
                char c = 0;
                int i2 = 0;
                while (i2 < length) {
                    String line = split[i2];
                    String[] parts = line.split("\\|");
                    if (parts.length != i) {
                        strArr = split;
                    } else {
                        try {
                            addr = Long.parseLong(parts[c].replace("0x", ""), 16);
                            strArr = split;
                        } catch (Exception e) {
                            strArr = split;
                        }
                        try {
                            int size2 = (int) Long.parseLong(parts[1]);
                            if (!namesByAddr.containsKey(Long.valueOf(addr))) {
                                Long valueOf = Long.valueOf(addr);
                                Object[] objArr = new Object[1];
                                try {
                                    objArr[0] = Long.valueOf(addr);
                                    namesByAddr.put(valueOf, String.format("sub_%X [code]", objArr));
                                    sizesByAddr.put(Long.valueOf(addr), Integer.valueOf(size2));
                                } catch (Exception e2) {
                                }
                            }
                        } catch (Exception e3) {
                            i2++;
                            split = strArr;
                            i = 2;
                            c = 0;
                        }
                    }
                    i2++;
                    split = strArr;
                    i = 2;
                    c = 0;
                }
            }
            final int codeOnlyCount = namesByAddr.size() - symbolCount;
            final List<String> names = new ArrayList<>();
            final List<Long> addresses = new ArrayList<>();
            final List<Integer> sizes = new ArrayList<>();
            List<Long> sortedAddrs = new ArrayList<>(namesByAddr.keySet());
            Collections.sort(sortedAddrs);
            Iterator<Long> it = sortedAddrs.iterator();
            while (it.hasNext()) {
                Long addr3 = it.next();
                String name2 = namesByAddr.get(addr3);
                int size3 = sizesByAddr.get(addr3).intValue();
                if (size3 != 0) {
                    names.add(name2 + "  (0x" + Long.toHexString(addr3.longValue()) + ", " + size3 + "B)");
                    addresses.add(addr3);
                    sizes.add(Integer.valueOf(size3));
                    it = it;
                }
            }
            runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1791lambda$runFunctionExplorer$30$nikangiproMainActivity(names, symbolCount, codeOnlyCount, addresses, sizes);
                }
            });
        } catch (Throwable t) {
            final String errMsg = "[-] Function Explorer error: " + t.getClass().getSimpleName();
            runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.m1807lambda$showXrefsDialog$18$nikangiproMainActivity(errMsg);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runFunctionExplorer$30$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1791lambda$runFunctionExplorer$30$nikangiproMainActivity(List names, int symbolCount, int codeOnlyCount, final List addresses, final List sizes) {
        if (names.isEmpty()) {
            Toast.makeText(this, "No functions found (symbols or code pattern)", 1).show();
        } else {
            new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert).setTitle(getString(R.string.nav_func_explorer) + " (" + names.size() + " — " + symbolCount + " sym, " + codeOnlyCount + " code)").setItems((CharSequence[]) names.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda25
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.m1790lambda$runFunctionExplorer$29$nikangiproMainActivity(addresses, sizes, dialogInterface, i);
                }
            }).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runFunctionExplorer$29$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1790lambda$runFunctionExplorer$29$nikangiproMainActivity(List addresses, List sizes, DialogInterface d, int which) {
        final long addr = ((Long) addresses.get(which)).longValue();
        final int size = Math.min(((Integer) sizes.get(which)).intValue(), 4096);
        new Thread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m1789lambda$runFunctionExplorer$28$nikangiproMainActivity(addr, size);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$runFunctionExplorer$28$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1789lambda$runFunctionExplorer$28$nikangiproMainActivity(long addr, int size) {
        String pseudo;
        try {
            pseudo = decompileFunction(this.currentFilePath, addr, size);
        } catch (Throwable t) {
            pseudo = "[-] Decompile error: " + t.getClass().getSimpleName();
        }
        final String finalPseudo = pseudo;
        runOnUiThread(new Runnable() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.m1807lambda$showXrefsDialog$18$nikangiproMainActivity(finalPseudo);
            }
        });
    }

    private void showLanguageDialog() {
        String[] labels = {getString(R.string.lang_english), getString(R.string.lang_chinese)};
        final String[] codes = {LocaleHelper.LANG_ENGLISH, LocaleHelper.LANG_CHINESE};
        new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert).setTitle(getString(R.string.dlg_language_title)).setItems(labels, new DialogInterface.OnClickListener() { // from class: nika.ngipro.MainActivity$$ExternalSyntheticLambda14
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m1800lambda$showLanguageDialog$33$nikangiproMainActivity(codes, dialogInterface, i);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showLanguageDialog$33$nika-ngipro-MainActivity, reason: not valid java name */
    public /* synthetic */ void m1800lambda$showLanguageDialog$33$nikangiproMainActivity(String[] codes, DialogInterface dialog, int which) {
        LocaleHelper.applyLanguage(this, codes[which]);
        recreate();
    }

    private void performLogout() {
        SharedPreferences prefs = getSharedPreferences("NGI_PREFS", 0);
        prefs.edit().clear().apply();
        new SessionStore(this).logout();
        Toast.makeText(this, getString(R.string.disconnected), 0).show();
        startActivity(new Intent(this, (Class<?>) LoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void openScreen(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
