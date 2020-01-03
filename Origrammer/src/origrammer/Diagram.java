package origrammer;

import java.awt.Color;
import java.util.ArrayList;


public class Diagram {
	public double paperSize;
	private int recPaperWidth = 0;
	private int recPaperHeight = 0;
	private Color paperColor;
	public ArrayList<Step> steps = new ArrayList<>();
	public String dataFilePath = "";
	
	
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


	public int getPaperWidth() {
		return recPaperWidth;
	}


	public void setPaperWidth(int paperWidth) {
		this.recPaperWidth = paperWidth;
	}


	public int getPaperHeight() {
		return recPaperHeight;
	}


	public void setPaperHeight(int paperHeight) {
		this.recPaperHeight = paperHeight;
	}


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
