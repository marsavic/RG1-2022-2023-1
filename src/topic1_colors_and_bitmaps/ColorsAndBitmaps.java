package topic1_colors_and_bitmaps;


import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetColorPicker;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.drawingx.utils.camera.CameraSimple;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;

public class ColorsAndBitmaps implements Drawing {

	public Image imgSolidColor() {
		
		WritableImage image = new WritableImage(400, 400);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
			}
		}
		
		return image;
	}

	
	public Image imgLinearGradient() {
		
		WritableImage image = new WritableImage(400, 400);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
			}
		}
		
		return image;
	}
	
	
	public Image imgLinearGradient2() {
		
		WritableImage image = new WritableImage(400, 400);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
			}
		}
		
		return image;
	}
	
	

	public Image imgRadialGradient() {
		
		int w = 400;
		int h = 400;
		
		WritableImage image = new WritableImage(w, h);
		PixelWriter pw = image.getPixelWriter();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				
			}
		}
		
		return image;
	}


	public Image imgRadialGradientOpacity() {
		
		int w = 400;
		int h = 400;
		
		WritableImage image = new WritableImage(w, h);
		PixelWriter pw = image.getPixelWriter();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				
			}
		}
		
		return image;
	}

	
	public Image imgFixedHue() {
		WritableImage image = new WritableImage(400, 400);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
			}
		}
		
		return image;
	}

	
	public Image imgFixedSaturation() {
		WritableImage image = new WritableImage(400, 400);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
			}
		}
		
		return image;
	}
	
	
	public Image imgFixedBrightness() {
		WritableImage image = new WritableImage(400, 400);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
			}
		}
		
		return image;
	}
	
	
	public Image imgDisk1() {
		int w = 400;
		int h = 400;
		
		WritableImage image = new WritableImage(w, h);
		PixelWriter pw = image.getPixelWriter();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				
			}
		}
		
		return image;
	}
	
	
	public Image imgDisk2() {
		int w = 400;
		int h = 400;
		
		WritableImage image = new WritableImage(w, h);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				
			}
		}
		
		return image;
	}
	
	
	public Image imgRainbow() {
		int w = 500;
		int h = 500;
		
		WritableImage image = new WritableImage(w, h);
		PixelWriter pw = image.getPixelWriter();
		
		double r0 = 0.5;
		double r1 = 0.75;
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				
			}
		}
		
		return image;
	}
	
	
	public Image imgWave() {
		
		WritableImage image = new WritableImage(500, 200);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
			}
		}
		
		return image;
	}
	
	
	public Image imgTablecloth() {
		
		int w = 410;
		int h = 410;
		
		int d = 10;
		
		WritableImage image = new WritableImage(w, h);
		PixelWriter pw = image.getPixelWriter();
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				
			}
		}
		
		return image;
	}
	
	
	// ============================================================================================
	
	
	Image[] images;
	

	@Override
	public void init(View view) {
		images = new Image[] {
			imgSolidColor(),
			imgLinearGradient(),
			imgLinearGradient2(),
			imgRadialGradient(),
			imgRadialGradientOpacity(),
			
			imgFixedHue(),
			imgFixedSaturation(),
			imgFixedBrightness(),
			imgDisk1(),
			imgDisk2(),
			imgRainbow(),

			imgWave(),
			imgTablecloth(),
		};
		
		view.setImageSmoothing(false);
	}
	
	
	@GadgetColorPicker
	Color colorBackground = new Color(0.2, 0.2, 0.2, 1);
	
	@GadgetInteger(min = 0, max = 16)
	int imageIndex = 0;


	CameraSimple camera = new CameraSimple();
	
	
	
	@Override
	public void draw(View view) {
		view.setTransformation(camera.getTransformation());
		
		DrawingUtils.clear(view, colorBackground);
		view.drawImageCentered(Vector.ZERO, images[imageIndex]);
	}
	
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 600);
	}


	@Override
	public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld, Vector pointerViewBase) {
		camera.receiveEvent(view, event, state, pointerWorld, pointerViewBase);
	}
	
}

