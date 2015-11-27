package rw.opath2.core;

public abstract class OneStepTravelTimeCalculatorFactory<TNode> implements TravelTimeCalculatorFactory<TNode> {

	@Override
	public TravelTimeCalculator create(final TNode node1, final TNode node2, final double startTimestamp) {
		return new TravelTimeCalculator() {

			@Override
			public void nextStep() {
			}

			@Override
			public boolean hasArrived() {
				return true;
			}

			@Override
			public double getCurrentTimestamp() {
				return startTimestamp + travelTime(node1, node2, startTimestamp);
			}
		};
	}

	protected abstract double travelTime(TNode node1, TNode node2, double startTimestamp);

}