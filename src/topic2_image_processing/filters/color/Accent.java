package topic2_image_processing.filters.color;

import javafx.scene.paint.Color;
import topic2_image_processing.filters.ColorFilter;

/**
 * Akcentuje boju ciji je hue zadat u parametru konstruktora filtera. Sve boje koje imaju hue blizu zadatog zadrzava
 * neizmenjene, dok sve ostale desaturira u sivu.
 */
public class Accent extends ColorFilter {
	final double accentHue;      // Koji hue akcentujemo
	final double delta = 30;     // Koliko odstupanje dozvoljavamo
	
	
	public Accent(double hue) {
		this.accentHue = hue;
	}


	@Override
	public Color processColor(Color input) {
		return input;
	}
	
}