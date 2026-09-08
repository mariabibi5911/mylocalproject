package androidx.navigation.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AbstractListDetailFragment.kt */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u00002\u00020\u0001:\u0001&B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0004H\u0016J$\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H&J$\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0007J\"\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0017J\u001a\u0010 \u001a\u00020\u001b2\u0006\u0010!\u001a\u00020\u00122\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u0010\u0010\"\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\u0018H\u0017J\u001a\u0010$\u001a\u00020\u001b2\u0006\u0010!\u001a\u00020\u00122\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0007J\u0012\u0010%\u001a\u00020\u001b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0017R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f¨\u0006'"}, d2 = {"Landroidx/navigation/fragment/AbstractListDetailFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_detailPaneNavHostFragment", "Landroidx/navigation/fragment/NavHostFragment;", "detailPaneNavHostFragment", "getDetailPaneNavHostFragment", "()Landroidx/navigation/fragment/NavHostFragment;", "graphId", "", "onBackPressedCallback", "Landroidx/activity/OnBackPressedCallback;", "slidingPaneLayout", "Landroidx/slidingpanelayout/widget/SlidingPaneLayout;", "getSlidingPaneLayout", "()Landroidx/slidingpanelayout/widget/SlidingPaneLayout;", "onCreateDetailPaneNavHostFragment", "onCreateListPaneView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "onInflate", "", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "onListPaneViewCreated", "view", "onSaveInstanceState", "outState", "onViewCreated", "onViewStateRestored", "InnerOnBackPressedCallback", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes.dex */
public abstract class AbstractListDetailFragment extends Fragment {
    private NavHostFragment _detailPaneNavHostFragment;
    private int graphId;
    private OnBackPressedCallback onBackPressedCallback;

    public abstract View onCreateListPaneView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public final SlidingPaneLayout getSlidingPaneLayout() {
        return (SlidingPaneLayout) requireView();
    }

    public final NavHostFragment getDetailPaneNavHostFragment() {
        NavHostFragment navHostFragment = this._detailPaneNavHostFragment;
        if (navHostFragment == null) {
            throw new IllegalStateException(("Fragment " + this + " was called before onCreateView().").toString());
        }
        if (navHostFragment != null) {
            return navHostFragment;
        }
        throw new NullPointerException("null cannot be cast to non-null type androidx.navigation.fragment.NavHostFragment");
    }

    /* compiled from: AbstractListDetailFragment.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Landroidx/navigation/fragment/AbstractListDetailFragment$InnerOnBackPressedCallback;", "Landroidx/activity/OnBackPressedCallback;", "Landroidx/slidingpanelayout/widget/SlidingPaneLayout$PanelSlideListener;", "slidingPaneLayout", "Landroidx/slidingpanelayout/widget/SlidingPaneLayout;", "(Landroidx/slidingpanelayout/widget/SlidingPaneLayout;)V", "handleOnBackPressed", "", "onPanelClosed", "panel", "Landroid/view/View;", "onPanelOpened", "onPanelSlide", "slideOffset", "", "navigation-fragment_release"}, k = 1, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes.dex */
    private static final class InnerOnBackPressedCallback extends OnBackPressedCallback implements SlidingPaneLayout.PanelSlideListener {
        private final SlidingPaneLayout slidingPaneLayout;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InnerOnBackPressedCallback(SlidingPaneLayout slidingPaneLayout) {
            super(true);
            Intrinsics.checkNotNullParameter(slidingPaneLayout, "slidingPaneLayout");
            this.slidingPaneLayout = slidingPaneLayout;
            slidingPaneLayout.addPanelSlideListener(this);
        }

        @Override // androidx.activity.OnBackPressedCallback
        public void handleOnBackPressed() {
            this.slidingPaneLayout.closePane();
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelSlide(View panel, float slideOffset) {
            Intrinsics.checkNotNullParameter(panel, "panel");
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelOpened(View panel) {
            Intrinsics.checkNotNullParameter(panel, "panel");
            setEnabled(true);
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelClosed(View panel) {
            Intrinsics.checkNotNullParameter(panel, "panel");
            setEnabled(false);
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
    }

    @Override // androidx.fragment.app.Fragment
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NavHostFragment newNavHostFragment;
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        if (savedInstanceState != null) {
            this.graphId = savedInstanceState.getInt(NavHostFragment.KEY_GRAPH_ID);
        }
        final SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(inflater.getContext());
        slidingPaneLayout.setId(R.id.sliding_pane_layout);
        View listPaneView = onCreateListPaneView(inflater, slidingPaneLayout, savedInstanceState);
        if (!Intrinsics.areEqual(listPaneView, slidingPaneLayout) && !Intrinsics.areEqual(listPaneView.getParent(), slidingPaneLayout)) {
            slidingPaneLayout.addView(listPaneView);
        }
        Context context = inflater.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "inflater.context");
        FragmentContainerView $this$onCreateView_u24lambda_u2d3 = new FragmentContainerView(context);
        $this$onCreateView_u24lambda_u2d3.setId(R.id.sliding_pane_detail_container);
        int detailWidth = inflater.getContext().getResources().getDimensionPixelSize(R.dimen.sliding_pane_detail_pane_width);
        SlidingPaneLayout.LayoutParams $this$onCreateView_u24lambda_u2d4 = new SlidingPaneLayout.LayoutParams(detailWidth, -1);
        $this$onCreateView_u24lambda_u2d4.weight = 1.0f;
        slidingPaneLayout.addView($this$onCreateView_u24lambda_u2d3, $this$onCreateView_u24lambda_u2d4);
        Fragment existingNavHostFragment = getChildFragmentManager().findFragmentById(R.id.sliding_pane_detail_container);
        if (existingNavHostFragment != null) {
            newNavHostFragment = (NavHostFragment) existingNavHostFragment;
        } else {
            newNavHostFragment = onCreateDetailPaneNavHostFragment();
            FragmentManager childFragmentManager = getChildFragmentManager();
            Intrinsics.checkNotNullExpressionValue(childFragmentManager, "childFragmentManager");
            FragmentTransaction $this$onCreateView_u24lambda_u2d6_u24lambda_u2d5 = childFragmentManager.beginTransaction();
            Intrinsics.checkNotNullExpressionValue($this$onCreateView_u24lambda_u2d6_u24lambda_u2d5, "beginTransaction()");
            $this$onCreateView_u24lambda_u2d6_u24lambda_u2d5.setReorderingAllowed(true);
            $this$onCreateView_u24lambda_u2d6_u24lambda_u2d5.add(R.id.sliding_pane_detail_container, newNavHostFragment);
            $this$onCreateView_u24lambda_u2d6_u24lambda_u2d5.commit();
        }
        this._detailPaneNavHostFragment = newNavHostFragment;
        this.onBackPressedCallback = new InnerOnBackPressedCallback(slidingPaneLayout);
        SlidingPaneLayout $this$doOnLayout$iv = slidingPaneLayout;
        if (ViewCompat.isLaidOut($this$doOnLayout$iv) && !$this$doOnLayout$iv.isLayoutRequested()) {
            OnBackPressedCallback onBackPressedCallback = this.onBackPressedCallback;
            Intrinsics.checkNotNull(onBackPressedCallback);
            onBackPressedCallback.setEnabled(slidingPaneLayout.isSlideable() && slidingPaneLayout.isOpen());
        } else {
            $this$doOnLayout$iv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: androidx.navigation.fragment.AbstractListDetailFragment$onCreateView$$inlined$doOnLayout$1
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Intrinsics.checkParameterIsNotNull(view, "view");
                    view.removeOnLayoutChangeListener(this);
                    OnBackPressedCallback onBackPressedCallback2 = AbstractListDetailFragment.this.onBackPressedCallback;
                    Intrinsics.checkNotNull(onBackPressedCallback2);
                    onBackPressedCallback2.setEnabled(slidingPaneLayout.isSlideable() && slidingPaneLayout.isOpen());
                }
            });
        }
        OnBackPressedDispatcher onBackPressedDispatcher = requireActivity().getOnBackPressedDispatcher();
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        OnBackPressedCallback onBackPressedCallback2 = this.onBackPressedCallback;
        Intrinsics.checkNotNull(onBackPressedCallback2);
        onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback2);
        return slidingPaneLayout;
    }

    public NavHostFragment onCreateDetailPaneNavHostFragment() {
        if (this.graphId != 0) {
            return NavHostFragment.Companion.create$default(NavHostFragment.INSTANCE, this.graphId, null, 2, null);
        }
        return new NavHostFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        View listPaneView = getSlidingPaneLayout().getChildAt(0);
        Intrinsics.checkNotNullExpressionValue(listPaneView, "listPaneView");
        onListPaneViewCreated(listPaneView, savedInstanceState);
    }

    public void onListPaneViewCreated(View view, Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        OnBackPressedCallback onBackPressedCallback = this.onBackPressedCallback;
        Intrinsics.checkNotNull(onBackPressedCallback);
        onBackPressedCallback.setEnabled(getSlidingPaneLayout().isSlideable() && getSlidingPaneLayout().isOpen());
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        Intrinsics.checkNotNullParameter(outState, "outState");
        super.onSaveInstanceState(outState);
        int i = this.graphId;
        if (i != 0) {
            outState.putInt(NavHostFragment.KEY_GRAPH_ID, i);
        }
    }
}
