package origrammer;

import java.awt.Color;

public class DiagramDataSet {
	
	public String title;
	public String author;
	public String comments;
	public int recPaperWidth;
	public int recPaperHeight;
	public Color faceUpColor;
	public Color faceDownColor;
	public StepDataSet[] steps;
	
	public DiagramDataSet() {
		
	}
	
	public DiagramDataSet(Diagram d) {
		int stepsNum = d.steps.size();
		steps = new StepDataSet[stepsNum];
		
		for (int i=0; i<stepsNum; i++) {
			steps[i] = new StepDataSet(d.steps.get(i));
		}
		
		title = d.title;
		author = d.author;
		comments = d.comments;
		recPaperWidth = d.recPaperWidth;
		recPaperHeight = d.recPaperHeight;
		faceUpColor = d.faceUpColor;
		faceDownColor = d.faceDownColor;
		
	}
	
	public void recover(Diagram d) {
		d.steps.clear();
		
		for (int i=0; i<steps.length; i++) {
			d.steps.add(steps[i].getStep());
		}		
		
		d.recPaperWidth = recPaperWidth;
		d.recPaperHeight = recPaperHeight;
		d.faceUpColor = faceUpColor;
		d.faceDownColor = faceDownColor;
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

	public Color getFaceUpColor() {
		return faceUpColor;
	}

	public void setFaceUpColor(Color faceUpColor) {
		this.faceUpColor = faceUpColor;
	}

	public Color getFaceDownColor() {
		return faceDownColor;
	}

	public void setFaceDownColor(Color faceDownColor) {
		this.faceDownColor = faceDownColor;
	}

	public StepDataSet[] getSteps() {
		return steps;
	}

	public void setSteps(StepDataSet[] steps) {
		this.steps = steps;
	}
	
}
