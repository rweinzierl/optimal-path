package rw.opath2.core;

/**
 * 
 * Calculates travel time for an edge.
 * Calculation does not have to complete in 1 step. This allows the path finder to postpone completing the time calculation for an
 * edge if it turns out to take longer than others. 
 *
 */
public interface TravelTimeCalculator {

	void nextStep() throws Exception;

	double getCurrentTimestamp();

	boolean hasArrived();

}
