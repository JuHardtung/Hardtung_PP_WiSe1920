package origrammer;

import java.awt.Color;

public class DiagramDataSet {
	
	public int recPaperWidth;
	public int recPaperHeight;
	public Color paperColor;
	public StepDataSet[] steps;
	
	
	public DiagramDataSet() {
		
	}
	
	public DiagramDataSet(Diagram d) {
		int stepsNum = d.steps.size();
		steps = new StepDataSet[stepsNum];
		
		for (int i=0; i<stepsNum; i++) {
			steps[i] = new StepDataSet(d.steps.get(i));
		}
	}
	
	public void recover(Diagram d) {
		d.steps.clear();
		for (int i=0; i<steps.length; i++) {
			d.steps.add(steps[i].getStep());
		}
	}

	public int getRecPaperWidth() {
		return recPaperWidth;
	}

	public void setRecPaperWidth(int recPaperWidth) {
		this.recPaperWidth = recPaperWidth;
	}

	public int getRecPaperHeight() {
		return recPaperHeight;
	}

	public void setRecPaperHeight(int recPaperHeight) {
		this.recPaperHeight = recPaperHeight;
	}

	public Color getPaperColor() {
		return paperColor;
	}

	public void setPaperColor(Color paperColor) {
		this.paperColor = paperColor;
	}

	public StepDataSet[] getSteps() {
		return steps;
	}

	public void setSteps(StepDataSet[] steps) {
		this.steps = steps;
	}
	
}
