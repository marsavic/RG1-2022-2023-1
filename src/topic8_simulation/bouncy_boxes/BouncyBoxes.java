package topic8_simulation.bouncy_boxes;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.drawingx.gadgets.annotations.GadgetBoolean;
import mars.geometry.Vector;


public class BouncyBoxes implements Drawing {

	final Vector areaP = new Vector(-300, -200);
	final Vector areaD = new Vector( 600,  400);
	
	int nBoxes = 16;
	Box[] boxes;

	Vector r = new Vector(10);
	
	
	@GadgetAnimation
	double time = 0.0;

	@GadgetBoolean
	boolean showVelocities = false;

	
	
	{
		boxes = new Box[nBoxes];
		for (int i = 0; i < nBoxes; i++) {
			boxes[i] = new Box_Formula(
//			boxes[i] = new Box_Events(
//			boxes[i] = new Box_Ticks(
					areaD,
					Vector.randomInBox(areaD),
					Vector.randomGaussian(100),
					Color.hsb(360.0 * i / nBoxes, 0.7, 0.9)
			);
		}
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.hsb(0, 0, 0.3));
		
		view.setFill(Color.hsb(0, 0, 0, 0.3));
		view.fillRect(areaP.sub(r), areaD.add(r.mul(2)));		

		view.setStroke(Color.WHITE);
		for (Box box : boxes) {			
			Vector p = areaP.add(box.getPosition(time));
			Vector v = box.getVelocity(time);

			view.setFill(box.getColor());
			view.fillRectCentered(p, r);
			
			if (showVelocities) {
				DrawingUtils.drawArrow(view, p, v.mul(0.3));
			}
		}
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 600);
	}

}
