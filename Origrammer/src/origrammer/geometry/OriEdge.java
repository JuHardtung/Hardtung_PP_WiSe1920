package origrammer.geometry;

public class OriEdge {
	
	public OriVertex sv = null;
	public OriVertex ev = null;
	public int type = 0;
	
	public OriEdge() {
		
	}
	
	public OriEdge(OriVertex sv, OriVertex ev, int type) {
		this.type = type;
		this.sv = sv;
		this.ev = ev;
	}
	
	public OriVertex oppositeVertex(OriVertex v) {
		return v == sv ? ev : sv;
	}

}
