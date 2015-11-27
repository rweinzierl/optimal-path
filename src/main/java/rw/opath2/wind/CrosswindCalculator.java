package rw.opath2.wind;


/*
 * All angles are in radians
 * 
 * TODO: optimize: allow for static calls with limited functionality
 */
public class CrosswindCalculator {

	private double directionToDestination;
	private double flightSpeed;
	private double windDirection;
	private double windSpeed;

	private boolean onTrack;
	private double correctionAngle;
	private double crosswindComponent;
	private double speedTowardsDestination;
	private double directionOverGround;
	private double speedOverGround;

	public void setDirectionToDestination(double directionToDestination) {
		this.directionToDestination = directionToDestination;
	}

	public void setFlightSpeed(double flightSpeed) {
		this.flightSpeed = flightSpeed;
	}

	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public void calculate() {
		crosswindComponent = Math.sin(windDirection - directionToDestination) * windSpeed;
		if (Math.abs(crosswindComponent) <= flightSpeed) {
			correctionAngle = Math.asin(-crosswindComponent / flightSpeed);
			onTrack = true;
		} else {
			correctionAngle = Math.signum(-crosswindComponent) * Math.PI / 2;
			onTrack = false;
		}
		speedTowardsDestination = Math.cos(correctionAngle) * flightSpeed
				+ Math.cos(windDirection - directionToDestination) * windSpeed;
		if (onTrack) {
			directionOverGround = directionToDestination;
			speedOverGround = speedTowardsDestination;
		} else {
			double y = Math.sin(windDirection) + Math.sin(directionToDestination + correctionAngle);
			double x = Math.cos(windDirection) + Math.cos(directionToDestination + correctionAngle);
			directionOverGround = Math.atan2(y, x);
			speedOverGround = Math.sqrt(x * x + y * y);
		}
		correctionAngle = correctAngleOvershoot(correctionAngle);
		directionOverGround = correctAngleOvershoot(directionOverGround);
	}

	private final double correctAngleOvershoot(double angle) {
		if (angle > Math.PI)
			return Math.PI;
		else if (angle < -Math.PI)
			return -Math.PI;
		else
			return angle;
	}

	public double getCorrectionAngle() {
		return correctionAngle;
	}

	public double getCrosswindComponent() {
		return crosswindComponent;
	}

	public double getSpeedTowardsDestination() {
		return speedTowardsDestination;
	}

	public double getDirectionToDestination() {
		return directionToDestination;
	}

	public double getFlightSpeed() {
		return flightSpeed;
	}

	public double getFlightDirection() {
		return correctionAngle + directionToDestination;
	}

	public double getWindDirection() {
		return windDirection;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public double getDirectionOverGround() {
		return directionOverGround;
	}

	public double getSpeedOverGround() {
		return speedOverGround;
	}

	public boolean isOnTrack() {
		return onTrack;
	}

}
