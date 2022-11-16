package topic4_vector_graphics;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Transformation;
import mars.geometry.Vector;


public class SquishyFace implements Drawing {

	double r = 50.0;                       // Poluprecnik glave.
	double xEye = 17.5;                    // Udaljenost oka od centra po x-osi.
	double yEye = 17.5;                    // Udaljenost oka od centra po y-osi.
	double rEye = 7.5;                     // Poluprecnik oka.
	double rMouth = 32.5;                  // Poluprecnik luka usta.
	double phiMouth = 1.0/3;               // Ugao luka usta.

	double rWall = 350;                    // Poluprecnik zida.
	
	
	@GadgetVector
	Vector p = Vector.ZERO;
	
	
	private void drawFace(View view) {
		// Glava
		view.setFill(Color.hsb(60, 0.9, 0.9));
		view.fillCircleCentered(new Vector(0, 0), r);
		
		// Oci
		view.setFill(Color.hsb(0, 0, 0));
		view.fillCircleCentered(new Vector(-xEye, yEye), rEye);
		view.fillCircleCentered(new Vector(xEye, yEye), rEye);
		
		// Usta
		view.setLineWidth(7.5);
		view.setStroke(Color.hsb(0, 0, 0));
		view.strokeArcCentered(new Vector(0, 0), new Vector(rMouth), 0.75 - phiMouth/2, phiMouth);
	}
	
	
	@Override
	public void draw(View view) {
		view.stateStore();
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		// Crtamo zid
		view.setFill(Color.gray(0.375));
		view.fillCircleCenteredInverted(Vector.ZERO, rWall);       // Crta invertovani krug, tj. boji spoljasnost kruga.
		view.setFill(Color.gray(0.25));
		view.fillCircleCenteredInverted(Vector.ZERO, rWall + 8);   // Jos jedan invertovani krug, malo veceg poluprecnika.
		
		
		double d = p.norm();                   // Udaljenost kursora od koordinatnog pocetka
		double phi = p.angle();                // Ugao izmedju kursora i x-ose (u okretima)

		Vector c;                              // Konacna pozicija centra smajlija
		double k;		                       // Faktor kompresije smajlija. Broj iz (0, 1], gde 1 znaci da nije kompresovan, a 0 da je skroz spljosten.
		
		if (d <= rWall - r) {                  // Ukoliko je smajli u potpunosti unutar granica:
			k = 1;                             // - ne kompresujemo ga
			c = p;                             // - centar mu je u kursoru
		} else {                               // Ukoliko izlazi iz granica:
			k = (rWall - r) / d;               // - izracunavamo kompresiju nekom opadajucom funkcijom po d koja za d = rWall-r daje 1, a tezi 0 kad d tezi beskonacnosti. 
			c = p.normalizedTo(rWall - r * k); // - racunamo centar tako da kompresovan smajli dodiruje zid
		}
		
		Transformation t = new Transformation()
				.rotate(-phi)                  // Pre-rotacija, da bi nakon post-rotacije ostao isto orijentisan.
				.scale(k, 1/k)                 // Skaliramo x koordinate k puta, y koordinate 1/k puta. Povrsina je ocuvana. (Walt Disney's principles of animation)
				.rotate(phi)                   // Post-rotacija. Rotiramo ga tako da pravac po kome se kompresuje bude upravo pravac od koordinatnog pocetka do centra smajlija.
				.translate(c)                  // Pomeramo ga u izracunati centar.
				;
		
		view.setTransformation(t);
		drawFace(view);
		
		view.stateRestore();
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}
}
