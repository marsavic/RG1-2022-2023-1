package topic5_procedural_generation;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.application.Options;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.geometry.Transformation;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;
import mars.random.fixed.continuous.PerlinNoise;
import mars.random.sampling.Sampling;


public class Squares implements Drawing {

	int nx = 40;
	int ny = 30;
	Vector n = new Vector(nx, ny);
	
	Vector p = Vector.ZERO;
	PerlinNoise pn;
	
	{
		setup();
	}
	
	
	void setup() {
		pn = new PerlinNoise(Sampling.rng.nextLong());
	}
	
	
	@Override
	public void draw(View view) {
		view.setTransformation(Transformation.scaling(20));
		DrawingUtils.clear(view, Color.BLACK);
		
		for (int ix = 0; ix < nx; ix++) {
			for (int iy = 0; iy < ny; iy++) {
				Vector i = new Vector(ix, iy);
				Vector c = i.mul(2).sub(n).add(new Vector(1, 1));
				
				double vp = pn.getValue(c.x / 20, c.y / 20, 0);     // u [-1, 1]
				double vm = 2 / (1 + p.distanceTo(c) / 6) - 1;      // u [-1, 1]
				
				double k = (vp + vm) / 2;                           // u [-1, 1]
				double k1 = (k+1)/2;                                // k skalirano na [0, 1]
				
				Transformation t = new Transformation().rotate(0.25*k).translate(c);
				
				view.stateStore();
				
				view.addTransformation(t);
				
				view.setFill(Color.gray(k1));
				view.fillRectCentered(Vector.ZERO, new Vector(0.9));
				
				view.setFill(Color.gray(k1 * 0.75));
				view.fillRectCentered(Vector.ZERO, new Vector(0.7));
				
				view.stateRestore();
			}
		}
	}
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		if (event.isKeyPress(KeyCode.ENTER)) {
			setup();
		}
		p = pointerWorld;
	}
	
	
	public static void main(String[] args) {
		Options options = new Options();
		options.drawingSize = new Vector(800, 800);
		options.hideMouseCursor = true;
		DrawingApplication.launch(options);
	}
	
}
