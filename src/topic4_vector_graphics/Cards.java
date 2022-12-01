package topic4_vector_graphics;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.geometry.Transformation;
import mars.geometry.Vector;
import mars.random.RNG;


public class Cards implements Drawing {

	@GadgetInteger(min = 1, max = 16)
	int nCards = 6;

	Vector size = new Vector(150, 200);
	Vector a = new Vector(20, 20);
		
	double r = 48;
	
	Vector d = new Vector(16, 16);

	
	void drawCard(View view) {
		// Okvir karte sa senkom
		view.setFill(Color.WHITE);
		view.setEffect(new DropShadow(32, Color.BLACK));
		view.fillRoundRectCentered(Vector.ZERO, size.div(2), a);
		view.setEffect(null);
		view.setStroke(Color.RED);
		view.setLineWidth(16);
		view.strokeCircleCentered(Vector.ZERO, r);
		view.setLineCap(StrokeLineCap.BUTT);
		view.strokeLine(Vector.polar(r, 1.0 / 8), Vector.polar(r, 5.0 / 8));
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		RNG rng = new RNG(235023289427398423l);
		view.stateStore();
		view.setTransformation(Transformation.translation(new Vector(-120, -120)));
		for(int i = nCards - 1; i >= 0; i--) {
			view.stateStore();
			view.addTransformation(new Transformation ().rotate(0.01 * rng.nextGaussian()).translate(d.mul(i)));
			drawCard(view);
			view.stateRestore();
		}
		view.stateRestore();
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 600);
	}
	
}
