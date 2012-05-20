package neocognitron;

import java.awt.Point;

/**
 * Object which stores the location of a cell within a specific
 * layer. It is a very similar to a three dimensional point.
 * 
 * @author Nicholas J. Conn
 *
 */
public class Location {
	
	// The specific plane within a layer
	private int k;
	// The location of a cell within a specific plane.
	private Point n;

	/**
	 * Instantiate a Location using k, the plane, and (x,y), the location in the plane
	 * 
	 * @param k		The specific plane within the layer
	 * @param x		The specific location within the plane. Sometimes called n.
	 * @param y		The specific location within the plane. Sometimes called m.
	 */
	public Location ( int k, int x, int y) {
		this.k = k;
		this.n = new Point(x,y);
	}
	
	/**
	 * Instantiate a Location using k, the plane, and n, the location in the plane
	 * 
	 * @param k		The specific plane within the layer.
	 * @param n		The specific location within the plane.
	 */
	public Location ( int k, Point n) {
		this.k = k;
		this.n = n;
	}
	
	/**
	 * Get which plane the specific cell is located within.
	 * 
	 * @return	The plane number.
	 */
	public int getPlane() {
		return k;
	}
	
	/**
	 * Get where the cell is within the plane
	 * 
	 * @return	The point location within the plane.
	 */
	public Point getPoint() {
		return n;
	}
	
	/**
	 * Set where the cell is within a specific plane
	 * 
	 * @param p		New point within the plane value.
	 */
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
