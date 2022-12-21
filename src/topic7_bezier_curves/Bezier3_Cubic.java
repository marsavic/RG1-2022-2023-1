package topic7_bezier_curves;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.drawingx.gadgets.annotations.GadgetBoolean;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;


public class Bezier3_Cubic implements Drawing {
	
	@GadgetAnimation(min = 0, max = 1, speed = 0.2)
	double t = 0.0;
	
	
	@GadgetVector
	Vector p0 = new Vector(-310, -100);

	@GadgetVector
	Vector p1 = new Vector(-110,  230);
	
	@GadgetVector
	Vector p2 = new Vector( 180, -250);

	@GadgetVector
	Vector p3 = new Vector( 320, 200);
	
	@GadgetBoolean
	boolean drawConstruction = false;
	
	@GadgetBoolean
	boolean drawCurve = true;

	@GadgetBoolean
	boolean drawDots = false;
	
	@GadgetInteger
	int n = 64;
	
	double r = 6;
	

	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		view.setLineWidth(2.0);
		
		if (drawCurve) {
			view.setStroke(Color.WHITE);
			view.beginPath();
			view.moveTo(p0);
			view.bezierCurveTo(p1, p2, p3);
			view.stroke();
		}

		if (drawDots) {
			view.setFill(Color.hsb(0, 0, 1, 0.5));
			for (int i = 0; i <= n; i++) {
				double u = 1.0 * i / n;
				Vector p01 = Vector.lerp(p0, p1, u);
				Vector p12 = Vector.lerp(p1, p2, u);
				Vector p23 = Vector.lerp(p2, p3, u);
				
				Vector p012 = Vector.lerp(p01, p12, u);
				Vector p123 = Vector.lerp(p12, p23, u);
				
				Vector p0123 = Vector.lerp(p012, p123, u);
				view.fillCircleCentered(p0123, r/2);
			}
		}

		Vector p01 = Vector.lerp(p0, p1, t);
		Vector p12 = Vector.lerp(p1, p2, t);		
		Vector p23 = Vector.lerp(p2, p3, t);
		
		Vector p012 = Vector.lerp(p01, p12, t);
		Vector p123 = Vector.lerp(p12, p23, t);
		
		Vector p0123 = Vector.lerp(p012, p123, t);

		view.setFill(Color.hsb(240, 0.6, 1));
		view.fillCircleCentered(p0, r);
		view.fillCircleCentered(p1, r);
		view.fillCircleCentered(p2, r);
		view.fillCircleCentered(p3, r);

		if (drawConstruction) {
			view.setStroke(Color.hsb(240, 0.6, 1));
			view.strokePolyline(p0, p1, p2, p3);
			
			view.setStroke(Color.hsb(120, 0.6, 1));
			view.setFill  (Color.hsb(120, 0.6, 1));
			view.strokePolyline(p01, p12, p23);
			view.fillCircleCentered(p01, r);
			view.fillCircleCentered(p12, r);
			view.fillCircleCentered(p23, r);
			
			view.setStroke(Color.hsb(0, 0.6, 1));
			view.setFill  (Color.hsb(0, 0.6, 1));
			view.strokePolyline(p012, p123);
			view.fillCircleCentered(p012, r);
			view.fillCircleCentered(p123, r);
		}
		
		view.setFill  (Color.hsb(0, 0, 1));
		view.fillCircleCentered(p0123, r);
	}
	
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 600);
	}
	
}
