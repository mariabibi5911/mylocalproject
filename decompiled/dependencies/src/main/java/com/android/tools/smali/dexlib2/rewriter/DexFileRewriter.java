package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;
import java.util.Set;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class DexFileRewriter implements Rewriter<DexFile> {

    @Nonnull
    protected final Rewriters rewriters;

    public DexFileRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public DexFile rewrite(@Nonnull DexFile value) {
        return new RewrittenDexFile(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenDexFile implements DexFile {

        @Nonnull
        protected final DexFile dexFile;

        public RewrittenDexFile(@Nonnull DexFile dexFile) {
            this.dexFile = dexFile;
        }

        @Override // com.android.tools.smali.dexlib2.iface.DexFile
        @Nonnull
        public Set<? extends ClassDef> getClasses() {
            return RewriterUtils.rewriteSet(DexFileRewriter.this.rewriters.getClassDefRewriter(), this.dexFile.getClasses());
        }

        @Override // com.android.tools.smali.dexlib2.iface.DexFile
        @Nonnull
        public Opcodes getOpcodes() {
            return this.dexFile.getOpcodes();
        }
    }
}
