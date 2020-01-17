package origrammer;

import java.util.Arrays;


public class StepDataSet {
	
	//public ArrayList<OriLine> lines = new ArrayList<OriLine>();
	public OriLineProxy[] lines;
	public String stepDescription;
	public int stepNumber;

	public StepDataSet() {
		
	}
	
	public StepDataSet(Step s) {
		int lineNum = s.lines.size();
		lines = new OriLineProxy[lineNum];
		for (int i=0; i<lineNum; i++) {
			lines[i] = new OriLineProxy(s.lines.get(i));
		}
		stepDescription = s.stepDescription;
		stepNumber = s.stepNumber;
	}
	
	public void recover(Step s) {
		s.lines.clear();
		for (int i=0; i<lines.length; i++) {
			s.lines.add(lines[i].getLine());
		}		
		s.stepDescription = stepDescription;
		s.stepNumber = stepNumber;
	}
	
	public Step getStep() {
		Step tmpStep = new Step();
		
		for (int i=0; i<lines.length; i++) {
			tmpStep.lines.add(lines[i].getLine());
		}
		tmpStep.stepDescription = stepDescription;
		tmpStep.stepNumber = stepNumber;
		
		return tmpStep;
	}

	public OriLineProxy[] getLines() {
		return lines;
	}

	public void setLines(OriLineProxy[] lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		return "StepDataSet [lines=" + Arrays.toString(lines) + ", stepDescription=" + stepDescription + ", stepNumber="
				+ stepNumber + "]";
	}

}
