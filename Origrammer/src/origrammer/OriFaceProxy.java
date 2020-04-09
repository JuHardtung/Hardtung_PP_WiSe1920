package origrammer;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import origrammer.geometry.OriFace;

public class OriFaceProxy {
	
	private double[][] pathPoints; 
	private boolean isFaceUp;

	
	public OriFaceProxy() {
	}
	
	public OriFaceProxy(OriFace f) {
		pathPoints = getPoints(f.path);
		isFaceUp = f.isFaceUp();
	}
	
	/**
	 * Turns GeneralPath to pathPoints[][] for exporting to .xml
	 * @param path the GeneralPath of a filledFace
	 * @return pathPoints[][]
	 */
	public double[][] getPoints(GeneralPath path) {
		List<double[]> pointList = new ArrayList<double[]>();
		double[] coords = new double[6];
		int numSubPaths = 0;
		for (PathIterator pi = path.getPathIterator(null); !pi.isDone(); pi.next()) {
			switch (pi.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				pointList.add(Arrays.copyOf(coords, 2));
				++ numSubPaths;
				break;
			case PathIterator.SEG_LINETO:
				pointList.add(Arrays.copyOf(coords, 2));
				break;
			case PathIterator.SEG_CLOSE:
				if (numSubPaths > 1) {
					throw new IllegalArgumentException("Path contains multiple subpaths");
				}
				return pointList.toArray(new double[pointList.size()][]);
			default:
				throw new IllegalArgumentException("Path contains curves");
			}
		}
		throw new IllegalArgumentException("Unclosed path");
	}
	
	
	/**
	 * Returns an OriFace from OriFaceProxy
	 * @return
	 */
	public OriFace getFace() {
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, pathPoints.length);
		path.moveTo(pathPoints[0][0], pathPoints[0][1]);
		for (int i = 0; i < pathPoints.length; i++) {
			path.lineTo(pathPoints[i][0], pathPoints[i][1]);
		}
		path.closePath();
		
		return new OriFace(path, isFaceUp);
	}

	public double[][] getPath() {
		return pathPoints;
	}

	public void setPath(double[][] path) {
		this.pathPoints = path;
	}

	public boolean isFaceUp() {
		return isFaceUp;
	}

	public void setFaceUp(boolean isFaceUp) {
		this.isFaceUp = isFaceUp;
	}

	@Override
	public String toString() {
		return "OriFaceProxy [path=" + pathPoints + ", isFaceUp=" + isFaceUp + "]";
	}
	
}
