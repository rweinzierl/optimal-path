package rw.opath2.io;


public interface NodeWriterCsv<TNode> {

	public String getId(TNode node);

	public String[] getHeaders();

	public String[] getValues(TNode node);

}
