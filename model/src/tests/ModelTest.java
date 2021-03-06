package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Vector2f;

import modeler.MainFrame;
import solution.manip.Manip;
import solution.scene.Transformation;
import solution.shape.Cube;


public class ModelTest {
	final static double EPS = 1e-2;
	final static double GAMMA = 2.2;
	final static int LOW_8_BITS = 0xFF;
	final static double MAX_BYTE = 255.0;				
	final String testDirectory = "scenes/";

	public void compareImages(MainFrame m, String test) {
		BufferedImage studentImage = m.writeImage(testDirectory + test + ".png");
		
		m.setVisible(false);
		m.dispose();

		System.out.println("studentImage size: " + studentImage.getWidth() + " " + studentImage.getHeight());
		BufferedImage correctImage = null;
		try {
			correctImage = ImageIO.read(new File(testDirectory + test + ".correct.png"));
		} catch (IOException e) {
			fail("Failed to load correct image " + testDirectory + test + ".correct.png");
		}

		// compare images 
		assertEquals("Image has the wrong width.", correctImage.getWidth(), studentImage.getWidth());
		assertEquals("Image has the wrong height.", correctImage.getHeight(), studentImage.getHeight());
		int numErrors = 0;
		int maxErrors = 10;
		String s = null;
		for (int y = 0; y < correctImage.getHeight(); y++) {
			for (int x = 0; x < correctImage.getWidth(); x++) {
				int correctRGB = correctImage.getRGB(x, y);
				int studentRGB = studentImage.getRGB(x, y);
				int correctR = correctRGB & LOW_8_BITS;
				int correctG = (correctRGB >> 8) & LOW_8_BITS;
				int correctB = (correctRGB >> 16) & LOW_8_BITS;
				int studentR = studentRGB & LOW_8_BITS;
				int studentG = (studentRGB >> 8) & LOW_8_BITS;
				int studentB = (studentRGB >> 16) & LOW_8_BITS;
				if (
						(((correctRGB >> 0) & LOW_8_BITS) - ((studentRGB >> 0) & LOW_8_BITS) > 1) || 
						(((correctRGB >> 8) & LOW_8_BITS) - ((studentRGB >> 8) & LOW_8_BITS) > 1) ||
						(((correctRGB >> 16) & LOW_8_BITS) - ((studentRGB >> 16) & LOW_8_BITS) > 1)
					) {
					if (s == null) {
						s = "; correct(" + correctR + ", " + correctG + ", " + correctB + ")";
						s += " student(" + studentR + ", " + studentG + ", " + studentB + ")";
					}
					numErrors += 1;
					if (numErrors >= maxErrors) {
						fail("Exceeds " + maxErrors + " errors; Image(x=" + x + ", y=" + y +"); " + s);
					}
					
				}
				
//				assertEquals("Image(x=" + x + ", y=" + y + ") has the wrong red value" + s, correctRGB & LOW_8_BITS, studentRGB & LOW_8_BITS, 1);
//				assertEquals("Image(x=" + x + ", y=" + y + ") has the wrong green value" + s, (correctRGB >> 8) & LOW_8_BITS, (studentRGB >> 8) & LOW_8_BITS, 1);
//				assertEquals("Image(x=" + x + ", y=" + y + ") has the wrong blue value" + s, (correctRGB >> 16) & LOW_8_BITS, (studentRGB >> 16) & LOW_8_BITS, 1);
			}
		}		
	}

	public void runTest(String test) throws InterruptedException {
		String filePath = testDirectory + test + ".xml";
		MainFrame m = new MainFrame(false);
		m.setVisible(true);
		
		try { Thread.sleep(500); } finally {};
		m.openTree(filePath);
		
		compareImages(m, test);
		
	}

	public MainFrame setupManip(Class<? extends Manip> c, int axis, Vector2f mousePosition, Vector2f mouseDelta) throws InterruptedException {
		MainFrame m = new MainFrame(false);
		m.setVisible(true);
		
		try { Thread.sleep(1000); } finally {};
		
		Transformation t = m.addNewShape(Cube.class);
		try {
			m.currentManip = c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.currentManip.setTransformation(t);
		m.refresh();
		m.currentManip.setPickedInfo(axis, m.pViewCam, mousePosition);
		m.currentManip.dragged(mousePosition, mouseDelta);
		m.refresh();
		return m;
	}

}