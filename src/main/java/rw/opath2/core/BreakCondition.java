package rw.opath2.core;

public interface BreakCondition<TNode> {

	public boolean stopBeforeCalculationStep(double currentTimestamp);

	public boolean stopAfterArrivalAtNode(TNode start, TNode end, double arrivalTimestamp);
}
