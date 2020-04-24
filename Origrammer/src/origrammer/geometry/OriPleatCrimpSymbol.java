package origrammer.geometry;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.vecmath.Vector2d;

public class OriPleatCrimpSymbol {
	
	final public static int TYPE_NONE = 0;
	final public static int TYPE_PLEAT = 1;
	final public static int TYPE_CRIMP = 2;
	
	private int type = TYPE_NONE;
	private Vector2d position = new Vector2d();
	private boolean  isSwitchedDir;
	private	int layersCount;
	private boolean isSelected;

	
	public OriPleatCrimpSymbol() {
	}
	
	public OriPleatCrimpSymbol(OriPleatCrimpSymbol pcs) {
		this.type = pcs.type;
		this.position = pcs.position;
		this.isSwitchedDir = pcs.isSwitchedDir;
		this.layersCount = pcs.layersCount;
		this.isSelected = pcs.isSelected;
	}
	
	public OriPleatCrimpSymbol(Vector2d position, boolean isSwitchedDir, int layersCount) {
		this.position = position;
		this.isSwitchedDir = isSwitchedDir;
		this.layersCount = layersCount;
	}
	
	public OriPleatCrimpSymbol(Vector2d position, boolean isSwitchedDir, int layersCount, int type) {
		this.position = position;
		this.isSwitchedDir = isSwitchedDir;
		this.layersCount = layersCount;
		this.type = type;
	}
	
	/**
	 * 
	 * @return The Shapes for rendering the OriPleatCrimpSymbol
	 */
	public ArrayList<Shape> getShapesForDrawing() {
		ArrayList<Shape> shapes = new ArrayList<>();
		Vector2d uvHori = new Vector2d(1,0);
		double translatX;
		double translatY;

		Vector2d uvDiagonal;

		Vector2d p0 = new Vector2d(position.x, position.y);
		Vector2d p1 = new Vector2d();		   
		Vector2d p2 = new Vector2d();
		Vector2d p3 = new Vector2d();

		if (isSwitchedDir) {
			uvDiagonal = new Vector2d(1, -0.6);
			uvHori = new Vector2d(-1, 0);
			translatX = 20;
			translatY = -5.5;
		} else {
			uvDiagonal = new Vector2d(-1, -0.6);
			uvHori = new Vector2d(1, 0);
			translatX = -20;
			translatY = -5.5;
		}

		/**		  
		 * 		(p2)______(p3)
		 *         \			 
		 *      	\		   
		 * (p0)______(p1)	
		 * 
		 * (p4)______(p5)
		 * 			/
		 * 		   /
		 * 		(p6)_____(p7)
		 */

		//top part
		for (int i = 0; i < layersCount; i++) {
			p0.x += translatX;
			p0.y += translatY;
			p1.x = p0.x + uvHori.x * 55;
			p1.y = p0.y + uvHori.y * 55;

			double p0TransX = uvHori.x * 20 * (layersCount - 1) - uvHori.x * 20 * (i + 1);
			double p0TransY = uvHori.y * 20 * (layersCount - 1) - uvHori.y * 20 * (i + 1);

			shapes.add(new Line2D.Double(p0.x - p0TransX, p0.y, p1.x, p1.y));

			p2.x = p1.x + uvDiagonal.x * 25;
			p2.y = p1.y + uvDiagonal.y * 25;
			shapes.add(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));

			p3.x = p2.x + uvHori.x * 35 + uvHori.x * 20 * i;
			p3.y = p2.y + uvHori.y * 35 + uvHori.y * 20 * i;
			shapes.add(new Line2D.Double(p2.x, p2.y, p3.x, p3.y));
		}
		
		if (type == TYPE_CRIMP)  {
			Vector2d p4 = new Vector2d(position.x, position.y);
			Vector2d p5 = new Vector2d();		   
			Vector2d p6 = new Vector2d();
			Vector2d p7 = new Vector2d();

			if (isSwitchedDir) {
				uvDiagonal = new Vector2d(1, 0.6);
				uvHori = new Vector2d(-1, 0);
				translatX = 20;
				translatY = 5.5;
			} else {
				uvDiagonal = new Vector2d(-1, 0.6);
				uvHori = new Vector2d(1, 0);
				translatX = -20;
				translatY = 5.5;
			}
			
			for (int j = 0; j < layersCount; j++) {
				//starting line with length 55 = Distance(p4,p5)
				p4.x += translatX;
				p4.y += translatY;
				p5.x = p4.x + uvHori.x * 55;
				p5.y = p4.y + uvHori.y * 55;
				
				double p0TransX = uvHori.x * 20 * (layersCount - 1) - uvHori.x * 20 * (j + 1);
				shapes.add(new Line2D.Double(p4.x - p0TransX, p4.y, p5.x, p5.y));
				
				//diagonal line
				p6.x = p5.x + uvDiagonal.x * 25;
				p6.y = p5.y + uvDiagonal.y * 25;
				shapes.add(new Line2D.Double(p5.x, p5.y, p6.x, p6.y));
				
				//35 minimal endLine length + 20 for each additional layer
				p7.x = p6.x + uvHori.x * 35 + uvHori.x * 20 * j;
				p7.y = p6.y + uvHori.y * 35 + uvHori.y * 20 * j;
				shapes.add(new Line2D.Double(p6.x, p6.y, p7.x, p7.y));
			}
		}
		return shapes;
	}
	
	public void moveBy(double xTrans, double yTrans) {
		position.x += xTrans;
		position.y += yTrans;
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public boolean isSwitchedDir() {
		return isSwitchedDir;
	}

	public void setIsSwitchedDir(boolean isSwitchedDir) {
		this.isSwitchedDir = isSwitchedDir;
	}

	public int getLayersCount() {
		return layersCount;
	}

	public void setLayersCount(int layersCount) {
		this.layersCount = layersCount;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
