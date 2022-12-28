package topic8_simulation.demo.springs;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.drawingx.gadgets.annotations.RecurseGadgets;
import mars.drawingx.utils.camera.CameraSimple;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;
import mars.random.sampling.Sampling;

import java.util.function.DoubleFunction;

public class TestCloth implements Drawing {
	
	@GadgetAnimation
	double time = 0.0;
	
	@RecurseGadgets
	SpringSystem cloth;
	
	int n = 49;
	int m = 33;
	double mass = 10;
	double d = 10;

	CameraSimple camera = new CameraSimple();
	
	
	Node[][] nodes = new Node[n][m];

	
	
	void resetNodePositions() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				nodes[i][j].p = new Vector(i-(n-1.0)/2, j-(m-1.0)/2).mul(d).add(new Vector(Sampling.gaussian(0.01), Sampling.gaussian(0.01)));
				nodes[i][j].v = new Vector(0, 0);
			}
		}
	}
	

	{
		cloth = new SpringSystem(0.0);
		cloth.gravity = new Vector(0, -1000);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				nodes[i][j] = new Node(null, mass / (n*m));
				if (j == m-1 && i % 8 == 0) {
					nodes[i][j].fixed = true;
				}
			}
		}
		resetNodePositions();
		
		for (int i = 0; i < n; i++) {
			cloth.addNodes(nodes[i]);
		}

		DoubleFunction<Double> f = z -> 10 * (z < d ? -1 : 1) *(z-d)*(z-d);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (i < n - 1) cloth.addSpring(new Spring(nodes[i][j], nodes[i+1][j  ], f));
				if (j < m - 1) cloth.addSpring(new Spring(nodes[i][j], nodes[i  ][j+1], f));
			}
		}		
	}
	
	
	@Override
	public void draw(View view) {
		view.setTransformation(camera.getTransformation());

		DrawingUtils.clear(view, Color.gray(0.2));
		
		cloth.updateTo(time);

		
		for (int i = 0; i < n-1; i++) {
			for (int j = 0; j < m-1; j++) {
				Vector[] poly = {
						nodes[i  ][j  ].p,
						nodes[i+1][j  ].p,
						nodes[i+1][j+1].p,
						nodes[i  ][j+1].p,
				};

				double za = poly[0].sub(poly[1]).norm();
				double zb = poly[1].sub(poly[3]).norm();
				double z = za / (za + zb);
				
				view.setFill(Color.hsb(0, 0.7, 0.3 + 0.7*z, 0.8));
				view.fillPolygon(poly);
			}
		}	

		view.setStroke(Color.hsb(0, 0, 0, 0.8));
		view.setLineWidth(0.5);
		for (Spring spring : cloth.springs) {
			Node node0 = spring.endpoints.get(0);
			Node node1 = spring.endpoints.get(1);
			view.strokeLine(node0.p, node1.p);
		}
	}
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
		
		if (event.isKeyPress(KeyCode.ENTER)) {
			resetNodePositions();
		}
	}


	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}	

}
