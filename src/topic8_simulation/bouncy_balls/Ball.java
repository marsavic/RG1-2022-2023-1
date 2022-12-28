package topic8_simulation.bouncy_balls;

import javafx.scene.paint.Color;
import mars.geometry.Vector;


public abstract class Ball {
	public abstract Vector getPosition(double time); // Vraca poziciju u trenutku time. 
	public abstract Vector getVelocity(double time); // Vraca brzinu u trenutku time.
	public abstract Color getColor();                // Vraca boju.
}
