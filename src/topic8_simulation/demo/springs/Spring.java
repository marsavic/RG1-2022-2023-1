package topic8_simulation.demo.springs;

import mars.tuples.Pair;

import java.util.function.DoubleFunction;


public class Spring {
	public Pair<Node> endpoints;
	public DoubleFunction<Double> tensionFunction;
	
	
	public Spring(Node endpoint0, Node endpoint1, DoubleFunction<Double> tensionFunction) {
		this.endpoints = new Pair<>(endpoint0, endpoint1);
		this.tensionFunction = tensionFunction;
	}

}
