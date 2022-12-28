package topic8_simulation.bouncy_balls;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.drawingx.gadgets.annotations.GadgetBoolean;
import mars.geometry.Vector;


public class BouncyBalls implements Drawing {
	// Donji levi ugao i dimenzije pravougaonika u kome će loptice biti generisane.
	// Pod je definisan pravom koja prolazi kroz donju stranicu tog pravougaonika. 
	final Vector areaP = new Vector(-300, -200);
	final Vector areaD = new Vector( 600,  400);
	
	double r = 10;                     // Poluprecnik loptice
	Vector g = new Vector(0, -500);    // Gravitacija    

	
	@GadgetAnimation
	double time = 0.0;
	
	@GadgetBoolean
	boolean showVelocities = false;
	
	
	int nBalls = 40;     // Ukupan broj loptica
	List<Ball> balls;
	
	
	
	{
		// Inicijalizacija. Postavljamo loptice u slucajno izabrane tacke unutar zadatog pravougaonika

		balls = new ArrayList<>();
		for (int i = 0; i < nBalls; i++) {
			balls.add(
//					new Ball_Formula(
//					new Ball_Events(
					new Ball_Ticks(
							Vector.randomInBox(areaD),              // Polozaj
							Vector.ZERO,              // Pocetna brzina
//							Vector.ZERO,
							g,                                      // Ubrzanje
							Color.hsb(360.0 * i / nBalls, 0.7, 0.9) // Boja
					)
			);
		}
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		// Pod
		
		view.setFill(Color.gray(0.25));
		Vector ll = new Vector(view.getCornerLowerLeft().x, areaP.y - r);
		view.fillRect(ll, view.getDiagonal().inverseY());		
		
		// Loptice

		view.setStroke(Color.WHITE);
		for (Ball ball : balls) {
			Vector p = areaP.add(ball.getPosition(time));
			Vector v = ball.getVelocity(time);
			
			view.setFill(ball.getColor());
			view.fillCircleCentered(p, r);
			if (showVelocities) {
				DrawingUtils.drawArrow(view, p, v.mul(0.1));
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 600);
	}
	
}
