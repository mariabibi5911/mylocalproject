package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
/* loaded from: classes7.dex */
public abstract class AbstractNetwork<N, E> implements Network<N, E> {
    @Override // com.google.common.graph.PredecessorsFunction
    public /* bridge */ /* synthetic */ Iterable predecessors(Object obj) {
        Iterable predecessors;
        predecessors = predecessors((AbstractNetwork<N, E>) ((Network) obj));
        return predecessors;
    }

    @Override // com.google.common.graph.SuccessorsFunction
    public /* bridge */ /* synthetic */ Iterable successors(Object obj) {
        Iterable successors;
        successors = successors((AbstractNetwork<N, E>) ((Network) obj));
        return successors;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.graph.AbstractNetwork$1, reason: invalid class name */
    /* loaded from: classes7.dex */
    public class AnonymousClass1 extends AbstractGraph<N> {
        AnonymousClass1() {
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public /* bridge */ /* synthetic */ Iterable predecessors(Object obj) {
            return predecessors((AnonymousClass1) obj);
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public /* bridge */ /* synthetic */ Iterable successors(Object obj) {
            return successors((AnonymousClass1) obj);
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public Set<N> nodes() {
            return AbstractNetwork.this.nodes();
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public Set<EndpointPair<N>> edges() {
            if (AbstractNetwork.this.allowsParallelEdges()) {
                return super.edges();
            }
            return new AbstractSet<EndpointPair<N>>() { // from class: com.google.common.graph.AbstractNetwork.1.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                public Iterator<EndpointPair<N>> iterator() {
                    return Iterators.transform(AbstractNetwork.this.edges().iterator(), new Function<E, EndpointPair<N>>() { // from class: com.google.common.graph.AbstractNetwork.1.1.1
                        @Override // com.google.common.base.Function
                        public /* bridge */ /* synthetic */ Object apply(Object obj) {
                            return apply((C00371) obj);
                        }

                        @Override // com.google.common.base.Function
                        public EndpointPair<N> apply(E edge) {
                            return AbstractNetwork.this.incidentNodes(edge);
                        }
                    });
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return AbstractNetwork.this.edges().size();
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(@CheckForNull Object obj) {
                    if (!(obj instanceof EndpointPair)) {
                        return false;
                    }
                    EndpointPair<?> endpointPair = (EndpointPair) obj;
                    return AnonymousClass1.this.isOrderingCompatible(endpointPair) && AnonymousClass1.this.nodes().contains(endpointPair.nodeU()) && AnonymousClass1.this.successors((AnonymousClass1) endpointPair.nodeU()).contains(endpointPair.nodeV());
                }
            };
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public ElementOrder<N> nodeOrder() {
            return AbstractNetwork.this.nodeOrder();
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public ElementOrder<N> incidentEdgeOrder() {
            return ElementOrder.unordered();
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public boolean isDirected() {
            return AbstractNetwork.this.isDirected();
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public boolean allowsSelfLoops() {
            return AbstractNetwork.this.allowsSelfLoops();
        }

        @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
        public Set<N> adjacentNodes(N node) {
            return AbstractNetwork.this.adjacentNodes(node);
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.PredecessorsFunction
        public Set<N> predecessors(N node) {
            return AbstractNetwork.this.predecessors((Object) node);
        }

        @Override // com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N node) {
            return AbstractNetwork.this.successors((Object) node);
        }
    }

    @Override // com.google.common.graph.Network
    public Graph<N> asGraph() {
        return new AnonymousClass1();
    }

    @Override // com.google.common.graph.Network
    public int degree(N node) {
        if (isDirected()) {
            return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
        }
        return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
    }

    @Override // com.google.common.graph.Network
    public int inDegree(N node) {
        return isDirected() ? inEdges(node).size() : degree(node);
    }

    @Override // com.google.common.graph.Network
    public int outDegree(N node) {
        return isDirected() ? outEdges(node).size() : degree(node);
    }

    @Override // com.google.common.graph.Network
    public Set<E> adjacentEdges(E edge) {
        EndpointPair<N> endpointPair = incidentNodes(edge);
        Set<E> endpointPairIncidentEdges = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
        return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of((Object) edge));
    }

    @Override // com.google.common.graph.Network
    public Set<E> edgesConnecting(N nodeU, N nodeV) {
        Set<E> outEdgesU = outEdges(nodeU);
        Set<E> inEdgesV = inEdges(nodeV);
        if (outEdgesU.size() <= inEdgesV.size()) {
            return Collections.unmodifiableSet(Sets.filter(outEdgesU, connectedPredicate(nodeU, nodeV)));
        }
        return Collections.unmodifiableSet(Sets.filter(inEdgesV, connectedPredicate(nodeV, nodeU)));
    }

    @Override // com.google.common.graph.Network
    public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
        validateEndpoints(endpoints);
        return edgesConnecting(endpoints.nodeU(), endpoints.nodeV());
    }

    private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
        return new Predicate<E>() { // from class: com.google.common.graph.AbstractNetwork.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.base.Predicate
            public boolean apply(E e) {
                return AbstractNetwork.this.incidentNodes(e).adjacentNode(nodePresent).equals(nodeToCheck);
            }
        };
    }

    @Override // com.google.common.graph.Network
    @CheckForNull
    public E edgeConnectingOrNull(N nodeU, N nodeV) {
        Set<E> edgesConnecting = edgesConnecting(nodeU, nodeV);
        switch (edgesConnecting.size()) {
            case 0:
                return null;
            case 1:
                return edgesConnecting.iterator().next();
            default:
                throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", nodeU, nodeV));
        }
    }

    @Override // com.google.common.graph.Network
    @CheckForNull
    public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
        validateEndpoints(endpoints);
        return edgeConnectingOrNull(endpoints.nodeU(), endpoints.nodeV());
    }

    @Override // com.google.common.graph.Network
    public boolean hasEdgeConnecting(N nodeU, N nodeV) {
        Preconditions.checkNotNull(nodeU);
        Preconditions.checkNotNull(nodeV);
        return nodes().contains(nodeU) && successors((Object) nodeU).contains(nodeV);
    }

    @Override // com.google.common.graph.Network
    public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
        Preconditions.checkNotNull(endpoints);
        if (!isOrderingCompatible(endpoints)) {
            return false;
        }
        return hasEdgeConnecting(endpoints.nodeU(), endpoints.nodeV());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void validateEndpoints(EndpointPair<?> endpoints) {
        Preconditions.checkNotNull(endpoints);
        Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
    }

    protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
        return endpoints.isOrdered() || !isDirected();
    }

    @Override // com.google.common.graph.Network
    public final boolean equals(@CheckForNull Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Network)) {
            return false;
        }
        Network<?, ?> other = (Network) obj;
        return isDirected() == other.isDirected() && nodes().equals(other.nodes()) && edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other));
    }

    @Override // com.google.common.graph.Network
    public final int hashCode() {
        return edgeIncidentNodesMap(this).hashCode();
    }

    public String toString() {
        boolean isDirected = isDirected();
        boolean allowsParallelEdges = allowsParallelEdges();
        boolean allowsSelfLoops = allowsSelfLoops();
        String valueOf = String.valueOf(nodes());
        String valueOf2 = String.valueOf(edgeIncidentNodesMap(this));
        return new StringBuilder(String.valueOf(valueOf).length() + 87 + String.valueOf(valueOf2).length()).append("isDirected: ").append(isDirected).append(", allowsParallelEdges: ").append(allowsParallelEdges).append(", allowsSelfLoops: ").append(allowsSelfLoops).append(", nodes: ").append(valueOf).append(", edges: ").append(valueOf2).toString();
    }

    private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(final Network<N, E> network) {
        Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>() { // from class: com.google.common.graph.AbstractNetwork.3
            @Override // com.google.common.base.Function
            public /* bridge */ /* synthetic */ Object apply(Object obj) {
                return apply((AnonymousClass3) obj);
            }

            @Override // com.google.common.base.Function
            public EndpointPair<N> apply(E edge) {
                return Network.this.incidentNodes(edge);
            }
        };
        return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
    }
}
