package topic8_simulation.bouncy_balls;

import javafx.scene.paint.Color;
import mars.geometry.Vector;


public class Ball_Events extends Ball {
	double t;
	Vector p, v, a;
	Color color;
	
	double tNextEvent;
	
	
	
	public Ball_Events(Vector p, Vector v, Vector a, Color color) {
		t = 0;
		this.p = p;
		this.v = v;
		this.a = a;
		this.color = color;
		
		tNextEvent = calculateNextEvent();
	}
	
	
	private double calculateNextEvent() {
		// Trazimo sledeci dogadjaj (odbijanje od podloge (y = 0)) tako sto
		// resimo kvadratnu jednacinu p.y + v.y*tD + a*tD*tD/2 = 0 po tD.
		double tD = (-v.y - Math.sqrt(v.y * v.y - 2 * a.y * p.y)) / a.y;
		return t + tD;
	}
	

	private void updateTo(double time) {
		while (tNextEvent <= time) { // Ako je vreme sledeceg dogadjaja pre zeljenog vremena.
			// Prebacujemo stanje u momenat tNextEvent.
			double tD = tNextEvent - t;
			p = p.add(v.mul(tD)).add(a.mul(tD * tD / 2));
			v = v.add(a.mul(tD));
			t = tNextEvent;
			
			// Realizujemo dogadjaj (menjamo smer brzine).
			v = new Vector(v.x, -v.y);
			
			tNextEvent = calculateNextEvent();
		}		
	}
	
	
	@Override
	public Vector getPosition(double time) {
		updateTo(time);
		double td = time - t;
		return p.add(v.mul(td)).add(a.mul(td * td / 2));
	}

	
	@Override
	public Vector getVelocity(double time) {
		updateTo(time);
		return v.add(a.mul(time - t));
	}
	
	
	@Override
	public Color getColor() {
		return color;
	}
	
}
