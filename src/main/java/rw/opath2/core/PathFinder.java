package rw.opath2.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

class Edge<TNode> {
	TNode startNode;
	TNode endNode;
	double startTimestamp;
	TravelTimeCalculator travelTimeCalculator;
	int index;
}

public class PathFinder<TNode> {

	private Topology<TNode> topology;
	private TravelTimeCalculatorFactory<TNode> travelTimeCalculatorFactory;
	private EdgeCallback<TNode> edgeCallback;
	private BreakCondition<TNode> breakCondition;
	private boolean stopped = false;

	private int edgeCount = 0;

	public PathFinder(Topology<TNode> topology, TravelTimeCalculatorFactory<TNode> travelTimeCalculatorFactory,
			EdgeCallback<TNode> edgeCallback, BreakCondition<TNode> breakCondition) {
		this.topology = topology;
		this.travelTimeCalculatorFactory = travelTimeCalculatorFactory;
		this.edgeCallback = edgeCallback;
		this.breakCondition = breakCondition;
	}

	public PathFinder(Topology<TNode> topology, TravelTimeCalculatorFactory<TNode> travelTimeCalculatorFactory,
			EdgeCallback<TNode> edgeCallback) {
		this(topology, travelTimeCalculatorFactory, edgeCallback, new BreakCondition<TNode>() {

			@Override
			public boolean stopAfterArrivalAtNode(TNode start, TNode end, double arrivalTimestamp) {
				return true;
			}

			@Override
			public boolean stopBeforeCalculationStep(double currentTimestamp) {
				return true;
			}
		});
	}

	private Map<TNode, List<Edge<TNode>>> edgesByEndNode = new HashMap<TNode, List<Edge<TNode>>>();
	private PriorityQueue<Edge<TNode>> edgeByCurrentTimestamp = new PriorityQueue<Edge<TNode>>(11,
			new Comparator<Edge<TNode>>() {
				@Override
				public int compare(Edge<TNode> edge1, Edge<TNode> edge2) {
					double ts1 = edge1.travelTimeCalculator.getCurrentTimestamp();
					double ts2 = edge2.travelTimeCalculator.getCurrentTimestamp();
					if (ts1 != ts2)
						return Double.compare(ts1, ts2);
					else
						return edge1.index - edge2.index;
				}
			});

	public void init(TNode startNode, double startTimestamp) throws Exception {
		addConfirmedNode(startNode, startTimestamp);
		edgeCallback.edge(null, startNode, startTimestamp);
	}

	public boolean nextNode() throws Exception {
		Edge<TNode> edge = runUntilFirstEdgeArrives();
		if (edge != null) {
			double arrivalTimestamp = edge.travelTimeCalculator.getCurrentTimestamp();
			edgeCallback.edge(edge.startNode, edge.endNode, arrivalTimestamp);
			addConfirmedNode(edge.endNode, arrivalTimestamp);
			stopped = breakCondition.stopAfterArrivalAtNode(edge.startNode, edge.endNode, arrivalTimestamp);
			return true;
		} else
			return false;
	}

	public void run() throws Exception {
		while (nextNode())
			;
	}

	private Edge<TNode> runUntilFirstEdgeArrives() throws Exception {
		if (!edgeByCurrentTimestamp.isEmpty())
			while (!stopped) {
				Edge<TNode> edge = edgeByCurrentTimestamp.peek();
				if (edge.travelTimeCalculator.hasArrived())
					return edge;
				else {
					stopped = breakCondition.stopBeforeCalculationStep(edge.travelTimeCalculator.getCurrentTimestamp());
					if (!stopped) {
						edgeByCurrentTimestamp.remove(edge);
						edge.travelTimeCalculator.nextStep();
						edgeByCurrentTimestamp.add(edge);
					}
				}
			}
		return null;
	}

	private void addConfirmedNode(TNode node, double arrivalTimestamp) {
		Set<TNode> sourceNodes = new HashSet<TNode>();
		List<Edge<TNode>> sourceEdges = edgesByEndNode.remove(node);
		if (sourceEdges != null)
			for (Edge<TNode> sourceEdge : sourceEdges) {
				sourceNodes.add(sourceEdge.startNode);
				edgeByCurrentTimestamp.remove(sourceEdge);
			}
		Collection<TNode> neighbours = topology.getNeighbours(node);
		for (TNode neighbour : neighbours)
			if (!sourceNodes.contains(neighbour)) {
				Edge<TNode> edge = new Edge<TNode>();
				edge.startNode = node;
				edge.endNode = neighbour;
				edge.startTimestamp = arrivalTimestamp;
				edge.index = edgeCount++;
				edge.travelTimeCalculator = travelTimeCalculatorFactory.create(edge.startNode, edge.endNode,
						edge.startTimestamp);
				edgeByCurrentTimestamp.add(edge);
				List<Edge<TNode>> edgesWithSameEndpoint = edgesByEndNode.get(neighbour);
				if (edgesWithSameEndpoint == null) {
					edgesWithSameEndpoint = new ArrayList<Edge<TNode>>();
					edgesByEndNode.put(neighbour, edgesWithSameEndpoint);
				}
				edgesWithSameEndpoint.add(edge);
			}
	}

}
