package rw.opath2.standard;

import java.util.ArrayList;
import java.util.Collection;

import rw.opath2.core.Topology;

public class RectangularToplology {

	private static final int[] RANGE1 = { -1, 1 };

	private static void addNodesLevel1(NodeIJ node, Collection<NodeIJ> neighbours) {
		for (int di : RANGE1)
			neighbours.add(new NodeIJ(node.i + di, node.j));
		for (int dj : RANGE1)
			neighbours.add(new NodeIJ(node.i, node.j + dj));
	}

	private static void addNodesLevel2(NodeIJ node, Collection<NodeIJ> neighbours) {
		for (int di : RANGE1)
			for (int dj : RANGE1)
				neighbours.add(new NodeIJ(node.i + di, node.j + dj));
	}

	private static final int[] RANGE2 = { -2, 2 };

	private static void addNodesLevel3(NodeIJ node, Collection<NodeIJ> neighbours) {
		for (int di : RANGE2)
			for (int dj : RANGE1) {
				neighbours.add(new NodeIJ(node.i + di, node.j + dj));
				neighbours.add(new NodeIJ(node.i + dj, node.j + di));
			}
	}

	private static final int[] RANGE3 = { -3, 3 };

	private static void addNodesLevel4(NodeIJ node, Collection<NodeIJ> neighbours) {
		for (int di : RANGE3)
			for (int dj : RANGE1) {
				neighbours.add(new NodeIJ(node.i + di, node.j + dj));
				neighbours.add(new NodeIJ(node.i + dj, node.j + di));
			}
	}

	private static void addNodesLevel5(NodeIJ node, Collection<NodeIJ> neighbours) {
		for (int di : RANGE3)
			for (int dj : RANGE2) {
				neighbours.add(new NodeIJ(node.i + di, node.j + dj));
				neighbours.add(new NodeIJ(node.i + dj, node.j + di));
			}
	}

	public static Topology<NodeIJ> TOPLOLOGY_4_NEIGHBOURS = new Topology<NodeIJ>() {
		@Override
		public Collection<NodeIJ> getNeighbours(NodeIJ node) {
			ArrayList<NodeIJ> neighbours = new ArrayList<NodeIJ>(4);
			addNodesLevel1(node, neighbours);
			return neighbours;
		}
	};

	public static Topology<NodeIJ> TOPLOLOGY_8_NEIGHBOURS = new Topology<NodeIJ>() {
		@Override
		public Collection<NodeIJ> getNeighbours(NodeIJ node) {
			ArrayList<NodeIJ> neighbours = new ArrayList<NodeIJ>(8);
			addNodesLevel1(node, neighbours);
			addNodesLevel2(node, neighbours);
			return neighbours;
		}
	};

	public static Topology<NodeIJ> TOPLOLOGY_16_NEIGHBOURS = new Topology<NodeIJ>() {
		@Override
		public Collection<NodeIJ> getNeighbours(NodeIJ node) {
			ArrayList<NodeIJ> neighbours = new ArrayList<NodeIJ>(16);
			addNodesLevel1(node, neighbours);
			addNodesLevel2(node, neighbours);
			addNodesLevel3(node, neighbours);
			return neighbours;
		}
	};

	public static Topology<NodeIJ> TOPLOLOGY_24_NEIGHBOURS = new Topology<NodeIJ>() {
		@Override
		public Collection<NodeIJ> getNeighbours(NodeIJ node) {
			ArrayList<NodeIJ> neighbours = new ArrayList<NodeIJ>(24);
			addNodesLevel1(node, neighbours);
			addNodesLevel2(node, neighbours);
			addNodesLevel3(node, neighbours);
			addNodesLevel4(node, neighbours);
			return neighbours;
		}
	};

	public static Topology<NodeIJ> TOPLOLOGY_32_NEIGHBOURS = new Topology<NodeIJ>() {
		@Override
		public Collection<NodeIJ> getNeighbours(NodeIJ node) {
			ArrayList<NodeIJ> neighbours = new ArrayList<NodeIJ>(32);
			addNodesLevel1(node, neighbours);
			addNodesLevel2(node, neighbours);
			addNodesLevel3(node, neighbours);
			addNodesLevel4(node, neighbours);
			addNodesLevel5(node, neighbours);
			return neighbours;
		}
	};

}
