package androidx.window.layout;

import android.graphics.Rect;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.window.core.Bounds;
import androidx.window.core.SpecificationComputer;
import androidx.window.layout.FoldingFeature;
import androidx.window.layout.HardwareFoldingFeature;
import androidx.window.sidecar.SidecarDeviceState;
import androidx.window.sidecar.SidecarDisplayFeature;
import androidx.window.sidecar.SidecarWindowLayoutInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SidecarAdapter.kt */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bJ\u001c\u0010\n\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u000b2\b\u0010\t\u001a\u0004\u0018\u00010\u000bH\u0002J(\u0010\f\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\r2\u000e\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\rH\u0002J\u001a\u0010\u000e\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u000f2\b\u0010\t\u001a\u0004\u0018\u00010\u000fJ\u001f\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\bH\u0000¢\u0006\u0002\b\u0014J\u0018\u0010\u0010\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0017\u001a\u00020\bJ\"\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\r2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000b0\r2\u0006\u0010\u0013\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Landroidx/window/layout/SidecarAdapter;", "", "verificationMode", "Landroidx/window/core/SpecificationComputer$VerificationMode;", "(Landroidx/window/core/SpecificationComputer$VerificationMode;)V", "isEqualSidecarDeviceState", "", "first", "Landroidx/window/sidecar/SidecarDeviceState;", "second", "isEqualSidecarDisplayFeature", "Landroidx/window/sidecar/SidecarDisplayFeature;", "isEqualSidecarDisplayFeatures", "", "isEqualSidecarWindowLayoutInfo", "Landroidx/window/sidecar/SidecarWindowLayoutInfo;", "translate", "Landroidx/window/layout/DisplayFeature;", "feature", "deviceState", "translate$window_release", "Landroidx/window/layout/WindowLayoutInfo;", "extensionInfo", "state", "sidecarDisplayFeatures", "Companion", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class SidecarAdapter {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final String TAG = SidecarAdapter.class.getSimpleName();
    private final SpecificationComputer.VerificationMode verificationMode;

    /* JADX WARN: Multi-variable type inference failed */
    public SidecarAdapter() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public SidecarAdapter(SpecificationComputer.VerificationMode verificationMode) {
        Intrinsics.checkNotNullParameter(verificationMode, "verificationMode");
        this.verificationMode = verificationMode;
    }

    public /* synthetic */ SidecarAdapter(SpecificationComputer.VerificationMode verificationMode, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? SpecificationComputer.VerificationMode.QUIET : verificationMode);
    }

    public final List<DisplayFeature> translate(List<SidecarDisplayFeature> sidecarDisplayFeatures, SidecarDeviceState deviceState) {
        Intrinsics.checkNotNullParameter(sidecarDisplayFeatures, "sidecarDisplayFeatures");
        Intrinsics.checkNotNullParameter(deviceState, "deviceState");
        List<SidecarDisplayFeature> $this$mapNotNull$iv = sidecarDisplayFeatures;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
            SidecarDisplayFeature sidecarFeature = (SidecarDisplayFeature) element$iv$iv$iv;
            DisplayFeature translate$window_release = translate$window_release(sidecarFeature, deviceState);
            if (translate$window_release != null) {
                destination$iv$iv.add(translate$window_release);
            }
        }
        return (List) destination$iv$iv;
    }

    public final WindowLayoutInfo translate(SidecarWindowLayoutInfo extensionInfo, SidecarDeviceState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        if (extensionInfo == null) {
            return new WindowLayoutInfo(CollectionsKt.emptyList());
        }
        SidecarDeviceState deviceState = new SidecarDeviceState();
        Companion companion = INSTANCE;
        int devicePosture = companion.getSidecarDevicePosture$window_release(state);
        companion.setSidecarDevicePosture(deviceState, devicePosture);
        List sidecarDisplayFeatures = companion.getSidecarDisplayFeatures(extensionInfo);
        List displayFeatures = translate((List<SidecarDisplayFeature>) sidecarDisplayFeatures, deviceState);
        return new WindowLayoutInfo(displayFeatures);
    }

    public final boolean isEqualSidecarDeviceState(SidecarDeviceState first, SidecarDeviceState second) {
        if (Intrinsics.areEqual(first, second)) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        Companion companion = INSTANCE;
        int firstPosture = companion.getSidecarDevicePosture$window_release(first);
        int secondPosture = companion.getSidecarDevicePosture$window_release(second);
        return firstPosture == secondPosture;
    }

    public final boolean isEqualSidecarWindowLayoutInfo(SidecarWindowLayoutInfo first, SidecarWindowLayoutInfo second) {
        if (Intrinsics.areEqual(first, second)) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        Companion companion = INSTANCE;
        List firstDisplayFeatures = companion.getSidecarDisplayFeatures(first);
        List secondDisplayFeatures = companion.getSidecarDisplayFeatures(second);
        return isEqualSidecarDisplayFeatures(firstDisplayFeatures, secondDisplayFeatures);
    }

    private final boolean isEqualSidecarDisplayFeatures(List<SidecarDisplayFeature> first, List<SidecarDisplayFeature> second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null || first.size() != second.size()) {
            return false;
        }
        int size = first.size();
        int i = 0;
        while (i < size) {
            int i2 = i;
            i++;
            SidecarDisplayFeature firstFeature = first.get(i2);
            SidecarDisplayFeature secondFeature = second.get(i2);
            if (!isEqualSidecarDisplayFeature(firstFeature, secondFeature)) {
                return false;
            }
        }
        return true;
    }

    private final boolean isEqualSidecarDisplayFeature(SidecarDisplayFeature first, SidecarDisplayFeature second) {
        if (Intrinsics.areEqual(first, second)) {
            return true;
        }
        return first != null && second != null && first.getType() == second.getType() && Intrinsics.areEqual(first.getRect(), second.getRect());
    }

    public final DisplayFeature translate$window_release(SidecarDisplayFeature feature, SidecarDeviceState deviceState) {
        HardwareFoldingFeature.Type type;
        FoldingFeature.State state;
        Intrinsics.checkNotNullParameter(feature, "feature");
        Intrinsics.checkNotNullParameter(deviceState, "deviceState");
        SpecificationComputer.Companion companion = SpecificationComputer.INSTANCE;
        String TAG2 = TAG;
        Intrinsics.checkNotNullExpressionValue(TAG2, "TAG");
        SidecarDisplayFeature checkedFeature = (SidecarDisplayFeature) SpecificationComputer.Companion.startSpecification$default(companion, feature, TAG2, this.verificationMode, null, 4, null).require("Type must be either TYPE_FOLD or TYPE_HINGE", new Function1<SidecarDisplayFeature, Boolean>() { // from class: androidx.window.layout.SidecarAdapter$translate$checkedFeature$1
            @Override // kotlin.jvm.functions.Function1
            public final Boolean invoke(SidecarDisplayFeature require) {
                Intrinsics.checkNotNullParameter(require, "$this$require");
                boolean z = true;
                if (require.getType() != 1 && require.getType() != 2) {
                    z = false;
                }
                return Boolean.valueOf(z);
            }
        }).require("Feature bounds must not be 0", new Function1<SidecarDisplayFeature, Boolean>() { // from class: androidx.window.layout.SidecarAdapter$translate$checkedFeature$2
            @Override // kotlin.jvm.functions.Function1
            public final Boolean invoke(SidecarDisplayFeature require) {
                Intrinsics.checkNotNullParameter(require, "$this$require");
                return Boolean.valueOf((require.getRect().width() == 0 && require.getRect().height() == 0) ? false : true);
            }
        }).require("TYPE_FOLD must have 0 area", new Function1<SidecarDisplayFeature, Boolean>() { // from class: androidx.window.layout.SidecarAdapter$translate$checkedFeature$3
            @Override // kotlin.jvm.functions.Function1
            public final Boolean invoke(SidecarDisplayFeature require) {
                Intrinsics.checkNotNullParameter(require, "$this$require");
                boolean z = true;
                if (require.getType() == 1 && require.getRect().width() != 0 && require.getRect().height() != 0) {
                    z = false;
                }
                return Boolean.valueOf(z);
            }
        }).require("Feature be pinned to either left or top", new Function1<SidecarDisplayFeature, Boolean>() { // from class: androidx.window.layout.SidecarAdapter$translate$checkedFeature$4
            @Override // kotlin.jvm.functions.Function1
            public final Boolean invoke(SidecarDisplayFeature require) {
                Intrinsics.checkNotNullParameter(require, "$this$require");
                return Boolean.valueOf(require.getRect().left == 0 || require.getRect().top == 0);
            }
        }).compute();
        if (checkedFeature == null) {
            return null;
        }
        switch (checkedFeature.getType()) {
            case 1:
                type = HardwareFoldingFeature.Type.INSTANCE.getFOLD();
                break;
            case 2:
                type = HardwareFoldingFeature.Type.INSTANCE.getHINGE();
                break;
            default:
                return null;
        }
        switch (INSTANCE.getSidecarDevicePosture$window_release(deviceState)) {
            case 0:
            case 1:
            case 4:
                return null;
            case 2:
                state = FoldingFeature.State.HALF_OPENED;
                break;
            case 3:
                state = FoldingFeature.State.FLAT;
                break;
            default:
                state = FoldingFeature.State.FLAT;
                break;
        }
        Rect rect = feature.getRect();
        Intrinsics.checkNotNullExpressionValue(rect, "feature.rect");
        return new HardwareFoldingFeature(new Bounds(rect), type, state);
    }

    /* compiled from: SidecarAdapter.kt */
    @Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0007J\u0015\u0010\n\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0000¢\u0006\u0002\b\u000bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0007H\u0007J \u0010\u0014\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u00102\u000e\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\rH\u0007R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Landroidx/window/layout/SidecarAdapter$Companion;", "", "()V", "TAG", "", "kotlin.jvm.PlatformType", "getRawSidecarDevicePosture", "", "sidecarDeviceState", "Landroidx/window/sidecar/SidecarDeviceState;", "getSidecarDevicePosture", "getSidecarDevicePosture$window_release", "getSidecarDisplayFeatures", "", "Landroidx/window/sidecar/SidecarDisplayFeature;", "info", "Landroidx/window/sidecar/SidecarWindowLayoutInfo;", "setSidecarDevicePosture", "", "posture", "setSidecarDisplayFeatures", "displayFeatures", "window_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final List<SidecarDisplayFeature> getSidecarDisplayFeatures(SidecarWindowLayoutInfo info) {
            Intrinsics.checkNotNullParameter(info, "info");
            try {
                List<SidecarDisplayFeature> list = info.displayFeatures;
                return list == null ? CollectionsKt.emptyList() : list;
            } catch (NoSuchFieldError e) {
                try {
                    Method methodGetFeatures = SidecarWindowLayoutInfo.class.getMethod("getDisplayFeatures", new Class[0]);
                    Object invoke = methodGetFeatures.invoke(info, new Object[0]);
                    if (invoke != null) {
                        return (List) invoke;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<androidx.window.sidecar.SidecarDisplayFeature>");
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                    return CollectionsKt.emptyList();
                }
            }
        }

        public final void setSidecarDisplayFeatures(SidecarWindowLayoutInfo info, List<SidecarDisplayFeature> displayFeatures) {
            Intrinsics.checkNotNullParameter(info, "info");
            Intrinsics.checkNotNullParameter(displayFeatures, "displayFeatures");
            try {
                info.displayFeatures = displayFeatures;
            } catch (NoSuchFieldError e) {
                try {
                    Method methodSetFeatures = SidecarWindowLayoutInfo.class.getMethod("setDisplayFeatures", List.class);
                    methodSetFeatures.invoke(info, displayFeatures);
                } catch (IllegalAccessException e2) {
                } catch (NoSuchMethodException e3) {
                } catch (InvocationTargetException e4) {
                }
            }
        }

        public final int getSidecarDevicePosture$window_release(SidecarDeviceState sidecarDeviceState) {
            Intrinsics.checkNotNullParameter(sidecarDeviceState, "sidecarDeviceState");
            int rawPosture = getRawSidecarDevicePosture(sidecarDeviceState);
            if (rawPosture < 0 || rawPosture > 4) {
                return 0;
            }
            return rawPosture;
        }

        public final int getRawSidecarDevicePosture(SidecarDeviceState sidecarDeviceState) {
            Intrinsics.checkNotNullParameter(sidecarDeviceState, "sidecarDeviceState");
            try {
                return sidecarDeviceState.posture;
            } catch (NoSuchFieldError e) {
                try {
                    Method methodGetPosture = SidecarDeviceState.class.getMethod("getPosture", new Class[0]);
                    Object invoke = methodGetPosture.invoke(sidecarDeviceState, new Object[0]);
                    if (invoke != null) {
                        return ((Integer) invoke).intValue();
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                    return 0;
                }
            }
        }

        public final void setSidecarDevicePosture(SidecarDeviceState sidecarDeviceState, int posture) {
            Intrinsics.checkNotNullParameter(sidecarDeviceState, "sidecarDeviceState");
            try {
                sidecarDeviceState.posture = posture;
            } catch (NoSuchFieldError e) {
                try {
                    Method methodSetPosture = SidecarDeviceState.class.getMethod("setPosture", Integer.TYPE);
                    methodSetPosture.invoke(sidecarDeviceState, Integer.valueOf(posture));
                } catch (IllegalAccessException e2) {
                } catch (NoSuchMethodException e3) {
                } catch (InvocationTargetException e4) {
                }
            }
        }
    }
}
