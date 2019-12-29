package origrammer;

import java.awt.Color;
import java.util.ArrayList;


public class Diagram {
	public double paperSize;
	//private double paperWidth;
	//private double paperHeight;
	private Color paperColor;
	public ArrayList<Step> steps = new ArrayList<>();
	
	
	public Diagram() {
		
	}
	
	public Diagram(double paperSize, Color paperColor) {
		this.paperSize = paperSize;
		this.paperColor = paperColor;
	}
	
	public Diagram(double paperSize, Color paperColor, Step step) {
		this.paperSize = paperSize;
		this.paperColor = paperColor;
		steps.add(step);
	}


//	public double getPaperWidth() {
//		return paperWidth;
//	}
//
//
//	public void setPaperWidth(double paperWidth) {
//		this.paperWidth = paperWidth;
//	}
//
//
//	public double getPaperHeight() {
//		return paperHeight;
//	}
//
//
//	public void setPaperHeight(double paperHeight) {
//		this.paperHeight = paperHeight;
//	}


	public Color getPaperColor() {
		return paperColor;
	}


	public void setPaperColor(Color paperColor) {
		this.paperColor = paperColor;
	}


	public void addStep(Step step) {
		steps.add(step);
	}

	@Override
	public String toString() {
		return "Diagram [paperSize=" + paperSize + ", paperColor=" + paperColor + ", steps=" + steps + "]";
	}
	
	

}
