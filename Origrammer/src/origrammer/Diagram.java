package origrammer;

import java.awt.Color;
import java.util.ArrayList;

public class Diagram {
	public String title;
	public String author;
	public String comments;
	public double paperSize;
	public int recPaperWidth;
	public int recPaperHeight;
	public Color faceUpColor;
	public Color faceDownColor;
	public ArrayList<Step> steps = new ArrayList<>();
	public String dataFilePath = "";
	
	public Diagram() {
		this.paperSize = Constants.DEFAULT_PAPER_SIZE;
	}
	
	public Diagram(double paperSize, Color faceUpColor, Color faceDownColor) {
		this.paperSize = paperSize;
		this.faceUpColor = faceUpColor;
		this.faceDownColor = faceDownColor;
	}
	
	public Diagram(double paperSize, Color faceUpColor, Color faceDownColor, Step step) {
		this.paperSize = paperSize;
		this.faceUpColor = faceUpColor;
		this.faceDownColor = faceDownColor;
		steps.add(step);
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public void addStep(Step step) {
		steps.add(step);
	}

	@Override
	public String toString() {
		return "Diagram [title=" + title + ", author=" + author + ", comments=" + comments + ", paperSize=" + paperSize
				+ ", recPaperWidth=" + recPaperWidth + ", recPaperHeight=" + recPaperHeight + ", faceUpColor="
				+ faceUpColor + ", faceDownColor=" + faceDownColor + ", steps=" + steps + ", dataFilePath="
				+ dataFilePath + "]";
	}

}
