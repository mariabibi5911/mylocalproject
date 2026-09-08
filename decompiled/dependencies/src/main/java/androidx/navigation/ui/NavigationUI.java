package androidx.navigation.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.customview.widget.Openable;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.ui.AppBarConfiguration;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

/* compiled from: NavigationUI.kt */
@Metadata(d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u001a\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0007J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0018\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\nH\u0007J \u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\bH\u0007J\"\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0007J\"\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007J\"\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0007J\"\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007J*\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0007J*\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007J\u0018\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\t\u001a\u00020\nH\u0007J \u0010\u0017\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\bH\u0007J\u0018\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\t\u001a\u00020\nH\u0007J \u0010\u0017\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\bH\u0007J\u001b\u0010 \u001a\u00020\b*\u00020!2\b\b\u0001\u0010\"\u001a\u00020#H\u0001¢\u0006\u0002\b$J!\u0010%\u001a\u00020\b*\u00020!2\u000e\u0010&\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010#0'H\u0001¢\u0006\u0002\b(¨\u0006)"}, d2 = {"Landroidx/navigation/ui/NavigationUI;", "", "()V", "findBottomSheetBehavior", "Lcom/google/android/material/bottomsheet/BottomSheetBehavior;", "view", "Landroid/view/View;", "navigateUp", "", "navController", "Landroidx/navigation/NavController;", "openableLayout", "Landroidx/customview/widget/Openable;", "configuration", "Landroidx/navigation/ui/AppBarConfiguration;", "onNavDestinationSelected", "item", "Landroid/view/MenuItem;", "saveState", "setupActionBarWithNavController", "", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "setupWithNavController", "toolbar", "Landroidx/appcompat/widget/Toolbar;", "collapsingToolbarLayout", "Lcom/google/android/material/appbar/CollapsingToolbarLayout;", "navigationBarView", "Lcom/google/android/material/navigation/NavigationBarView;", "navigationView", "Lcom/google/android/material/navigation/NavigationView;", "matchDestination", "Landroidx/navigation/NavDestination;", "destId", "", "matchDestination$navigation_ui_release", "matchDestinations", "destinationIds", "", "matchDestinations$navigation_ui_release", "navigation-ui_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public final class NavigationUI {
    public static final NavigationUI INSTANCE = new NavigationUI();

    @JvmStatic
    public static final void setupActionBarWithNavController(AppCompatActivity activity, NavController navController) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(navController, "navController");
        setupActionBarWithNavController$default(activity, navController, null, 4, null);
    }

    @JvmStatic
    public static final void setupWithNavController(Toolbar toolbar, NavController navController) {
        Intrinsics.checkNotNullParameter(toolbar, "toolbar");
        Intrinsics.checkNotNullParameter(navController, "navController");
        setupWithNavController$default(toolbar, navController, null, 4, null);
    }

    @JvmStatic
    public static final void setupWithNavController(CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, NavController navController) {
        Intrinsics.checkNotNullParameter(collapsingToolbarLayout, "collapsingToolbarLayout");
        Intrinsics.checkNotNullParameter(toolbar, "toolbar");
        Intrinsics.checkNotNullParameter(navController, "navController");
        setupWithNavController$default(collapsingToolbarLayout, toolbar, navController, null, 8, null);
    }

    private NavigationUI() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x009b, code lost:

        if (matchDestination$navigation_ui_release(r4, r6.getItemId()) == true) goto L16;
     */
    @JvmStatic
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final boolean onNavDestinationSelected(MenuItem item, NavController navController) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(navController, "navController");
        boolean z = true;
        NavOptions.Builder builder = new NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true);
        NavDestination currentDestination = navController.getCurrentDestination();
        Intrinsics.checkNotNull(currentDestination);
        NavGraph parent = currentDestination.getParent();
        Intrinsics.checkNotNull(parent);
        if (parent.findNode(item.getItemId()) instanceof ActivityNavigator.Destination) {
            builder.setEnterAnim(R.anim.nav_default_enter_anim).setExitAnim(R.anim.nav_default_exit_anim).setPopEnterAnim(R.anim.nav_default_pop_enter_anim).setPopExitAnim(R.anim.nav_default_pop_exit_anim);
        } else {
            builder.setEnterAnim(R.animator.nav_default_enter_anim).setExitAnim(R.animator.nav_default_exit_anim).setPopEnterAnim(R.animator.nav_default_pop_enter_anim).setPopExitAnim(R.animator.nav_default_pop_exit_anim);
        }
        if ((item.getOrder() & 196608) == 0) {
            builder.setPopUpTo(NavGraph.INSTANCE.findStartDestination(navController.getGraph()).getId(), false, true);
        }
        NavOptions options = builder.build();
        try {
            navController.navigate(item.getItemId(), (Bundle) null, options);
            NavDestination currentDestination2 = navController.getCurrentDestination();
            if (currentDestination2 != null) {
            }
            z = false;
            return z;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x009e, code lost:

        if (matchDestination$navigation_ui_release(r4, r8.getItemId()) == true) goto L18;
     */
    @NavigationUiSaveStateControl
    @JvmStatic
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final boolean onNavDestinationSelected(MenuItem item, NavController navController, boolean saveState) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(navController, "navController");
        if (!(!saveState)) {
            throw new IllegalStateException("Leave the saveState parameter out entirely to use the non-experimental version of this API, which saves the state by default".toString());
        }
        boolean z = true;
        NavOptions.Builder builder = new NavOptions.Builder().setLaunchSingleTop(true);
        NavDestination currentDestination = navController.getCurrentDestination();
        Intrinsics.checkNotNull(currentDestination);
        NavGraph parent = currentDestination.getParent();
        Intrinsics.checkNotNull(parent);
        if (parent.findNode(item.getItemId()) instanceof ActivityNavigator.Destination) {
            builder.setEnterAnim(R.anim.nav_default_enter_anim).setExitAnim(R.anim.nav_default_exit_anim).setPopEnterAnim(R.anim.nav_default_pop_enter_anim).setPopExitAnim(R.anim.nav_default_pop_exit_anim);
        } else {
            builder.setEnterAnim(R.animator.nav_default_enter_anim).setExitAnim(R.animator.nav_default_exit_anim).setPopEnterAnim(R.animator.nav_default_pop_enter_anim).setPopExitAnim(R.animator.nav_default_pop_exit_anim);
        }
        if ((item.getOrder() & 196608) == 0) {
            NavOptions.Builder.setPopUpTo$default(builder, NavGraph.INSTANCE.findStartDestination(navController.getGraph()).getId(), false, false, 4, (Object) null);
        }
        NavOptions options = builder.build();
        try {
            navController.navigate(item.getItemId(), (Bundle) null, options);
            NavDestination currentDestination2 = navController.getCurrentDestination();
            if (currentDestination2 != null) {
            }
            z = false;
            return z;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @JvmStatic
    public static final boolean navigateUp(NavController navController, Openable openableLayout) {
        Intrinsics.checkNotNullParameter(navController, "navController");
        return navigateUp(navController, new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(openableLayout).build());
    }

    @JvmStatic
    public static final boolean navigateUp(NavController navController, AppBarConfiguration configuration) {
        Intrinsics.checkNotNullParameter(navController, "navController");
        Intrinsics.checkNotNullParameter(configuration, "configuration");
        Openable openableLayout = configuration.getOpenableLayout();
        NavDestination currentDestination = navController.getCurrentDestination();
        Set topLevelDestinations = configuration.getTopLevelDestinations();
        if (openableLayout != null && currentDestination != null && matchDestinations$navigation_ui_release(currentDestination, topLevelDestinations)) {
            openableLayout.open();
            return true;
        }
        if (navController.navigateUp()) {
            return true;
        }
        AppBarConfiguration.OnNavigateUpListener fallbackOnNavigateUpListener = configuration.getFallbackOnNavigateUpListener();
        if (fallbackOnNavigateUpListener != null) {
            return fallbackOnNavigateUpListener.onNavigateUp();
        }
        return false;
    }

    @JvmStatic
    public static final void setupActionBarWithNavController(AppCompatActivity activity, NavController navController, Openable openableLayout) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(navController, "navController");
        setupActionBarWithNavController(activity, navController, new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(openableLayout).build());
    }

    public static /* synthetic */ void setupActionBarWithNavController$default(AppCompatActivity appCompatActivity, NavController navController, AppBarConfiguration appBarConfiguration, int i, Object obj) {
        if ((i & 4) != 0) {
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        }
        setupActionBarWithNavController(appCompatActivity, navController, appBarConfiguration);
    }

    @JvmStatic
    public static final void setupActionBarWithNavController(AppCompatActivity activity, NavController navController, AppBarConfiguration configuration) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        Intrinsics.checkNotNullParameter(navController, "navController");
        Intrinsics.checkNotNullParameter(configuration, "configuration");
        navController.addOnDestinationChangedListener(new ActionBarOnDestinationChangedListener(activity, configuration));
    }

    @JvmStatic
    public static final void setupWithNavController(Toolbar toolbar, NavController navController, Openable openableLayout) {
        Intrinsics.checkNotNullParameter(toolbar, "toolbar");
        Intrinsics.checkNotNullParameter(navController, "navController");
        setupWithNavController(toolbar, navController, new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(openableLayout).build());
    }

    public static /* synthetic */ void setupWithNavController$default(Toolbar toolbar, NavController navController, AppBarConfiguration appBarConfiguration, int i, Object obj) {
        if ((i & 4) != 0) {
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        }
        setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @JvmStatic
    public static final void setupWithNavController(Toolbar toolbar, final NavController navController, final AppBarConfiguration configuration) {
        Intrinsics.checkNotNullParameter(toolbar, "toolbar");
        Intrinsics.checkNotNullParameter(navController, "navController");
        Intrinsics.checkNotNullParameter(configuration, "configuration");
        navController.addOnDestinationChangedListener(new ToolbarOnDestinationChangedListener(toolbar, configuration));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: androidx.navigation.ui.NavigationUI$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationUI.m82setupWithNavController$lambda1(NavController.this, configuration, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setupWithNavController$lambda-1, reason: not valid java name */
    public static final void m82setupWithNavController$lambda1(NavController navController, AppBarConfiguration configuration, View it) {
        Intrinsics.checkNotNullParameter(navController, "$navController");
        Intrinsics.checkNotNullParameter(configuration, "$configuration");
        navigateUp(navController, configuration);
    }

    @JvmStatic
    public static final void setupWithNavController(CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, NavController navController, Openable openableLayout) {
        Intrinsics.checkNotNullParameter(collapsingToolbarLayout, "collapsingToolbarLayout");
        Intrinsics.checkNotNullParameter(toolbar, "toolbar");
        Intrinsics.checkNotNullParameter(navController, "navController");
        setupWithNavController(collapsingToolbarLayout, toolbar, navController, new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(openableLayout).build());
    }

    public static /* synthetic */ void setupWithNavController$default(CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, NavController navController, AppBarConfiguration appBarConfiguration, int i, Object obj) {
        if ((i & 8) != 0) {
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        }
        setupWithNavController(collapsingToolbarLayout, toolbar, navController, appBarConfiguration);
    }

    @JvmStatic
    public static final void setupWithNavController(CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, final NavController navController, final AppBarConfiguration configuration) {
        Intrinsics.checkNotNullParameter(collapsingToolbarLayout, "collapsingToolbarLayout");
        Intrinsics.checkNotNullParameter(toolbar, "toolbar");
        Intrinsics.checkNotNullParameter(navController, "navController");
        Intrinsics.checkNotNullParameter(configuration, "configuration");
        navController.addOnDestinationChangedListener(new CollapsingToolbarOnDestinationChangedListener(collapsingToolbarLayout, toolbar, configuration));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: androidx.navigation.ui.NavigationUI$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationUI.m83setupWithNavController$lambda2(NavController.this, configuration, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setupWithNavController$lambda-2, reason: not valid java name */
    public static final void m83setupWithNavController$lambda2(NavController navController, AppBarConfiguration configuration, View it) {
        Intrinsics.checkNotNullParameter(navController, "$navController");
        Intrinsics.checkNotNullParameter(configuration, "$configuration");
        navigateUp(navController, configuration);
    }

    @JvmStatic
    public static final void setupWithNavController(final NavigationView navigationView, final NavController navController) {
        Intrinsics.checkNotNullParameter(navigationView, "navigationView");
        Intrinsics.checkNotNullParameter(navController, "navController");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { // from class: androidx.navigation.ui.NavigationUI$$ExternalSyntheticLambda3
            @Override // com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                boolean m84setupWithNavController$lambda3;
                m84setupWithNavController$lambda3 = NavigationUI.m84setupWithNavController$lambda3(NavController.this, navigationView, menuItem);
                return m84setupWithNavController$lambda3;
            }
        });
        final WeakReference weakReference = new WeakReference(navigationView);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() { // from class: androidx.navigation.ui.NavigationUI$setupWithNavController$4
            @Override // androidx.navigation.NavController.OnDestinationChangedListener
            public void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
                Intrinsics.checkNotNullParameter(controller, "controller");
                Intrinsics.checkNotNullParameter(destination, "destination");
                NavigationView view = weakReference.get();
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this);
                    return;
                }
                Menu $this$forEach$iv = view.getMenu();
                Intrinsics.checkNotNullExpressionValue($this$forEach$iv, "view.menu");
                int size = $this$forEach$iv.size();
                for (int index$iv = 0; index$iv < size; index$iv++) {
                    MenuItem item = $this$forEach$iv.getItem(index$iv);
                    Intrinsics.checkExpressionValueIsNotNull(item, "getItem(index)");
                    item.setChecked(NavigationUI.matchDestination$navigation_ui_release(destination, item.getItemId()));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setupWithNavController$lambda-3, reason: not valid java name */
    public static final boolean m84setupWithNavController$lambda3(NavController navController, NavigationView navigationView, MenuItem item) {
        Intrinsics.checkNotNullParameter(navController, "$navController");
        Intrinsics.checkNotNullParameter(navigationView, "$navigationView");
        Intrinsics.checkNotNullParameter(item, "item");
        boolean handled = onNavDestinationSelected(item, navController);
        if (handled) {
            ViewParent parent = navigationView.getParent();
            if (parent instanceof Openable) {
                ((Openable) parent).close();
            } else {
                BottomSheetBehavior bottomSheetBehavior = findBottomSheetBehavior(navigationView);
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(5);
                }
            }
        }
        return handled;
    }

    @NavigationUiSaveStateControl
    @JvmStatic
    public static final void setupWithNavController(final NavigationView navigationView, final NavController navController, final boolean saveState) {
        Intrinsics.checkNotNullParameter(navigationView, "navigationView");
        Intrinsics.checkNotNullParameter(navController, "navController");
        if (!(!saveState)) {
            throw new IllegalStateException("Leave the saveState parameter out entirely to use the non-experimental version of this API, which saves the state by default".toString());
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { // from class: androidx.navigation.ui.NavigationUI$$ExternalSyntheticLambda0
            @Override // com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                boolean m85setupWithNavController$lambda5;
                m85setupWithNavController$lambda5 = NavigationUI.m85setupWithNavController$lambda5(NavController.this, saveState, navigationView, menuItem);
                return m85setupWithNavController$lambda5;
            }
        });
        final WeakReference weakReference = new WeakReference(navigationView);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() { // from class: androidx.navigation.ui.NavigationUI$setupWithNavController$7
            @Override // androidx.navigation.NavController.OnDestinationChangedListener
            public void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
                Intrinsics.checkNotNullParameter(controller, "controller");
                Intrinsics.checkNotNullParameter(destination, "destination");
                NavigationView view = weakReference.get();
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this);
                    return;
                }
                Menu $this$forEach$iv = view.getMenu();
                Intrinsics.checkNotNullExpressionValue($this$forEach$iv, "view.menu");
                int size = $this$forEach$iv.size();
                for (int index$iv = 0; index$iv < size; index$iv++) {
                    MenuItem item = $this$forEach$iv.getItem(index$iv);
                    Intrinsics.checkExpressionValueIsNotNull(item, "getItem(index)");
                    item.setChecked(NavigationUI.matchDestination$navigation_ui_release(destination, item.getItemId()));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setupWithNavController$lambda-5, reason: not valid java name */
    public static final boolean m85setupWithNavController$lambda5(NavController navController, boolean $saveState, NavigationView navigationView, MenuItem item) {
        Intrinsics.checkNotNullParameter(navController, "$navController");
        Intrinsics.checkNotNullParameter(navigationView, "$navigationView");
        Intrinsics.checkNotNullParameter(item, "item");
        boolean handled = onNavDestinationSelected(item, navController, $saveState);
        if (handled) {
            ViewParent parent = navigationView.getParent();
            if (parent instanceof Openable) {
                ((Openable) parent).close();
            } else {
                BottomSheetBehavior bottomSheetBehavior = findBottomSheetBehavior(navigationView);
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(5);
                }
            }
        }
        return handled;
    }

    @JvmStatic
    public static final BottomSheetBehavior<?> findBottomSheetBehavior(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            Object parent = view.getParent();
            if (parent instanceof View) {
                return findBottomSheetBehavior((View) parent);
            }
            return null;
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof BottomSheetBehavior)) {
            return null;
        }
        return (BottomSheetBehavior) behavior;
    }

    @JvmStatic
    public static final void setupWithNavController(NavigationBarView navigationBarView, final NavController navController) {
        Intrinsics.checkNotNullParameter(navigationBarView, "navigationBarView");
        Intrinsics.checkNotNullParameter(navController, "navController");
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() { // from class: androidx.navigation.ui.NavigationUI$$ExternalSyntheticLambda2
            @Override // com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                boolean m86setupWithNavController$lambda6;
                m86setupWithNavController$lambda6 = NavigationUI.m86setupWithNavController$lambda6(NavController.this, menuItem);
                return m86setupWithNavController$lambda6;
            }
        });
        final WeakReference weakReference = new WeakReference(navigationBarView);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() { // from class: androidx.navigation.ui.NavigationUI$setupWithNavController$9
            @Override // androidx.navigation.NavController.OnDestinationChangedListener
            public void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
                Intrinsics.checkNotNullParameter(controller, "controller");
                Intrinsics.checkNotNullParameter(destination, "destination");
                NavigationBarView view = weakReference.get();
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this);
                    return;
                }
                Menu $this$forEach$iv = view.getMenu();
                Intrinsics.checkNotNullExpressionValue($this$forEach$iv, "view.menu");
                int size = $this$forEach$iv.size();
                for (int index$iv = 0; index$iv < size; index$iv++) {
                    MenuItem item = $this$forEach$iv.getItem(index$iv);
                    Intrinsics.checkExpressionValueIsNotNull(item, "getItem(index)");
                    if (NavigationUI.matchDestination$navigation_ui_release(destination, item.getItemId())) {
                        item.setChecked(true);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setupWithNavController$lambda-6, reason: not valid java name */
    public static final boolean m86setupWithNavController$lambda6(NavController navController, MenuItem item) {
        Intrinsics.checkNotNullParameter(navController, "$navController");
        Intrinsics.checkNotNullParameter(item, "item");
        return onNavDestinationSelected(item, navController);
    }

    @NavigationUiSaveStateControl
    @JvmStatic
    public static final void setupWithNavController(NavigationBarView navigationBarView, final NavController navController, final boolean saveState) {
        Intrinsics.checkNotNullParameter(navigationBarView, "navigationBarView");
        Intrinsics.checkNotNullParameter(navController, "navController");
        if (!(!saveState)) {
            throw new IllegalStateException("Leave the saveState parameter out entirely to use the non-experimental version of this API, which saves the state by default".toString());
        }
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() { // from class: androidx.navigation.ui.NavigationUI$$ExternalSyntheticLambda5
            @Override // com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                boolean m87setupWithNavController$lambda8;
                m87setupWithNavController$lambda8 = NavigationUI.m87setupWithNavController$lambda8(NavController.this, saveState, menuItem);
                return m87setupWithNavController$lambda8;
            }
        });
        final WeakReference weakReference = new WeakReference(navigationBarView);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() { // from class: androidx.navigation.ui.NavigationUI$setupWithNavController$12
            @Override // androidx.navigation.NavController.OnDestinationChangedListener
            public void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
                Intrinsics.checkNotNullParameter(controller, "controller");
                Intrinsics.checkNotNullParameter(destination, "destination");
                NavigationBarView view = weakReference.get();
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this);
                    return;
                }
                Menu $this$forEach$iv = view.getMenu();
                Intrinsics.checkNotNullExpressionValue($this$forEach$iv, "view.menu");
                int size = $this$forEach$iv.size();
                for (int index$iv = 0; index$iv < size; index$iv++) {
                    MenuItem item = $this$forEach$iv.getItem(index$iv);
                    Intrinsics.checkExpressionValueIsNotNull(item, "getItem(index)");
                    if (NavigationUI.matchDestination$navigation_ui_release(destination, item.getItemId())) {
                        item.setChecked(true);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setupWithNavController$lambda-8, reason: not valid java name */
    public static final boolean m87setupWithNavController$lambda8(NavController navController, boolean $saveState, MenuItem item) {
        Intrinsics.checkNotNullParameter(navController, "$navController");
        Intrinsics.checkNotNullParameter(item, "item");
        return onNavDestinationSelected(item, navController, $saveState);
    }

    @JvmStatic
    public static final boolean matchDestination$navigation_ui_release(NavDestination $this$matchDestination, int destId) {
        boolean z;
        Intrinsics.checkNotNullParameter($this$matchDestination, "<this>");
        Sequence $this$any$iv = NavDestination.INSTANCE.getHierarchy($this$matchDestination);
        Iterator<NavDestination> it = $this$any$iv.iterator();
        do {
            z = false;
            if (!it.hasNext()) {
                return false;
            }
            Object element$iv = it.next();
            NavDestination it2 = (NavDestination) element$iv;
            if (it2.getId() == destId) {
                z = true;
            }
        } while (!z);
        return true;
    }

    @JvmStatic
    public static final boolean matchDestinations$navigation_ui_release(NavDestination $this$matchDestinations, Set<Integer> destinationIds) {
        Intrinsics.checkNotNullParameter($this$matchDestinations, "<this>");
        Intrinsics.checkNotNullParameter(destinationIds, "destinationIds");
        Sequence $this$any$iv = NavDestination.INSTANCE.getHierarchy($this$matchDestinations);
        for (Object element$iv : $this$any$iv) {
            NavDestination it = (NavDestination) element$iv;
            if (destinationIds.contains(Integer.valueOf(it.getId()))) {
                return true;
            }
        }
        return false;
    }
}
