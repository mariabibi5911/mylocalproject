package androidx.navigation;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.viewmodel.CreationExtras;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NavControllerViewModel.kt */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0000\u0018\u0000 \u000e2\u00020\u00012\u00020\u0002:\u0001\u000eB\u0005¢\u0006\u0002\u0010\u0003J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0006J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0006H\u0016J\b\u0010\f\u001a\u00020\tH\u0014J\b\u0010\r\u001a\u00020\u0006H\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Landroidx/navigation/NavControllerViewModel;", "Landroidx/lifecycle/ViewModel;", "Landroidx/navigation/NavViewModelStoreProvider;", "()V", "viewModelStores", "", "", "Landroidx/lifecycle/ViewModelStore;", "clear", "", "backStackEntryId", "getViewModelStore", "onCleared", "toString", "Companion", "navigation-runtime_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class NavControllerViewModel extends ViewModel implements NavViewModelStoreProvider {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() { // from class: androidx.navigation.NavControllerViewModel$Companion$FACTORY$1
        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public /* synthetic */ ViewModel create(Class cls, CreationExtras creationExtras) {
            return ViewModelProvider.Factory.CC.$default$create(this, cls, creationExtras);
        }

        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public <T extends ViewModel> T create(Class<T> modelClass) {
            Intrinsics.checkNotNullParameter(modelClass, "modelClass");
            return new NavControllerViewModel();
        }
    };
    private final Map<String, ViewModelStore> viewModelStores = new LinkedHashMap();

    @JvmStatic
    public static final NavControllerViewModel getInstance(ViewModelStore viewModelStore) {
        return INSTANCE.getInstance(viewModelStore);
    }

    public final void clear(String backStackEntryId) {
        Intrinsics.checkNotNullParameter(backStackEntryId, "backStackEntryId");
        ViewModelStore viewModelStore = this.viewModelStores.remove(backStackEntryId);
        if (viewModelStore != null) {
            viewModelStore.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.ViewModel
    public void onCleared() {
        for (ViewModelStore store : this.viewModelStores.values()) {
            store.clear();
        }
        this.viewModelStores.clear();
    }

    @Override // androidx.navigation.NavViewModelStoreProvider
    public ViewModelStore getViewModelStore(String backStackEntryId) {
        Intrinsics.checkNotNullParameter(backStackEntryId, "backStackEntryId");
        ViewModelStore viewModelStore = this.viewModelStores.get(backStackEntryId);
        if (viewModelStore == null) {
            ViewModelStore viewModelStore2 = new ViewModelStore();
            this.viewModelStores.put(backStackEntryId, viewModelStore2);
            return viewModelStore2;
        }
        return viewModelStore;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NavControllerViewModel{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("} ViewModelStores (");
        Iterator viewModelStoreIterator = this.viewModelStores.keySet().iterator();
        while (viewModelStoreIterator.hasNext()) {
            sb.append(viewModelStoreIterator.next());
            if (viewModelStoreIterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(')');
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }

    /* compiled from: NavControllerViewModel.kt */
    @Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Landroidx/navigation/NavControllerViewModel$Companion;", "", "()V", "FACTORY", "Landroidx/lifecycle/ViewModelProvider$Factory;", "getInstance", "Landroidx/navigation/NavControllerViewModel;", "viewModelStore", "Landroidx/lifecycle/ViewModelStore;", "navigation-runtime_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final NavControllerViewModel getInstance(ViewModelStore viewModelStore) {
            Intrinsics.checkNotNullParameter(viewModelStore, "viewModelStore");
            ViewModelProvider $this$get$iv = new ViewModelProvider(viewModelStore, NavControllerViewModel.FACTORY, null, 4, null);
            return (NavControllerViewModel) $this$get$iv.get(NavControllerViewModel.class);
        }
    }
}
