package rw.opath2.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import ucar.unidata.geoloc.Bearing;
import ucar.unidata.geoloc.EarthEllipsoid;

public class NodeUtil {

	public static <TNode> Set<TNode> getAllNodes(Topology<TNode> topology, TNode startNode) {
		Set<TNode> nodes = new LinkedHashSet<>();
		nodes.add(startNode);
		while (true) {
			Set<TNode> neighbours = new HashSet<>();
			for (TNode node : nodes)
				for (TNode neighbour : topology.getNeighbours(node))
					if (!nodes.contains(neighbour))
						neighbours.add(neighbour);
			if (neighbours.isEmpty())
				break;
			else
				nodes.addAll(neighbours);
		}
		return nodes;
	}

	public static <TNode> Set<TNode> getSecondOrderNeighbours(Topology<TNode> topology, TNode node) {
		Set<TNode> excludes = new HashSet<>(topology.getNeighbours(node));
		excludes.add(node);
		Set<TNode> retval = new LinkedHashSet<>();
		for (TNode node0 : topology.getNeighbours(node))
			retval.addAll(topology.getNeighbours(node0));
		retval.removeAll(excludes);
		return retval;
	}

	public static <TNode extends NodeLngLat> TNode findClosestNode(double lng, double lat, Topology<TNode> topology,
			TNode startNode) {
		Collection<TNode> nodes = getAllNodes(topology, startNode);
		return findClosestNode(lng, lat, nodes);
	}

	public static <TNode extends NodeLngLat> TNode findClosestNode(double lng, double lat, Collection<TNode> nodes) {
		Bearing bearing = new Bearing();
		TNode currentNode = null;
		double currentDistance = Double.POSITIVE_INFINITY;
		for (TNode node : nodes) {
			double distance = getDistance(bearing, lng, lat, node.getLng(), node.getLat());
			if (distance < currentDistance) {
				currentNode = node;
				currentDistance = distance;
			}

		}
		return currentNode;
	}

	private static double getDistance(Bearing bearing, double lng0, double lat0, double lng1, double lat1) {
		try {
			Bearing.calculateBearing(EarthEllipsoid.WGS84, lat0, lng0, lat1, lng1, bearing);
		} catch (IllegalArgumentException e) {
			// gets "Too many iterations" when points are far apart
			return Double.POSITIVE_INFINITY;
		}
		return bearing.getDistance() * 1000;
	}
}
