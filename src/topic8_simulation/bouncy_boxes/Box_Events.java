package topic8_simulation.bouncy_boxes;

import javafx.scene.paint.Color;
import mars.geometry.Vector;


public class Box_Events extends Box {
	
	double t;
	Vector p, v;
	Color color;
	Vector areaD;
	

	
	public Box_Events(Vector areaD, Vector p, Vector v, Color color) {
		this.areaD = areaD;
		this.p = p;
		this.v = v;
		this.color = color;
		
		this.t = 0;
	}		
	
	
	private void updateTo(double time) {
		double tNextEvent;
		int nextEvent;
		
		do {
			// Trazimo sledeci dogadjaj.
			tNextEvent = Double.POSITIVE_INFINITY;
			nextEvent = 0;
			
			if (v.x < 0) { // Ako ide u levo racunamo vreme susreta sa levim zidom.
				double dt = - p.x / v.x;
				if (dt >= 0 && t + dt < tNextEvent) {
					tNextEvent = t + dt;
					nextEvent = 1;
				}
			} else {       // Ako ide u desno racunamo vreme susreta sa desnim zidom.
				double dt = (areaD.x - p.x) / v.x;
				if (dt >= 0 && t + dt < tNextEvent) {
					tNextEvent = t + dt;
					nextEvent = 2;
				}
			}
			if (v.y < 0) { // Ako ide na dole racunamo vreme susreta sa donjim zidom.
				double dt = - p.y / v.y;
				if (dt >= 0 && t + dt < tNextEvent) {
					tNextEvent = t + dt;
					nextEvent = 3;
				}
			} else {       // Ako ide na gore racunamo vreme susreta sa gornjim zidom.
				double dt = (areaD.y - p.y) / v.y;
				if (dt >= 0 && t + dt < tNextEvent) {
					tNextEvent = t + dt;
					nextEvent = 4;
				}
			}
			
			if (tNextEvent <= time) { // Ako je vreme sledeceg dogadjaja pre trenutnog vremena.
				// Prebacujemo stanje u momenat tNextEvent.
				p = p.add(v.mul(tNextEvent - t));
				t = tNextEvent;
				
				// Realizujemo dogadjaj (menjamo smer brzine).
				if (nextEvent == 1 || nextEvent == 2) {
					v = new Vector(-v.x, v.y);
				} else {
					v = new Vector(v.x, -v.y);
				}
			}
		} while (tNextEvent <= time);
	}

	
	@Override
	public Vector getPosition(double time) {
		updateTo(time);
		return p.add(v.mul(time - t));
	}
	
	
	@Override
	public Vector getVelocity(double time) {
		updateTo(time);
		return v;
	}
	
	
	@Override
	public Color getColor() {
		return color;
	}

}