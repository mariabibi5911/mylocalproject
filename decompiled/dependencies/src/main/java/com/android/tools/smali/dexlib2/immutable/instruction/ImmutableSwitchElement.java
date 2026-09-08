package com.android.tools.smali.dexlib2.immutable.instruction;

import com.android.tools.smali.dexlib2.iface.instruction.SwitchElement;
import com.android.tools.smali.util.ImmutableConverter;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ImmutableSwitchElement implements SwitchElement {
    private static final ImmutableConverter<ImmutableSwitchElement, SwitchElement> CONVERTER = new ImmutableConverter<ImmutableSwitchElement, SwitchElement>() { // from class: com.android.tools.smali.dexlib2.immutable.instruction.ImmutableSwitchElement.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        public boolean isImmutable(@Nonnull SwitchElement item) {
            return item instanceof ImmutableSwitchElement;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.tools.smali.util.ImmutableConverter
        @Nonnull
        public ImmutableSwitchElement makeImmutable(@Nonnull SwitchElement item) {
            return ImmutableSwitchElement.of(item);
        }
    };
    protected final int key;
    protected final int offset;

    public ImmutableSwitchElement(int key, int offset) {
        this.key = key;
        this.offset = offset;
    }

    @Nonnull
    public static ImmutableSwitchElement of(SwitchElement switchElement) {
        if (switchElement instanceof ImmutableSwitchElement) {
            return (ImmutableSwitchElement) switchElement;
        }
        return new ImmutableSwitchElement(switchElement.getKey(), switchElement.getOffset());
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.SwitchElement
    public int getKey() {
        return this.key;
    }

    @Override // com.android.tools.smali.dexlib2.iface.instruction.SwitchElement
    public int getOffset() {
        return this.offset;
    }

    @Nonnull
    public static ImmutableList<ImmutableSwitchElement> immutableListOf(@Nullable List<? extends SwitchElement> list) {
        return CONVERTER.toList(list);
    }
}
