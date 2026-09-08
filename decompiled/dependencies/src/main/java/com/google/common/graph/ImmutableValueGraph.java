package com.google.common.graph;

import androidx.exifinterface.media.ExifInterface;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

@Immutable(containerOf = {"N", ExifInterface.GPS_MEASUREMENT_INTERRUPTED})
@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class ImmutableValueGraph<N, V> extends StandardValueGraph<N, V> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ Set adjacentNodes(Object obj) {
        return super.adjacentNodes(obj);
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.ValueGraph
    @CheckForNull
    public /* bridge */ /* synthetic */ Object edgeValueOrDefault(EndpointPair endpointPair, @CheckForNull Object obj) {
        return super.edgeValueOrDefault(endpointPair, obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.ValueGraph
    @CheckForNull
    public /* bridge */ /* synthetic */ Object edgeValueOrDefault(Object obj, Object obj2, @CheckForNull Object obj3) {
        return super.edgeValueOrDefault(obj, obj2, obj3);
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(EndpointPair endpointPair) {
        return super.hasEdgeConnecting(endpointPair);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ boolean hasEdgeConnecting(Object obj, Object obj2) {
        return super.hasEdgeConnecting(obj, obj2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public /* bridge */ /* synthetic */ Set incidentEdges(Object obj) {
        return super.incidentEdges(obj);
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ boolean isDirected() {
        return super.isDirected();
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ ElementOrder nodeOrder() {
        return super.nodeOrder();
    }

    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public /* bridge */ /* synthetic */ Set nodes() {
        return super.nodes();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Set predecessors(Object obj) {
        return super.predecessors((ImmutableValueGraph<N, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Set successors(Object obj) {
        return super.successors((ImmutableValueGraph<N, V>) obj);
    }

    private ImmutableValueGraph(ValueGraph<N, V> graph) {
        super(ValueGraphBuilder.from(graph), getNodeConnections(graph), graph.edges().size());
    }

    public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
        if (graph instanceof ImmutableValueGraph) {
            return (ImmutableValueGraph) graph;
        }
        return new ImmutableValueGraph<>(graph);
    }

    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
        return (ImmutableValueGraph) Preconditions.checkNotNull(graph);
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.stable();
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.ValueGraph
    public ImmutableGraph<N> asGraph() {
        return new ImmutableGraph<>(this);
    }

    private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
        ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
        for (N node : graph.nodes()) {
            nodeConnections.put(node, connectionsOf(graph, node));
        }
        return nodeConnections.buildOrThrow();
    }

    private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
        Function<N, V> successorNodeToValueFn = new Function() { // from class: com.google.common.graph.ImmutableValueGraph$$ExternalSyntheticLambda0
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                Object requireNonNull;
                requireNonNull = Objects.requireNonNull(ValueGraph.this.edgeValueOrDefault(node, obj, null));
                return requireNonNull;
            }
        };
        if (graph.isDirected()) {
            return DirectedGraphConnections.ofImmutable(node, graph.incidentEdges(node), successorNodeToValueFn);
        }
        return UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn));
    }

    /* loaded from: classes7.dex */
    public static class Builder<N, V> {
        private final MutableValueGraph<N, V> mutableValueGraph;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(ValueGraphBuilder<N, V> graphBuilder) {
            this.mutableValueGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
        }

        public Builder<N, V> addNode(N node) {
            this.mutableValueGraph.addNode(node);
            return this;
        }

        public Builder<N, V> putEdgeValue(N nodeU, N nodeV, V value) {
            this.mutableValueGraph.putEdgeValue(nodeU, nodeV, value);
            return this;
        }

        public Builder<N, V> putEdgeValue(EndpointPair<N> endpoints, V value) {
            this.mutableValueGraph.putEdgeValue(endpoints, value);
            return this;
        }

        public ImmutableValueGraph<N, V> build() {
            return ImmutableValueGraph.copyOf(this.mutableValueGraph);
        }
    }
}
