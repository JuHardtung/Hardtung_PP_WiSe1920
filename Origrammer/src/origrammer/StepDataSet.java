package origrammer;

import java.util.ArrayList;

import origrammer.geometry.OriLine;

public class StepDataSet {
	
	public ArrayList<OriLine> lines = new ArrayList<OriLine>();

	public StepDataSet() {
		
	}
	
	public StepDataSet(Step s) {
		int lineNum = s.lines.size();
		for (int i=0; i<lineNum; i++) {
			lines.add(new OriLine(s.lines.get(i)));
		}
	}
	
	public Step getStep() {
		Step tmpStep = new Step();
		tmpStep.lines = lines;
		
		return tmpStep;
	}

	public ArrayList<OriLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<OriLine> lines) {
		this.lines = lines;
	}

}
