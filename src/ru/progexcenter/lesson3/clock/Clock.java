

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.sun.istack.internal.NotNull;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Class for displaying clock on frame
 */
public class Clock {
	protected static Texture clockTexture;
	protected static Texture hourArrowTexture;
	protected static Texture minArrowTexture;
	protected static Texture secArrowTexture;
	//hard-coded image information
	protected static final float sec_h = 187f, sec_w = 20f, min_h = 142f, min_w = 19, hour_h = 105, hour_w = 48,
			center_x = 378f/2, center_y = 378f/2, sec_center_y = 50f, min_center_y = 9.5f, hout_center_y =  12f;

	/**
	 * Initialize texture variables
	 */
	protected static void loadTextures(){
		try{
			//возможны проблемы с путями - укажите абсолютный путь в таком случае.
			String path = "";
			clockTexture =  TextureIO.newTexture(new File(path + "clock.png"), false);
			hourArrowTexture =  TextureIO.newTexture(new File(path + "hour.png"), false);
			minArrowTexture =  TextureIO.newTexture(new File(path + "minute.png"), false);
			secArrowTexture =  TextureIO.newTexture(new File(path + "second.png"), false);
		}catch (IOException ex){
			System.out.println("cannot load textures: " + ex);
			System.exit(1);
		}
	}

	/**
	 * Setup OpenGL projection matrix for given width and height
	 * @param gl2 OpenGL context
	 * @param width of window
	 * @param height of window
	 */
	protected static void setup( GL2 gl2, int width, int height) {
		loadTextures();
		gl2.glMatrixMode( GL2.GL_PROJECTION );
		gl2.glLoadIdentity();

		// coordinate system origin at lower left with width and height same as the window
		GLU glu = new GLU();
		glu.gluOrtho2D( 0.0f, width, 0.0f, height );

		gl2.glMatrixMode( GL2.GL_MODELVIEW );
		gl2.glLoadIdentity();

		//very nice :3
		gl2.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NICEST);
		gl2.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NICEST);


		gl2.glEnable(GL.GL_BLEND);
		gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		gl2.glEnable(GL.GL_TEXTURE_2D);
		gl2.glViewport( 0, 0, width, height );
	}

	protected static void render( GL2 gl2, int width, int height ) {
		gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
		gl2.glClearColor(1,1,1,0);
		// draw a triangle filling the window
		gl2.glLoadIdentity();
		gl2.glEnable(GL.GL_BLEND);
		gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		//draw clock background
		Calendar c = Calendar.getInstance();
		drawTexture(gl2, clockTexture, getInitPosition(width, height));

		drawHoursArrow(gl2, c);
		drawMinutesArrow(gl2, c);
		drawSecondsArrow(gl2, c);
		gl2.glFlush();
	}

	static void drawSecondsArrow(GL2 gl2, Calendar calendar){
		//TODO get angle of arrow, use #getArrowPoints, use #drawTexture
		
	}

	static void drawMinutesArrow(GL2 gl2, Calendar calendar){
		//TODO get angle of arrow, use #getArrowPoints, use #drawTexture
		
	}

	static void drawHoursArrow(GL2 gl2, Calendar calendar){
		//TODO get angle of arrow, use #getArrowPoints, use #drawTexture

	}

	/**
	 * Get actual points of clock arrow.
	 * @param width (pixels) of arrow image
	 * @param height (pixels) of arrow image
	 * @param centerX (pixels) of arrow in image
	 * @param centerY (pixels)
	 * @param angle (radians) of arrow at current time
	 * @return actual points array in format of @see #drawTexture
	 */
	static float[][] getArrowPoints(float width, float height, float centerX, float centerY, float angle){
		//TODO #getInitPosition, translatePoints to center, rotate to angle, translate to center of window
		//center of window is: center_x, center_y
		float[][] points; 
		
		return points;
	}

	/**
	 * Draws quad with texture.
	 * @param gl2 OpenGL context
	 * @param texture Loaded texture object
	 * @param points array of 4 points (which is array 2 coordinates [x,y]) in order:
	 *               [down-left, up-left, up-right, down-right]
	 */
	static void drawTexture(GL2 gl2, Texture texture, float[][] points){
		texture.bind(gl2);
		gl2.glBegin( GL.GL_TRIANGLE_STRIP );
			gl2.glTexCoord2f(0.0f, 1.0f);
			gl2.glVertex2f(points[1][0] , points[1][1]);
			gl2.glTexCoord2f(0.0f, 0.0f);
			gl2.glVertex2f(points[0][0], points[0][1]);
			gl2.glTexCoord2f(1.0f, 1.0f);
			gl2.glVertex2f(points[2][0], points[2][1]);
			gl2.glTexCoord2f(1.0f, 0.0f);
			gl2.glVertex2f(points[3][0], points[3][1]);
		gl2.glEnd();		
	}

	/**
	 * Creates quad coordinates.
	 * @param width of quad
	 * @param height of quad
	 * @return array of points in correct format (@see Clock.drawTexture)
	 */
	static float[][] getInitPosition(float width, float height){
		return new float[][]{{0, 0}, {0, height}, {width, height}, {width, 0}};
	}

	/**
	 * Matrix multiplication
	 * @param left matrix (2d float array) with size MxK
	 * @param right matrix (2d float array) with size KxN
	 * @return matrix (2d float array) with size MxN
	 */
	static float[][] multiply(float[][] left, float[][] right){
		//TODO implement multiplication
		float[][] C = new float[left.length][right[0].length];
		
		return C;
	}

	/**
	 * Matrix-vector multiplication
	 * @param left vector (1d float array) with size 1xK
	 * @param right matrix (2d float array) with size KxN
	 * @return vector (1d float array) with size 1xN
	 */
	@NotNull
	static float[] multiply(float[] left, float[][] right){
		return multiply(new float[][]{left},right)[0];
	}

	/**
	 * TODO see https://ru.wikipedia.org/wiki/Матрица_поворота
	 * @param angle (radians) for rotate
	 * @return rotation matrix (2x2)
	 */
	@NotNull
	static float[][] getRotationMatrix(float angle){
		
	}


	/**
	 * see https://ru.wikipedia.org/wiki/Матрица_поворота
	 * @param x (pixels)
	 * @param y (pixels)
	 * @return translation matrix (3x3)
	 */
	@NotNull
	static float[][] getTranslationMatrix(float x, float y){
		return new float[][]{
				{1, 0, 0},
				{0, 1, 0},
				{x, y, 1}
		};
	}

	static void translatePoints(float[][] points, float x, float y){
		//TODO for each point add coordinate w=1, multiply with translation matrix
		//and assign result to point
		
	}

	static void rotatePoints(float[][] points, float angle){
		//TODO for each point assign the point coordinates multiplication with rotation matrix
		
	}

}
