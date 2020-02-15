package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriEqualAnglSymbol;

public class OriEqualAnglProxy {
	
	private Vector2d v;
	private Vector2d a;
	private Vector2d b;
	private int dividerCount;
	
	public OriEqualAnglProxy() {
		
	}
	
	public OriEqualAnglProxy(OriEqualAnglSymbol s) {
		this.v = s.getV();
		this.a = s.getA();
		this.b = s.getB();
		this.dividerCount = s.getDividerCount();
	}
	
	public OriEqualAnglSymbol getSymbol() {
		return new OriEqualAnglSymbol(v, a, b, dividerCount);
	}

	public Vector2d getV() {
		return v;
	}

	public void setV(Vector2d v) {
		this.v = v;
	}

	public Vector2d getA() {
		return a;
	}

	public void setA(Vector2d a) {
		this.a = a;
	}

	public Vector2d getB() {
		return b;
	}

	public void setB(Vector2d b) {
		this.b = b;
	}

	public int getDividerCount() {
		return dividerCount;
	}

	public void setDividerCount(int dividerCount) {
		this.dividerCount = dividerCount;
	}
}
