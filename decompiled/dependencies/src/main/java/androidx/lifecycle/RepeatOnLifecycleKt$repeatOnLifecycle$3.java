package androidx.lifecycle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import com.android.tools.smali.dexlib2.dexbacked.raw.HeaderItem;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;

/* compiled from: RepeatOnLifecycle.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
@DebugMetadata(c = "androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3", f = "RepeatOnLifecycle.kt", i = {}, l = {HeaderItem.FIELD_START_OFFSET}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes.dex */
final class RepeatOnLifecycleKt$repeatOnLifecycle$3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<CoroutineScope, Continuation<? super Unit>, Object> $block;
    final /* synthetic */ Lifecycle.State $state;
    final /* synthetic */ Lifecycle $this_repeatOnLifecycle;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public RepeatOnLifecycleKt$repeatOnLifecycle$3(Lifecycle lifecycle, Lifecycle.State state, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super RepeatOnLifecycleKt$repeatOnLifecycle$3> continuation) {
        super(2, continuation);
        this.$this_repeatOnLifecycle = lifecycle;
        this.$state = state;
        this.$block = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        RepeatOnLifecycleKt$repeatOnLifecycle$3 repeatOnLifecycleKt$repeatOnLifecycle$3 = new RepeatOnLifecycleKt$repeatOnLifecycle$3(this.$this_repeatOnLifecycle, this.$state, this.$block, continuation);
        repeatOnLifecycleKt$repeatOnLifecycle$3.L$0 = obj;
        return repeatOnLifecycleKt$repeatOnLifecycle$3;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((RepeatOnLifecycleKt$repeatOnLifecycle$3) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RepeatOnLifecycle.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    @DebugMetadata(c = "androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1", f = "RepeatOnLifecycle.kt", i = {0, 0}, l = {166}, m = "invokeSuspend", n = {"launchedJob", "observer"}, s = {"L$0", "L$1"})
    /* renamed from: androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ CoroutineScope $$this$coroutineScope;
        final /* synthetic */ Function2<CoroutineScope, Continuation<? super Unit>, Object> $block;
        final /* synthetic */ Lifecycle.State $state;
        final /* synthetic */ Lifecycle $this_repeatOnLifecycle;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass1(Lifecycle lifecycle, Lifecycle.State state, CoroutineScope coroutineScope, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$this_repeatOnLifecycle = lifecycle;
            this.$state = state;
            this.$$this$coroutineScope = coroutineScope;
            this.$block = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.$this_repeatOnLifecycle, this.$state, this.$$this$coroutineScope, this.$block, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000a. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:11:0x00d5  */
        /* JADX WARN: Removed duplicated region for block: B:14:0x00df  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x00ff  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0109  */
        /* JADX WARN: Type inference failed for: r7v5, types: [androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1, T] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            AnonymousClass1 anonymousClass1;
            Ref.ObjectRef launchedJob;
            Ref.ObjectRef launchedJob2;
            Job job;
            LifecycleEventObserver it;
            Job job2;
            LifecycleEventObserver it2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    anonymousClass1 = this;
                    if (anonymousClass1.$this_repeatOnLifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
                        return Unit.INSTANCE;
                    }
                    final Ref.ObjectRef launchedJob3 = new Ref.ObjectRef();
                    Ref.ObjectRef observer = new Ref.ObjectRef();
                    try {
                        Lifecycle.State state = anonymousClass1.$state;
                        Lifecycle lifecycle = anonymousClass1.$this_repeatOnLifecycle;
                        final CoroutineScope coroutineScope = anonymousClass1.$$this$coroutineScope;
                        final Function2<CoroutineScope, Continuation<? super Unit>, Object> function2 = anonymousClass1.$block;
                        anonymousClass1.L$0 = launchedJob3;
                        anonymousClass1.L$1 = observer;
                        anonymousClass1.L$2 = state;
                        anonymousClass1.L$3 = lifecycle;
                        anonymousClass1.L$4 = coroutineScope;
                        anonymousClass1.L$5 = function2;
                        anonymousClass1.label = 1;
                        AnonymousClass1 uCont$iv = anonymousClass1;
                        CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(uCont$iv), 1);
                        cancellable$iv.initCancellability();
                        final CancellableContinuationImpl cont = cancellable$iv;
                        final Lifecycle.Event startWorkEvent = Lifecycle.Event.upTo(state);
                        final Lifecycle.Event cancelWorkEvent = Lifecycle.Event.downFrom(state);
                        final Mutex mutex = MutexKt.Mutex$default(false, 1, null);
                        observer.element = new LifecycleEventObserver() { // from class: androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1
                            /* JADX WARN: Type inference failed for: r0v5, types: [T, kotlinx.coroutines.Job] */
                            @Override // androidx.lifecycle.LifecycleEventObserver
                            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                                ?? launch$default;
                                Intrinsics.checkNotNullParameter(lifecycleOwner, "<anonymous parameter 0>");
                                Intrinsics.checkNotNullParameter(event, "event");
                                if (event == Lifecycle.Event.this) {
                                    Ref.ObjectRef<Job> objectRef = launchedJob3;
                                    launch$default = BuildersKt__Builders_commonKt.launch$default(coroutineScope, null, null, new AnonymousClass1(mutex, function2, null), 3, null);
                                    objectRef.element = launch$default;
                                    return;
                                }
                                if (event == cancelWorkEvent) {
                                    Job job3 = launchedJob3.element;
                                    if (job3 != null) {
                                        Job.DefaultImpls.cancel$default(job3, (CancellationException) null, 1, (Object) null);
                                    }
                                    launchedJob3.element = null;
                                }
                                if (event == Lifecycle.Event.ON_DESTROY) {
                                    CancellableContinuation<Unit> cancellableContinuation = cont;
                                    Result.Companion companion = Result.INSTANCE;
                                    cancellableContinuation.resumeWith(Result.m168constructorimpl(Unit.INSTANCE));
                                }
                            }

                            /* compiled from: RepeatOnLifecycle.kt */
                            @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 6, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
                            @DebugMetadata(c = "androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1", f = "RepeatOnLifecycle.kt", i = {0, 1}, l = {171, 110}, m = "invokeSuspend", n = {"$this$withLock_u24default$iv", "$this$withLock_u24default$iv"}, s = {"L$0", "L$0"})
                            /* renamed from: androidx.lifecycle.RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1, reason: invalid class name */
                            /* loaded from: classes.dex */
                            static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
                                final /* synthetic */ Function2<CoroutineScope, Continuation<? super Unit>, Object> $block;
                                final /* synthetic */ Mutex $mutex;
                                Object L$0;
                                Object L$1;
                                int label;

                                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                                /* JADX WARN: Multi-variable type inference failed */
                                AnonymousClass1(Mutex mutex, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super AnonymousClass1> continuation) {
                                    super(2, continuation);
                                    this.$mutex = mutex;
                                    this.$block = function2;
                                }

                                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                                public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                                    return new AnonymousClass1(this.$mutex, this.$block, continuation);
                                }

                                @Override // kotlin.jvm.functions.Function2
                                public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                                    return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
                                }

                                /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
                                /* JADX WARN: Removed duplicated region for block: B:21:0x0065 A[RETURN] */
                                /* JADX WARN: Removed duplicated region for block: B:22:0x0066  */
                                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                                /*
                                    Code decompiled incorrectly, please refer to instructions dump.
                                */
                                public final Object invokeSuspend(Object $result) {
                                    AnonymousClass1 anonymousClass1;
                                    Function2<CoroutineScope, Continuation<? super Unit>, Object> function2;
                                    Object owner$iv;
                                    Mutex $this$withLock_u24default$iv;
                                    Mutex $this$withLock_u24default$iv2;
                                    Object owner$iv2;
                                    Mutex $this$withLock_u24default$iv3;
                                    Throwable th;
                                    RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1 repeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1;
                                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                    switch (this.label) {
                                        case 0:
                                            ResultKt.throwOnFailure($result);
                                            anonymousClass1 = this;
                                            Mutex $this$withLock_u24default$iv4 = anonymousClass1.$mutex;
                                            function2 = anonymousClass1.$block;
                                            owner$iv = null;
                                            anonymousClass1.L$0 = $this$withLock_u24default$iv4;
                                            anonymousClass1.L$1 = function2;
                                            anonymousClass1.label = 1;
                                            if ($this$withLock_u24default$iv4.lock(null, anonymousClass1) == coroutine_suspended) {
                                                return coroutine_suspended;
                                            }
                                            $this$withLock_u24default$iv = $this$withLock_u24default$iv4;
                                            $this$withLock_u24default$iv2 = null;
                                            try {
                                                repeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1 = new RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1(function2, null);
                                                anonymousClass1.L$0 = $this$withLock_u24default$iv;
                                                anonymousClass1.L$1 = null;
                                                anonymousClass1.label = 2;
                                                if (CoroutineScopeKt.coroutineScope(repeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1, anonymousClass1) != coroutine_suspended) {
                                                    return coroutine_suspended;
                                                }
                                                owner$iv2 = owner$iv;
                                                $this$withLock_u24default$iv3 = $this$withLock_u24default$iv;
                                                Unit unit = Unit.INSTANCE;
                                                $this$withLock_u24default$iv3.unlock(owner$iv2);
                                                return Unit.INSTANCE;
                                            } catch (Throwable th2) {
                                                owner$iv2 = owner$iv;
                                                $this$withLock_u24default$iv3 = $this$withLock_u24default$iv;
                                                th = th2;
                                                $this$withLock_u24default$iv3.unlock(owner$iv2);
                                                throw th;
                                            }
                                        case 1:
                                            anonymousClass1 = this;
                                            $this$withLock_u24default$iv2 = null;
                                            function2 = (Function2) anonymousClass1.L$1;
                                            owner$iv = null;
                                            $this$withLock_u24default$iv = (Mutex) anonymousClass1.L$0;
                                            ResultKt.throwOnFailure($result);
                                            repeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1 = new RepeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1(function2, null);
                                            anonymousClass1.L$0 = $this$withLock_u24default$iv;
                                            anonymousClass1.L$1 = null;
                                            anonymousClass1.label = 2;
                                            if (CoroutineScopeKt.coroutineScope(repeatOnLifecycleKt$repeatOnLifecycle$3$1$1$1$1$1$1, anonymousClass1) != coroutine_suspended) {
                                            }
                                            break;
                                        case 2:
                                            owner$iv2 = null;
                                            $this$withLock_u24default$iv3 = (Mutex) this.L$0;
                                            try {
                                                ResultKt.throwOnFailure($result);
                                                Unit unit2 = Unit.INSTANCE;
                                                $this$withLock_u24default$iv3.unlock(owner$iv2);
                                                return Unit.INSTANCE;
                                            } catch (Throwable th3) {
                                                th = th3;
                                                $this$withLock_u24default$iv3.unlock(owner$iv2);
                                                throw th;
                                            }
                                        default:
                                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                    }
                                }
                            }
                        };
                        T t = observer.element;
                        if (t == 0) {
                            throw new NullPointerException("null cannot be cast to non-null type androidx.lifecycle.LifecycleEventObserver");
                        }
                        lifecycle.addObserver((LifecycleEventObserver) t);
                        Object result = cancellable$iv.getResult();
                        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            DebugProbesKt.probeCoroutineSuspended(anonymousClass1);
                        }
                        if (result == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        launchedJob = launchedJob3;
                        launchedJob2 = observer;
                        job2 = (Job) launchedJob.element;
                        if (job2 != null) {
                            Job.DefaultImpls.cancel$default(job2, (CancellationException) null, 1, (Object) null);
                        }
                        it2 = (LifecycleEventObserver) launchedJob2.element;
                        if (it2 != null) {
                            anonymousClass1.$this_repeatOnLifecycle.removeObserver(it2);
                        }
                        return Unit.INSTANCE;
                    } catch (Throwable th) {
                        th = th;
                        launchedJob = launchedJob3;
                        launchedJob2 = observer;
                        job = (Job) launchedJob.element;
                        if (job != null) {
                            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
                        }
                        it = (LifecycleEventObserver) launchedJob2.element;
                        if (it != null) {
                            anonymousClass1.$this_repeatOnLifecycle.removeObserver(it);
                        }
                        throw th;
                    }
                case 1:
                    anonymousClass1 = this;
                    launchedJob2 = (Ref.ObjectRef) anonymousClass1.L$1;
                    launchedJob = (Ref.ObjectRef) anonymousClass1.L$0;
                    try {
                        ResultKt.throwOnFailure($result);
                        job2 = (Job) launchedJob.element;
                        if (job2 != null) {
                        }
                        it2 = (LifecycleEventObserver) launchedJob2.element;
                        if (it2 != null) {
                        }
                        return Unit.INSTANCE;
                    } catch (Throwable th2) {
                        th = th2;
                        job = (Job) launchedJob.element;
                        if (job != null) {
                        }
                        it = (LifecycleEventObserver) launchedJob2.element;
                        if (it != null) {
                        }
                        throw th;
                    }
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                CoroutineScope $this$coroutineScope = (CoroutineScope) this.L$0;
                this.label = 1;
                if (BuildersKt.withContext(Dispatchers.getMain().getImmediate(), new AnonymousClass1(this.$this_repeatOnLifecycle, this.$state, $this$coroutineScope, this.$block, null), this) != coroutine_suspended) {
                    break;
                } else {
                    return coroutine_suspended;
                }
            case 1:
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
