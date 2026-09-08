package com.android.tools.smali.dexlib2.rewriter;

import com.android.tools.smali.dexlib2.iface.debug.DebugItem;
import com.android.tools.smali.dexlib2.iface.debug.EndLocal;
import com.android.tools.smali.dexlib2.iface.debug.LocalInfo;
import com.android.tools.smali.dexlib2.iface.debug.RestartLocal;
import com.android.tools.smali.dexlib2.iface.debug.StartLocal;
import com.android.tools.smali.dexlib2.iface.reference.StringReference;
import com.android.tools.smali.dexlib2.iface.reference.TypeReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DebugItemRewriter implements Rewriter<DebugItem> {

    @Nonnull
    protected final Rewriters rewriters;

    public DebugItemRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // com.android.tools.smali.dexlib2.rewriter.Rewriter
    @Nonnull
    public DebugItem rewrite(@Nonnull DebugItem value) {
        switch (value.getDebugItemType()) {
            case 3:
                return new RewrittenStartLocal((StartLocal) value);
            case 4:
            default:
                return value;
            case 5:
                return new RewrittenEndLocal((EndLocal) value);
            case 6:
                return new RewrittenRestartLocal((RestartLocal) value);
        }
    }

    /* loaded from: classes.dex */
    protected class BaseRewrittenLocalInfoDebugItem<T extends DebugItem & LocalInfo> implements DebugItem, LocalInfo {

        @Nonnull
        protected T debugItem;

        public BaseRewrittenLocalInfoDebugItem(@Nonnull T debugItem) {
            this.debugItem = debugItem;
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
        public int getDebugItemType() {
            return this.debugItem.getDebugItemType();
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.DebugItem
        public int getCodeAddress() {
            return this.debugItem.getCodeAddress();
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getName() {
            return this.debugItem.getName();
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getType() {
            return (String) RewriterUtils.rewriteNullable(DebugItemRewriter.this.rewriters.getTypeRewriter(), this.debugItem.getType());
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getSignature() {
            return this.debugItem.getSignature();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenStartLocal extends BaseRewrittenLocalInfoDebugItem<StartLocal> implements StartLocal {
        public RewrittenStartLocal(@Nonnull StartLocal debugItem) {
            super(debugItem);
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
        public int getRegister() {
            return ((StartLocal) this.debugItem).getRegister();
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
        @Nullable
        public StringReference getNameReference() {
            return ((StartLocal) this.debugItem).getNameReference();
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
        @Nullable
        public TypeReference getTypeReference() {
            TypeReference typeReference = ((StartLocal) this.debugItem).getTypeReference();
            if (typeReference == null) {
                return null;
            }
            return RewriterUtils.rewriteTypeReference(DebugItemRewriter.this.rewriters.getTypeRewriter(), typeReference);
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.StartLocal
        @Nullable
        public StringReference getSignatureReference() {
            return ((StartLocal) this.debugItem).getSignatureReference();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenEndLocal extends BaseRewrittenLocalInfoDebugItem<EndLocal> implements EndLocal {
        public RewrittenEndLocal(@Nonnull EndLocal instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.EndLocal
        public int getRegister() {
            return ((EndLocal) this.debugItem).getRegister();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class RewrittenRestartLocal extends BaseRewrittenLocalInfoDebugItem<RestartLocal> implements RestartLocal {
        public RewrittenRestartLocal(@Nonnull RestartLocal instruction) {
            super(instruction);
        }

        @Override // com.android.tools.smali.dexlib2.iface.debug.RestartLocal
        public int getRegister() {
            return ((RestartLocal) this.debugItem).getRegister();
        }
    }
}
