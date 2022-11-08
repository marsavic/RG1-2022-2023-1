package topic2_image_processing.filters.binary;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import topic2_image_processing.filters.BinaryFilter;

/**
 * Menja piksele prve slike koji imaju hue koji je blizak zadatom pikselu, sa pikselima iz druge slike.
 * Ostale piksele ostavlja u originalnoj boji. 
 */
public class ChromaKey extends BinaryFilter {
	final double hue;            // Koji hue menjamo
	final double delta = 20;     // Koliko odstupanje dozvoljavamo


	public ChromaKey(double hue) {
		this.hue = hue;
	}



	@Override
	public Image process(Image input1, Image input2) {
		final int w = (int) input1.getWidth();
		final int h = (int) input1.getHeight();
		
		if (input2.getWidth() != w || input2.getHeight() != h) {
			throw new IllegalArgumentException("Input images must have the same size.");
		}

		WritableImage output = new WritableImage(w, h);

		PixelReader pr1 = input1.getPixelReader();
		PixelReader pr2 = input2.getPixelReader();
		PixelWriter pw = output.getPixelWriter();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				
			}
		}
		
		return output;
	}
	
}
