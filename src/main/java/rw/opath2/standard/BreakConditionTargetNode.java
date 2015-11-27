package rw.opath2.standard;

import rw.opath2.core.BreakCondition;

public class BreakConditionTargetNode<TNode> implements BreakCondition<TNode> {

	private TNode targetNode;

	public BreakConditionTargetNode(TNode targetNode) {
		this.targetNode = targetNode;
	}

	@Override
	public boolean stopBeforeCalculationStep(double currentTimestamp) {
		return false;
	}

	@Override
	public boolean stopAfterArrivalAtNode(TNode start, TNode end, double arrivalTimestamp) {
		return targetNode.equals(end);
	}

}
