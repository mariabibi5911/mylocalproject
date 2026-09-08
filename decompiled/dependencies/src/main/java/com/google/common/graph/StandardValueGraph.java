package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
class StandardValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean allowsSelfLoops;
    long edgeCount;
    private final boolean isDirected;
    final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object obj) {
        return predecessors((StandardValueGraph<N, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object obj) {
        return successors((StandardValueGraph<N, V>) obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StandardValueGraph(AbstractGraphBuilder<? super N> builder) {
        this(builder, builder.nodeOrder.createMap(builder.expectedNodeCount.or((Optional<Integer>) 10).intValue()), 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StandardValueGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder, Map<N, GraphConnections<N, V>> map, long j) {
        MapIteratorCache<N, GraphConnections<N, V>> mapIteratorCache;
        this.isDirected = abstractGraphBuilder.directed;
        this.allowsSelfLoops = abstractGraphBuilder.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) abstractGraphBuilder.nodeOrder.cast();
        if (map instanceof TreeMap) {
            mapIteratorCache = new MapRetrievalCache<>(map);
        } else {
            mapIteratorCache = new MapIteratorCache<>(map);
        }
        this.nodeConnections = mapIteratorCache;
        this.edgeCount = Graphs.checkNonNegative(j);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> adjacentNodes(N node) {
        return checkedConnections(node).adjacentNodes();
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public Set<N> predecessors(N node) {
        return checkedConnections(node).predecessors();
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public Set<N> successors(N node) {
        return checkedConnections(node).successors();
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> incidentEdges(N node) {
        final GraphConnections<N, V> connections = checkedConnections(node);
        return new IncidentEdgeSet<N>(this, this, node) { // from class: com.google.common.graph.StandardValueGraph.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<EndpointPair<N>> iterator() {
                return connections.incidentEdgeIterator(this.node);
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(N nodeU, N nodeV) {
        return hasEdgeConnectingInternal(Preconditions.checkNotNull(nodeU), Preconditions.checkNotNull(nodeV));
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
        Preconditions.checkNotNull(endpoints);
        return isOrderingCompatible(endpoints) && hasEdgeConnectingInternal(endpoints.nodeU(), endpoints.nodeV());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckForNull
    public V edgeValueOrDefault(N n, N n2, @CheckForNull V v) {
        return (V) edgeValueOrDefaultInternal(Preconditions.checkNotNull(n), Preconditions.checkNotNull(n2), v);
    }

    @CheckForNull
    public V edgeValueOrDefault(EndpointPair<N> endpoints, @CheckForNull V defaultValue) {
        validateEndpoints(endpoints);
        return edgeValueOrDefaultInternal(endpoints.nodeU(), endpoints.nodeV(), defaultValue);
    }

    @Override // com.google.common.graph.AbstractBaseGraph
    protected long edgeCount() {
        return this.edgeCount;
    }

    private final GraphConnections<N, V> checkedConnections(N node) {
        GraphConnections<N, V> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            String valueOf = String.valueOf(node);
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 38).append("Node ").append(valueOf).append(" is not an element of this graph.").toString());
        }
        return connections;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean containsNode(@CheckForNull N node) {
        return this.nodeConnections.containsKey(node);
    }

    private final boolean hasEdgeConnectingInternal(N nodeU, N nodeV) {
        GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        return connectionsU != null && connectionsU.successors().contains(nodeV);
    }

    @CheckForNull
    private final V edgeValueOrDefaultInternal(N nodeU, N nodeV, @CheckForNull V defaultValue) {
        GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        V value = connectionsU == null ? null : connectionsU.value(nodeV);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
