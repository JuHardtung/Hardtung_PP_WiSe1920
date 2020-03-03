package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriPicSymbol;

public class OriPicSymbolProxy {
	
	private Vector2d position;
	private int width;
	private int height;
	private double scale;
	private double degrees;
	private int type;
	
	public OriPicSymbolProxy() {
		
	}
	
	public OriPicSymbolProxy(OriPicSymbol s) {
		this.position = s.getPosition();
		this.width = s.getWidth();
		this.height = s.getHeight();
		this.scale = s.getScale();
		this.degrees = s.getDegrees();
		this.type = s.getType();
	}
	
	public OriPicSymbol getSymbol() {
		return new OriPicSymbol(position, width, height, scale, degrees, type);
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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
	
}
