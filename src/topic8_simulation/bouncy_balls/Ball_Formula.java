package topic8_simulation.bouncy_balls;

import javafx.scene.paint.Color;
import mars.geometry.Vector;
import mars.utils.Numeric;


public class Ball_Formula extends Ball {
	double t;
	Vector p, v, a;
	Color color;

	
	
	public Ball_Formula(Vector p, Vector v, Vector a, Color color) {
		// Vracamo lopticu u prethodni trenutak kada je dodirivala pod.
		t = (-v.y + Math.sqrt(v.y*v.y - 2*a.y*p.y)) / a.y;
		this.p = p.add(v.mul(t)).add(a.mul(t * t /2));
		this.v = v.add(a.mul(t));
		this.a = a;
		this.color = color;
	}		
	
	
	@Override
	public Vector getPosition(double time) {
		double tPeriod = - 2 * v.y / a.y;
		double tT = time - t;
		double td = Numeric.mod(tT, tPeriod);
		return new Vector(p.x + v.x*tT + a.x*tT*tT/2, p.y + v.y*td + a.y*td*td/2);
	}
	
	
	@Override
	public Vector getVelocity(double time) {
		double tPeriod = - 2 * v.y / a.y;
		double tT = time - t;
		double tD = Numeric.mod(tT, tPeriod);
		return new Vector(v.x + a.x*tT, v.y + a.y*tD);
	}
	
	
	@Override
	public Color getColor() {
		return color;
	}
	
}
