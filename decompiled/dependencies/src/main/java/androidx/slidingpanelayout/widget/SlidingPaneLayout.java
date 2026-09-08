package androidx.slidingpanelayout.widget;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.Openable;
import androidx.customview.widget.ViewDragHelper;
import androidx.slidingpanelayout.widget.FoldingFeatureObserver;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.window.layout.FoldingFeature;
import androidx.window.layout.WindowInfoTracker;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class SlidingPaneLayout extends ViewGroup implements Openable {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.slidingpanelayout.widget.SlidingPaneLayout";
    public static final int LOCK_MODE_LOCKED = 3;
    public static final int LOCK_MODE_LOCKED_CLOSED = 2;
    public static final int LOCK_MODE_LOCKED_OPEN = 1;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private static boolean sEdgeSizeUsingSystemGestureInsets;
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    private boolean mDisplayListReflectionLoaded;
    final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    FoldingFeature mFoldingFeature;
    private FoldingFeatureObserver mFoldingFeatureObserver;
    private Method mGetDisplayList;
    private float mInitialMotionX;
    private float mInitialMotionY;
    boolean mIsUnableToDrag;
    private int mLockMode;
    private FoldingFeatureObserver.OnFoldingFeatureChangeListener mOnFoldingFeatureChangeListener;
    private PanelSlideListener mPanelSlideListener;
    private final List<PanelSlideListener> mPanelSlideListeners;
    private int mParallaxBy;
    private float mParallaxOffset;
    final ArrayList<DisableLayerRunnable> mPostedRunnables;
    boolean mPreservedOpenState;
    private Field mRecreateDisplayList;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    float mSlideOffset;
    int mSlideRange;
    View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;

    /* loaded from: classes.dex */
    public interface PanelSlideListener {
        void onPanelClosed(View view);

        void onPanelOpened(View view);

        void onPanelSlide(View view, float f);
    }

    static {
        sEdgeSizeUsingSystemGestureInsets = Build.VERSION.SDK_INT >= 29;
    }

    public final void setLockMode(int lockMode) {
        this.mLockMode = lockMode;
    }

    public final int getLockMode() {
        return this.mLockMode;
    }

    /* loaded from: classes.dex */
    public static class SimplePanelSlideListener implements PanelSlideListener {
        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelSlide(View panel, float slideOffset) {
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelOpened(View panel) {
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelClosed(View panel) {
        }
    }

    public SlidingPaneLayout(Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSliderFadeColor = 0;
        this.mSlideOffset = 1.0f;
        this.mPanelSlideListeners = new CopyOnWriteArrayList();
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        this.mPostedRunnables = new ArrayList<>();
        this.mOnFoldingFeatureChangeListener = new FoldingFeatureObserver.OnFoldingFeatureChangeListener() { // from class: androidx.slidingpanelayout.widget.SlidingPaneLayout.1
            @Override // androidx.slidingpanelayout.widget.FoldingFeatureObserver.OnFoldingFeatureChangeListener
            public void onFoldingFeatureChange(FoldingFeature foldingFeature) {
                SlidingPaneLayout.this.mFoldingFeature = foldingFeature;
                Transition changeBounds = new ChangeBounds();
                changeBounds.setDuration(300L);
                changeBounds.setInterpolator(PathInterpolatorCompat.create(0.2f, 0.0f, 0.0f, 1.0f));
                TransitionManager.beginDelayedTransition(SlidingPaneLayout.this, changeBounds);
                SlidingPaneLayout.this.requestLayout();
            }
        };
        float density = context.getResources().getDisplayMetrics().density;
        setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility(this, 1);
        ViewDragHelper create = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        this.mDragHelper = create;
        create.setMinVelocity(400.0f * density);
        WindowInfoTracker repo = WindowInfoTracker.CC.getOrCreate(context);
        Executor mainExecutor = ContextCompat.getMainExecutor(context);
        FoldingFeatureObserver foldingFeatureObserver = new FoldingFeatureObserver(repo, mainExecutor);
        setFoldingFeatureObserver(foldingFeatureObserver);
    }

    private void setFoldingFeatureObserver(FoldingFeatureObserver foldingFeatureObserver) {
        this.mFoldingFeatureObserver = foldingFeatureObserver;
        foldingFeatureObserver.setOnFoldingFeatureChangeListener(this.mOnFoldingFeatureChangeListener);
    }

    public void setParallaxDistance(int parallaxBy) {
        this.mParallaxBy = parallaxBy;
        requestLayout();
    }

    public int getParallaxDistance() {
        return this.mParallaxBy;
    }

    @Deprecated
    public void setSliderFadeColor(int color) {
        this.mSliderFadeColor = color;
    }

    @Deprecated
    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }

    @Deprecated
    public void setCoveredFadeColor(int color) {
        this.mCoveredFadeColor = color;
    }

    @Deprecated
    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    @Deprecated
    public void setPanelSlideListener(PanelSlideListener listener) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            removePanelSlideListener(panelSlideListener);
        }
        if (listener != null) {
            addPanelSlideListener(listener);
        }
        this.mPanelSlideListener = listener;
    }

    public void addPanelSlideListener(PanelSlideListener listener) {
        this.mPanelSlideListeners.add(listener);
    }

    public void removePanelSlideListener(PanelSlideListener listener) {
        this.mPanelSlideListeners.remove(listener);
    }

    void dispatchOnPanelSlide(View panel) {
        for (PanelSlideListener listener : this.mPanelSlideListeners) {
            listener.onPanelSlide(panel, this.mSlideOffset);
        }
    }

    void dispatchOnPanelOpened(View panel) {
        for (PanelSlideListener listener : this.mPanelSlideListeners) {
            listener.onPanelOpened(panel);
        }
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelClosed(View panel) {
        for (PanelSlideListener listener : this.mPanelSlideListeners) {
            listener.onPanelClosed(panel);
        }
        sendAccessibilityEvent(32);
    }

    void updateObscuredViewsVisibility(View panel) {
        int left;
        int bottom;
        int top;
        int right;
        boolean isLayoutRtl;
        int clampedChildRight;
        View view = panel;
        boolean isLayoutRtl2 = isLayoutRtlSupport();
        int startBound = isLayoutRtl2 ? getWidth() - getPaddingRight() : getPaddingLeft();
        int endBound = isLayoutRtl2 ? getPaddingLeft() : getWidth() - getPaddingRight();
        int topBound = getPaddingTop();
        int bottomBound = getHeight() - getPaddingBottom();
        if (view != null && viewIsOpaque(panel)) {
            left = panel.getLeft();
            right = panel.getRight();
            top = panel.getTop();
            bottom = panel.getBottom();
        } else {
            left = 0;
            bottom = 0;
            top = 0;
            right = 0;
        }
        int i = 0;
        int childCount = getChildCount();
        while (i < childCount) {
            View child = getChildAt(i);
            if (child != view) {
                if (child.getVisibility() == 8) {
                    isLayoutRtl = isLayoutRtl2;
                } else {
                    int clampedChildLeft = Math.max(isLayoutRtl2 ? endBound : startBound, child.getLeft());
                    int clampedChildTop = Math.max(topBound, child.getTop());
                    isLayoutRtl = isLayoutRtl2;
                    int clampedChildRight2 = Math.min(isLayoutRtl2 ? startBound : endBound, child.getRight());
                    int clampedChildBottom = Math.min(bottomBound, child.getBottom());
                    if (clampedChildLeft >= left && clampedChildTop >= top && clampedChildRight2 <= right && clampedChildBottom <= bottom) {
                        clampedChildRight = 4;
                    } else {
                        clampedChildRight = 0;
                    }
                    child.setVisibility(clampedChildRight);
                }
                i++;
                view = panel;
                isLayoutRtl2 = isLayoutRtl;
            } else {
                return;
            }
        }
    }

    void setAllChildrenVisible() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 4) {
                child.setVisibility(0);
            }
        }
    }

    private static boolean viewIsOpaque(View v) {
        if (v.isOpaque()) {
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() == 1) {
            View detailView = new TouchBlocker(child);
            super.addView(detailView, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (view.getParent() instanceof TouchBlocker) {
            super.removeView((View) view.getParent());
        } else {
            super.removeView(view);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        Activity activity;
        super.onAttachedToWindow();
        this.mFirstLayout = true;
        if (this.mFoldingFeatureObserver != null && (activity = getActivityOrNull(getContext())) != null) {
            this.mFoldingFeatureObserver.registerLayoutStateChangeCallback(activity);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        FoldingFeatureObserver foldingFeatureObserver = this.mFoldingFeatureObserver;
        if (foldingFeatureObserver != null) {
            foldingFeatureObserver.unregisterLayoutStateChangeCallback();
        }
        int count = this.mPostedRunnables.size();
        for (int i = 0; i < count; i++) {
            DisableLayerRunnable dlr = this.mPostedRunnables.get(i);
            dlr.run();
        }
        this.mPostedRunnables.clear();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidthSpec;
        int newWidth;
        ArrayList<Rect> splitViews;
        int heightMode;
        int i;
        int childWidthSpec2;
        int widthMode;
        int i2 = heightMeasureSpec;
        int widthMode2 = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode2 = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int layoutHeight = 0;
        int maxLayoutHeight = 0;
        switch (heightMode2) {
            case Integer.MIN_VALUE:
                maxLayoutHeight = (heightSize - getPaddingTop()) - getPaddingBottom();
                break;
            case 1073741824:
                int paddingTop = (heightSize - getPaddingTop()) - getPaddingBottom();
                maxLayoutHeight = paddingTop;
                layoutHeight = paddingTop;
                break;
        }
        float weightSum = 0.0f;
        boolean canSlide = false;
        int widthAvailable = Math.max((widthSize - getPaddingLeft()) - getPaddingRight(), 0);
        int widthRemaining = widthAvailable;
        int childCount = getChildCount();
        if (childCount > 2) {
            Log.e(TAG, "onMeasure: More than two child views are not supported.");
        }
        this.mSlideableView = null;
        int i3 = 0;
        while (i3 < childCount) {
            View child = getChildAt(i3);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int heightSize2 = heightSize;
            int widthSize2 = widthSize;
            if (child.getVisibility() == 8) {
                lp.dimWhenOffset = false;
                widthMode = widthMode2;
            } else {
                if (lp.weight > 0.0f) {
                    weightSum += lp.weight;
                    if (lp.width == 0) {
                        widthMode = widthMode2;
                    }
                }
                int horizontalMargin = lp.leftMargin + lp.rightMargin;
                int childWidthSize = Math.max(widthAvailable - horizontalMargin, 0);
                float weightSum2 = weightSum;
                if (lp.width == -2) {
                    childWidthSpec2 = View.MeasureSpec.makeMeasureSpec(childWidthSize, widthMode2 == 0 ? widthMode2 : Integer.MIN_VALUE);
                } else {
                    int childWidthSpec3 = lp.width;
                    if (childWidthSpec3 == -1) {
                        childWidthSpec2 = View.MeasureSpec.makeMeasureSpec(childWidthSize, widthMode2);
                    } else {
                        int childWidthSpec4 = lp.width;
                        childWidthSpec2 = View.MeasureSpec.makeMeasureSpec(childWidthSpec4, 1073741824);
                    }
                }
                int paddingTop2 = getPaddingTop() + getPaddingBottom();
                widthMode = widthMode2;
                int widthMode3 = lp.height;
                int childHeightSpec = getChildMeasureSpec(i2, paddingTop2, widthMode3);
                child.measure(childWidthSpec2, childHeightSpec);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (childHeight > layoutHeight) {
                    if (heightMode2 == Integer.MIN_VALUE) {
                        layoutHeight = Math.min(childHeight, maxLayoutHeight);
                    } else if (heightMode2 == 0) {
                        layoutHeight = childHeight;
                    }
                }
                widthRemaining -= childWidth;
                if (i3 == 0) {
                    weightSum = weightSum2;
                } else {
                    boolean z = widthRemaining < 0;
                    lp.slideable = z;
                    boolean canSlide2 = z | canSlide;
                    boolean canSlide3 = lp.slideable;
                    if (canSlide3) {
                        this.mSlideableView = child;
                    }
                    canSlide = canSlide2;
                    weightSum = weightSum2;
                }
            }
            i3++;
            heightSize = heightSize2;
            widthSize = widthSize2;
            widthMode2 = widthMode;
        }
        int widthSize3 = widthSize;
        if (canSlide || weightSum > 0.0f) {
            int i4 = 0;
            while (i4 < childCount) {
                View child2 = getChildAt(i4);
                if (child2.getVisibility() != 8) {
                    LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                    boolean skippedFirstPass = lp2.width == 0 && lp2.weight > 0.0f;
                    int measuredWidth = skippedFirstPass ? 0 : child2.getMeasuredWidth();
                    int newWidth2 = measuredWidth;
                    if (canSlide) {
                        int i5 = lp2.leftMargin;
                        int newWidth3 = lp2.rightMargin;
                        int horizontalMargin2 = i5 + newWidth3;
                        newWidth = widthAvailable - horizontalMargin2;
                        int childWidthSpec5 = View.MeasureSpec.makeMeasureSpec(newWidth, 1073741824);
                        childWidthSpec = childWidthSpec5;
                    } else if (lp2.weight <= 0.0f) {
                        childWidthSpec = 0;
                        newWidth = newWidth2;
                    } else {
                        int widthToDistribute = Math.max(0, widthRemaining);
                        int addedWidth = (int) ((lp2.weight * widthToDistribute) / weightSum);
                        int newWidth4 = measuredWidth + addedWidth;
                        int childWidthSpec6 = View.MeasureSpec.makeMeasureSpec(newWidth4, 1073741824);
                        newWidth = newWidth4;
                        childWidthSpec = childWidthSpec6;
                    }
                    int childHeightSpec2 = measureChildHeight(child2, i2, getPaddingTop() + getPaddingBottom());
                    if (measuredWidth != newWidth) {
                        child2.measure(childWidthSpec, childHeightSpec2);
                        int childHeight2 = child2.getMeasuredHeight();
                        if (childHeight2 > layoutHeight) {
                            if (heightMode2 == Integer.MIN_VALUE) {
                                layoutHeight = Math.min(childHeight2, maxLayoutHeight);
                            } else if (heightMode2 == 0) {
                                layoutHeight = childHeight2;
                            }
                        }
                    }
                }
                i4++;
                i2 = heightMeasureSpec;
            }
        }
        ArrayList<Rect> splitViews2 = splitViewPositions();
        if (splitViews2 != null && !canSlide) {
            int i6 = 0;
            while (i6 < childCount) {
                View child3 = getChildAt(i6);
                if (child3.getVisibility() == 8) {
                    splitViews = splitViews2;
                    heightMode = heightMode2;
                } else {
                    Rect splitView = splitViews2.get(i6);
                    LayoutParams lp3 = (LayoutParams) child3.getLayoutParams();
                    int horizontalMargin3 = lp3.leftMargin + lp3.rightMargin;
                    splitViews = splitViews2;
                    int childHeightSpec3 = View.MeasureSpec.makeMeasureSpec(child3.getMeasuredHeight(), 1073741824);
                    heightMode = heightMode2;
                    int childWidthSpec7 = View.MeasureSpec.makeMeasureSpec(splitView.width(), Integer.MIN_VALUE);
                    child3.measure(childWidthSpec7, childHeightSpec3);
                    if ((child3.getMeasuredWidthAndState() & 16777216) == 1) {
                        i = 1073741824;
                    } else if (getMinimumWidth(child3) == 0 || splitView.width() >= getMinimumWidth(child3)) {
                        int childWidthSpec8 = View.MeasureSpec.makeMeasureSpec(splitView.width(), 1073741824);
                        child3.measure(childWidthSpec8, childHeightSpec3);
                    } else {
                        i = 1073741824;
                    }
                    int childWidthSpec9 = View.MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin3, i);
                    child3.measure(childWidthSpec9, childHeightSpec3);
                    if (i6 != 0) {
                        lp3.slideable = true;
                        canSlide = true;
                        this.mSlideableView = child3;
                        i6++;
                        splitViews2 = splitViews;
                        heightMode2 = heightMode;
                    }
                }
                i6++;
                splitViews2 = splitViews;
                heightMode2 = heightMode;
            }
        }
        int measuredHeight = getPaddingTop() + layoutHeight + getPaddingBottom();
        setMeasuredDimension(widthSize3, measuredHeight);
        this.mCanSlide = canSlide;
        if (this.mDragHelper.getViewDragState() != 0 && !canSlide) {
            this.mDragHelper.abort();
        }
    }

    private static int getMinimumWidth(View child) {
        if (child instanceof TouchBlocker) {
            return ViewCompat.getMinimumWidth(((TouchBlocker) child).getChildAt(0));
        }
        return ViewCompat.getMinimumWidth(child);
    }

    private static int measureChildHeight(View child, int spec, int padding) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        boolean skippedFirstPass = lp.width == 0 && lp.weight > 0.0f;
        if (skippedFirstPass) {
            int childHeightSpec = getChildMeasureSpec(spec, padding, lp.height);
            return childHeightSpec;
        }
        int childHeightSpec2 = View.MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), 1073741824);
        return childHeightSpec2;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingStart;
        int childCount;
        int i;
        int childLeft;
        int childRight;
        boolean isLayoutRtl;
        int width;
        int paddingStart2;
        boolean isLayoutRtl2 = isLayoutRtlSupport();
        int width2 = r - l;
        int paddingStart3 = isLayoutRtl2 ? getPaddingRight() : getPaddingLeft();
        int paddingEnd = isLayoutRtl2 ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount2 = getChildCount();
        int xStart = paddingStart3;
        int nextXStart = xStart;
        if (this.mFirstLayout) {
            this.mSlideOffset = (this.mCanSlide && this.mPreservedOpenState) ? 0.0f : 1.0f;
        }
        int i2 = 0;
        while (i2 < childCount2) {
            View child = getChildAt(i2);
            if (child.getVisibility() == 8) {
                isLayoutRtl = isLayoutRtl2;
                width = width2;
                paddingStart = paddingStart3;
                childCount = childCount2;
            } else {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int offset = 0;
                if (!lp.slideable) {
                    paddingStart = paddingStart3;
                    childCount = childCount2;
                    if (this.mCanSlide && (i = this.mParallaxBy) != 0) {
                        offset = (int) ((1.0f - this.mSlideOffset) * i);
                        xStart = nextXStart;
                    } else {
                        xStart = nextXStart;
                    }
                } else {
                    int margin = lp.leftMargin + lp.rightMargin;
                    int range = (Math.min(nextXStart, width2 - paddingEnd) - xStart) - margin;
                    this.mSlideRange = range;
                    if (isLayoutRtl2) {
                        paddingStart = paddingStart3;
                        paddingStart2 = lp.rightMargin;
                    } else {
                        paddingStart = paddingStart3;
                        paddingStart2 = lp.leftMargin;
                    }
                    childCount = childCount2;
                    int childCount3 = xStart + paddingStart2 + range + (childWidth / 2);
                    int margin2 = width2 - paddingEnd;
                    lp.dimWhenOffset = childCount3 > margin2;
                    int pos = (int) (range * this.mSlideOffset);
                    xStart += pos + paddingStart2;
                    int lpMargin = this.mSlideRange;
                    this.mSlideOffset = pos / lpMargin;
                }
                if (isLayoutRtl2) {
                    childRight = (width2 - xStart) + offset;
                    childLeft = childRight - childWidth;
                } else {
                    childLeft = xStart - offset;
                    childRight = childLeft + childWidth;
                }
                int childBottom = paddingTop + child.getMeasuredHeight();
                child.layout(childLeft, paddingTop, childRight, childBottom);
                int nextXOffset = 0;
                isLayoutRtl = isLayoutRtl2;
                FoldingFeature foldingFeature = this.mFoldingFeature;
                if (foldingFeature == null) {
                    width = width2;
                } else {
                    width = width2;
                    if (foldingFeature.getOrientation() == FoldingFeature.Orientation.VERTICAL && this.mFoldingFeature.isSeparating()) {
                        nextXOffset = this.mFoldingFeature.getBounds().width();
                    }
                }
                nextXStart += child.getWidth() + Math.abs(nextXOffset);
            }
            i2++;
            paddingStart3 = paddingStart;
            isLayoutRtl2 = isLayoutRtl;
            width2 = width;
            childCount2 = childCount;
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide && this.mParallaxBy != 0) {
                parallaxOtherViews(this.mSlideOffset);
            }
            updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            this.mFirstLayout = true;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && !this.mCanSlide) {
            this.mPreservedOpenState = child == this.mSlideableView;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View secondChild;
        int action = ev.getActionMasked();
        if (!this.mCanSlide && action == 0 && getChildCount() > 1 && (secondChild = getChildAt(1)) != null) {
            this.mPreservedOpenState = this.mDragHelper.isViewUnder(secondChild, (int) ev.getX(), (int) ev.getY());
        }
        if (!this.mCanSlide || (this.mIsUnableToDrag && action != 0)) {
            this.mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        if (action == 3 || action == 1) {
            this.mDragHelper.cancel();
            return false;
        }
        boolean interceptTap = false;
        switch (action) {
            case 0:
                this.mIsUnableToDrag = false;
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                if (this.mDragHelper.isViewUnder(this.mSlideableView, (int) x, (int) y) && isDimmed(this.mSlideableView)) {
                    interceptTap = true;
                    break;
                }
                break;
            case 2:
                float x2 = ev.getX();
                float y2 = ev.getY();
                float adx = Math.abs(x2 - this.mInitialMotionX);
                float ady = Math.abs(y2 - this.mInitialMotionY);
                int slop = this.mDragHelper.getTouchSlop();
                if (adx > slop && ady > adx) {
                    this.mDragHelper.cancel();
                    this.mIsUnableToDrag = true;
                    return false;
                }
                break;
        }
        boolean interceptForDrag = this.mDragHelper.shouldInterceptTouchEvent(ev);
        return interceptForDrag || interceptTap;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mCanSlide) {
            return super.onTouchEvent(ev);
        }
        this.mDragHelper.processTouchEvent(ev);
        switch (ev.getActionMasked()) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                break;
            case 1:
                if (isDimmed(this.mSlideableView)) {
                    float x2 = ev.getX();
                    float y2 = ev.getY();
                    float dx = x2 - this.mInitialMotionX;
                    float dy = y2 - this.mInitialMotionY;
                    int slop = this.mDragHelper.getTouchSlop();
                    if ((dx * dx) + (dy * dy) < slop * slop && this.mDragHelper.isViewUnder(this.mSlideableView, (int) x2, (int) y2)) {
                        closePane(0);
                        break;
                    }
                }
                break;
        }
        return true;
    }

    private boolean closePane(int initialVelocity) {
        if (!this.mCanSlide) {
            this.mPreservedOpenState = false;
        }
        if (!this.mFirstLayout && !smoothSlideTo(1.0f, initialVelocity)) {
            return false;
        }
        this.mPreservedOpenState = false;
        return true;
    }

    private boolean openPane(int initialVelocity) {
        if (!this.mCanSlide) {
            this.mPreservedOpenState = true;
        }
        if (this.mFirstLayout || smoothSlideTo(0.0f, initialVelocity)) {
            this.mPreservedOpenState = true;
            return true;
        }
        return false;
    }

    @Deprecated
    public void smoothSlideOpen() {
        openPane();
    }

    @Override // androidx.customview.widget.Openable
    public void open() {
        openPane();
    }

    public boolean openPane() {
        return openPane(0);
    }

    @Deprecated
    public void smoothSlideClosed() {
        closePane();
    }

    @Override // androidx.customview.widget.Openable
    public void close() {
        closePane();
    }

    public boolean closePane() {
        return closePane(0);
    }

    @Override // androidx.customview.widget.Openable
    public boolean isOpen() {
        return !this.mCanSlide || this.mSlideOffset == 0.0f;
    }

    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }

    public boolean isSlideable() {
        return this.mCanSlide;
    }

    void onPanelDragged(int newLeft) {
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
            return;
        }
        boolean isLayoutRtl = isLayoutRtlSupport();
        LayoutParams lp = (LayoutParams) this.mSlideableView.getLayoutParams();
        int childWidth = this.mSlideableView.getWidth();
        int newStart = isLayoutRtl ? (getWidth() - newLeft) - childWidth : newLeft;
        int paddingStart = isLayoutRtl ? getPaddingRight() : getPaddingLeft();
        int lpMargin = isLayoutRtl ? lp.rightMargin : lp.leftMargin;
        int startBound = paddingStart + lpMargin;
        float f = (newStart - startBound) / this.mSlideRange;
        this.mSlideOffset = f;
        if (this.mParallaxBy != 0) {
            parallaxOtherViews(f);
        }
        dispatchOnPanelSlide(this.mSlideableView);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean isLayoutRtl = isLayoutRtlSupport();
        boolean enableEdgeLeftTracking = isOpen() ^ isLayoutRtl;
        if (enableEdgeLeftTracking) {
            this.mDragHelper.setEdgeTrackingEnabled(1);
            Insets gestureInsets = getSystemGestureInsets();
            if (gestureInsets != null) {
                ViewDragHelper viewDragHelper = this.mDragHelper;
                viewDragHelper.setEdgeSize(Math.max(viewDragHelper.getDefaultEdgeSize(), gestureInsets.left));
            }
        } else {
            this.mDragHelper.setEdgeTrackingEnabled(2);
            Insets gestureInsets2 = getSystemGestureInsets();
            if (gestureInsets2 != null) {
                ViewDragHelper viewDragHelper2 = this.mDragHelper;
                viewDragHelper2.setEdgeSize(Math.max(viewDragHelper2.getDefaultEdgeSize(), gestureInsets2.right));
            }
        }
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int save = canvas.save();
        if (this.mCanSlide && !lp.slideable && this.mSlideableView != null) {
            canvas.getClipBounds(this.mTmpRect);
            if (isLayoutRtlSupport()) {
                Rect rect = this.mTmpRect;
                rect.left = Math.max(rect.left, this.mSlideableView.getRight());
            } else {
                Rect rect2 = this.mTmpRect;
                rect2.right = Math.min(rect2.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(save);
        return result;
    }

    private Insets getSystemGestureInsets() {
        WindowInsetsCompat rootInsetsCompat;
        if (!sEdgeSizeUsingSystemGestureInsets || (rootInsetsCompat = ViewCompat.getRootWindowInsets(this)) == null) {
            return null;
        }
        Insets gestureInsets = rootInsetsCompat.getSystemGestureInsets();
        return gestureInsets;
    }

    void invalidateChildRegion(View v) {
        ViewCompat.setLayerPaint(v, ((LayoutParams) v.getLayoutParams()).dimPaint);
    }

    boolean smoothSlideTo(float slideOffset, int velocity) {
        int startBound;
        if (!this.mCanSlide) {
            return false;
        }
        boolean isLayoutRtl = isLayoutRtlSupport();
        LayoutParams lp = (LayoutParams) this.mSlideableView.getLayoutParams();
        if (isLayoutRtl) {
            int startBound2 = getPaddingRight() + lp.rightMargin;
            int childWidth = this.mSlideableView.getWidth();
            startBound = (int) (getWidth() - ((startBound2 + (this.mSlideRange * slideOffset)) + childWidth));
        } else {
            int x = getPaddingLeft();
            int startBound3 = x + lp.leftMargin;
            startBound = (int) (startBound3 + (this.mSlideRange * slideOffset));
        }
        ViewDragHelper viewDragHelper = this.mDragHelper;
        View view = this.mSlideableView;
        if (!viewDragHelper.smoothSlideViewTo(view, startBound, view.getTop())) {
            return false;
        }
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mDragHelper.continueSettling(true)) {
            if (!this.mCanSlide) {
                this.mDragHelper.abort();
            } else {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    @Deprecated
    public void setShadowDrawable(Drawable d) {
        setShadowDrawableLeft(d);
    }

    public void setShadowDrawableLeft(Drawable d) {
        this.mShadowDrawableLeft = d;
    }

    public void setShadowDrawableRight(Drawable d) {
        this.mShadowDrawableRight = d;
    }

    @Deprecated
    public void setShadowResource(int resId) {
        setShadowDrawableLeft(getResources().getDrawable(resId));
    }

    public void setShadowResourceLeft(int resId) {
        setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setShadowResourceRight(int resId) {
        setShadowDrawableRight(ContextCompat.getDrawable(getContext(), resId));
    }

    @Override // android.view.View
    public void draw(Canvas c) {
        Drawable shadowDrawable;
        int right;
        int left;
        super.draw(c);
        boolean isLayoutRtl = isLayoutRtlSupport();
        if (isLayoutRtl) {
            shadowDrawable = this.mShadowDrawableRight;
        } else {
            shadowDrawable = this.mShadowDrawableLeft;
        }
        View shadowView = getChildCount() > 1 ? getChildAt(1) : null;
        if (shadowView == null || shadowDrawable == null) {
            return;
        }
        int top = shadowView.getTop();
        int bottom = shadowView.getBottom();
        int shadowWidth = shadowDrawable.getIntrinsicWidth();
        if (isLayoutRtlSupport()) {
            left = shadowView.getRight();
            right = left + shadowWidth;
        } else {
            right = shadowView.getLeft();
            left = right - shadowWidth;
        }
        shadowDrawable.setBounds(left, top, right, bottom);
        shadowDrawable.draw(c);
    }

    private void parallaxOtherViews(float slideOffset) {
        boolean isLayoutRtl = isLayoutRtlSupport();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v != this.mSlideableView) {
                float f = 1.0f - this.mParallaxOffset;
                int i2 = this.mParallaxBy;
                int oldOffset = (int) (f * i2);
                this.mParallaxOffset = slideOffset;
                int newOffset = (int) ((1.0f - slideOffset) * i2);
                int dx = oldOffset - newOffset;
                v.offsetLeftAndRight(isLayoutRtl ? -dx : dx);
            }
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                    return true;
                }
            }
        }
        if (checkV) {
            if (v.canScrollHorizontally(isLayoutRtlSupport() ? dx : -dx)) {
                return true;
            }
        }
        return false;
    }

    boolean isDimmed(View child) {
        if (child == null) {
            return false;
        }
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return this.mCanSlide && lp.dimWhenOffset && this.mSlideOffset > 0.0f;
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) p);
        }
        return new LayoutParams(p);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isOpen = isSlideable() ? isOpen() : this.mPreservedOpenState;
        ss.mLockMode = this.mLockMode;
        return ss;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.isOpen) {
            openPane();
        } else {
            closePane();
        }
        this.mPreservedOpenState = ss.isOpen;
        setLockMode(ss.mLockMode);
    }

    /* loaded from: classes.dex */
    private class DragHelperCallback extends ViewDragHelper.Callback {
        DragHelperCallback() {
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View child, int pointerId) {
            if (!isDraggable()) {
                return false;
            }
            return ((LayoutParams) child.getLayoutParams()).slideable;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewDragStateChanged(int state) {
            if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
                if (SlidingPaneLayout.this.mSlideOffset == 1.0f) {
                    SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
                    slidingPaneLayout.updateObscuredViewsVisibility(slidingPaneLayout.mSlideableView);
                    SlidingPaneLayout slidingPaneLayout2 = SlidingPaneLayout.this;
                    slidingPaneLayout2.dispatchOnPanelClosed(slidingPaneLayout2.mSlideableView);
                    SlidingPaneLayout.this.mPreservedOpenState = false;
                    return;
                }
                SlidingPaneLayout slidingPaneLayout3 = SlidingPaneLayout.this;
                slidingPaneLayout3.dispatchOnPanelOpened(slidingPaneLayout3.mSlideableView);
                SlidingPaneLayout.this.mPreservedOpenState = true;
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewCaptured(View capturedChild, int activePointerId) {
            SlidingPaneLayout.this.setAllChildrenVisible();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            SlidingPaneLayout.this.onPanelDragged(left);
            SlidingPaneLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left;
            LayoutParams lp = (LayoutParams) releasedChild.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                int startToRight = SlidingPaneLayout.this.getPaddingRight() + lp.rightMargin;
                if (xvel < 0.0f || (xvel == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    startToRight += SlidingPaneLayout.this.mSlideRange;
                }
                int childWidth = SlidingPaneLayout.this.mSlideableView.getWidth();
                left = (SlidingPaneLayout.this.getWidth() - startToRight) - childWidth;
            } else {
                int left2 = SlidingPaneLayout.this.getPaddingLeft() + lp.leftMargin;
                if (xvel > 0.0f || (xvel == 0.0f && SlidingPaneLayout.this.mSlideOffset > 0.5f)) {
                    left = left2 + SlidingPaneLayout.this.mSlideRange;
                } else {
                    left = left2;
                }
            }
            SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
            SlidingPaneLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View child) {
            return SlidingPaneLayout.this.mSlideRange;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            LayoutParams lp = (LayoutParams) SlidingPaneLayout.this.mSlideableView.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                int startBound = SlidingPaneLayout.this.getWidth() - ((SlidingPaneLayout.this.getPaddingRight() + lp.rightMargin) + SlidingPaneLayout.this.mSlideableView.getWidth());
                int endBound = startBound - SlidingPaneLayout.this.mSlideRange;
                int newLeft = Math.max(Math.min(left, startBound), endBound);
                return newLeft;
            }
            int startBound2 = SlidingPaneLayout.this.getPaddingLeft() + lp.leftMargin;
            int endBound2 = SlidingPaneLayout.this.mSlideRange + startBound2;
            int newLeft2 = Math.min(Math.max(left, startBound2), endBound2);
            return newLeft2;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            if (!isDraggable()) {
                return;
            }
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, pointerId);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            if (!isDraggable()) {
                return;
            }
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, pointerId);
        }

        private boolean isDraggable() {
            if (SlidingPaneLayout.this.mIsUnableToDrag || SlidingPaneLayout.this.getLockMode() == 3) {
                return false;
            }
            if (SlidingPaneLayout.this.isOpen() && SlidingPaneLayout.this.getLockMode() == 1) {
                return false;
            }
            return SlidingPaneLayout.this.isOpen() || SlidingPaneLayout.this.getLockMode() != 2;
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = {R.attr.layout_weight};
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight;

        public LayoutParams() {
            super(-1, -1);
            this.weight = 0.0f;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.weight = 0.0f;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.weight = 0.0f;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.weight = 0.0f;
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.weight = 0.0f;
            this.weight = source.weight;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.weight = 0.0f;
            TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
            this.weight = a.getFloat(0, 0.0f);
            a.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.slidingpanelayout.widget.SlidingPaneLayout.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, null);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean isOpen;
        int mLockMode;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.isOpen = in.readInt() != 0;
            this.mLockMode = in.readInt();
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isOpen ? 1 : 0);
            parcel.writeInt(this.mLockMode);
        }
    }

    /* loaded from: classes.dex */
    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        AccessibilityDelegate() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
            super.onInitializeAccessibilityNodeInfo(host, superNode);
            copyNodeInfoNoChildren(info, superNode);
            superNode.recycle();
            info.setClassName(SlidingPaneLayout.ACCESSIBILITY_CLASS_NAME);
            info.setSource(host);
            Object parentForAccessibility = ViewCompat.getParentForAccessibility(host);
            if (parentForAccessibility instanceof View) {
                info.setParent((View) parentForAccessibility);
            }
            int childCount = SlidingPaneLayout.this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = SlidingPaneLayout.this.getChildAt(i);
                if (!filter(child) && child.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(child, 1);
                    info.addChild(child);
                }
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(SlidingPaneLayout.ACCESSIBILITY_CLASS_NAME);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            if (!filter(child)) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
            return false;
        }

        public boolean filter(View child) {
            return SlidingPaneLayout.this.isDimmed(child);
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest, AccessibilityNodeInfoCompat src) {
            Rect rect = this.mTmpRect;
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());
            dest.addAction(src.getActions());
            dest.setMovementGranularities(src.getMovementGranularities());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TouchBlocker extends FrameLayout {
        TouchBlocker(View view) {
            super(view.getContext());
            addView(view);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

        @Override // android.view.View
        public boolean onGenericMotionEvent(MotionEvent event) {
            return true;
        }
    }

    /* loaded from: classes.dex */
    private class DisableLayerRunnable implements Runnable {
        final View mChildView;

        DisableLayerRunnable(View childView) {
            this.mChildView = childView;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mChildView.getParent() == SlidingPaneLayout.this) {
                this.mChildView.setLayerType(0, null);
                SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
            }
            SlidingPaneLayout.this.mPostedRunnables.remove(this);
        }
    }

    boolean isLayoutRtlSupport() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private ArrayList<Rect> splitViewPositions() {
        Rect splitPosition;
        FoldingFeature foldingFeature = this.mFoldingFeature;
        if (foldingFeature == null || !foldingFeature.isSeparating() || this.mFoldingFeature.getBounds().left == 0 || this.mFoldingFeature.getBounds().top != 0 || (splitPosition = getFoldBoundsInView(this.mFoldingFeature, this)) == null) {
            return null;
        }
        Rect leftRect = new Rect(getPaddingLeft(), getPaddingTop(), Math.max(getPaddingLeft(), splitPosition.left), getHeight() - getPaddingBottom());
        int rightBound = getWidth() - getPaddingRight();
        Rect rightRect = new Rect(Math.min(rightBound, splitPosition.right), getPaddingTop(), rightBound, getHeight() - getPaddingBottom());
        return new ArrayList<>(Arrays.asList(leftRect, rightRect));
    }

    private static Rect getFoldBoundsInView(FoldingFeature foldingFeature, View view) {
        int[] viewLocationInWindow = new int[2];
        view.getLocationInWindow(viewLocationInWindow);
        Rect viewRect = new Rect(viewLocationInWindow[0], viewLocationInWindow[1], viewLocationInWindow[0] + view.getWidth(), viewLocationInWindow[1] + view.getWidth());
        Rect foldRectInView = new Rect(foldingFeature.getBounds());
        boolean intersects = foldRectInView.intersect(viewRect);
        if ((foldRectInView.width() == 0 && foldRectInView.height() == 0) || !intersects) {
            return null;
        }
        foldRectInView.offset(-viewLocationInWindow[0], -viewLocationInWindow[1]);
        return foldRectInView;
    }

    private static Activity getActivityOrNull(Context context) {
        for (Context iterator = context; iterator instanceof ContextWrapper; iterator = ((ContextWrapper) iterator).getBaseContext()) {
            if (iterator instanceof Activity) {
                return (Activity) iterator;
            }
        }
        return null;
    }
}
