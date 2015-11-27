package rw.opath2.core;

public interface EdgeCallback<TNode> {
	
	public void edge(TNode start, TNode end, double arrivalTimestamp) throws Exception;
	
}
