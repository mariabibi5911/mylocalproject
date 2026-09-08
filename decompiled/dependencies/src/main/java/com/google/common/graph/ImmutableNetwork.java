package com.google.common.graph;

import androidx.exifinterface.media.ExifInterface;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.Immutable;
import java.util.Map;
import java.util.Set;

@Immutable(containerOf = {"N", ExifInterface.LONGITUDE_EAST})
@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public final class ImmutableNetwork<N, E> extends StandardNetwork<N, E> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set adjacentNodes(Object obj) {
        return super.adjacentNodes(obj);
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ boolean allowsParallelEdges() {
        return super.allowsParallelEdges();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ ElementOrder edgeOrder() {
        return super.edgeOrder();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set edges() {
        return super.edges();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set edgesConnecting(Object obj, Object obj2) {
        return super.edgesConnecting(obj, obj2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set inEdges(Object obj) {
        return super.inEdges(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set incidentEdges(Object obj) {
        return super.incidentEdges(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ EndpointPair incidentNodes(Object obj) {
        return super.incidentNodes(obj);
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ boolean isDirected() {
        return super.isDirected();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ ElementOrder nodeOrder() {
        return super.nodeOrder();
    }

    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set nodes() {
        return super.nodes();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.Network
    public /* bridge */ /* synthetic */ Set outEdges(Object obj) {
        return super.outEdges(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Set predecessors(Object obj) {
        return super.predecessors((ImmutableNetwork<N, E>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.StandardNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Set successors(Object obj) {
        return super.successors((ImmutableNetwork<N, E>) obj);
    }

    private ImmutableNetwork(Network<N, E> network) {
        super(NetworkBuilder.from(network), getNodeConnections(network), getEdgeToReferenceNode(network));
    }

    public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> network) {
        if (network instanceof ImmutableNetwork) {
            return (ImmutableNetwork) network;
        }
        return new ImmutableNetwork<>(network);
    }

    @Deprecated
    public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network) {
        return (ImmutableNetwork) Preconditions.checkNotNull(network);
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public ImmutableGraph<N> asGraph() {
        return new ImmutableGraph<>(super.asGraph());
    }

    private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> network) {
        ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
        for (N node : network.nodes()) {
            nodeConnections.put(node, connectionsOf(network, node));
        }
        return nodeConnections.buildOrThrow();
    }

    private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> network) {
        ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
        for (E edge : network.edges()) {
            edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
        }
        return edgeToReferenceNode.buildOrThrow();
    }

    private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> network, N node) {
        if (network.isDirected()) {
            Map<E, N> inEdgeMap = Maps.asMap(network.inEdges(node), sourceNodeFn(network));
            Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), targetNodeFn(network));
            int selfLoopCount = network.edgesConnecting(node, node).size();
            if (network.allowsParallelEdges()) {
                return DirectedMultiNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
            }
            return DirectedNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
        }
        Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
        if (network.allowsParallelEdges()) {
            return UndirectedMultiNetworkConnections.ofImmutable(incidentEdgeMap);
        }
        return UndirectedNetworkConnections.ofImmutable(incidentEdgeMap);
    }

    private static <N, E> Function<E, N> sourceNodeFn(final Network<N, E> network) {
        return new Function() { // from class: com.google.common.graph.ImmutableNetwork$$ExternalSyntheticLambda0
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                Object source;
                source = Network.this.incidentNodes(obj).source();
                return source;
            }
        };
    }

    private static <N, E> Function<E, N> targetNodeFn(final Network<N, E> network) {
        return new Function() { // from class: com.google.common.graph.ImmutableNetwork$$ExternalSyntheticLambda1
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                Object target;
                target = Network.this.incidentNodes(obj).target();
                return target;
            }
        };
    }

    private static <N, E> Function<E, N> adjacentNodeFn(final Network<N, E> network, final N node) {
        return new Function() { // from class: com.google.common.graph.ImmutableNetwork$$ExternalSyntheticLambda2
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                Object adjacentNode;
                adjacentNode = Network.this.incidentNodes(obj).adjacentNode(node);
                return adjacentNode;
            }
        };
    }

    /* loaded from: classes7.dex */
    public static class Builder<N, E> {
        private final MutableNetwork<N, E> mutableNetwork;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(NetworkBuilder<N, E> networkBuilder) {
            this.mutableNetwork = (MutableNetwork<N, E>) networkBuilder.build();
        }

        public Builder<N, E> addNode(N node) {
            this.mutableNetwork.addNode(node);
            return this;
        }

        public Builder<N, E> addEdge(N nodeU, N nodeV, E edge) {
            this.mutableNetwork.addEdge(nodeU, nodeV, edge);
            return this;
        }

        public Builder<N, E> addEdge(EndpointPair<N> endpoints, E edge) {
            this.mutableNetwork.addEdge(endpoints, edge);
            return this;
        }

        public ImmutableNetwork<N, E> build() {
            return ImmutableNetwork.copyOf(this.mutableNetwork);
        }
    }
}
