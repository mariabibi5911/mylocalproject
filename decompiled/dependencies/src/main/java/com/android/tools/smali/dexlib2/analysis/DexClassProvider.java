package com.android.tools.smali.dexlib2.analysis;

import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DexClassProvider implements ClassProvider {
    private Map<String, ClassDef> classMap = Maps.newHashMap();
    private final DexFile dexFile;

    public DexClassProvider(DexFile dexFile) {
        this.dexFile = dexFile;
        for (ClassDef classDef : dexFile.getClasses()) {
            this.classMap.put(classDef.getType(), classDef);
        }
    }

    @Override // com.android.tools.smali.dexlib2.analysis.ClassProvider
    @Nullable
    public ClassDef getClassDef(String type) {
        return this.classMap.get(type);
    }
}
