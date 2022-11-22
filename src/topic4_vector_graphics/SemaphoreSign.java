package topic4_vector_graphics;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;


public class SemaphoreSign implements Drawing {
	
	private double a = 200;
	private Vector[] corners = new Vector[] {
			new Vector(a, 0),
			new Vector(0, a),
			new Vector(-a, 0),
			new Vector(0, -a),
	};
	
	private double r = 32; // Poluprecnik svetla
	private double d = 12; // Vertikalni razmak izmedju svetla
	@Override
	public void draw(View view) {
		// TODO Auto-generated method stub
				DrawingUtils.clear(view, Color.gray(0.125));
				
				view.setFill(Color.YELLOW);
				view.fillPolygon(corners);
				view.setStroke(Color.BLACK);
				view.setLineWidth(15);
				view.strokePolygon(corners);
				
				view.setFill(Color.BLACK);
				view.fillRectCentered(Vector.ZERO, new Vector(r + d, 2 * d + 3 * r));
				
				view.setFill(Color.YELLOW);
				view.fillCircleCentered(Vector.ZERO, r);
				
				view.setFill(Color.GREEN);
				view.fillCircleCentered(new Vector(0, 2 * r + d), r);
				
				view.setFill(Color.RED);
				view.fillCircleCentered(new Vector(0, -2 * r - d), r);
		
	}

	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}
