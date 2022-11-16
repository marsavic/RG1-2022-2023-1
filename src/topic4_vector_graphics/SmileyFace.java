package topic4_vector_graphics;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.geometry.Vector;


public class SmileyFace implements Drawing {

	@GadgetDouble(min = 0, max = 200)
	double r = 200;                        // Poluprecnik glave.
	
	@GadgetDouble(min = 0, max = 200)
	double xEye = 70.0;                    // Udaljenost oka od centra po x-osi.

	@GadgetDouble(min = 0, max = 300)
	double yEye = 70.0;                    // Udaljenost oka od centra po y-osi.

	@GadgetDouble(min = 0, max = 100)
	double rEye = 30.0;                    // Poluprecnik oka.

	@GadgetDouble(min = 0, max = 200)
	double rMouth = 130.0;                 // Poluprecnik luka usta.
	
	@GadgetDouble(min = 0, max = 0.5)
	double phiMouth = 1.0/3;               // Ugao luka usta.
	
	

	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.hsb(0, 0, 0.125));
		
		// Glava
		view.setFill(Color.hsb(60, 0.9, 0.9));
		view.fillCircleCentered(new Vector(0, 0), r);
		
		// Oci
		view.setFill(Color.hsb(0, 0, 0));
		view.fillCircleCentered(new Vector(-xEye, yEye), rEye);		
		view.fillCircleCentered(new Vector(xEye, yEye), rEye);
		
		// Usta
		view.setLineWidth(15);
		view.setStroke(Color.hsb(0, 0, 0));
		view.strokeArcCentered(new Vector(0, 0), new Vector(rMouth), 0.75 - phiMouth/2, phiMouth);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}
