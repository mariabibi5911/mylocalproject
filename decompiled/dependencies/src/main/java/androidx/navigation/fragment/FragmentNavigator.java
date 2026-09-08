package androidx.navigation.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.os.BundleKt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.NavigatorProvider;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FragmentNavigator.kt */
@Navigator.Name("fragment")
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0017\u0018\u0000 '2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003'()B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\r\u001a\u00020\u0002H\u0016J\u001a\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0002J*\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0017J$\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0002J*\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00110\u001e2\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\u0010\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020\u0011H\u0016J\u0010\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020\u0018H\u0016J\n\u0010#\u001a\u0004\u0018\u00010\u0018H\u0016J\u0018\u0010$\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020&H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Landroidx/navigation/fragment/FragmentNavigator;", "Landroidx/navigation/Navigator;", "Landroidx/navigation/fragment/FragmentNavigator$Destination;", "context", "Landroid/content/Context;", "fragmentManager", "Landroidx/fragment/app/FragmentManager;", "containerId", "", "(Landroid/content/Context;Landroidx/fragment/app/FragmentManager;I)V", "savedIds", "", "", "createDestination", "createFragmentTransaction", "Landroidx/fragment/app/FragmentTransaction;", "entry", "Landroidx/navigation/NavBackStackEntry;", "navOptions", "Landroidx/navigation/NavOptions;", "instantiateFragment", "Landroidx/fragment/app/Fragment;", "className", "args", "Landroid/os/Bundle;", "navigate", "", "navigatorExtras", "Landroidx/navigation/Navigator$Extras;", "entries", "", "onLaunchSingleTop", "backStackEntry", "onRestoreState", "savedState", "onSaveState", "popBackStack", "popUpTo", "", "Companion", "Destination", "Extras", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public class FragmentNavigator extends Navigator<Destination> {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    private static final String KEY_SAVED_IDS = "androidx-nav-fragment:navigator:savedIds";

    @Deprecated
    private static final String TAG = "FragmentNavigator";
    private final int containerId;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final Set<String> savedIds;

    public FragmentNavigator(Context context, FragmentManager fragmentManager, int containerId) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(fragmentManager, "fragmentManager");
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.savedIds = new LinkedHashSet();
    }

    @Override // androidx.navigation.Navigator
    public void popBackStack(NavBackStackEntry popUpTo, boolean savedState) {
        Intrinsics.checkNotNullParameter(popUpTo, "popUpTo");
        if (this.fragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring popBackStack() call: FragmentManager has already saved its state");
            return;
        }
        if (savedState) {
            List beforePopList = getState().getBackStack().getValue();
            NavBackStackEntry initialEntry = (NavBackStackEntry) CollectionsKt.first(beforePopList);
            List poppedList = beforePopList.subList(beforePopList.indexOf(popUpTo), beforePopList.size());
            for (NavBackStackEntry entry : CollectionsKt.reversed(poppedList)) {
                if (Intrinsics.areEqual(entry, initialEntry)) {
                    Log.i(TAG, "FragmentManager cannot save the state of the initial destination " + entry);
                } else {
                    this.fragmentManager.saveBackStack(entry.getId());
                    this.savedIds.add(entry.getId());
                }
            }
        } else {
            this.fragmentManager.popBackStack(popUpTo.getId(), 1);
        }
        getState().pop(popUpTo, savedState);
    }

    @Override // androidx.navigation.Navigator
    public Destination createDestination() {
        return new Destination(this);
    }

    @Deprecated(message = "Set a custom {@link androidx.fragment.app.FragmentFactory} via\n      {@link FragmentManager#setFragmentFactory(FragmentFactory)} to control\n      instantiation of Fragments.")
    public Fragment instantiateFragment(Context context, FragmentManager fragmentManager, String className, Bundle args) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(fragmentManager, "fragmentManager");
        Intrinsics.checkNotNullParameter(className, "className");
        Fragment instantiate = fragmentManager.getFragmentFactory().instantiate(context.getClassLoader(), className);
        Intrinsics.checkNotNullExpressionValue(instantiate, "fragmentManager.fragment…t.classLoader, className)");
        return instantiate;
    }

    @Override // androidx.navigation.Navigator
    public void navigate(List<NavBackStackEntry> entries, NavOptions navOptions, Navigator.Extras navigatorExtras) {
        Intrinsics.checkNotNullParameter(entries, "entries");
        if (this.fragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state");
            return;
        }
        for (NavBackStackEntry entry : entries) {
            navigate(entry, navOptions, navigatorExtras);
        }
    }

    private final void navigate(NavBackStackEntry entry, NavOptions navOptions, Navigator.Extras navigatorExtras) {
        boolean initialNavigation = getState().getBackStack().getValue().isEmpty();
        boolean restoreState = navOptions != null && !initialNavigation && navOptions.getRestoreState() && this.savedIds.remove(entry.getId());
        if (restoreState) {
            this.fragmentManager.restoreBackStack(entry.getId());
            getState().push(entry);
            return;
        }
        FragmentTransaction ft = createFragmentTransaction(entry, navOptions);
        if (!initialNavigation) {
            ft.addToBackStack(entry.getId());
        }
        if (navigatorExtras instanceof Extras) {
            for (Map.Entry<View, String> entry2 : ((Extras) navigatorExtras).getSharedElements().entrySet()) {
                View key = entry2.getKey();
                String value = entry2.getValue();
                ft.addSharedElement(key, value);
            }
        }
        ft.commit();
        getState().push(entry);
    }

    @Override // androidx.navigation.Navigator
    public void onLaunchSingleTop(NavBackStackEntry backStackEntry) {
        Intrinsics.checkNotNullParameter(backStackEntry, "backStackEntry");
        if (this.fragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring onLaunchSingleTop() call: FragmentManager has already saved its state");
            return;
        }
        FragmentTransaction ft = createFragmentTransaction(backStackEntry, null);
        if (getState().getBackStack().getValue().size() > 1) {
            this.fragmentManager.popBackStack(backStackEntry.getId(), 1);
            ft.addToBackStack(backStackEntry.getId());
        }
        ft.commit();
        getState().onLaunchSingleTop(backStackEntry);
    }

    private final FragmentTransaction createFragmentTransaction(NavBackStackEntry entry, NavOptions navOptions) {
        Destination destination = (Destination) entry.getDestination();
        Bundle args = entry.getArguments();
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = this.context.getPackageName() + className;
        }
        Fragment frag = this.fragmentManager.getFragmentFactory().instantiate(this.context.getClassLoader(), className);
        Intrinsics.checkNotNullExpressionValue(frag, "fragmentManager.fragment…t.classLoader, className)");
        frag.setArguments(args);
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(ft, "fragmentManager.beginTransaction()");
        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            ft.setCustomAnimations(enterAnim != -1 ? enterAnim : 0, exitAnim != -1 ? exitAnim : 0, popEnterAnim != -1 ? popEnterAnim : 0, popExitAnim != -1 ? popExitAnim : 0);
        }
        ft.replace(this.containerId, frag);
        ft.setPrimaryNavigationFragment(frag);
        ft.setReorderingAllowed(true);
        return ft;
    }

    @Override // androidx.navigation.Navigator
    public Bundle onSaveState() {
        if (this.savedIds.isEmpty()) {
            return null;
        }
        return BundleKt.bundleOf(TuplesKt.to(KEY_SAVED_IDS, new ArrayList(this.savedIds)));
    }

    @Override // androidx.navigation.Navigator
    public void onRestoreState(Bundle savedState) {
        Intrinsics.checkNotNullParameter(savedState, "savedState");
        ArrayList savedIds = savedState.getStringArrayList(KEY_SAVED_IDS);
        if (savedIds != null) {
            this.savedIds.clear();
            CollectionsKt.addAll(this.savedIds, savedIds);
        }
    }

    /* compiled from: FragmentNavigator.kt */
    @Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0017\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0015\u0012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00000\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0017J\u000e\u0010\u0019\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\tJ\b\u0010\u001a\u001a\u00020\tH\u0016R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u001b"}, d2 = {"Landroidx/navigation/fragment/FragmentNavigator$Destination;", "Landroidx/navigation/NavDestination;", "navigatorProvider", "Landroidx/navigation/NavigatorProvider;", "(Landroidx/navigation/NavigatorProvider;)V", "fragmentNavigator", "Landroidx/navigation/Navigator;", "(Landroidx/navigation/Navigator;)V", "_className", "", "className", "getClassName", "()Ljava/lang/String;", "equals", "", "other", "", "hashCode", "", "onInflate", "", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "setClassName", "toString", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static class Destination extends NavDestination {
        private String _className;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Destination(Navigator<? extends Destination> fragmentNavigator) {
            super(fragmentNavigator);
            Intrinsics.checkNotNullParameter(fragmentNavigator, "fragmentNavigator");
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public Destination(NavigatorProvider navigatorProvider) {
            this((Navigator<? extends Destination>) navigatorProvider.getNavigator(FragmentNavigator.class));
            Intrinsics.checkNotNullParameter(navigatorProvider, "navigatorProvider");
        }

        @Override // androidx.navigation.NavDestination
        public void onInflate(Context context, AttributeSet attrs) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(attrs, "attrs");
            super.onInflate(context, attrs);
            TypedArray $this$use$iv = context.getResources().obtainAttributes(attrs, R.styleable.FragmentNavigator);
            Intrinsics.checkNotNullExpressionValue($this$use$iv, "context.resources.obtain…leable.FragmentNavigator)");
            String className = $this$use$iv.getString(R.styleable.FragmentNavigator_android_name);
            if (className != null) {
                setClassName(className);
            }
            Unit unit = Unit.INSTANCE;
            $this$use$iv.recycle();
        }

        public final Destination setClassName(String className) {
            Intrinsics.checkNotNullParameter(className, "className");
            this._className = className;
            return this;
        }

        public final String getClassName() {
            String str = this._className;
            if (str == null) {
                throw new IllegalStateException("Fragment class was not set".toString());
            }
            if (str != null) {
                return str;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
        }

        @Override // androidx.navigation.NavDestination
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" class=");
            String str = this._className;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            String sb2 = sb.toString();
            Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
            return sb2;
        }

        @Override // androidx.navigation.NavDestination
        public boolean equals(Object other) {
            return other != null && (other instanceof Destination) && super.equals(other) && Intrinsics.areEqual(this._className, ((Destination) other)._className);
        }

        @Override // androidx.navigation.NavDestination
        public int hashCode() {
            int result = super.hashCode();
            int i = result * 31;
            String str = this._className;
            int result2 = i + (str != null ? str.hashCode() : 0);
            return result2;
        }
    }

    /* compiled from: FragmentNavigator.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\fB\u001b\b\u0000\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\u0010\u0006R*\u0010\u0007\u001a\u001e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\bj\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0005`\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u00038F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\r"}, d2 = {"Landroidx/navigation/fragment/FragmentNavigator$Extras;", "Landroidx/navigation/Navigator$Extras;", "sharedElements", "", "Landroid/view/View;", "", "(Ljava/util/Map;)V", "_sharedElements", "Ljava/util/LinkedHashMap;", "Lkotlin/collections/LinkedHashMap;", "getSharedElements", "()Ljava/util/Map;", "Builder", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Extras implements Navigator.Extras {
        private final LinkedHashMap<View, String> _sharedElements;

        public Extras(Map<View, String> sharedElements) {
            Intrinsics.checkNotNullParameter(sharedElements, "sharedElements");
            LinkedHashMap<View, String> linkedHashMap = new LinkedHashMap<>();
            this._sharedElements = linkedHashMap;
            linkedHashMap.putAll(sharedElements);
        }

        public final Map<View, String> getSharedElements() {
            return MapsKt.toMap(this._sharedElements);
        }

        /* compiled from: FragmentNavigator.kt */
        @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0006J\u001a\u0010\u000b\u001a\u00020\u00002\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\rJ\u0006\u0010\u000e\u001a\u00020\u000fR*\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Landroidx/navigation/fragment/FragmentNavigator$Extras$Builder;", "", "()V", "_sharedElements", "Ljava/util/LinkedHashMap;", "Landroid/view/View;", "", "Lkotlin/collections/LinkedHashMap;", "addSharedElement", "sharedElement", "name", "addSharedElements", "sharedElements", "", "build", "Landroidx/navigation/fragment/FragmentNavigator$Extras;", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
        /* loaded from: classes.dex */
        public static final class Builder {
            private final LinkedHashMap<View, String> _sharedElements = new LinkedHashMap<>();

            public final Builder addSharedElements(Map<View, String> sharedElements) {
                Intrinsics.checkNotNullParameter(sharedElements, "sharedElements");
                for (Map.Entry<View, String> entry : sharedElements.entrySet()) {
                    View view = entry.getKey();
                    String name = entry.getValue();
                    addSharedElement(view, name);
                }
                return this;
            }

            public final Builder addSharedElement(View sharedElement, String name) {
                Intrinsics.checkNotNullParameter(sharedElement, "sharedElement");
                Intrinsics.checkNotNullParameter(name, "name");
                this._sharedElements.put(sharedElement, name);
                return this;
            }

            public final Extras build() {
                return new Extras(this._sharedElements);
            }
        }
    }

    /* compiled from: FragmentNavigator.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Landroidx/navigation/fragment/FragmentNavigator$Companion;", "", "()V", "KEY_SAVED_IDS", "", "TAG", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
