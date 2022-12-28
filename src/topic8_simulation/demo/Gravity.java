package topic8_simulation.demo;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.drawingx.gadgets.annotations.GadgetBoolean;
import mars.drawingx.gadgets.annotations.GadgetDoubleLogarithmic;
import mars.drawingx.utils.camera.CameraSimple;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;
import mars.random.RNG;

import java.util.stream.IntStream;


class Star {
	Vector p, v, a, pN, vN, aN;
	double m;
	double r;
	
	
	public Star(Vector p, Vector v, double m) {
		this.p = p;
		this.v = v;
		this.m = m;
		a = Vector.ZERO;
		r = Math.pow(Math.abs(m), 1.0/3);
	}
	
	
	public void setN(Vector pN, Vector vN, Vector aN) {
		this.pN = pN;
		this.vN = vN;
		this.aN = aN;
	}
	
	
	public void advance() {
		p = pN;
		v = vN;
		a = aN;
	}
}


public class Gravity implements Drawing {
	@GadgetAnimation
	Double time = 0.0;
	
	@GadgetBoolean
	Boolean clear = true;
	
	@GadgetBoolean
	Boolean showVelocity = false;
	
	@GadgetBoolean
	Boolean showAcceleration = false;
	
	@GadgetDoubleLogarithmic(min = 1e-6, max = 1)
	Double timeTickInterval = 0.005;
	
	@GadgetDoubleLogarithmic(min = 0.01, max = 100000)
	Double gamma = 100.0;

	@GadgetDoubleLogarithmic(min = 1, max = 1000000)
	Double mag = 1.0;

	CameraSimple camera = new CameraSimple();

	double timeTickNext = 0.0 + timeTickInterval;
	
	double pStD = 100.0;
	double vStD = 10.0;
	double massMin = 1;
	double massMax = 2;
	double minJoinDist = 1;
	

	RNG rng;

	int nStars = 600;
	Star[] stars;
	
	int iCenter = -1;

	
	
	Vector acceleration(int i) {
		Vector a = Vector.ZERO;
		for (int j = 0; j < nStars; j++) {
			if (i != j) {
				Vector d = stars[j].p.sub(stars[i].p);
				double l = d.norm();
				a = a.add(d.mul(gamma * stars[j].m / (l * l * l)));
			}
		}
		return a;
	}
	
	
	@Override
	public void init(View view) {
		rng = new RNG();
		
		stars = new Star[nStars];
		for (int i = 0; i < nStars; i++) {
			Vector p = Vector.randomGaussian(rng, pStD);
			Vector v = Vector.randomGaussian(rng, vStD);
			stars[i] = new Star(
					p,
					v,
					massMin * Math.pow(massMax/massMin, rng.nextDouble())
//					random.nextDouble() < 0.5 ? -1 * random.nextDouble() : 9 * random.nextDouble()
			);
		}
	
		Vector imp = Vector.ZERO;
		double m = 0;
		for (Star s : stars) {
			imp = imp.add(s.v.mul(s.m));
			m += s.m;
		}
		imp = imp.div(m);
		
		for (Star s : stars) {
			s.v = s.v.sub(imp);
		}

		view.setEffect(new GaussianBlur(2));
	}
	
	
	@Override
	public void draw(View view) {
		while (timeTickNext <= time) {
			// Provera da li se desio sudar.
			
			IntStream.range(0, nStars).parallel().forEach(i -> {
				Vector pi = stars[i].p;
				Vector vi = stars[i].v;
				double mi = stars[i].m;
				
				for (int j = i + 1; j < nStars; j++) {
					Vector pj = stars[j].p;
					Vector vj = stars[j].v;
					double mj = stars[j].m;
					
					if (pi.sub(pj).norm() < 0.4 * (stars[i].r + stars[j].r)) {
						stars[i] = new Star(
								pi.mul(mi).add(pj.mul(mj)).div(mi+mj),
								vi.mul(mi).add(vj.mul(mj)).div(mi+mj),
								mi + mj
						);
						stars[j] = stars[--nStars];
					}
				}
			});

			// Update-ovanje stanje.
			
			IntStream.range(0, nStars).parallel().forEach(i -> {
				Star star = stars[i];
				Vector a = acceleration(i);
				Vector v = star.v.add(a.mul(timeTickInterval));
				Vector p = star.p.add(star.v.mul(timeTickInterval)).add(a.mul(timeTickInterval*timeTickInterval/2));
				star.setN(p, v, a);
//				star.advance();
			});

			IntStream.range(0, nStars).parallel().forEach(i -> {
				stars[i].advance();
			});
			
			timeTickNext += timeTickInterval;
		}

		
		view.setTransformation(camera.getTransformation());
		
		if (clear) {
			DrawingUtils.clear(view, Color.hsb(0, 0, 0));
//			DrawingUtils.drawGrid(view, 100, 4, Color.hsb(0, 0, 0.1));
		}
		
		
		if (iCenter >= 0) {
			camera.setWorldFocusPoint(stars[iCenter].p);
		}
		
		// Iscrtavanje.

		view.setLineWidth(mag);
		
		for (int i = 0; i < nStars; i++) {
			if (showAcceleration) {
				view.setStroke(Color.hsb(210, 0.6, 0.8));
				DrawingUtils.drawArrow(view, stars[i].p, stars[i].a.mul(mag));
			}
			if (showVelocity) {
				view.setStroke(Color.hsb(330, 0.6, 0.8));
				DrawingUtils.drawArrow(view, stars[i].p, stars[i].v.mul(mag));
			}
			double k = Math.tanh(0.1 * stars[i].r);
			view.setFill(Color.hsb(stars[i].m > 0 ? 30 : 210, 0.6 * (1-k), 0.8 + 0.2*k, 1.0));
			view.fillCircleCentered(stars[i].p, stars[i].r * mag);
		}
		
//		DrawingUtils.drawInfoText(view, "Stars: " + nStars);
	}

	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		if (event.isKeyPress(KeyCode.A)) {
			double dBest = Double.POSITIVE_INFINITY;
			for (int i = 0; i < nStars; i++) {
				double d = pointerWorld.distanceTo(stars[i].p);
				if (d < dBest) {
					dBest = d;
					iCenter = i;
				}
			}
		}
		
		if (event.isKeyPress(KeyCode.SEMICOLON)) {
			iCenter = -1;
		}
		
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}	

	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}
}
