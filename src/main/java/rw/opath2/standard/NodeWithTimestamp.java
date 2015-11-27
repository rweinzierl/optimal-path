package rw.opath2.standard;

import java.util.Date;

public class NodeWithTimestamp<TNode> {

	public TNode node;
	public double timestamp;

	public NodeWithTimestamp(TNode node, double timestamp) {
		this.node = node;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return node + " " + new Date((long) timestamp);
	}

}
