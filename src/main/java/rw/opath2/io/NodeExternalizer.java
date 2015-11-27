package rw.opath2.io;

public interface NodeExternalizer<TNode> {

	public String getId(TNode node);
	
	public String[] toExternalForm(TNode node);
	
	public TNode fromExternalForm(String id, String[] externalForm);
	
}
