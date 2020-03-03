package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriGeomSymbol;

public class OriGeomSymbolProxy {
	
	private Vector2d position;
	private double radius;
	private int type;

	
	public OriGeomSymbolProxy() {

	}
	
	public OriGeomSymbolProxy (OriGeomSymbol g) {
		this.position = g.getPosition();
		this.radius = g.getRadius();
		this.type = g.getType();
	}
	
	public OriGeomSymbol getSymbol() {
		return new OriGeomSymbol(position, radius, type);
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
