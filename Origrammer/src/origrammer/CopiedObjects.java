package origrammer;

import java.util.ArrayList;

import origrammer.geometry.OriArrow;
import origrammer.geometry.OriEqualAnglSymbol;
import origrammer.geometry.OriEqualDistSymbol;
import origrammer.geometry.OriFace;
import origrammer.geometry.OriGeomSymbol;
import origrammer.geometry.OriLeaderBox;
import origrammer.geometry.OriLine;
import origrammer.geometry.OriPicSymbol;
import origrammer.geometry.OriPleatCrimpSymbol;
import origrammer.geometry.OriVertex;

public class CopiedObjects {

	public ArrayList<OriLine> lines = new ArrayList<>();
	public ArrayList<OriVertex> vertices = new ArrayList<>();
	public ArrayList<OriArrow> arrows = new ArrayList<>();
	public ArrayList<OriFace> filledFaces = new ArrayList<>();
	public ArrayList<OriLeaderBox> leaderBoxSymbols = new ArrayList<>();
	public ArrayList<OriPicSymbol> picSymbols = new ArrayList<>();
	public ArrayList<OriGeomSymbol> geomSymbols = new ArrayList<>();
	public ArrayList<OriEqualDistSymbol> equalDistSymbols = new ArrayList<>();
	public ArrayList<OriEqualAnglSymbol> equalAnglSymbols = new ArrayList<>();
	public ArrayList<OriPleatCrimpSymbol> pleatCrimpSymbols = new ArrayList<>();
	
	
	public CopiedObjects() {
		
	}
	
	public void clear() {
		lines.clear();
		vertices.clear();
		arrows.clear();
		filledFaces.clear();
		leaderBoxSymbols.clear();
		picSymbols.clear();
		geomSymbols.clear();
		equalDistSymbols.clear();
		equalAnglSymbols.clear();
		pleatCrimpSymbols.clear();
	}
	
	public int getCopiedObjectsCount() {
		int count = 0;
		count += lines.size();
		count += vertices.size();
		count += arrows.size();
		count += filledFaces.size();
		count += leaderBoxSymbols.size();
		count += picSymbols.size();
		count += geomSymbols.size();
		count += equalDistSymbols.size();
		count += equalAnglSymbols.size();
		count += pleatCrimpSymbols.size();

		return count;
	}
	
	
	@Override
	public String toString() {
		return "CopiedObjects [lines=" + lines + ", vertices=" + vertices + ", arrows=" + arrows + ", filledFaces="
				+ filledFaces + ", leaderBoxSymbols=" + leaderBoxSymbols + ", picSymbols=" + picSymbols
				+ ", geomSymbols=" + geomSymbols + ", equalDistSymbols=" + equalDistSymbols + ", equalAnglSymbols="
				+ equalAnglSymbols + ", pleatCrimpSymbols=" + pleatCrimpSymbols + "]";
	}
}
