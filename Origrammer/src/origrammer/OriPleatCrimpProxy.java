package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriPleatCrimpSymbol;

public class OriPleatCrimpProxy {

	private Vector2d position;
	private boolean isSwitchedDir;
	private int layersCount;
	private int type;
	
	public OriPleatCrimpProxy() {
		
	}
	
	public OriPleatCrimpProxy(OriPleatCrimpSymbol s) {
		this.position = s.getPosition();
		this.isSwitchedDir = s.isSwitchedDir();
		this.layersCount = s.getLayersCount();
		this.type = s.getType();
	}
	
	public OriPleatCrimpSymbol getSymbol() {
		return new OriPleatCrimpSymbol(position, isSwitchedDir, layersCount);
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public boolean isSwitchedDir() {
		return isSwitchedDir;
	}

	public void setSwitchedDir(boolean isSwitchedDir) {
		this.isSwitchedDir = isSwitchedDir;
	}

	public int getLayersCount() {
		return layersCount;
	}

	public void setLayersCount(int layersCount) {
		this.layersCount = layersCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
