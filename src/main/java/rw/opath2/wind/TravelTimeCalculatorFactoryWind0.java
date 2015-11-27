package rw.opath2.wind;

import java.io.Closeable;
import java.io.IOException;

import rw.opath2.core.NodeLngLat;
import rw.opath2.core.TravelTimeCalculator;
import rw.opath2.core.TravelTimeCalculatorFactory;
import ucar.unidata.geoloc.Bearing;
import ucar.unidata.geoloc.EarthEllipsoid;

public class TravelTimeCalculatorFactoryWind0<TNode extends NodeLngLat> implements TravelTimeCalculatorFactory<TNode>,
		Closeable {

	private double flightSpeed;
	private double stepSizeSeconds;
	private WindSource windSource;

	public TravelTimeCalculatorFactoryWind0(double flightSpeed, double stepSizeSeconds, WindSource windSource) {
		this.flightSpeed = flightSpeed;
		this.stepSizeSeconds = stepSizeSeconds;
		this.windSource = windSource;
	}

	@Override
	public TravelTimeCalculator create(final TNode node1, final TNode node2, final double startTimestamp) {

		return new TravelTimeCalculator() {

			double timestamp = startTimestamp;
			CrosswindCalculator cc = new CrosswindCalculator();
			double distanceTotal;
			double distanceSofar;
			double[] windUV = new double[2];
			{
				cc.setFlightSpeed(flightSpeed);
				// wind is always taken from the edge start point
				Bearing bearing = new Bearing();
				Bearing.calculateBearing(EarthEllipsoid.WGS84, node1.getLat(), node1.getLng(), node2.getLat(),
						node2.getLng(), bearing);
				cc.setDirectionToDestination(Math.toRadians(bearing.getAngle()));
				distanceTotal = bearing.getDistance() * 1000;
				distanceSofar = 0;
			}

			@Override
			public void nextStep() throws Exception {
				windSource.getUVWind((long) timestamp, node1.getLng(), node1.getLat(), windUV);
				cc.setWindSpeed(Math.sqrt(windUV[0] * windUV[0] + windUV[1] * windUV[1]));
				cc.setWindDirection(Math.atan2(windUV[1], windUV[0]));
				cc.calculate();
				if (cc.isOnTrack()) {
					double distance = cc.getSpeedOverGround() * stepSizeSeconds;
					if (distance >= (distanceTotal - distanceSofar)) {
						timestamp += (stepSizeSeconds * 1000) * (distanceTotal - distanceSofar) / distance;
						distanceSofar = distanceTotal;
					} else {
						distanceSofar += distance;
						timestamp += stepSizeSeconds * 1000;
					}
				} else
					timestamp += stepSizeSeconds * 1000;
			}

			@Override
			public boolean hasArrived() {
				return distanceSofar >= distanceTotal;
			}

			@Override
			public double getCurrentTimestamp() {
				return timestamp;
			}
		};
	}

	public double getArrivalTimestamp(final TNode node1, final TNode node2, final double startTimestamp)
			throws Exception {
		TravelTimeCalculator calc = create(node1, node2, startTimestamp);
		while (!calc.hasArrived())
			calc.nextStep();
		return calc.getCurrentTimestamp();
	}

	@Override
	public void close() throws IOException {
		windSource.close();
	}

}
