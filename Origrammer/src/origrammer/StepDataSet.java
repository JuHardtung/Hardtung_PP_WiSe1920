package origrammer;

import java.util.Arrays;


public class StepDataSet {
	
	//public ArrayList<OriLine> lines = new ArrayList<OriLine>();
	public OriLineProxy[] lines;
	public OriArrowProxy[] arrows;
	public OriFaceProxy[] faces;
	public String stepDescription;
	public int stepNumber;

	public StepDataSet() {
		
	}
	
	public StepDataSet(Step s) {
		int lineCount = s.lines.size();
		lines = new OriLineProxy[lineCount];
		for (int i=0; i<lineCount; i++) {
			lines[i] = new OriLineProxy(s.lines.get(i));
		}
		
		int arrowCount = s.arrows.size();
		arrows = new OriArrowProxy[arrowCount];
		for (int i=0; i<arrowCount; i++) {
			arrows[i] = new OriArrowProxy(s.arrows.get(i));
		}
		
		int faceCount = s.filledFaces.size();
		faces = new OriFaceProxy[faceCount];
		for (int i=0; i<faceCount; i++) {
			faces[i] = new OriFaceProxy(s.filledFaces.get(i));
		}
		stepDescription = s.stepDescription;
		stepNumber = s.stepNumber;
	}
	
	public void recover(Step s) {
		s.lines.clear();
		for (int i=0; i<lines.length; i++) {
			s.lines.add(lines[i].getLine());
		}
		s.arrows.clear();
		for (int i=0; i<arrows.length; i++) {
			s.arrows.add(arrows[i].getArrow());
		}
		s.filledFaces.clear();
		for (int i=0; i<faces.length; i++) {
			s.filledFaces.add(faces[i].getFace());
		}
		s.stepDescription = stepDescription;
		s.stepNumber = stepNumber;
	}
	
	public Step getStep() {
		Step tmpStep = new Step();
		
		for (int i=0; i<lines.length; i++) {
			tmpStep.lines.add(lines[i].getLine());
		}
		for (int i=0; i<arrows.length; i++) {
			tmpStep.arrows.add(arrows[i].getArrow());
		}
		for (int i=0; i<faces.length; i++) {
			tmpStep.filledFaces.add(faces[i].getFace());
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

	public OriArrowProxy[] getArrows() {
		return arrows;
	}

	public void setArrows(OriArrowProxy[] arrows) {
		this.arrows = arrows;
	}

	@Override
	public String toString() {
		return "StepDataSet [lines=" + Arrays.toString(lines) + ", stepDescription=" + stepDescription + ", stepNumber="
				+ stepNumber + "]";
	}

}
