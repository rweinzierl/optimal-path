package rw.opath2.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rw.opath2.standard.NodeWithTimestamp;

/**
 * 
 * Reads nodes from a text file in backward order.
 * 
 */
public class PathBackReader<TNode> extends PathReader<TNode> {

	public PathBackReader(File file, NodeExternalizer<TNode> externalizer) throws Exception {
		super(new BackReader(file), externalizer);
	}

	public List<NodeWithTimestamp<TNode>> findPathToTarget(TNode targetNode) throws Exception {
		String nextNodeId = externalizer.getId(targetNode);
		List<NodeWithTimestamp<TNode>> nodes = new ArrayList<NodeWithTimestamp<TNode>>();
		while (nextNodeId != null && readNode())
			if (nextNodeId.equals(currentNodeId)) {
				nodes.add(new NodeWithTimestamp<TNode>(currentNode, timestamp));
				nextNodeId = previousNodeId;
			}
		Collections.reverse(nodes);
		return nodes;
	}

}
