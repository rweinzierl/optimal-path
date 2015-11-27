package rw.opath2.core;

public class BreakConditionUtil {

	public static <TNode> BreakCondition<TNode> defaultBreakCondition() {
		return new BreakCondition<TNode>() {
	
			@Override
			public boolean stopAfterArrivalAtNode(TNode start, TNode end, double arrivalTimestamp) {
				return false;
			}
	
			@Override
			public boolean stopBeforeCalculationStep(double currentTimestamp) {
				return false;
			}
		};
	}

}
