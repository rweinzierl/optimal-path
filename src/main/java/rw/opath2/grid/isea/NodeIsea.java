package rw.opath2.grid.isea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import rw.opath2.core.NodeLngLat;
import rw.opath2.core.NodeUtil;
import rw.opath2.core.Topology;
import rw.opath2.io.NodeExternalizer;
import rw.opath2.io.NodeWriterCsv;

public class NodeIsea implements NodeLngLat {

	public static Topology<NodeIsea> TOPOLOGY = new Topology<NodeIsea>() {
		@Override
		public Collection<NodeIsea> getNeighbours(NodeIsea node) {
			return node.neighbours;
		}
	};

	public static void addSecondOrderNeighbours(List<NodeIsea> nodes) {
		List<Collection<NodeIsea>> newNeighbours = new ArrayList<>();
		for (int i = 0; i < nodes.size(); i++)
			newNeighbours.add(NodeUtil.getSecondOrderNeighbours(TOPOLOGY, nodes.get(i)));
		for (int i = 0; i < nodes.size(); i++)
			nodes.get(i).neighbours.addAll(newNeighbours.get(i));
	}

	public static NodeExternalizer<NodeIsea> EXTERNALIZER = new NodeExternalizer<NodeIsea>() {
		@Override
		public String getId(NodeIsea node) {
			return node.number + "/" + node.lng + "/" + node.lat;
		}

		@Override
		public String[] toExternalForm(NodeIsea node) {
			return new String[0];
		}

		@Override
		public NodeIsea fromExternalForm(String id, String[] externalForm) {
			StringTokenizer tok = new StringTokenizer(id, "/", false);
			NodeIsea node = new NodeIsea();
			node.number = Long.parseLong(tok.nextToken());
			node.lng = Double.parseDouble(tok.nextToken());
			node.lat = Double.parseDouble(tok.nextToken());
			return node;
		}
	};

	public static final NodeWriterCsv<NodeIsea> CSV_WRITER = new NodeWriterCsv<NodeIsea>() {

		@Override
		public String getId(NodeIsea node) {
			return node.number + "";
		}

		@Override
		public String[] getHeaders() {
			return new String[] { "lon", "lat" };
		}

		@Override
		public String[] getValues(NodeIsea node) {
			return new String[] { node.lng + "", node.lat + "" };
		}
	};
	public long number;
	public double lng;
	public double lat;
	public final List<NodeIsea> neighbours = new ArrayList<>();

	@Override
	public double getLng() {
		return lng;
	}

	@Override
	public double getLat() {
		return lat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (number ^ (number >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeIsea other = (NodeIsea) obj;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return number + "/" + lng + "/" + lat;
	}

}
