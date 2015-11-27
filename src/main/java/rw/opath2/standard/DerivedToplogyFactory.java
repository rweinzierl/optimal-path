package rw.opath2.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rw.opath2.core.Topology;

public class DerivedToplogyFactory {

	public static <TNode> Topology<TNode> constrainTopology(final Topology<TNode> topology,
			final NodeFilter<TNode> constraint) {
		return new Topology<TNode>() {
			public Collection<TNode> getNeighbours(TNode node) {
				ArrayList<TNode> neighbours = new ArrayList<TNode>();
				for (TNode neighbour : topology.getNeighbours(node))
					if (constraint.isValid(neighbour))
						neighbours.add(neighbour);
				return neighbours;
			};
		};
	}

	public static <TNode> Topology<TNode> to2ndOrder(final Topology<TNode> topology) {
		return new Topology<TNode>() {
			public Collection<TNode> getNeighbours(TNode node) {
				Set<TNode> neighbours = new HashSet<TNode>();
				for (TNode neighbour1 : topology.getNeighbours(node)) {
					neighbours.add(neighbour1);
					for (TNode neighbour2 : topology.getNeighbours(neighbour1))
						if (!neighbour2.equals(node))
							neighbours.add(neighbour2);
				}
				return neighbours;
			};
		};
	}

}
