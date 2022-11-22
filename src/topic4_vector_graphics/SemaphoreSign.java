package topic4_vector_graphics;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;


public class SemaphoreSign implements Drawing {
	
	private double a = 200;
	
	private double r = 32; // Poluprecnik svetla
	private double d = 12; // Vertikalni razmak izmedju svetla
	@Override
	public void draw(View view) {
		// TODO Auto-generated method stub
				DrawingUtils.clear(view, Color.gray(0.125));
				
				
		
	}

	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}

