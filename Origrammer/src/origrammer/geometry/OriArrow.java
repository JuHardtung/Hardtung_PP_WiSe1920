package origrammer.geometry;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.vecmath.Vector2d;

public class OriArrow {
	
	final public static int TYPE_NONE = 0;
	//ARROWS OF MOTION
	final public static int TYPE_VALLEY = 1;
	final public static int TYPE_MOUNTAIN = 2;
	final public static int TYPE_TURN_OVER = 3;
	//ARROWS OF ACTION
	final public static int TYPE_PUSH_HERE = 4;
	final public static int TYPE_PULL_HERE = 5;
	final public static int TYPE_INFLATE_HERE = 6;
	
	final public static int VALLEY_ARROW_WIDTH = 50;
	final public static int VALLEY_ARROW_HEIGHT = 50;

	private Vector2d p0;
	private Vector2d p1;
	private int type = TYPE_NONE;
	private boolean isSelected;
	private boolean isMirrored;
	private boolean isUnfold;
	private int arrowRadius;
	
	
	public OriArrow() {
	}
	
	public OriArrow(OriArrow a) {
		this.p0 = a.getP0();
		this.p1 = a.getP1();
		this.type = a.type;
		this.isMirrored = a.isMirrored;
		this.isUnfold = a.isUnfold;
		this.arrowRadius = a.getArrowRadius();
	}	
	
	public OriArrow(Vector2d p0, Vector2d p1, int type, boolean isMirrored, boolean isUnfold, int arrowRadius) {
		this.p0 = p0;
		this.p1 = p1;
		this.type = type;
		this.isMirrored = isMirrored;
		this.isUnfold = isUnfold;
		this.arrowRadius = arrowRadius;
	}
	
	public ArrayList<Shape> getShapesForDrawing() {
		ArrayList<Shape> shapes = new ArrayList<>();
		
		if (type == TYPE_VALLEY) {
			shapes =  getValleyFoldShapes();
		} else if (type == TYPE_MOUNTAIN) {
			shapes =  getMountainFoldShapes();
		} else if (type == TYPE_TURN_OVER) {
			shapes = getTurnOverShapes();
		} else if (type == TYPE_PUSH_HERE) {
			shapes = getPushHereShapes();
		} else if (type == TYPE_PULL_HERE) {
			shapes = getPullHereShapes();
		} else if (type == TYPE_INFLATE_HERE) {
			shapes = getInflateHereShapes();
		}
		return shapes;
	}
	
	private ArrayList<Shape> getValleyFoldShapes(){
		ArrayList<Shape> shapes = new ArrayList<>();
		
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		Vector2d nv = GeometryUtil.getNormalVector(uv);
		double fullArrowLength = GeometryUtil.Distance(p0, p1);
		
		Vector2d offsetP0 = new Vector2d(p0.x + uv.x * 4, p0.y + uv.y * 4);
		Vector2d offsetP1 = new Vector2d(p1.x - uv.x * 4, p1.y - uv.y * 4);
		double startAngle;
		double angleExtend = 90;
		double arrowLength = GeometryUtil.Distance(offsetP0, offsetP1);
		double helperAngle = Math.toRadians(15);

		//ARC
		Vector2d middlePoint = new Vector2d(offsetP0.x + uv.x * arrowLength / 2, offsetP0.y + uv.y * arrowLength / 2);
		Vector2d arcCenter = new Vector2d(middlePoint.x + nv.x * arrowLength / 2, middlePoint.y + nv.y * arrowLength / 2);
//		double theta = Math.toDegrees(Math.atan2(p0.y+arcCenter.y, p0.y-arcCenter.x));
//		double delta = Math.toDegrees(Math.atan2(p1.y+arcCenter.y, p1.x-arcCenter.x));

		if (isMirrored) {
			arcCenter = new Vector2d(middlePoint.x - nv.x * arrowLength / 2, middlePoint.y - nv.y * arrowLength / 2);
		    startAngle = Math.toDegrees(Math.atan2(offsetP1.y - arcCenter.y, offsetP1.x - arcCenter.x));
			angleExtend = -90;
		    nv.x = -nv.x;
		    nv.y = -nv.y;
		    helperAngle = -helperAngle;
		} else {
		    startAngle = Math.toDegrees(Math.atan2(offsetP1.y - arcCenter.y, offsetP1.x - arcCenter.x));
			angleExtend = 90;
		}
		
		//Values for the arrow arc
		double r = Math.sqrt(Math.pow(offsetP1.x - arcCenter.x, 2) + Math.pow(offsetP1.y - arcCenter.y, 2));
		double x = arcCenter.x - r;
		double y = arcCenter.y - r;
		double width = 2*r;
		double height = 2*r;

		Arc2D.Double arc = new Arc2D.Double(x, y, width, height, -startAngle, angleExtend, Arc2D.OPEN);
		
		//ValleyFold Arrowhead
		double rAngleX = uv.x * Math.cos(helperAngle) - uv.y * Math.sin(helperAngle);
		double rAngleY = uv.x * Math.sin(helperAngle) + uv.y * Math.cos(helperAngle);
		double lAngleX = nv.x * Math.cos(-helperAngle) - nv.y * Math.sin(-helperAngle);
		double lAngleY = nv.x * Math.sin(-helperAngle) + nv.y * Math.cos(-helperAngle);
		double arrowHeadLength;
		
		//length of arrowHead depended on total arrow length 
		//if total length < 150 --> arrowHead = 20
		//else arrowHead = 20 + 4 for every 150 units length
		if (fullArrowLength <= 150) {
			arrowHeadLength = 20;
		} else {
			arrowHeadLength = 20 + (fullArrowLength - 150) / 150 * 4;
		}
		

		Line2D.Double arrowHeadR = new Line2D.Double(offsetP1.x, offsetP1.y, offsetP1.x - rAngleX * arrowHeadLength, offsetP1.y - rAngleY * arrowHeadLength);
		Line2D.Double arrowHeadL = new Line2D.Double(offsetP1.x, offsetP1.y, offsetP1.x - lAngleX * arrowHeadLength, offsetP1.y - lAngleY * arrowHeadLength);
		
		shapes.add(arrowHeadL);
		shapes.add(arrowHeadR);
		shapes.add(arc);
		
		//UNFOLD Arrowhead
		if (isUnfold) {
			shapes.add(addUnfoldArrowHead(offsetP0, new Vector2d(arcCenter.x - nv.x*1.35*r, arcCenter.y - nv.y*1.35*r), arrowHeadLength));
		}
		
		return shapes;
	}
	
	private ArrayList<Shape> getMountainFoldShapes(){
		ArrayList<Shape> shapes = new ArrayList<>();
		
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		Vector2d nv = GeometryUtil.getNormalVector(uv);
		double fullArrowLength = GeometryUtil.Distance(p0, p1);

		double angleMinus30 = Math.toRadians(-30);
		Vector2d offsetP0 = new Vector2d(p0.x + uv.x * 4, p0.y + uv.y * 4);
		Vector2d offsetP1 = new Vector2d(p1.x - uv.x * 4, p1.y - uv.y * 4);
		double length = GeometryUtil.Distance(offsetP0, offsetP1);

		
		if (isMirrored) {
			angleMinus30 = Math.toRadians(30);
		    nv.x = -nv.x;
		    nv.y = -nv.y;
		} else {
			angleMinus30 = Math.toRadians(-30);

		}
		
		//ARROW CURVE
		double curveOffset = 3.5;
		Vector2d middle = new Vector2d(offsetP0.x + uv.x * length / curveOffset, offsetP0.y + uv.y * length / curveOffset);
		Vector2d middle2 = new Vector2d(offsetP1.x - uv.x * length / curveOffset, offsetP1.y - uv.y * length / curveOffset);

		double curveLength = 1.6;
		Vector2d curvePoint = new Vector2d(middle.x + nv.x * length * curveLength, middle.y + nv.y * length * curveLength);
		Vector2d curvePoint2 = new Vector2d(middle2.x + nv.x * length * curveLength, middle2.y + nv.y * length * curveLength);

		CubicCurve2D curve = new CubicCurve2D.Double();
		curve.setCurve(offsetP0.x, offsetP0.y, curvePoint.x, curvePoint.y, curvePoint2.x, curvePoint2.y, offsetP1.x, offsetP1.y);
		
		//ARROWHEAD
		Vector2d uv2 = GeometryUtil.getUnitVector(curvePoint2, offsetP1);
		//double angleMinus30 = Math.toRadians(-30);
		double angleMinus45X = uv2.x * Math.cos(angleMinus30) - uv2.y * Math.sin(angleMinus30);
		double angleMinus45Y = uv2.x * Math.sin(angleMinus30) + uv2.y * Math.cos(angleMinus30);
		Vector2d uvMinus45 = new Vector2d(angleMinus45X, angleMinus45Y);
		double arrowHeadLength;

		
		//length of arrowHead depended on total arrow length 
		//if total length < 150 --> arrowHead = 20
		//else arrowHead = 20 + 4 for every 150 units length
		if (fullArrowLength <= 150) {
			arrowHeadLength = 40;
		} else {
			arrowHeadLength = 40 + (fullArrowLength - 150) / 150 * 8;
		}
		
		Vector2d arrowHead1 = new Vector2d(offsetP1.x - uvMinus45.x * (arrowHeadLength+20), offsetP1.y - uvMinus45.y * (arrowHeadLength+20));
		Vector2d arrowHead2 = new Vector2d(offsetP1.x - uv2.x * arrowHeadLength, offsetP1.y - uv2.y * arrowHeadLength);

		Vector2d uvArrowHead = GeometryUtil.getUnitVector(arrowHead1, arrowHead2);		
		//offset arrowHead2 to compensate for the difference between the slight curve 
		//and the straight line between p1 and arrowHead2
		Vector2d adjArrowHead2 = new Vector2d(arrowHead2.x + uvArrowHead.x * 0.5, arrowHead2.y + uvArrowHead.y * 0.5);
		
		GeneralPath arrowHead = new GeneralPath();	
		arrowHead.moveTo(offsetP1.x, offsetP1.y);
		arrowHead.lineTo(arrowHead1.x, arrowHead1.y);
		arrowHead.lineTo(adjArrowHead2.x, adjArrowHead2.y);
		arrowHead.closePath();
		
		shapes.add(arrowHead);
		shapes.add(curve);
		//UNFOLD Arrowhead
		if (isUnfold) {
			shapes.add(addUnfoldArrowHead(offsetP0, curvePoint, arrowHeadLength-20));
		}
		return shapes;
	}
	
	private ArrayList<Shape> getTurnOverShapes() {
		ArrayList<Shape> shapes = new ArrayList<>();
		
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		Vector2d nv = GeometryUtil.getNormalVector(uv);
		double fullArrowLength = GeometryUtil.Distance(p0, p1);

		Vector2d offsetP0 = new Vector2d(p0.x + uv.x * 4, p0.y + uv.y * 4);
		Vector2d offsetP1 = new Vector2d(p1.x - uv.x * 4, p1.y - uv.y * 4);
		double startAngle;
		double angleExtend = 90;
		double arrowLength = GeometryUtil.Distance(offsetP0, offsetP1);
		double angle15 = Math.toRadians(15);

		Vector2d middlePoint = new Vector2d(offsetP0.x + uv.x * arrowLength / 2, offsetP0.y + uv.y * arrowLength / 2);
		Vector2d arcCenter = new Vector2d(middlePoint.x + nv.x * arrowLength / 2, middlePoint.y + nv.y * arrowLength / 2);

		if (isMirrored) {
			arcCenter = new Vector2d(middlePoint.x - nv.x * arrowLength /  2, middlePoint.y - nv.y * arrowLength / 2);
		    startAngle = Math.toDegrees(Math.atan2(offsetP1.y - arcCenter.y, offsetP1.x - arcCenter.x));
			angleExtend = -90;
		    nv.x = -nv.x;
		    nv.y = -nv.y;
		    angle15 = -angle15;
		} else {
		    startAngle = Math.toDegrees(Math.atan2(offsetP1.y - arcCenter.y, offsetP1.x - arcCenter.x));
			angleExtend = 90;
		}
		
		//Arrow Arc
		double r = Math.sqrt(Math.pow(offsetP1.x - arcCenter.x, 2) + Math.pow(offsetP1.y - arcCenter.y, 2));
		double x = arcCenter.x - r;
		double y = arcCenter.y - r;
		double width = 2 * r;
		double height = 2 * r;
		Arc2D.Double arc = new Arc2D.Double(x, y, width, height, -startAngle, angleExtend, Arc2D.OPEN);

		//Small Circle	
		Vector2d arcMiddle = new Vector2d(arcCenter.x - nv.x * r, arcCenter.y - nv.y * r);
		Vector2d circleBot = new Vector2d(arcMiddle.x + nv.x * (r/2.8), arcMiddle.y + nv.y * (r/2.8));
		double smallCircleRadius = GeometryUtil.Distance(circleBot, arcMiddle)/2;
		Vector2d smallCircleCenter = new Vector2d(circleBot.x - nv.x * smallCircleRadius, circleBot.y - nv.y * smallCircleRadius);
		Vector2d smallCircleTopL = new Vector2d(smallCircleCenter.x - smallCircleRadius, smallCircleCenter.y - smallCircleRadius);
		Arc2D.Double smallCircle = new Arc2D.Double(smallCircleTopL.x, smallCircleTopL.y, smallCircleRadius * 2, smallCircleRadius * 2, 0, 360, Arc2D.OPEN);

		//ValleyFold Arrowhead
		double rAngleX = uv.x * Math.cos(angle15) - uv.y * Math.sin(angle15);
		double rAngleY = uv.x * Math.sin(angle15) + uv.y * Math.cos(angle15);
		double lAngleX = nv.x * Math.cos(-angle15) - nv.y * Math.sin(-angle15);
		double lAngleY = nv.x * Math.sin(-angle15) + nv.y * Math.cos(-angle15);
		double arrowHeadLength;

		//length of arrowHead depended on total arrow length 
		//if total length < 150 --> arrowHead = 20
		//else arrowHead = 20 + 4 for every 150 units length
		if (fullArrowLength <= 150) {
			arrowHeadLength = 20;
		} else {
			arrowHeadLength = 20 + (fullArrowLength - 150) / 150 * 4;
		}
		
		GeneralPath path = new GeneralPath();
		path.moveTo(offsetP1.x - rAngleX * arrowHeadLength, offsetP1.y - rAngleY * arrowHeadLength);
		path.lineTo(offsetP1.x, offsetP1.y);
		path.lineTo(offsetP1.x - lAngleX * arrowHeadLength, offsetP1.y - lAngleY * arrowHeadLength);
		path.lineTo(offsetP1.x, offsetP1.y);
		path.closePath();
		
		shapes.add(path);
		shapes.add(smallCircle);
		shapes.add(arc);
		
		//UNFOLD Arrowhead
		if (isUnfold) {
			shapes.add(addUnfoldArrowHead(offsetP0, 
									new Vector2d(arcCenter.x - nv.x * 1.35 * r, arcCenter.y - nv.y * 1.35 * r), 
									arrowHeadLength));
		}
		return shapes;		
	}
	
	private ArrayList<Shape> getPushHereShapes() {
		/**
		 * 	       1		1 = offsetP1 
		 *       _- -_		2 = arrowHeadLP1
		 *     _-     -_	3 = smallArrowLP1
		 *    2--3   9--10  4 = bezierL
		 *      )     (		5 = arcLP0
		 *      4    8		6 = middlePoint
		 *     ) _-6-_ (	7 = arcRP0
		 *    5_-     -_7	8 = bezierR
		 *  				9 = smallArrowRP1
		 *  			   10 = arrowHeadRP1
		 */
		ArrayList<Shape> shapes = new ArrayList<>();
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		Vector2d nv = GeometryUtil.getNormalVector(uv);
		
		// 1
		Vector2d offsetP1 = new Vector2d(p1.x - uv.x * 4, p1.y - uv.y * 4);

		double angle45 = Math.toRadians(45);
		double angle45X = uv.x * Math.cos(angle45) - uv.y * Math.sin(angle45);
		double angle45Y = uv.x * Math.sin(angle45) + uv.y * Math.cos(angle45);
		Vector2d uv45 = new Vector2d(angle45X, angle45Y);
		
		double angleMinus45X = uv.x * Math.cos(-angle45) - uv.y * Math.sin(-angle45);
		double angleMinus45Y = uv.x * Math.sin(-angle45) + uv.y * Math.cos(-angle45);
		Vector2d uvMinus45 = new Vector2d(angleMinus45X, angleMinus45Y);

		// 2
		Vector2d arrowHeadLP1 = new Vector2d(p1.x - uv45.x * 60, p1.y - uv45.y * 60);
		// 10
		Vector2d arrowHeadRP1 = new Vector2d(p1.x - uvMinus45.x * 60, p1.y - uvMinus45.y * 60);
		// 3
		Vector2d smallArrowLP1 = new Vector2d(arrowHeadLP1.x + nv.x * 25, arrowHeadLP1.y + nv.y * 25);
		// 9
		Vector2d smallArrowRP1 = new Vector2d(arrowHeadRP1.x - nv.x * 25, arrowHeadRP1.y - nv.y * 25);

		double angle170 = Math.toRadians(175);
		double angle170X = uv.x * Math.cos(angle170) - uv.y * Math.sin(angle170);
		double angle170Y = uv.x * Math.sin(angle170) + uv.y * Math.cos(angle170);
		Vector2d uv170 = new Vector2d(angle170X, angle170Y);
		
		double angleMinus170X = uv.x * Math.cos(-angle170) - uv.y * Math.sin(-angle170);
		double angleMinus170Y = uv.x * Math.sin(-angle170) + uv.y * Math.cos(-angle170);
		Vector2d uvMinus170 = new Vector2d(angleMinus170X, angleMinus170Y);
		
		// 4
		Vector2d bezierL = new Vector2d(smallArrowLP1.x + uvMinus170.x * 45, smallArrowLP1.y + uvMinus170.y * 45);
		// 8
		Vector2d bezierR = new Vector2d(smallArrowRP1.x + uv170.x * 45, smallArrowRP1.y + uv170.y * 45);
		// 5
		Vector2d arcRP0 = new Vector2d(arrowHeadRP1.x - uv.x * 75, arrowHeadRP1.y - uv.y * 75);
		// 7
		Vector2d arcLP0 = new Vector2d(arrowHeadLP1.x - uv.x * 75, arrowHeadLP1.y - uv.y * 75);
		// 6
		Vector2d middleEndPoint = new Vector2d(p1.x - uv.x * 100, p1.y - uv.y * 100);
		

		GeneralPath pushHerePath = new GeneralPath();	
		pushHerePath.moveTo(offsetP1.x, offsetP1.y); 														// 1
		pushHerePath.lineTo(arrowHeadLP1.x, arrowHeadLP1.y);												// 1->2
		pushHerePath.lineTo(smallArrowLP1.x, smallArrowLP1.y);												// 2->3
		pushHerePath.curveTo(bezierL.x, bezierL.y, bezierL.x, bezierL.y, arcLP0.x, arcLP0.y);				// 3->4->5
		pushHerePath.lineTo(middleEndPoint.x, middleEndPoint.y);											// 5->6
		pushHerePath.lineTo(arcRP0.x, arcRP0.y);															// 6->7
		pushHerePath.curveTo(bezierR.x, bezierR.y, bezierR.x, bezierR.y, smallArrowRP1.x, smallArrowRP1.y); // 7->8->9
		pushHerePath.lineTo(arrowHeadRP1.x, arrowHeadRP1.y);												// 9->10
		pushHerePath.closePath();																			// 10->1
		shapes.add(pushHerePath);
		
		return shapes;		
	}
	
	private ArrayList<Shape> getPullHereShapes() {
		/**
		 * 	       1		1 = offsetP1 
		 *       _- -_		2 = arrowHeadLP1
		 *     _-     -_	3 = smallArrowLP1
		 *    2--3   7--8   4 = longArrowLP1
		 *      /  5  \		5 = middlePoint
		 *     / _- -_ \	6 = longArrowRP1
		 *    4_-     -_6	7 = smallArrowRP1
		 *  				8 = arrowHeadRP1
		 */
		ArrayList<Shape> shapes = new ArrayList<>();
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		// 1
		Vector2d offsetP1 = new Vector2d(p1.x - uv.x * 4, p1.y - uv.y * 4);
		
		double angle35 = Math.toRadians(35);
		double angle35X = uv.x * Math.cos(angle35) - uv.y * Math.sin(angle35);
		double angle35Y = uv.x * Math.sin(angle35) + uv.y * Math.cos(angle35);
		Vector2d uv35 = new Vector2d(angle35X, angle35Y);
		
		double angleMinus35X = uv.x * Math.cos(-angle35) - uv.y * Math.sin(-angle35);
		double angleMinus35Y = uv.x * Math.sin(-angle35) + uv.y * Math.cos(-angle35);
		Vector2d uvMinus35 = new Vector2d(angleMinus35X, angleMinus35Y);

		// 2
		Vector2d arrowHeadLP1 = new Vector2d(p1.x - uv35.x * 70, p1.y - uv35.y * 70);
		// 8
		Vector2d arrowHeadRP1 = new Vector2d(p1.x - uvMinus35.x * 70, p1.y - uvMinus35.y * 70);
		
		double angle80 = Math.toRadians(80);
		double angle80X = uv.x * Math.cos(angle80) - uv.y * Math.sin(angle80);
		double angle80Y = uv.x * Math.sin(angle80) + uv.y * Math.cos(angle80);
		Vector2d uv80 = new Vector2d(angle80X, angle80Y);
		
		double angleMinus80X = uv.x * Math.cos(-angle80) - uv.y * Math.sin(-angle80);
		double angleMinus80Y = uv.x * Math.sin(-angle80) + uv.y * Math.cos(-angle80);
		Vector2d uvMinus80 = new Vector2d(angleMinus80X, angleMinus80Y);
		
		// 3
		Vector2d smallArrowLP1 = new Vector2d(arrowHeadLP1.x + uv80.x * 20, arrowHeadLP1.y + uv80.y * 20);
		// 7
		Vector2d smallArrowRP1 = new Vector2d(arrowHeadRP1.x + uvMinus80.x * 20, arrowHeadRP1.y + uvMinus80.y * 20);
		
		double angle170 = Math.toRadians(170);
		double angle170X = uv.x * Math.cos(angle170) - uv.y * Math.sin(angle170);
		double angle170Y = uv.x * Math.sin(angle170) + uv.y * Math.cos(angle170);
		Vector2d uv170 = new Vector2d(angle170X, angle170Y);
		
		double angleMinus170X = uv.x * Math.cos(-angle170) - uv.y * Math.sin(-angle170);
		double angleMinus170Y = uv.x * Math.sin(-angle170) + uv.y * Math.cos(-angle170);
		Vector2d uvMinus170 = new Vector2d(angleMinus170X, angleMinus170Y);
		
		// 4
		Vector2d longArrowLP1 = new Vector2d(smallArrowLP1.x + uvMinus170.x * 70, smallArrowLP1.y + uvMinus170.y * 70);
		// 6
		Vector2d longArrowRP1 = new Vector2d(smallArrowRP1.x + uv170.x * 70, smallArrowRP1.y + uv170.y * 70);
		// 5
		Vector2d middlePoint = new Vector2d(p1.x - uv.x * 90.2, p1.y - uv.y * 90.2);

		
		ArrayList<Vector2d> pathList = new ArrayList<Vector2d>();
		pathList.add(offsetP1);
		pathList.add(arrowHeadLP1);
		pathList.add(smallArrowLP1);
		pathList.add(longArrowLP1);
		pathList.add(middlePoint);
		pathList.add(longArrowRP1);
		pathList.add(smallArrowRP1);
		pathList.add(arrowHeadRP1);	
		GeneralPath pullHerePath = GeometryUtil.createPathFromVertices(pathList);
			
		shapes.add(pullHerePath);
		return shapes;		
	}
	
	private ArrayList<Shape> getInflateHereShapes() {
		/**			 1		 
		 *  		/ \		 headL = 1 -> 4
		 * 		   2   3	 headR = 1 -> 5
		 * 		  /|   |\	 tailL = 2 -> 6
		 * 		 4 |   | 5	 tailR = 3 -> 7
		 * 		   6   7				 
		 */
		ArrayList<Shape> shapes = new ArrayList<>();
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);

		double angle45 = Math.toRadians(45);
		double angle45X = uv.x * Math.cos(angle45) - uv.y * Math.sin(angle45);
		double angle45Y = uv.x * Math.sin(angle45) + uv.y * Math.cos(angle45);
		Vector2d uv45 = new Vector2d(angle45X, angle45Y);
		
		double angleMinus45X = uv.x * Math.cos(-angle45) - uv.y * Math.sin(-angle45);
		double angleMinus45Y = uv.x * Math.sin(-angle45) + uv.y * Math.cos(-angle45);
		Vector2d uvMinus45 = new Vector2d(angleMinus45X, angleMinus45Y);
		
		// 1-> 4
		Line2D.Double arrowHeadL = new Line2D.Double(p1.x, p1.y, p1.x - uvMinus45.x * 70, p1.y - uvMinus45.y * 70);
		// 1 -> 5
		Line2D.Double arrowHeadR = new Line2D.Double(p1.x, p1.y, p1.x - uv45.x * 70, p1.y - uv45.y * 70);
		// 2
		Vector2d tailLP0 = new Vector2d(p1.x - uvMinus45.x * 40, p1.y - uvMinus45.y * 40);
		// 3
		Vector2d tailRP0 = new Vector2d(p1.x - uv45.x * 40, p1.y - uv45.y * 40);
		
		double tailLength = 100;
		// 2 -> 6
		Line2D.Double tailL = new Line2D.Double(tailLP0.x, tailLP0.y, tailLP0.x - uv.x * tailLength, tailLP0.y - uv.y * tailLength);
		// 3 -> 7
		Line2D.Double tailR = new Line2D.Double(tailRP0.x, tailRP0.y, tailRP0.x - uv.x * tailLength, tailRP0.y - uv.y * tailLength);

		shapes.add(arrowHeadL);
		shapes.add(arrowHeadR);
		shapes.add(tailL);
		shapes.add(tailR);
		return shapes;		
	}
	
	/**
	 * Creates a GeneralPath in form of the unfoldArrowHead that can be added to other OriArrows if necessary.
	 * @param offsetP0	slightly offset p0 of the OriArrow
	 * @param offsetP1 slightly offset p1 of the OriArrow
	 * @return GeneralPath in form of the UnfoldArrowHead
	 */
	private Shape addUnfoldArrowHead(Vector2d offsetP0, Vector2d offsetP1, double arrowHeadLength) {
		//UNFOLD Arrowhead
		/** 	 1			1 = offsetP0
		 *      / \         2 = helpL
		 *     /   \        3 = helpR
		 *    2 _4_ 3		4 = unfoldMiddle
		 *   /_-   -_\		5 = unfoldLP0
		 *  5-       -6     6 = unfoldRP0
		 */	
		Vector2d uv = GeometryUtil.getUnitVector(offsetP0, offsetP1);
		Vector2d nv = GeometryUtil.getNormalVector(uv);
		double angle15 = Math.toRadians(30);
		
		if (isMirrored) {
		    nv.x = -nv.x;
		    nv.y = -nv.y;
		    angle15 = -angle15;
		}

		//Unit vector for 1->2 and 1->5
		double unfoldLeftX = -uv.x * Math.cos(angle15) + uv.y * Math.sin(angle15);
		double unfoldLeftY = -uv.x * Math.sin(angle15) - uv.y * Math.cos(angle15);
		//Unit vector for 1->3 and 1->6
		double unfoldRightX = -uv.x * Math.cos(-angle15) + uv.y * Math.sin(-angle15);
		double unfoldRightY = -uv.x * Math.sin(-angle15) - uv.y * Math.cos(-angle15);
		
		//helper vertices to calc dist between 2 and 3 and subsequently calc unfoldMiddle (4)
		Vector2d helpL = new Vector2d(offsetP0.x - unfoldLeftX * (arrowHeadLength-arrowHeadLength*0.2), offsetP0.y - unfoldLeftY * (arrowHeadLength-arrowHeadLength*0.2));
		Vector2d helpR = new Vector2d(offsetP0.x - unfoldRightX * (arrowHeadLength-arrowHeadLength*0.2), offsetP0.y - unfoldRightY * (arrowHeadLength-arrowHeadLength*0.2));
		double unfoldDist = GeometryUtil.Distance(helpL, helpR);
		
		Vector2d unfoldUV = GeometryUtil.getUnitVector(helpL, helpR);
		Vector2d unfoldMiddle = new Vector2d(helpL.x + unfoldUV.x * (unfoldDist/2), helpL.y + unfoldUV.y * (unfoldDist/2));

		Vector2d unfoldLP0 = new Vector2d(offsetP0.x - unfoldLeftX * arrowHeadLength, offsetP0.y - unfoldLeftY * arrowHeadLength);
		Vector2d unfoldRP0 = new Vector2d(offsetP0.x - unfoldRightX * arrowHeadLength, offsetP0.y - unfoldRightY * arrowHeadLength);
	
		ArrayList<Vector2d> pathList = new ArrayList<Vector2d>();
		pathList.add(offsetP0);
		pathList.add(unfoldLP0);
		pathList.add(unfoldMiddle);
		pathList.add(unfoldRP0);
		
		GeneralPath unfoldPath = GeometryUtil.createPathFromVertices(pathList);
		
		return unfoldPath;
	}
	
	public Vector2d getP0() {
		return p0;
	}

	public void setP0(Vector2d p0) {
		this.p0 = p0;
	}

	public Vector2d getP1() {
		return p1;
	}

	public void setP1(Vector2d p1) {
		this.p1 = p1;
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

	public boolean isMirrored() {
		return isMirrored;
	}

	public void setMirrored(boolean isMirrored) {
		this.isMirrored = isMirrored;
	}

	public boolean isUnfold() {
		return isUnfold;
	}

	public void setUnfold(boolean isUnfold) {
		this.isUnfold = isUnfold;
	}

	public int getArrowRadius() {
		return arrowRadius;
	}

	public void setArrowRadius(int arrowRadius) {
		this.arrowRadius = arrowRadius;
	}

	@Override
	public String toString() {
		return "OriArrow [p0=" + p0 + ", p1=" + p1 + ", type=" + type + ", isSelected=" + isSelected + ", isMirrored="
				+ isMirrored + ", arrowRadius=" + arrowRadius + "]";
	}
	
}
