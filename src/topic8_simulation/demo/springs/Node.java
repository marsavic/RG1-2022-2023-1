package topic8_simulation.demo.springs;

import mars.geometry.Vector;

import java.util.ArrayList;
import java.util.List;


public class Node {
	public Vector p, v, f;
	public double mass;
	boolean fixed;
	
	protected List<Spring> springs = new ArrayList<>();
	
	
	
	public Node(Vector p, Vector v, double mass, boolean fixed) {
		this.p = p;
		this.v = v;
		this.mass = mass;
		this.fixed = fixed;
	}	
	
	
	public Node(Vector p, double mass) {
		this(p, Vector.ZERO, mass, false);
	}


	public Node(Vector p, Vector v) {
		this(p, v, 1, false);
	}
	
	
	public Node(Vector p) {
		this(p, Vector.ZERO, 1, false);
	}
	

	public Node(Vector p, double mass, boolean fixed) {
		this(p, Vector.ZERO, mass, fixed);
	}
	
	
	public Node(Vector p, Vector v, boolean fixed) {
		this(p, v, 1, fixed);
	}
	
	
	public Node(Vector p, boolean fixed) {
		this(p, Vector.ZERO, 1, fixed);
	}
	
}
