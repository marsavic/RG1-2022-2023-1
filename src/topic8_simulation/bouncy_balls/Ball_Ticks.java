 package topic8_simulation.bouncy_balls;

import javafx.scene.paint.Color;
import mars.geometry.Vector;


 public class Ball_Ticks extends Ball {
	 static final double intervalTick = 0.01;
	 
	 double t;
	 Vector p, v, a;
	 Color color;
	
	
	 public Ball_Ticks(Vector p, Vector v, Vector a, Color color) {
		 t = 0;
		 this.p = p;
		 this.v = v;
		 this.a = a;
		 this.color = color;
	 }
	 
	 
	 
	 private void update() {
		 if (v.y < 0 && p.y < 0) {
			 v = new Vector( v.x, -v.y);
		 }
		 
		 p = p.add(v.mul(intervalTick)).add(a.mul(intervalTick*intervalTick/2));
		 v = v.add(a.mul(intervalTick));
		 t += intervalTick;
	 }
	 
	 
	 private void updateTo(double time) {
		 while (t + intervalTick <= time) {
			 update();
		 }
	 }
	 
	 
	 @Override
	 public Vector getPosition(double time) {
		 updateTo(time);
		 return p;
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
