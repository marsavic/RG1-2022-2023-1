package topic7_bezier_curves;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.geometry.Vector;


public class Flower implements Drawing {
	
	@GadgetAnimation(start = true)
	double time = 0.0;
	
	Vector o  = new Vector( 0, -200);  // Koren cveta
	
	// levi list
	
	
	// stabljika
	

	// desni list
	
	
	// cvet
	@GadgetDouble(min = 0, max = 100)
	double rDisk  = 35;                   // poluprecnik
	
	@GadgetDouble(min = 0, max = 200)
	double lPetal = 100;                  // duzina latice
	
	@GadgetInteger
	int nPetals = 5;                      // broj latica
	
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		// levi list
		
		

		// stabljika
		
		
		
		// desni list
		
		


		// latice
		
		

		// centar cveta
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
	
}

