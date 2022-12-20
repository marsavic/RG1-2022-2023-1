package topic6_animation;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;
import mars.utils.Numeric;


public class Triangle implements Drawing {
	
	@GadgetAnimation
	double time = 0.0;

	@GadgetVector
	Vector p0 = new Vector(-200, -200);

	@GadgetVector
	Vector p1 = new Vector(300, -100);
	
	@GadgetVector
	Vector p2 = new Vector(100, 200);
	
	@GadgetDouble(min = 0, max = 1000)
	double speed = 300;                                 // Brzina kretanja loptice (u pikselima po sekundi).
	
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		view.setLineJoin(StrokeLineJoin.ROUND);         // Spojevi linija su zaobljeni.
		view.setStroke(Color.DODGERBLUE);
		view.setLineWidth(4);
		view.strokePolygon(p0, p1, p2);
		
		
	}
	
	private double smootherstep(double x) {
		return x * x * x * (x * (x * 6 - 15) + 10);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}
	
}

