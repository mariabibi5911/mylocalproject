package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.DirectedGraphConnections;
import com.google.common.graph.ElementOrder;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.CheckForNull;

/* JADX INFO: Access modifiers changed from: package-private */
@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class DirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private static final Object PRED = new Object();
    private final Map<N, Object> adjacentNodeValues;

    @CheckForNull
    private final List<NodeConnection<N>> orderedNodeConnections;
    private int predecessorCount;
    private int successorCount;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static final class PredAndSucc {
        private final Object successorValue;

        PredAndSucc(Object successorValue) {
            this.successorValue = successorValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes7.dex */
    public static abstract class NodeConnection<N> {
        final N node;

        NodeConnection(N n) {
            this.node = (N) Preconditions.checkNotNull(n);
        }

        /* loaded from: classes7.dex */
        static final class Pred<N> extends NodeConnection<N> {
            Pred(N node) {
                super(node);
            }

            public boolean equals(@CheckForNull Object that) {
                if (that instanceof Pred) {
                    return this.node.equals(((Pred) that).node);
                }
                return false;
            }

            public int hashCode() {
                return Pred.class.hashCode() + this.node.hashCode();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes7.dex */
        public static final class Succ<N> extends NodeConnection<N> {
            Succ(N node) {
                super(node);
            }

            public boolean equals(@CheckForNull Object that) {
                if (that instanceof Succ) {
                    return this.node.equals(((Succ) that).node);
                }
                return false;
            }

            public int hashCode() {
                return Succ.class.hashCode() + this.node.hashCode();
            }
        }
    }

    private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, @CheckForNull List<NodeConnection<N>> orderedNodeConnections, int predecessorCount, int successorCount) {
        this.adjacentNodeValues = (Map) Preconditions.checkNotNull(adjacentNodeValues);
        this.orderedNodeConnections = orderedNodeConnections;
        this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
        this.successorCount = Graphs.checkNonNegative(successorCount);
        Preconditions.checkState(predecessorCount <= adjacentNodeValues.size() && successorCount <= adjacentNodeValues.size());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <N, V> DirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
        List<NodeConnection<N>> orderedNodeConnections;
        switch (AnonymousClass5.$SwitchMap$com$google$common$graph$ElementOrder$Type[incidentEdgeOrder.type().ordinal()]) {
            case 1:
                orderedNodeConnections = null;
                break;
            case 2:
                orderedNodeConnections = new ArrayList<>();
                break;
            default:
                throw new AssertionError(incidentEdgeOrder.type());
        }
        return new DirectedGraphConnections<>(new HashMap(4, 1.0f), orderedNodeConnections, 0, 0);
    }

    /* renamed from: com.google.common.graph.DirectedGraphConnections$5, reason: invalid class name */
    /* loaded from: classes7.dex */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$graph$ElementOrder$Type;

        static {
            int[] iArr = new int[ElementOrder.Type.values().length];
            $SwitchMap$com$google$common$graph$ElementOrder$Type = iArr;
            try {
                iArr[ElementOrder.Type.UNORDERED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$graph$ElementOrder$Type[ElementOrder.Type.STABLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <N, V> DirectedGraphConnections<N, V> ofImmutable(N thisNode, Iterable<EndpointPair<N>> incidentEdges, Function<N, V> successorNodeToValueFn) {
        Preconditions.checkNotNull(thisNode);
        Preconditions.checkNotNull(successorNodeToValueFn);
        Map<N, Object> adjacentNodeValues = new HashMap<>();
        ImmutableList.Builder<NodeConnection<N>> orderedNodeConnectionsBuilder = ImmutableList.builder();
        int predecessorCount = 0;
        int successorCount = 0;
        for (EndpointPair<N> incidentEdge : incidentEdges) {
            if (incidentEdge.nodeU().equals(thisNode) && incidentEdge.nodeV().equals(thisNode)) {
                adjacentNodeValues.put(thisNode, new PredAndSucc(successorNodeToValueFn.apply(thisNode)));
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Pred<>(thisNode));
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Succ<>(thisNode));
                predecessorCount++;
                successorCount++;
            } else if (incidentEdge.nodeV().equals(thisNode)) {
                N predecessor = incidentEdge.nodeU();
                Object existingValue = adjacentNodeValues.put(predecessor, PRED);
                if (existingValue != null) {
                    adjacentNodeValues.put(predecessor, new PredAndSucc(existingValue));
                }
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Pred<>(predecessor));
                predecessorCount++;
            } else {
                Preconditions.checkArgument(incidentEdge.nodeU().equals(thisNode));
                N successor = incidentEdge.nodeV();
                V value = successorNodeToValueFn.apply(successor);
                Object existingValue2 = adjacentNodeValues.put(successor, value);
                if (existingValue2 != null) {
                    Preconditions.checkArgument(existingValue2 == PRED);
                    adjacentNodeValues.put(successor, new PredAndSucc(value));
                }
                orderedNodeConnectionsBuilder.add((ImmutableList.Builder<NodeConnection<N>>) new NodeConnection.Succ<>(successor));
                successorCount++;
            }
        }
        return new DirectedGraphConnections<>(adjacentNodeValues, orderedNodeConnectionsBuilder.build(), predecessorCount, successorCount);
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> adjacentNodes() {
        if (this.orderedNodeConnections == null) {
            return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
        }
        return new AbstractSet<N>() { // from class: com.google.common.graph.DirectedGraphConnections.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<N> iterator() {
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                final Set<N> seenNodes = new HashSet<>();
                return new AbstractIterator<N>(this) { // from class: com.google.common.graph.DirectedGraphConnections.1.1
                    @Override // com.google.common.collect.AbstractIterator
                    @CheckForNull
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            NodeConnection<N> nodeConnection = (NodeConnection) nodeConnections.next();
                            boolean added = seenNodes.add(nodeConnection.node);
                            if (added) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DirectedGraphConnections.this.adjacentNodeValues.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object obj) {
                return DirectedGraphConnections.this.adjacentNodeValues.containsKey(obj);
            }
        };
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> predecessors() {
        return new AbstractSet<N>() { // from class: com.google.common.graph.DirectedGraphConnections.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>(this) { // from class: com.google.common.graph.DirectedGraphConnections.2.1
                        @Override // com.google.common.collect.AbstractIterator
                        @CheckForNull
                        protected N computeNext() {
                            while (entries.hasNext()) {
                                Map.Entry<N, Object> entry = (Map.Entry) entries.next();
                                if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
                                    return entry.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>(this) { // from class: com.google.common.graph.DirectedGraphConnections.2.2
                    @Override // com.google.common.collect.AbstractIterator
                    @CheckForNull
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            NodeConnection<N> nodeConnection = (NodeConnection) nodeConnections.next();
                            if (nodeConnection instanceof NodeConnection.Pred) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DirectedGraphConnections.this.predecessorCount;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object obj) {
                return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }

    @Override // com.google.common.graph.GraphConnections
    public Set<N> successors() {
        return new AbstractSet<N>() { // from class: com.google.common.graph.DirectedGraphConnections.3
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>(this) { // from class: com.google.common.graph.DirectedGraphConnections.3.1
                        @Override // com.google.common.collect.AbstractIterator
                        @CheckForNull
                        protected N computeNext() {
                            while (entries.hasNext()) {
                                Map.Entry<N, Object> entry = (Map.Entry) entries.next();
                                if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
                                    return entry.getKey();
                                }
                            }
                            return endOfData();
                        }
                    };
                }
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>(this) { // from class: com.google.common.graph.DirectedGraphConnections.3.2
                    @Override // com.google.common.collect.AbstractIterator
                    @CheckForNull
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            NodeConnection<N> nodeConnection = (NodeConnection) nodeConnections.next();
                            if (nodeConnection instanceof NodeConnection.Succ) {
                                return nodeConnection.node;
                            }
                        }
                        return endOfData();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DirectedGraphConnections.this.successorCount;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(@CheckForNull Object obj) {
                return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }

    @Override // com.google.common.graph.GraphConnections
    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N thisNode) {
        final Iterator<EndpointPair<N>> resultWithDoubleSelfLoop;
        Preconditions.checkNotNull(thisNode);
        List<NodeConnection<N>> list = this.orderedNodeConnections;
        if (list == null) {
            resultWithDoubleSelfLoop = Iterators.concat(Iterators.transform(predecessors().iterator(), new Function() { // from class: com.google.common.graph.DirectedGraphConnections$$ExternalSyntheticLambda0
                @Override // com.google.common.base.Function
                public final Object apply(Object obj) {
                    EndpointPair ordered;
                    ordered = EndpointPair.ordered(obj, thisNode);
                    return ordered;
                }
            }), Iterators.transform(successors().iterator(), new Function() { // from class: com.google.common.graph.DirectedGraphConnections$$ExternalSyntheticLambda1
                @Override // com.google.common.base.Function
                public final Object apply(Object obj) {
                    EndpointPair ordered;
                    ordered = EndpointPair.ordered(thisNode, obj);
                    return ordered;
                }
            }));
        } else {
            resultWithDoubleSelfLoop = Iterators.transform(list.iterator(), new Function() { // from class: com.google.common.graph.DirectedGraphConnections$$ExternalSyntheticLambda2
                @Override // com.google.common.base.Function
                public final Object apply(Object obj) {
                    return DirectedGraphConnections.lambda$incidentEdgeIterator$2(thisNode, (DirectedGraphConnections.NodeConnection) obj);
                }
            });
        }
        final AtomicBoolean alreadySeenSelfLoop = new AtomicBoolean(false);
        return new AbstractIterator<EndpointPair<N>>(this) { // from class: com.google.common.graph.DirectedGraphConnections.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.AbstractIterator
            @CheckForNull
            public EndpointPair<N> computeNext() {
                while (resultWithDoubleSelfLoop.hasNext()) {
                    EndpointPair<N> edge = (EndpointPair) resultWithDoubleSelfLoop.next();
                    if (edge.nodeU().equals(edge.nodeV())) {
                        if (!alreadySeenSelfLoop.getAndSet(true)) {
                            return edge;
                        }
                    } else {
                        return edge;
                    }
                }
                return endOfData();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ EndpointPair lambda$incidentEdgeIterator$2(Object thisNode, NodeConnection connection) {
        if (connection instanceof NodeConnection.Succ) {
            return EndpointPair.ordered(thisNode, connection.node);
        }
        return EndpointPair.ordered(connection.node, thisNode);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V value(N n) {
        Preconditions.checkNotNull(n);
        V v = (V) this.adjacentNodeValues.get(n);
        if (v == PRED) {
            return null;
        }
        if (v instanceof PredAndSucc) {
            return (V) ((PredAndSucc) v).successorValue;
        }
        return v;
    }

    @Override // com.google.common.graph.GraphConnections
    public void removePredecessor(N node) {
        boolean removedPredecessor;
        Preconditions.checkNotNull(node);
        Object previousValue = this.adjacentNodeValues.get(node);
        if (previousValue == PRED) {
            this.adjacentNodeValues.remove(node);
            removedPredecessor = true;
        } else {
            boolean removedPredecessor2 = previousValue instanceof PredAndSucc;
            if (removedPredecessor2) {
                this.adjacentNodeValues.put(node, ((PredAndSucc) previousValue).successorValue);
                removedPredecessor = true;
            } else {
                removedPredecessor = false;
            }
        }
        if (removedPredecessor) {
            int i = this.predecessorCount - 1;
            this.predecessorCount = i;
            Graphs.checkNonNegative(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.remove(new NodeConnection.Pred(node));
            }
        }
    }

    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V removeSuccessor(Object obj) {
        Object obj2;
        Object obj3;
        Preconditions.checkNotNull(obj);
        Object obj4 = this.adjacentNodeValues.get(obj);
        if (obj4 == null || obj4 == (obj3 = PRED)) {
            obj2 = null;
        } else if (obj4 instanceof PredAndSucc) {
            this.adjacentNodeValues.put(obj, obj3);
            obj2 = ((PredAndSucc) obj4).successorValue;
        } else {
            this.adjacentNodeValues.remove(obj);
            obj2 = obj4;
        }
        if (obj2 != null) {
            int i = this.successorCount - 1;
            this.successorCount = i;
            Graphs.checkNonNegative(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.remove(new NodeConnection.Succ(obj));
            }
        }
        if (obj2 == null) {
            return null;
        }
        return (V) obj2;
    }

    @Override // com.google.common.graph.GraphConnections
    public void addPredecessor(N node, V unused) {
        boolean addedPredecessor;
        Map<N, Object> map = this.adjacentNodeValues;
        Object obj = PRED;
        Object previousValue = map.put(node, obj);
        if (previousValue == null) {
            addedPredecessor = true;
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, previousValue);
            addedPredecessor = false;
        } else if (previousValue != obj) {
            this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
            addedPredecessor = true;
        } else {
            addedPredecessor = false;
        }
        if (addedPredecessor) {
            int i = this.predecessorCount + 1;
            this.predecessorCount = i;
            Graphs.checkPositive(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.add(new NodeConnection.Pred(node));
            }
        }
    }

    @Override // com.google.common.graph.GraphConnections
    @CheckForNull
    public V addSuccessor(N n, V v) {
        Object obj;
        Object put = this.adjacentNodeValues.put(n, v);
        if (put == null) {
            obj = null;
        } else if (put instanceof PredAndSucc) {
            this.adjacentNodeValues.put(n, new PredAndSucc(v));
            obj = ((PredAndSucc) put).successorValue;
        } else if (put == PRED) {
            this.adjacentNodeValues.put(n, new PredAndSucc(v));
            obj = null;
        } else {
            obj = put;
        }
        if (obj == null) {
            int i = this.successorCount + 1;
            this.successorCount = i;
            Graphs.checkPositive(i);
            List<NodeConnection<N>> list = this.orderedNodeConnections;
            if (list != null) {
                list.add(new NodeConnection.Succ(n));
            }
        }
        if (obj == null) {
            return null;
        }
        return (V) obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPredecessor(@CheckForNull Object value) {
        return value == PRED || (value instanceof PredAndSucc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSuccessor(@CheckForNull Object value) {
        return (value == PRED || value == null) ? false : true;
    }
}
