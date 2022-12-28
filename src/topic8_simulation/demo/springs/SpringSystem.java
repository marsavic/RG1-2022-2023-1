package topic8_simulation.demo.springs;

import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetDoubleLogarithmic;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;

import java.util.HashSet;
import java.util.Set;


public class SpringSystem {

	@GadgetDoubleLogarithmic(min = 1e-5, max = 1)
	Double timeTickInterval = 0.001;
	
	private double timeLastUpdate;

	Set<Node> nodes = new HashSet<>();
	Set<Spring> springs = new HashSet<>();
	
	@GadgetVector
	Vector gravity = Vector.ZERO;
	
	@GadgetDouble(min = 0, max = 10)
	double friction = 0.0;
	
	
	
	public SpringSystem(double time) {
		this.timeLastUpdate = time;
	}
	
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	
	public void addNodes(Node... nodes) {
		for (Node node : nodes) {
			addNode(node);
		}
	}
	
	
	public void addSpring(Spring spring) {
		springs.add(spring);
		for (Node n : spring.endpoints) {
			n.springs.add(spring);
		}
	}
	
	
	private void update() {
		timeLastUpdate += timeTickInterval;
		
		nodes.parallelStream().forEach(node -> {
			if (!node.fixed) {
				node.f = Vector.ZERO;
				for (Spring spring : node.springs) {
					Node nodeOther = spring.endpoints.other(node);
					
					double d = node.p.distanceTo(nodeOther.p);
					double t = spring.tensionFunction.apply(d);
					Vector f = (t == 0) ? Vector.ZERO : nodeOther.p.sub(node.p).normalizedTo(t);
					node.f = node.f.add(f);
				}
			}
		});
		
		nodes.parallelStream().forEach(node -> {
			if (!node.fixed) {
				Vector a = node.f.div(node.mass).add(gravity);
				double speed = node.v.norm();
				if (speed > 0) {
					a = a.sub(node.v.normalizedTo(friction*speed));
				}

				
				node.v = node.v.add(a.mul(timeTickInterval));
				node.p = node.p.add(node.v.mul(timeTickInterval)).add(a.mul(timeTickInterval*timeTickInterval/2));
			}
		});
	}
	
	
	public void updateTo(double time) {
		while (timeLastUpdate + timeTickInterval <= time) {
			update();
		}
	}
}
