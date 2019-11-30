package origrammer;

import java.util.ArrayList;

import origrammer.geometry.OriLine;

public class Diagram {
	
	public ArrayList<OriLine> lines = new ArrayList<>();
	
	public double size;
	
	public Diagram(double size) {
		this.size = size;
		
		OriLine l0 = new OriLine(-1,1,1,1, OriLine.TYPE_EDGE);
		OriLine l1 = new OriLine(1,1,1,-1, OriLine.TYPE_EDGE);
		OriLine l2 = new OriLine(1,-1,-1,-1, OriLine.TYPE_EDGE);
		OriLine l3 = new OriLine(-1,-1,-1,1, OriLine.TYPE_EDGE);
		lines.add(l0);
		lines.add(l1);
		lines.add(l2);
		lines.add(l3);
	}

}
