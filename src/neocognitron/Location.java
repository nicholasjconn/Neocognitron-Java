package neocognitron;

import java.awt.Point;

public class Location {
	
	private int k;
	private Point n;

	
	public Location ( int k, int x, int y) {
		
		this.k = k;
		this.n = new Point(x,y);
	}
	
	public Location ( int k, Point n) {
		
		this.k = k;
		this.n = n;
	}
	
	public int getPlane() {
		return k;
	}
	
	public Point getPoint() {
		return n;
	}
	
	public void setPoint(Point p) {
		n = p;
	}

	@Override public boolean equals(Object o) {
		
		Location l = (Location) o;
		
		if (k != l.getPlane())
			return false;
		if (n.getX() != l.getPoint().getX())
			return false;
		if (n.getY() != l.getPoint().getY())
			return false;
		
		return true;
	}
}
