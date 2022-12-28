package topic8_simulation.bouncy_boxes;

import javafx.scene.paint.Color;
import mars.geometry.Vector;


public class Box_Formula extends Box {
	
	double t;
	Vector p, v;
	Color color;
	Vector areaD;
	

	
	public Box_Formula(Vector areaD, Vector p, Vector v, Color color) {
		this.areaD = areaD;
		this.p = p;
		this.v = v;
		this.color = color;
		
		this.t = 0;
	}		
	

	@Override
	public Vector getPosition(double time) {
		Vector bigD = areaD.mul(2);
		
		Vector tPeriod = bigD.div(v).abs();
		Vector dt = new Vector(time - t).mod(tPeriod);

		Vector q = p.add(v.mul(dt)).mod(bigD);
		return areaD.sub(q.sub(areaD).abs());			
	}
	
	
	@Override
	public Vector getVelocity(double time) {
		Vector bigD = areaD.mul(2);
		
		Vector tPeriod = bigD.div(v).abs();
		Vector dt = new Vector(time - t).mod(tPeriod);
		
		Vector q = p.add(v.mul(dt)).mod(bigD);
		return q.sub(areaD).signum().inverse().mul(v);			
	}
	
	
	@Override
	public Color getColor() {
		return color;
	}

}