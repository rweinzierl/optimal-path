package rw.opath2.core;

public interface TravelTimeCalculatorFactory<TNode> {

	TravelTimeCalculator create(TNode node1, TNode node2, double startTimestamp);

}
