package topic8_simulation;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.geometry.Transformation;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;



public class Drive implements Drawing {
	
	@GadgetAnimation(start = true)
	double time = 0.0;
	
	double intervalTick = 1.0 / 300;  // Interval azuriranja fizike
	
	
	
	double kFriction = -1.0;          // Koficijent trenja (negativan, jer deluje u pravcu suprotnom od brzine)

	double t = 0.0;                   // Trenutak u kome je poslednji put azurirano
	Vector p = Vector.ZERO;           // Polozaj u tom trenutku
	Vector v = Vector.ZERO;           // Brzina u tom trenutku
	
	
	Vector fDrive;                    // Sila koja potice od "gasa"
	boolean drive = false;            // Da li pritiskamo gas
	Vector pointer = Vector.ZERO;     // Tacka ka kojoj deluje ta sila
	double forceIntensity = 800.0;    // Intenzitet sile koja deluje na kola kad je pritisnut gas
	
	
	
	void update() {
		fDrive = drive ? pointer.sub(p).normalizedTo(forceIntensity) : Vector.ZERO;

		Vector fFriction = v.mul(kFriction);  // Stavljamo da je trenje proporcionalno brzini (iako to bas nije)
		Vector a = fDrive.add(fFriction);     // Ubrzanje je zbir svih sila podeljen sa masom a = F/m (smatramo da je masa 1)
		
		p = p.add(v.mul(intervalTick)).add(a.mul(intervalTick * intervalTick / 2)); // p = p + v*dt + 0.5*a*dt^2
		v = v.add(a.mul(intervalTick));                                             // v = v + a*dt
		t += intervalTick;                                                          // t = t + dt
	}
	
	
	void updateTo(double time) {
		while (t + intervalTick <= time) {
			update();
		}
	}
	
	
	void drawCar(View view) {
		view.stateStore();
		
		view.addTransformation(Transformation.identity()
				.rotate(v.angle())
				.translate(p)
				);
		
		view.setFill(Color.hsb(0, 0.0, 0.25));
		view.fillRect(new Vector(-30, -30), new Vector(20, 60));
		view.fillRect(new Vector( 10, -30), new Vector(20, 60));
		
		view.setFill(Color.hsb(0, 0.7, 0.8));
		view.fillRect(new Vector(-40, -20), new Vector(80, 40));
		
		view.stateRestore();
	}
	
	
	@Override
	public void draw(View view) {
		DrawingUtils.clear(view, Color.gray(0.125));
		
		updateTo(time);
		
		drawCar(view);
	}
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		drive = state.mouseButtonPressed(1); // Levi klik 1, a desni klik 3
		pointer = pointerWorld;
	}	
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}
	
}
