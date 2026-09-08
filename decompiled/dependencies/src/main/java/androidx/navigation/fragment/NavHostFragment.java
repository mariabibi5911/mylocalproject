package androidx.navigation.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NavHostFragment.kt */
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u0000 02\u00020\u00012\u00020\u0002:\u00010B\u0005¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0015\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170\u0016H\u0015J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0017J\u0012\u0010\u001c\u001a\u00020\u00192\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0017J\u0010\u0010\u001f\u001a\u00020\u00192\u0006\u0010\r\u001a\u00020\u000eH\u0015J\u0010\u0010 \u001a\u00020\u00192\u0006\u0010\u0011\u001a\u00020\u0012H\u0015J&\u0010!\u001a\u0004\u0018\u00010\u00142\u0006\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010%2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010&\u001a\u00020\u0019H\u0016J\"\u0010'\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010(\u001a\u00020)2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0017J\u0010\u0010*\u001a\u00020\u00192\u0006\u0010+\u001a\u00020\tH\u0017J\u0010\u0010,\u001a\u00020\u00192\u0006\u0010-\u001a\u00020\u001eH\u0017J\u001a\u0010.\u001a\u00020\u00192\u0006\u0010/\u001a\u00020\u00142\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016R\u0014\u0010\u0004\u001a\u00020\u00058BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\fR\u0011\u0010\r\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Landroidx/navigation/fragment/NavHostFragment;", "Landroidx/fragment/app/Fragment;", "Landroidx/navigation/NavHost;", "()V", "containerId", "", "getContainerId", "()I", "defaultNavHost", "", "graphId", "isPrimaryBeforeOnCreate", "Ljava/lang/Boolean;", "navController", "Landroidx/navigation/NavController;", "getNavController", "()Landroidx/navigation/NavController;", "navHostController", "Landroidx/navigation/NavHostController;", "viewParent", "Landroid/view/View;", "createFragmentNavigator", "Landroidx/navigation/Navigator;", "Landroidx/navigation/fragment/FragmentNavigator$Destination;", "onAttach", "", "context", "Landroid/content/Context;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateNavController", "onCreateNavHostController", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onInflate", "attrs", "Landroid/util/AttributeSet;", "onPrimaryNavigationFragmentChanged", "isPrimaryNavigationFragment", "onSaveInstanceState", "outState", "onViewCreated", "view", "Companion", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public class NavHostFragment extends Fragment implements NavHost {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final String KEY_DEFAULT_NAV_HOST = "android-support-nav:fragment:defaultHost";
    public static final String KEY_GRAPH_ID = "android-support-nav:fragment:graphId";
    private static final String KEY_NAV_CONTROLLER_STATE = "android-support-nav:fragment:navControllerState";
    public static final String KEY_START_DESTINATION_ARGS = "android-support-nav:fragment:startDestinationArgs";
    private boolean defaultNavHost;
    private int graphId;
    private Boolean isPrimaryBeforeOnCreate;
    private NavHostController navHostController;
    private View viewParent;

    @JvmStatic
    public static final NavHostFragment create(int i) {
        return INSTANCE.create(i);
    }

    @JvmStatic
    public static final NavHostFragment create(int i, Bundle bundle) {
        return INSTANCE.create(i, bundle);
    }

    @JvmStatic
    public static final NavController findNavController(Fragment fragment) {
        return INSTANCE.findNavController(fragment);
    }

    @Override // androidx.navigation.NavHost
    public final NavController getNavController() {
        NavHostController navHostController = this.navHostController;
        if (navHostController == null) {
            throw new IllegalStateException("NavController is not available before onCreate()".toString());
        }
        if (navHostController != null) {
            return navHostController;
        }
        throw new NullPointerException("null cannot be cast to non-null type androidx.navigation.NavHostController");
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        super.onAttach(context);
        if (this.defaultNavHost) {
            getParentFragmentManager().beginTransaction().setPrimaryNavigationFragment(this).commit();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c8  */
    @Override // androidx.fragment.app.Fragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCreate(Bundle savedInstanceState) {
        boolean z;
        Bundle navState;
        Context context = requireContext();
        Intrinsics.checkNotNullExpressionValue(context, "requireContext()");
        NavHostController navHostController = new NavHostController(context);
        this.navHostController = navHostController;
        Intrinsics.checkNotNull(navHostController);
        navHostController.setLifecycleOwner(this);
        while (true) {
            if (!(context instanceof ContextWrapper)) {
                break;
            }
            if (context instanceof OnBackPressedDispatcherOwner) {
                NavHostController navHostController2 = this.navHostController;
                Intrinsics.checkNotNull(navHostController2);
                OnBackPressedDispatcher onBackPressedDispatcher = ((OnBackPressedDispatcherOwner) context).getOnBackPressedDispatcher();
                Intrinsics.checkNotNullExpressionValue(onBackPressedDispatcher, "context as OnBackPressed…).onBackPressedDispatcher");
                navHostController2.setOnBackPressedDispatcher(onBackPressedDispatcher);
                break;
            }
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            Intrinsics.checkNotNullExpressionValue(baseContext, "context.baseContext");
            context = baseContext;
        }
        NavHostController navHostController3 = this.navHostController;
        Intrinsics.checkNotNull(navHostController3);
        Boolean bool = this.isPrimaryBeforeOnCreate;
        if (bool != null) {
            if (bool == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Boolean");
            }
            if (bool.booleanValue()) {
                z = true;
                navHostController3.enableOnBackPressed(z);
                this.isPrimaryBeforeOnCreate = null;
                NavHostController navHostController4 = this.navHostController;
                Intrinsics.checkNotNull(navHostController4);
                ViewModelStore viewModelStore = getViewModelStore();
                Intrinsics.checkNotNullExpressionValue(viewModelStore, "viewModelStore");
                navHostController4.setViewModelStore(viewModelStore);
                NavHostController navHostController5 = this.navHostController;
                Intrinsics.checkNotNull(navHostController5);
                onCreateNavHostController(navHostController5);
                navState = null;
                if (savedInstanceState != null) {
                    navState = savedInstanceState.getBundle(KEY_NAV_CONTROLLER_STATE);
                    if (savedInstanceState.getBoolean(KEY_DEFAULT_NAV_HOST, false)) {
                        this.defaultNavHost = true;
                        getParentFragmentManager().beginTransaction().setPrimaryNavigationFragment(this).commit();
                    }
                    this.graphId = savedInstanceState.getInt(KEY_GRAPH_ID);
                }
                if (navState != null) {
                    NavHostController navHostController6 = this.navHostController;
                    Intrinsics.checkNotNull(navHostController6);
                    navHostController6.restoreState(navState);
                }
                if (this.graphId == 0) {
                    NavHostController navHostController7 = this.navHostController;
                    Intrinsics.checkNotNull(navHostController7);
                    navHostController7.setGraph(this.graphId);
                } else {
                    Bundle args = getArguments();
                    int graphId = args != null ? args.getInt(KEY_GRAPH_ID) : 0;
                    Bundle startDestinationArgs = args != null ? args.getBundle(KEY_START_DESTINATION_ARGS) : null;
                    if (graphId != 0) {
                        NavHostController navHostController8 = this.navHostController;
                        Intrinsics.checkNotNull(navHostController8);
                        navHostController8.setGraph(graphId, startDestinationArgs);
                    }
                }
                super.onCreate(savedInstanceState);
            }
        }
        z = false;
        navHostController3.enableOnBackPressed(z);
        this.isPrimaryBeforeOnCreate = null;
        NavHostController navHostController42 = this.navHostController;
        Intrinsics.checkNotNull(navHostController42);
        ViewModelStore viewModelStore2 = getViewModelStore();
        Intrinsics.checkNotNullExpressionValue(viewModelStore2, "viewModelStore");
        navHostController42.setViewModelStore(viewModelStore2);
        NavHostController navHostController52 = this.navHostController;
        Intrinsics.checkNotNull(navHostController52);
        onCreateNavHostController(navHostController52);
        navState = null;
        if (savedInstanceState != null) {
        }
        if (navState != null) {
        }
        if (this.graphId == 0) {
        }
        super.onCreate(savedInstanceState);
    }

    protected void onCreateNavHostController(NavHostController navHostController) {
        Intrinsics.checkNotNullParameter(navHostController, "navHostController");
        onCreateNavController(navHostController);
    }

    @Deprecated(message = "Override {@link #onCreateNavHostController(NavHostController)} to gain\n      access to the full {@link NavHostController} that is created by this NavHostFragment.")
    protected void onCreateNavController(NavController navController) {
        Intrinsics.checkNotNullParameter(navController, "navController");
        NavigatorProvider $this$plusAssign$iv = navController.get_navigatorProvider();
        Context requireContext = requireContext();
        Intrinsics.checkNotNullExpressionValue(requireContext, "requireContext()");
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager, "childFragmentManager");
        Navigator navigator$iv = new DialogFragmentNavigator(requireContext, childFragmentManager);
        $this$plusAssign$iv.addNavigator(navigator$iv);
        NavigatorProvider $this$plusAssign$iv2 = navController.get_navigatorProvider();
        $this$plusAssign$iv2.addNavigator(createFragmentNavigator());
    }

    @Override // androidx.fragment.app.Fragment
    public void onPrimaryNavigationFragmentChanged(boolean isPrimaryNavigationFragment) {
        NavHostController navHostController = this.navHostController;
        if (navHostController != null) {
            if (navHostController != null) {
                navHostController.enableOnBackPressed(isPrimaryNavigationFragment);
                return;
            }
            return;
        }
        this.isPrimaryBeforeOnCreate = Boolean.valueOf(isPrimaryNavigationFragment);
    }

    @Deprecated(message = "Use {@link #onCreateNavController(NavController)}")
    protected Navigator<? extends FragmentNavigator.Destination> createFragmentNavigator() {
        Context requireContext = requireContext();
        Intrinsics.checkNotNullExpressionValue(requireContext, "requireContext()");
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager, "childFragmentManager");
        return new FragmentNavigator(requireContext, childFragmentManager, getContainerId());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        Context context = inflater.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "inflater.context");
        FragmentContainerView containerView = new FragmentContainerView(context);
        containerView.setId(getContainerId());
        return containerView;
    }

    private final int getContainerId() {
        int id = getId();
        if (id != 0 && id != -1) {
            return id;
        }
        return R.id.nav_host_fragment_container;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        if (!(view instanceof ViewGroup)) {
            throw new IllegalStateException(("created host view " + view + " is not a ViewGroup").toString());
        }
        Navigation.setViewNavController(view, this.navHostController);
        if (view.getParent() != null) {
            Object parent = view.getParent();
            if (parent == null) {
                throw new NullPointerException("null cannot be cast to non-null type android.view.View");
            }
            View view2 = (View) parent;
            this.viewParent = view2;
            Intrinsics.checkNotNull(view2);
            if (view2.getId() == getId()) {
                View view3 = this.viewParent;
                Intrinsics.checkNotNull(view3);
                Navigation.setViewNavController(view3, this.navHostController);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray $this$use$iv = context.obtainStyledAttributes(attrs, androidx.navigation.R.styleable.NavHost);
        Intrinsics.checkNotNullExpressionValue($this$use$iv, "context.obtainStyledAttr…yleable.NavHost\n        )");
        int graphId = $this$use$iv.getResourceId(androidx.navigation.R.styleable.NavHost_navGraph, 0);
        if (graphId != 0) {
            this.graphId = graphId;
        }
        Unit unit = Unit.INSTANCE;
        $this$use$iv.recycle();
        TypedArray $this$use$iv2 = context.obtainStyledAttributes(attrs, R.styleable.NavHostFragment);
        Intrinsics.checkNotNullExpressionValue($this$use$iv2, "context.obtainStyledAttr…tyleable.NavHostFragment)");
        boolean defaultHost = $this$use$iv2.getBoolean(R.styleable.NavHostFragment_defaultNavHost, false);
        if (defaultHost) {
            this.defaultNavHost = true;
        }
        Unit unit2 = Unit.INSTANCE;
        $this$use$iv2.recycle();
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        Intrinsics.checkNotNullParameter(outState, "outState");
        super.onSaveInstanceState(outState);
        NavHostController navHostController = this.navHostController;
        Intrinsics.checkNotNull(navHostController);
        Bundle navState = navHostController.saveState();
        if (navState != null) {
            outState.putBundle(KEY_NAV_CONTROLLER_STATE, navState);
        }
        if (this.defaultNavHost) {
            outState.putBoolean(KEY_DEFAULT_NAV_HOST, true);
        }
        int i = this.graphId;
        if (i != 0) {
            outState.putInt(KEY_GRAPH_ID, i);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        View it = this.viewParent;
        if (it != null && Navigation.findNavController(it) == this.navHostController) {
            Navigation.setViewNavController(it, null);
        }
        this.viewParent = null;
    }

    /* compiled from: NavHostFragment.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\n\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\rH\u0007J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00048\u0006X\u0087T¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Landroidx/navigation/fragment/NavHostFragment$Companion;", "", "()V", "KEY_DEFAULT_NAV_HOST", "", "KEY_GRAPH_ID", "KEY_NAV_CONTROLLER_STATE", "KEY_START_DESTINATION_ARGS", "create", "Landroidx/navigation/fragment/NavHostFragment;", "graphResId", "", "startDestinationArgs", "Landroid/os/Bundle;", "findNavController", "Landroidx/navigation/NavController;", "fragment", "Landroidx/fragment/app/Fragment;", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final NavHostFragment create(int i) {
            return create$default(this, i, null, 2, null);
        }

        private Companion() {
        }

        @JvmStatic
        public final NavController findNavController(Fragment fragment) {
            Dialog dialog;
            Window window;
            Intrinsics.checkNotNullParameter(fragment, "fragment");
            for (Fragment findFragment = fragment; findFragment != null; findFragment = findFragment.getParentFragment()) {
                if (findFragment instanceof NavHostFragment) {
                    NavHostController navHostController = ((NavHostFragment) findFragment).navHostController;
                    if (navHostController != null) {
                        return navHostController;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type androidx.navigation.NavController");
                }
                Fragment primaryNavFragment = findFragment.getParentFragmentManager().getPrimaryNavigationFragment();
                if (primaryNavFragment instanceof NavHostFragment) {
                    NavHostController navHostController2 = ((NavHostFragment) primaryNavFragment).navHostController;
                    if (navHostController2 != null) {
                        return navHostController2;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type androidx.navigation.NavController");
                }
            }
            View view = fragment.getView();
            if (view != null) {
                return Navigation.findNavController(view);
            }
            View view2 = null;
            DialogFragment dialogFragment = fragment instanceof DialogFragment ? (DialogFragment) fragment : null;
            if (dialogFragment != null && (dialog = dialogFragment.getDialog()) != null && (window = dialog.getWindow()) != null) {
                view2 = window.getDecorView();
            }
            View dialogDecorView = view2;
            if (dialogDecorView != null) {
                return Navigation.findNavController(dialogDecorView);
            }
            throw new IllegalStateException("Fragment " + fragment + " does not have a NavController set");
        }

        public static /* synthetic */ NavHostFragment create$default(Companion companion, int i, Bundle bundle, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                bundle = null;
            }
            return companion.create(i, bundle);
        }

        @JvmStatic
        public final NavHostFragment create(int graphResId, Bundle startDestinationArgs) {
            Bundle b = null;
            if (graphResId != 0) {
                b = new Bundle();
                b.putInt(NavHostFragment.KEY_GRAPH_ID, graphResId);
            }
            if (startDestinationArgs != null) {
                if (b == null) {
                    b = new Bundle();
                }
                b.putBundle(NavHostFragment.KEY_START_DESTINATION_ARGS, startDestinationArgs);
            }
            NavHostFragment result = new NavHostFragment();
            if (b != null) {
                result.setArguments(b);
            }
            return result;
        }
    }
}
