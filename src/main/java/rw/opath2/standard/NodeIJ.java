package rw.opath2.standard;

import java.util.StringTokenizer;

import rw.opath2.io.NodeExternalizer;

public class NodeIJ {
	public final int i;
	public final int j;

	public NodeIJ(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		NodeIJ node = (NodeIJ) obj;
		return node.i == i && node.j == j;
	}

	@Override
	public String toString() {
		return externalizer.getId(this);
	}

	public static NodeExternalizer<NodeIJ> externalizer = new NodeExternalizer<NodeIJ>() {
		@Override
		public String getId(NodeIJ node) {
			return node.i + "/" + node.j;
		}

		@Override
		public String[] toExternalForm(NodeIJ node) {
			return new String[0];
		}

		@Override
		public NodeIJ fromExternalForm(String id, String[] externalForm) {
			StringTokenizer tok = new StringTokenizer(id, "/", false);
			return new NodeIJ(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()));
		}
	};

}