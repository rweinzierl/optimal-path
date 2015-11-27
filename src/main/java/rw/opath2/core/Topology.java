package rw.opath2.core;

import java.util.Collection;

public interface Topology<TNode> {

	Collection<TNode> getNeighbours(TNode node);

}