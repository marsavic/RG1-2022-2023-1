package topic8_simulation.demo;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.application.Options;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.*;
import mars.drawingx.utils.camera.CameraSimple;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;
import mars.random.RNG;
import mars.random.fixed.continuous.FractalNoise1D;
import mars.utils.Numeric;

import java.util.Arrays;
import java.util.function.UnaryOperator;


class Unit {
	int type;
	Vector p, v, f;
	
	
	public Unit(int type, Vector p, Vector v) {
		this.type = type;
		this.p = p;
		this.v = v;
	}
}


public class SmoothLife implements Drawing {
	@GadgetAnimation(start = false, speed = 1)
	double time;

//	@GadgetDoubleLogarithmic(min = 1e-6, max = 1)
	double timeTickInterval = 1.0/75;

	
	@GadgetLongRandomStream(seed = 130985409318l)
	Long seed = 0l;
	Long seedLast = null;
	
	@GadgetDouble(min = 0, max = 6)
	double friction = 3;
	
	
	static final double size = 700;
	double sizeFactor = 2.0;

	@GadgetDouble(min = 10, max = size/2)
	double forceRadius = 100;
	
	@GadgetDoubleLogarithmic(min = 0.1, max = 10)
	double forceRuggedness = 2;

	Vector boxD = new Vector(size);
	Vector boxP = boxD.div(-2);
	

	double massMin = 1;
	double massMax = 3;
	
	
	@GadgetInteger(min = 1, max = 6)
	int nTypes = 6;
	int nTypesLast = 0;
	
	double[] mass;
	double[] radius;
	UnaryOperator<Vector>[][] interactions;

	int nUnits = 200;
	Unit[] units;
	

	RNG rng;

	
	CameraSimple camera = new CameraSimple();
	Unit unitFollow = null;
	Vector offset = Vector.ZERO;

	double timeTickNext;

	
	void init() {
		time = 0.0;
		timeTickNext = time + timeTickInterval;
		rng = new RNG(seed);
		initTypes();
		initUnits();
	}

	
	@SuppressWarnings("unchecked")
	void initTypes() {
		mass = new double[nTypes];
		radius = new double[nTypes];
		for (int it = 0; it < nTypes; it++) {
			mass[it] = massMin * Math.pow(massMax / massMin, rng.nextDouble());
			radius[it] = Math.sqrt(mass[it]);
		}
		
		interactions = new UnaryOperator[nTypes][nTypes];
		
		for (int itA_ = 0; itA_ < nTypes; itA_++) {
			for (int itB_ = 0; itB_ < nTypes; itB_++) {
				final int itA = itA_;
				final int itB = itB_;
		
				final FractalNoise1D fn = new FractalNoise1D(3, Numeric.PHI, Numeric.PHI, rng.nextLong());
				
				interactions[itA][itB] = (Vector r) -> {
					double d = r.norm();
					Vector rn = r.normalized();
					Vector f = Vector.ZERO;
					
					double s = d - sizeFactor * (radius[itA] + radius[itB]); 
					if (s < 0) {
						f = f.add(rn.mul(600*s));
					}
					if (d < forceRadius) {
						double x = d / forceRadius;
						f = f.add(rn.mul(d * mass[itB] * fn.getGaussian(x*forceRuggedness) * (1 - x*x*x*x)));
					}
					
					return f;
				};
			}
		}
	}
	
	
	void initUnits() {
		units = new Unit[nUnits];
		for (int iu = 0; iu < nUnits; iu++) {
			units[iu] = new Unit(
					rng.nextInt(nTypes),
					Vector.randomInBox(rng, boxD),
					Vector.randomGaussian(rng, 0)
			);
		}
	}


	Vector force(Unit a) {
		Vector f = Vector.ZERO;
		for (Unit b : units) {
			if (a != b) {
				Vector d = Vector.f((x, m) -> (x < m-x ? -x < x+m ? x : x+m : -(m-x)), b.p.sub(a.p).mod(boxD), boxD);
				f = f.add(interactions[a.type][b.type].apply(d));
			}
		}
		return f;
	}
	
	
	void update() {
		Arrays.stream(units).parallel().forEach(unit -> {
			unit.f = force(unit);
		});

		Arrays.stream(units).parallel().forEach(unit -> {
			Vector a = unit.f.div(mass[unit.type]);
			double speed = unit.v.norm();
			if (speed > 0) {
				a = a.sub(unit.v.normalizedTo(friction*speed));
			}
			unit.v = unit.v.add(a.mul(timeTickInterval));
			unit.p = unit.p.add(unit.v.mul(timeTickInterval)).add(a.mul(timeTickInterval*timeTickInterval/2)).mod(boxD);
		});

		timeTickNext += timeTickInterval;
	}

	
	Vector getDisplayPosition(Vector v) {
		return v.mod(boxP.add(offset), boxD);	
	}
	

	@Override
	public void draw(View view) {
		if (seed != seedLast || nTypes != nTypesLast) {
			init();
			seedLast = seed;
			nTypesLast = nTypes;

		}
		
		while (timeTickNext <= time) {
			update();
		}

		
		view.setTransformation(camera.getTransformation());
		if (unitFollow != null) {
			camera.setWorldFocusPoint(getDisplayPosition(unitFollow.p));
		}
		offset = camera.getWorldFocusPoint();

		
		DrawingUtils.clear(view, Color.gray(0.0));
		view.setLineWidth(1.0);
		view.setStroke(Color.gray(1, 0.2));
		view.strokeRect(boxP.add(offset), boxD);
		
		for (Unit unit : units) {
			view.setFill(Color.hsb(360.0 * unit.type / nTypes, 0.6, 1.0));
			view.fillCircleCentered(getDisplayPosition(unit.p), sizeFactor * radius[unit.type]);
		}
	}

	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		if (event.isKeyPress(KeyCode.A)) {
			double dBest = Double.POSITIVE_INFINITY;
			for (Unit unit : units) {
				double d = pointerWorld.distanceTo(getDisplayPosition(unit.p));
				if (d < dBest) {
					dBest = d;
					unitFollow = unit;
				}
			}
		}
		
		if (event.isKeyPress(KeyCode.SEMICOLON)) {
			unitFollow = null;
		}
		
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}	

	
	public static void main(String[] args) {
		Options options = new Options();
		options.drawingSize = new Vector(SmoothLife2.size + 100);
		
		DrawingApplication.launch(options);
	}
}
