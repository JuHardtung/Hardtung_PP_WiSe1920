package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriVertex;

public class OriVertexProxy {

	private Vector2d p;
	
	
	public OriVertexProxy() {
	}
	
	public OriVertexProxy(OriVertex v) {
		this.p = v.getP();
	}
	
	public OriVertex getVertex() {
		return new OriVertex(p);
	}

	public Vector2d getP() {
		return p;
	}

	public void setP(Vector2d p) {
		this.p = p;
	}
	
}
