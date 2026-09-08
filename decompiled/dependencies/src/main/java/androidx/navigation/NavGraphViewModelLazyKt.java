package androidx.navigation;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: NavGraphViewModelLazy.kt */
@Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\u001a>\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0010\b\n\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bH\u0087\bø\u0001\u0000\u001aP\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0010\b\n\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b2\u0010\b\n\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bH\u0087\bø\u0001\u0000\u001a<\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0010\b\n\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bH\u0087\bø\u0001\u0000\u001aN\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0010\b\n\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b2\u0010\b\n\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bH\u0087\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u000e²\u0006\u0016\u0010\u000f\u001a\u00020\u0010\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003X\u008a\u0084\u0002²\u0006\u0016\u0010\u000f\u001a\u00020\u0010\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003X\u008a\u0084\u0002²\u0006\u0016\u0010\u000f\u001a\u00020\u0010\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003X\u008a\u0084\u0002²\u0006\u0016\u0010\u000f\u001a\u00020\u0010\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003X\u008a\u0084\u0002"}, d2 = {"navGraphViewModels", "Lkotlin/Lazy;", "VM", "Landroidx/lifecycle/ViewModel;", "Landroidx/fragment/app/Fragment;", "navGraphId", "", "factoryProducer", "Lkotlin/Function0;", "Landroidx/lifecycle/ViewModelProvider$Factory;", "extrasProducer", "Landroidx/lifecycle/viewmodel/CreationExtras;", "navGraphRoute", "", "navigation-fragment_release", "backStackEntry", "Landroidx/navigation/NavBackStackEntry;"}, k = 2, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class NavGraphViewModelLazyKt {
    public static /* synthetic */ Lazy navGraphViewModels$default(Fragment $this$navGraphViewModels_u24default, int navGraphId, Function0 factoryProducer, int i, Object obj) {
        if ((i & 2) != 0) {
            factoryProducer = null;
        }
        Intrinsics.checkNotNullParameter($this$navGraphViewModels_u24default, "<this>");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$2($this$navGraphViewModels_u24default, navGraphId));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$1(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels_u24default, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$1(backStackEntry$delegate), factoryProducer == null ? new NavGraphViewModelLazyKt$navGraphViewModels$2(backStackEntry$delegate) : factoryProducer);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Superseded by navGraphViewModels that takes a CreationExtras producer")
    public static final /* synthetic */ <VM extends ViewModel> Lazy<VM> navGraphViewModels(Fragment $this$navGraphViewModels, int navGraphId, Function0<? extends ViewModelProvider.Factory> function0) {
        Intrinsics.checkNotNullParameter($this$navGraphViewModels, "<this>");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$2($this$navGraphViewModels, navGraphId));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$1(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$1(backStackEntry$delegate), function0 == null ? new NavGraphViewModelLazyKt$navGraphViewModels$2(backStackEntry$delegate) : function0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: navGraphViewModels$lambda-0, reason: not valid java name */
    public static final NavBackStackEntry m71navGraphViewModels$lambda0(Lazy<NavBackStackEntry> lazy) {
        return lazy.getValue();
    }

    public static /* synthetic */ Lazy navGraphViewModels$default(Fragment $this$navGraphViewModels_u24default, int navGraphId, Function0 extrasProducer, Function0 factoryProducer, int i, Object obj) {
        if ((i & 2) != 0) {
            extrasProducer = null;
        }
        if ((i & 4) != 0) {
            factoryProducer = null;
        }
        Intrinsics.checkNotNullParameter($this$navGraphViewModels_u24default, "<this>");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$4($this$navGraphViewModels_u24default, navGraphId));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$2(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels_u24default, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$3(extrasProducer, backStackEntry$delegate), factoryProducer == null ? new NavGraphViewModelLazyKt$navGraphViewModels$4(backStackEntry$delegate) : factoryProducer);
    }

    public static final /* synthetic */ <VM extends ViewModel> Lazy<VM> navGraphViewModels(Fragment $this$navGraphViewModels, int navGraphId, Function0<? extends CreationExtras> function0, Function0<? extends ViewModelProvider.Factory> function02) {
        Intrinsics.checkNotNullParameter($this$navGraphViewModels, "<this>");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$4($this$navGraphViewModels, navGraphId));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$2(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$3(function0, backStackEntry$delegate), function02 == null ? new NavGraphViewModelLazyKt$navGraphViewModels$4(backStackEntry$delegate) : function02);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: navGraphViewModels$lambda-1, reason: not valid java name */
    public static final NavBackStackEntry m72navGraphViewModels$lambda1(Lazy<NavBackStackEntry> lazy) {
        return lazy.getValue();
    }

    public static /* synthetic */ Lazy navGraphViewModels$default(Fragment $this$navGraphViewModels_u24default, String navGraphRoute, Function0 factoryProducer, int i, Object obj) {
        if ((i & 2) != 0) {
            factoryProducer = null;
        }
        Intrinsics.checkNotNullParameter($this$navGraphViewModels_u24default, "<this>");
        Intrinsics.checkNotNullParameter(navGraphRoute, "navGraphRoute");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$6($this$navGraphViewModels_u24default, navGraphRoute));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$3(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels_u24default, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$5(backStackEntry$delegate), factoryProducer == null ? new NavGraphViewModelLazyKt$navGraphViewModels$6(backStackEntry$delegate) : factoryProducer);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Superseded by navGraphViewModels that takes a CreationExtras producer")
    public static final /* synthetic */ <VM extends ViewModel> Lazy<VM> navGraphViewModels(Fragment $this$navGraphViewModels, String navGraphRoute, Function0<? extends ViewModelProvider.Factory> function0) {
        Intrinsics.checkNotNullParameter($this$navGraphViewModels, "<this>");
        Intrinsics.checkNotNullParameter(navGraphRoute, "navGraphRoute");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$6($this$navGraphViewModels, navGraphRoute));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$3(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$5(backStackEntry$delegate), function0 == null ? new NavGraphViewModelLazyKt$navGraphViewModels$6(backStackEntry$delegate) : function0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: navGraphViewModels$lambda-2, reason: not valid java name */
    public static final NavBackStackEntry m73navGraphViewModels$lambda2(Lazy<NavBackStackEntry> lazy) {
        return lazy.getValue();
    }

    public static /* synthetic */ Lazy navGraphViewModels$default(Fragment $this$navGraphViewModels_u24default, String navGraphRoute, Function0 extrasProducer, Function0 factoryProducer, int i, Object obj) {
        if ((i & 2) != 0) {
            extrasProducer = null;
        }
        if ((i & 4) != 0) {
            factoryProducer = null;
        }
        Intrinsics.checkNotNullParameter($this$navGraphViewModels_u24default, "<this>");
        Intrinsics.checkNotNullParameter(navGraphRoute, "navGraphRoute");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$8($this$navGraphViewModels_u24default, navGraphRoute));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$4(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels_u24default, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$7(extrasProducer, backStackEntry$delegate), factoryProducer == null ? new NavGraphViewModelLazyKt$navGraphViewModels$8(backStackEntry$delegate) : factoryProducer);
    }

    public static final /* synthetic */ <VM extends ViewModel> Lazy<VM> navGraphViewModels(Fragment $this$navGraphViewModels, String navGraphRoute, Function0<? extends CreationExtras> function0, Function0<? extends ViewModelProvider.Factory> function02) {
        Intrinsics.checkNotNullParameter($this$navGraphViewModels, "<this>");
        Intrinsics.checkNotNullParameter(navGraphRoute, "navGraphRoute");
        Lazy backStackEntry$delegate = LazyKt.lazy(new NavGraphViewModelLazyKt$navGraphViewModels$backStackEntry$8($this$navGraphViewModels, navGraphRoute));
        Function0 storeProducer = new NavGraphViewModelLazyKt$navGraphViewModels$storeProducer$4(backStackEntry$delegate);
        Intrinsics.reifiedOperationMarker(4, "VM");
        return FragmentViewModelLazyKt.createViewModelLazy($this$navGraphViewModels, Reflection.getOrCreateKotlinClass(ViewModel.class), storeProducer, new NavGraphViewModelLazyKt$navGraphViewModels$7(function0, backStackEntry$delegate), function02 == null ? new NavGraphViewModelLazyKt$navGraphViewModels$8(backStackEntry$delegate) : function02);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: navGraphViewModels$lambda-3, reason: not valid java name */
    public static final NavBackStackEntry m74navGraphViewModels$lambda3(Lazy<NavBackStackEntry> lazy) {
        return lazy.getValue();
    }
}
