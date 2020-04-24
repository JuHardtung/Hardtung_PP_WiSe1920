package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriArrow;

public class OriArrowProxy {
	
	private Vector2d position;
	private double scale;
	private double degrees;
	private int type;

	
	public OriArrowProxy() {
	}
	
	public OriArrowProxy(OriArrow a) {
		position = a.getPosition();
		scale = a.getScale();
		degrees = a.getDegrees();
		type = a.getType();
	}
	
	public OriArrow getArrow() {
		return new OriArrow(position, scale, degrees, type);
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getDegrees() {
		return degrees;
	}

	public void setDegrees(double degrees) {
		this.degrees = degrees;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "OriArrowProxy [position=" + position + ", scale=" + scale
				+ ", degrees=" + degrees + ", type=" + type + "]";
	}
	
}
