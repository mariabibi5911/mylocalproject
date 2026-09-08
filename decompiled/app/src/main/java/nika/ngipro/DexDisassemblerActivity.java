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
import com.android.tools.smali.dexlib2.DexFileFactory;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.MethodImplementation;
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.Instruction;
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OffsetInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.ThreeRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction;
import com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import nika.ngipro.DexEntryAdapter;

/* loaded from: classes4.dex */
public class DexDisassemblerActivity extends AppCompatActivity {
    public static final String EXTRA_FILE_PATH = "file_path";
    private View detailContainer;
    private TextView dexCountText;
    private TextView dexDetailText;
    private DexFile dexFile;
    private RecyclerView dexRecycler;
    private String filePath;
    private final List<Method> flatMethods = new ArrayList();
    private View listContainer;
    private Toolbar toolbarDetail;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.attach(newBase));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dex_view);
        this.filePath = getIntent().getStringExtra("file_path");
        this.listContainer = findViewById(R.id.listContainer);
        this.detailContainer = findViewById(R.id.detailContainer);
        this.dexRecycler = (RecyclerView) findViewById(R.id.dexRecycler);
        this.dexCountText = (TextView) findViewById(R.id.dexCountText);
        this.dexDetailText = (TextView) findViewById(R.id.dexDetailText);
        this.toolbarDetail = (Toolbar) findViewById(R.id.toolbarDexDetail);
        Toolbar toolbarList = (Toolbar) findViewById(R.id.toolbarDexList);
        toolbarList.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.DexDisassemblerActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DexDisassemblerActivity.this.m1729lambda$onCreate$0$nikangiproDexDisassemblerActivity(view);
            }
        });
        this.toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() { // from class: nika.ngipro.DexDisassemblerActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DexDisassemblerActivity.this.m1730lambda$onCreate$1$nikangiproDexDisassemblerActivity(view);
            }
        });
        this.dexRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadDex();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$nika-ngipro-DexDisassemblerActivity, reason: not valid java name */
    public /* synthetic */ void m1729lambda$onCreate$0$nikangiproDexDisassemblerActivity(View v) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$nika-ngipro-DexDisassemblerActivity, reason: not valid java name */
    public /* synthetic */ void m1730lambda$onCreate$1$nikangiproDexDisassemblerActivity(View v) {
        showList();
    }

    private void loadDex() {
        String str = this.filePath;
        if (str == null || str.isEmpty()) {
            Toast.makeText(this, "No file loaded", 0).show();
            finish();
        } else {
            new Thread(new Runnable() { // from class: nika.ngipro.DexDisassemblerActivity$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    DexDisassemblerActivity.this.m1728lambda$loadDex$3$nikangiproDexDisassemblerActivity();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$loadDex$3$nika-ngipro-DexDisassemblerActivity, reason: not valid java name */
    public /* synthetic */ void m1728lambda$loadDex$3$nikangiproDexDisassemblerActivity() {
        final List<DexEntryAdapter.DexEntry> entries = new ArrayList<>();
        String errorMsg = null;
        try {
            DexBackedDexFile loadDexFile = DexFileFactory.loadDexFile(new File(this.filePath), Opcodes.getDefault());
            this.dexFile = loadDexFile;
            for (ClassDef classDef : loadDexFile.getClasses()) {
                for (Method method : classDef.getMethods()) {
                    MethodImplementation impl = method.getImplementation();
                    if (impl != null) {
                        int count = 0;
                        for (Instruction instruction : impl.getInstructions()) {
                            count++;
                        }
                        if (count != 0) {
                            DexEntryAdapter.DexEntry e = new DexEntryAdapter.DexEntry();
                            StringBuilder params = new StringBuilder();
                            for (CharSequence p : method.getParameterTypes()) {
                                params.append(p);
                            }
                            e.signature = classDef.getType() + "->" + method.getName() + "(" + ((Object) params) + ")" + method.getReturnType();
                            e.instructionCount = count;
                            e.methodIndex = this.flatMethods.size();
                            this.flatMethods.add(method);
                            entries.add(e);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            errorMsg = "Failed to load DEX: " + t.getClass().getSimpleName() + (t.getMessage() != null ? " — " + t.getMessage() : "");
        }
        final String finalError = errorMsg;
        runOnUiThread(new Runnable() { // from class: nika.ngipro.DexDisassemblerActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DexDisassemblerActivity.this.m1727lambda$loadDex$2$nikangiproDexDisassemblerActivity(finalError, entries);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$loadDex$2$nika-ngipro-DexDisassemblerActivity, reason: not valid java name */
    public /* synthetic */ void m1727lambda$loadDex$2$nikangiproDexDisassemblerActivity(String finalError, List entries) {
        if (finalError != null) {
            this.dexCountText.setText(finalError);
            return;
        }
        this.dexCountText.setText(entries.size() + " methods with bytecode found");
        this.dexRecycler.setAdapter(new DexEntryAdapter(entries, new DexEntryAdapter.OnEntryClick() { // from class: nika.ngipro.DexDisassemblerActivity$$ExternalSyntheticLambda5
            @Override // nika.ngipro.DexEntryAdapter.OnEntryClick
            public final void onClick(DexEntryAdapter.DexEntry dexEntry) {
                DexDisassemblerActivity.this.openMethod(dexEntry);
            }
        }));
        if (entries.isEmpty()) {
            this.dexCountText.setText("No methods with bytecode found");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openMethod(final DexEntryAdapter.DexEntry entry) {
        this.toolbarDetail.setTitle(entry.signature.length() > 40 ? "…" + entry.signature.substring(entry.signature.length() - 40) : entry.signature);
        this.dexDetailText.setText("Disassembling…");
        this.listContainer.setVisibility(8);
        this.detailContainer.setVisibility(0);
        new Thread(new Runnable() { // from class: nika.ngipro.DexDisassemblerActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DexDisassemblerActivity.this.m1732lambda$openMethod$5$nikangiproDexDisassemblerActivity(entry);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$openMethod$5$nika-ngipro-DexDisassemblerActivity, reason: not valid java name */
    public /* synthetic */ void m1732lambda$openMethod$5$nikangiproDexDisassemblerActivity(DexEntryAdapter.DexEntry entry) {
        String result;
        try {
            Method method = this.flatMethods.get(entry.methodIndex);
            result = disassembleMethod(entry.signature, method);
        } catch (Throwable t) {
            result = "[-] Disassembly error: " + t.getClass().getSimpleName();
        }
        final String finalResult = result;
        runOnUiThread(new Runnable() { // from class: nika.ngipro.DexDisassemblerActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DexDisassemblerActivity.this.m1731lambda$openMethod$4$nikangiproDexDisassemblerActivity(finalResult);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$openMethod$4$nika-ngipro-DexDisassemblerActivity, reason: not valid java name */
    public /* synthetic */ void m1731lambda$openMethod$4$nikangiproDexDisassemblerActivity(String finalResult) {
        this.dexDetailText.setText(finalResult);
    }

    private String disassembleMethod(String signature, Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(".method ").append(signature).append("\n\n");
        MethodImplementation impl = method.getImplementation();
        if (impl == null) {
            sb.append("(no implementation — abstract/native)\n");
            return sb.toString();
        }
        sb.append("registers: ").append(impl.getRegisterCount()).append("\n\n");
        int addr = 0;
        for (Instruction instr : impl.getInstructions()) {
            sb.append(String.format("%04X: %s\n", Integer.valueOf(addr), formatInstruction(instr)));
            addr += instr.getCodeUnits();
        }
        sb.append("\n.end method\n");
        return sb.toString();
    }

    private static String formatInstruction(Instruction instr) {
        Object ref;
        StringBuilder sb = new StringBuilder(instr.getOpcode().name);
        List<String> parts = new ArrayList<>();
        try {
            if (instr instanceof RegisterRangeInstruction) {
                RegisterRangeInstruction r = (RegisterRangeInstruction) instr;
                int start = r.getStartRegister();
                int count = r.getRegisterCount();
                parts.add("{v" + start + " .. v" + (Math.max(count - 1, 0) + start) + "}");
            } else if (instr instanceof FiveRegisterInstruction) {
                FiveRegisterInstruction f = (FiveRegisterInstruction) instr;
                int count2 = f.getRegisterCount();
                List<String> regs = new ArrayList<>();
                int[] vals = {f.getRegisterC(), f.getRegisterD(), f.getRegisterE(), f.getRegisterF(), f.getRegisterG()};
                for (int i = 0; i < count2 && i < 5; i++) {
                    regs.add("v" + vals[i]);
                }
                parts.add("{" + DexDisassemblerActivity$$ExternalSyntheticBackport0.m(", ", regs) + "}");
            } else if (instr instanceof ThreeRegisterInstruction) {
                ThreeRegisterInstruction r2 = (ThreeRegisterInstruction) instr;
                parts.add("v" + r2.getRegisterA());
                parts.add("v" + r2.getRegisterB());
                parts.add("v" + r2.getRegisterC());
            } else if (instr instanceof TwoRegisterInstruction) {
                TwoRegisterInstruction r3 = (TwoRegisterInstruction) instr;
                parts.add("v" + r3.getRegisterA());
                parts.add("v" + r3.getRegisterB());
            } else if (instr instanceof OneRegisterInstruction) {
                parts.add("v" + ((OneRegisterInstruction) instr).getRegisterA());
            }
        } catch (Throwable th) {
        }
        try {
            if (instr instanceof WideLiteralInstruction) {
                parts.add("#" + ((WideLiteralInstruction) instr).getWideLiteral());
            } else if (instr instanceof NarrowLiteralInstruction) {
                parts.add("#" + ((NarrowLiteralInstruction) instr).getNarrowLiteral());
            }
        } catch (Throwable th2) {
        }
        try {
            if ((instr instanceof ReferenceInstruction) && (ref = ((ReferenceInstruction) instr).getReference()) != null) {
                parts.add(ref.toString());
            }
        } catch (Throwable th3) {
        }
        try {
            if (instr instanceof OffsetInstruction) {
                int off = ((OffsetInstruction) instr).getCodeOffset();
                parts.add((off >= 0 ? "+" : "") + off);
            }
        } catch (Throwable th4) {
        }
        if (!parts.isEmpty()) {
            sb.append(" ").append(DexDisassemblerActivity$$ExternalSyntheticBackport0.m(", ", parts));
        }
        return sb.toString();
    }

    private void showList() {
        this.detailContainer.setVisibility(8);
        this.listContainer.setVisibility(0);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.detailContainer.getVisibility() == 0) {
            showList();
        } else {
            super.onBackPressed();
        }
    }
}
