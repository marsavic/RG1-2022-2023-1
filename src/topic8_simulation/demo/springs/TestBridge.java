package topic8_simulation.demo.springs;

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

public class TestBridge implements Drawing {
	
	@RecurseGadgets
	SpringSystem bridge;
	
	int n = 100;
	double m = 0.3;
	double l = 600;

	@GadgetAnimation
	Double time = 0.0;
	
	CameraSimple camera = new CameraSimple();
	
	
	{
		bridge = new SpringSystem(0.0);
		bridge.gravity = new Vector(0, -1000);
		
		Node[] nodes = new Node[n];
		for (int i = 0; i < n; i++) {
			nodes[i] = new Node(new Vector(l*(i/(n-1.0) - 0.5), 300), m / n);
		}
		nodes[0  ].fixed = true;
		nodes[n-1].fixed = true;
		
		bridge.addNodes(nodes);
		for (int i = 0; i < n-1; i++) {
			bridge.addSpring(new Spring(nodes[i], nodes[i+1], d -> d*d));
		}		
	}
	
	
	@Override
	public void draw(View view) {
		view.addTransformation(camera.getTransformation());

		DrawingUtils.clear(view, Color.gray(0.2));
		
		bridge.updateTo(time);
		
		view.setFill(Color.gray(0.9));
		view.setStroke(Color.gray(0.6));
		view.setLineWidth(2);
		for (Spring spring : bridge.springs) {
			Node node0 = spring.endpoints.get(0);
			Node node1 = spring.endpoints.get(1);
			view.strokeLine(node0.p, node1.p);
		}
		for (Node node : bridge.nodes) {
			view.fillCircleCentered(node.p, 3);
		}
	}
	
	
	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}


	public static void main(String[] args) {
		DrawingApplication.launch(800, 800);
	}	

}
