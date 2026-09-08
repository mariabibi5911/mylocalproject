package androidx.navigation.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.FloatingWindow;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.NavigatorState;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/* compiled from: DialogFragmentNavigator.kt */
@Navigator.Name("dialog")
@Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u0000 \u001f2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u001f B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\r\u001a\u00020\u0002H\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J*\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0018\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001eH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Landroidx/navigation/fragment/DialogFragmentNavigator;", "Landroidx/navigation/Navigator;", "Landroidx/navigation/fragment/DialogFragmentNavigator$Destination;", "context", "Landroid/content/Context;", "fragmentManager", "Landroidx/fragment/app/FragmentManager;", "(Landroid/content/Context;Landroidx/fragment/app/FragmentManager;)V", "observer", "Landroidx/lifecycle/LifecycleEventObserver;", "restoredTagsAwaitingAttach", "", "", "createDestination", "navigate", "", "entry", "Landroidx/navigation/NavBackStackEntry;", "entries", "", "navOptions", "Landroidx/navigation/NavOptions;", "navigatorExtras", "Landroidx/navigation/Navigator$Extras;", "onAttach", "state", "Landroidx/navigation/NavigatorState;", "popBackStack", "popUpTo", "savedState", "", "Companion", "Destination", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class DialogFragmentNavigator extends Navigator<Destination> {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    private static final String TAG = "DialogFragmentNavigator";
    private final Context context;
    private final FragmentManager fragmentManager;
    private final LifecycleEventObserver observer;
    private final Set<String> restoredTagsAwaitingAttach;

    public DialogFragmentNavigator(Context context, FragmentManager fragmentManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(fragmentManager, "fragmentManager");
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.restoredTagsAwaitingAttach = new LinkedHashSet();
        this.observer = new LifecycleEventObserver() { // from class: androidx.navigation.fragment.DialogFragmentNavigator$$ExternalSyntheticLambda1
            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                DialogFragmentNavigator.m78observer$lambda3(DialogFragmentNavigator.this, lifecycleOwner, event);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: observer$lambda-3, reason: not valid java name */
    public static final void m78observer$lambda3(DialogFragmentNavigator this$0, LifecycleOwner source, Lifecycle.Event event) {
        Object element$iv;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(event, "event");
        boolean dialogOnBackStack = false;
        if (event == Lifecycle.Event.ON_CREATE) {
            DialogFragment dialogFragment = (DialogFragment) source;
            Iterable $this$any$iv = this$0.getState().getBackStack().getValue();
            if (!($this$any$iv instanceof Collection) || !((Collection) $this$any$iv).isEmpty()) {
                Iterator it = $this$any$iv.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Object element$iv2 = it.next();
                    NavBackStackEntry it2 = (NavBackStackEntry) element$iv2;
                    if (Intrinsics.areEqual(it2.getId(), dialogFragment.getTag())) {
                        dialogOnBackStack = true;
                        break;
                    }
                }
            }
            if (dialogOnBackStack) {
                return;
            }
            dialogFragment.dismiss();
            return;
        }
        if (event == Lifecycle.Event.ON_STOP) {
            DialogFragment dialogFragment2 = (DialogFragment) source;
            if (dialogFragment2.requireDialog().isShowing()) {
                return;
            }
            List beforePopList = this$0.getState().getBackStack().getValue();
            ListIterator iterator$iv = beforePopList.listIterator(beforePopList.size());
            while (true) {
                if (!iterator$iv.hasPrevious()) {
                    element$iv = null;
                    break;
                }
                element$iv = iterator$iv.previous();
                NavBackStackEntry it3 = (NavBackStackEntry) element$iv;
                if (Intrinsics.areEqual(it3.getId(), dialogFragment2.getTag())) {
                    break;
                }
            }
            if (element$iv == null) {
                throw new IllegalStateException(("Dialog " + dialogFragment2 + " has already been popped off of the Navigation back stack").toString());
            }
            NavBackStackEntry poppedEntry = (NavBackStackEntry) element$iv;
            if (!Intrinsics.areEqual(CollectionsKt.lastOrNull(beforePopList), poppedEntry)) {
                Log.i(TAG, "Dialog " + dialogFragment2 + " was dismissed while it was not the top of the back stack, popping all dialogs above this dismissed dialog");
            }
            this$0.popBackStack(poppedEntry, false);
        }
    }

    @Override // androidx.navigation.Navigator
    public void popBackStack(NavBackStackEntry popUpTo, boolean savedState) {
        Intrinsics.checkNotNullParameter(popUpTo, "popUpTo");
        if (this.fragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring popBackStack() call: FragmentManager has already saved its state");
            return;
        }
        List beforePopList = getState().getBackStack().getValue();
        List poppedList = beforePopList.subList(beforePopList.indexOf(popUpTo), beforePopList.size());
        for (NavBackStackEntry entry : CollectionsKt.reversed(poppedList)) {
            Fragment existingFragment = this.fragmentManager.findFragmentByTag(entry.getId());
            if (existingFragment != null) {
                existingFragment.getLifecycle().removeObserver(this.observer);
                ((DialogFragment) existingFragment).dismiss();
            }
        }
        getState().pop(popUpTo, savedState);
    }

    @Override // androidx.navigation.Navigator
    public Destination createDestination() {
        return new Destination(this);
    }

    @Override // androidx.navigation.Navigator
    public void navigate(List<NavBackStackEntry> entries, NavOptions navOptions, Navigator.Extras navigatorExtras) {
        Intrinsics.checkNotNullParameter(entries, "entries");
        if (this.fragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state");
            return;
        }
        for (NavBackStackEntry entry : entries) {
            navigate(entry);
        }
    }

    private final void navigate(NavBackStackEntry entry) {
        Destination destination = (Destination) entry.getDestination();
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = this.context.getPackageName() + className;
        }
        Fragment frag = this.fragmentManager.getFragmentFactory().instantiate(this.context.getClassLoader(), className);
        Intrinsics.checkNotNullExpressionValue(frag, "fragmentManager.fragment…ader, className\n        )");
        if (!DialogFragment.class.isAssignableFrom(frag.getClass())) {
            throw new IllegalArgumentException(("Dialog destination " + destination.getClassName() + " is not an instance of DialogFragment").toString());
        }
        DialogFragment dialogFragment = (DialogFragment) frag;
        dialogFragment.setArguments(entry.getArguments());
        dialogFragment.getLifecycle().addObserver(this.observer);
        dialogFragment.show(this.fragmentManager, entry.getId());
        getState().push(entry);
    }

    @Override // androidx.navigation.Navigator
    public void onAttach(NavigatorState state) {
        Lifecycle lifecycle;
        Intrinsics.checkNotNullParameter(state, "state");
        super.onAttach(state);
        for (NavBackStackEntry entry : state.getBackStack().getValue()) {
            DialogFragment fragment = (DialogFragment) this.fragmentManager.findFragmentByTag(entry.getId());
            if (fragment == null || (lifecycle = fragment.getLifecycle()) == null) {
                this.restoredTagsAwaitingAttach.add(entry.getId());
            } else {
                lifecycle.addObserver(this.observer);
            }
        }
        this.fragmentManager.addFragmentOnAttachListener(new FragmentOnAttachListener() { // from class: androidx.navigation.fragment.DialogFragmentNavigator$$ExternalSyntheticLambda0
            @Override // androidx.fragment.app.FragmentOnAttachListener
            public final void onAttachFragment(FragmentManager fragmentManager, Fragment fragment2) {
                DialogFragmentNavigator.m79onAttach$lambda5(DialogFragmentNavigator.this, fragmentManager, fragment2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onAttach$lambda-5, reason: not valid java name */
    public static final void m79onAttach$lambda5(DialogFragmentNavigator this$0, FragmentManager fragmentManager, Fragment childFragment) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(fragmentManager, "<anonymous parameter 0>");
        Intrinsics.checkNotNullParameter(childFragment, "childFragment");
        Set<String> set = this$0.restoredTagsAwaitingAttach;
        boolean needToAddObserver = TypeIntrinsics.asMutableCollection(set).remove(childFragment.getTag());
        if (needToAddObserver) {
            childFragment.getLifecycle().addObserver(this$0.observer);
        }
    }

    /* compiled from: DialogFragmentNavigator.kt */
    @Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0015\u0012\u000e\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00000\u0007¢\u0006\u0002\u0010\bJ\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0017J\u000e\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\nR\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u001b"}, d2 = {"Landroidx/navigation/fragment/DialogFragmentNavigator$Destination;", "Landroidx/navigation/NavDestination;", "Landroidx/navigation/FloatingWindow;", "navigatorProvider", "Landroidx/navigation/NavigatorProvider;", "(Landroidx/navigation/NavigatorProvider;)V", "fragmentNavigator", "Landroidx/navigation/Navigator;", "(Landroidx/navigation/Navigator;)V", "_className", "", "className", "getClassName", "()Ljava/lang/String;", "equals", "", "other", "", "hashCode", "", "onInflate", "", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "setClassName", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static class Destination extends NavDestination implements FloatingWindow {
        private String _className;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Destination(Navigator<? extends Destination> fragmentNavigator) {
            super(fragmentNavigator);
            Intrinsics.checkNotNullParameter(fragmentNavigator, "fragmentNavigator");
        }

        public final String getClassName() {
            String str = this._className;
            if (str == null) {
                throw new IllegalStateException("DialogFragment class was not set".toString());
            }
            if (str != null) {
                return str;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public Destination(NavigatorProvider navigatorProvider) {
            this((Navigator<? extends Destination>) navigatorProvider.getNavigator(DialogFragmentNavigator.class));
            Intrinsics.checkNotNullParameter(navigatorProvider, "navigatorProvider");
        }

        @Override // androidx.navigation.NavDestination
        public void onInflate(Context context, AttributeSet attrs) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(attrs, "attrs");
            super.onInflate(context, attrs);
            TypedArray $this$use$iv = context.getResources().obtainAttributes(attrs, R.styleable.DialogFragmentNavigator);
            Intrinsics.checkNotNullExpressionValue($this$use$iv, "context.resources.obtain…ntNavigator\n            )");
            String className = $this$use$iv.getString(R.styleable.DialogFragmentNavigator_android_name);
            Destination className2 = className != null ? setClassName(className) : null;
            $this$use$iv.recycle();
        }

        public final Destination setClassName(String className) {
            Intrinsics.checkNotNullParameter(className, "className");
            this._className = className;
            return this;
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

    /* compiled from: DialogFragmentNavigator.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Landroidx/navigation/fragment/DialogFragmentNavigator$Companion;", "", "()V", "TAG", "", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
