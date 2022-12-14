package topic5_procedural_generation;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.application.Options;
import mars.drawingx.application.parameters.WindowState;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.geometry.Transformation;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;
import mars.random.RNG;
import mars.time.Timer;

import java.util.SortedSet;
import java.util.TreeSet;


interface Art {
	void draw(View view, double depth); // depth ce biti z-udaljenost od kamere
}


record Rock(double brightness) implements Art {
	@Override
	public void draw(View view, double depth) {
		view.setFill(DarkForest.COLOR_BACKGROUND.interpolate(Color.hsb(10, 0.25, brightness), (1 / (1 + depth / 500))));
		view.fillOvalCentered(Vector.ZERO, new Vector(40, 20));
	}
}


class Tree implements Art {
	int nLevels = 7;
	private final long seed;
	Vector d;
	
	public Tree(long seed) {
		this.seed = seed;
		RNG rng = new RNG(seed);
		d = new Vector(0, 700).add(Vector.randomGaussian(rng, 50));
	}

	private void draw(View view, int lvl, double depth, RNG rng) {
		if (lvl == nLevels) return;
		
		view.setLineWidth(60);
		view.setStroke(DarkForest.COLOR_BACKGROUND.interpolate(Color.hsb(110, 1 - Math.pow(0.88, lvl), 1), (1 / (1 + depth / 500))));
		view.strokeLine(Vector.ZERO, d);
		
		for (int i = 0; i < 2; i++) {
			view.stateStore();
			view.addTransformation(Transformation.identity()
							.scale(rng.nextDouble(0.7, 0.9))
							.rotate(rng.nextDouble(-0.05, 0.1))
							.translate(d)
					);
			draw(view, lvl + 1, depth, rng.split());
			view.stateRestore();
		}
	}
	
	@Override
	public void draw(View view, double depth) {
		draw(view, 0, depth, new RNG(seed));
	}
}


record Vec3(double x, double y, double z) {
	public static final Vec3 ZERO = new Vec3(0, 0, 0);
	public static final Vec3 UNIT = new Vec3(1, 1, 1);
	public static Vec3 rnd(RNG rng) { return new Vec3(rng.nextDouble(), rng.nextDouble(), rng.nextDouble()); }
	public Vec3 add(Vec3 o) { return new Vec3(x + o.x, y + o.y, z + o.z);}
	public Vec3 sub(Vec3 o) { return new Vec3(x - o.x, y - o.y, z - o.z);}
	public Vec3 mul(Vec3 o) { return new Vec3(x * o.x, y * o.y, z * o.z);}
	public Vec3 mul(double k) { return new Vec3(k*x, k*y, k*z);}
	public Vector xy() { return new Vector(x, y);}
}


record PositionedArt(Vec3 p, Art art) {}


public class DarkForest implements Drawing {
	
	public static final Color COLOR_BACKGROUND = Color.hsb(10, 0.25, 0.125);
	
	int nTrees = 160;
	int nRocks = 1000;
	double size = 13000;
	Vec3 camera = new Vec3(0, 1200, -0.4 * size);
	Vec3 cameraVelocity = Vec3.ZERO;
	Timer timer = new Timer();
	
	SortedSet<PositionedArt> positionedArts = new TreeSet<>((a, b) -> Double.compare(b.p().z(), a.p().z()));
	// Koristimo komparator koji odrzava skup sortiran opadajuce po z, dakle od nazad ka napred. Tim redosledom ce se iscrtavati.

	
	public DarkForest() {
		RNG rng = new RNG();
	
		// Kreiramo drvece
		for (int i = 0; i < nTrees; i++) {
			Vec3 p = Vec3.rnd(rng).mul(size).add(Vec3.UNIT.mul(-size / 2)).mul(new Vec3(1, 0, 1));
			positionedArts.add(new PositionedArt(p, new Tree(rng.nextLong())));
		}
		
		// Kreiramo kamenje
		for (int i = 0; i < nRocks; i++) {
			Vec3 p = Vec3.rnd(rng).mul(size).add(Vec3.UNIT.mul(-size / 2)).mul(new Vec3(1, 0, 1));
			positionedArts.add(new PositionedArt(p, new Rock(rng.nextDouble(0.2, 0.4))));
		}
	}
	
	
	@GadgetDouble(min = -10000, max = 10000)
	double screenDistance = 1000; // Koliko je udaljena kamera (gledalac) od ekrana;
	double timeLastFrame;
	
	@Override
	public void draw(View view) {
		double timeFrame = timer.getTime();
		double timeDelta = timeFrame - timeLastFrame;
		camera = camera.add(cameraVelocity.mul(timeDelta));
		
		DrawingUtils.clear(view, COLOR_BACKGROUND);
		for (PositionedArt pa : positionedArts) {
			Vec3 pRel = pa.p().sub(camera);
			if (pRel.z() <= 0) continue;

			view.stateStore();
			view.addTransformation(Transformation.identity()
						.translate(pRel.xy())
						.scale(screenDistance / pRel.z())
			);
			pa.art().draw(view, pRel.z());
			view.stateRestore();
		}

		timeLastFrame = timeFrame;
	}
	
	
	public static void main(String[] args) {
		Options options = new Options();
		options.initialWindowState = WindowState.FULL_SCREEN;
		options.constructGui = false;
		options.hideMouseCursor = true;
		DrawingApplication.launch(options);
	}
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		cameraVelocity = new Vec3(
			state.keyDir(KeyCode.RIGHT  ) - state.keyDir(KeyCode.LEFT     ),
			state.keyDir(KeyCode.PAGE_UP) - state.keyDir(KeyCode.PAGE_DOWN),
			state.keyDir(KeyCode.UP     ) - state.keyDir(KeyCode.DOWN     )
		).mul(6000);
	}
}
