package ru.progexcenter.lesson3.sierpinsky;

/**
 * User: vlastachu
 * Date: 14.09.14
 * Time: 17:31
 */
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Shape {
	static int steps;
	protected static void setup( GL2 gl2, int width, int height , final int _steps) {
		gl2.glMatrixMode( GL2.GL_PROJECTION );
		gl2.glLoadIdentity();

		// coordinate system origin at lower left with width and height same as the window
		GLU glu = new GLU();
		glu.gluOrtho2D( 0.0f, width, 0.0f, height );

		gl2.glMatrixMode( GL2.GL_MODELVIEW );
		gl2.glLoadIdentity();

		gl2.glViewport( 0, 0, width, height );
		steps = _steps;
	}

	protected static void render( GL2 gl2, int width, int height ) {
		gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
		//fill window with white color
		gl2.glClearColor(1,1,1,0);
		// draw a triangle filling the window
		gl2.glLoadIdentity();
		gl2.glBegin( GL.GL_TRIANGLES );
			//set the red color
			gl2.glColor3f( 1, 0, 0 );
			//set 2D points 
			//down left
			gl2.glVertex2f( 0, 0 );
			//down right
			gl2.glVertex2f( width, 0 );
			//center top
			gl2.glVertex2f( width / 2, height );
			fractal(gl2, steps, width/2, height, 0, 0, width);
		gl2.glEnd();

	}

	static void fractal(GL2 gl2, int _steps, float upx, float upy, float bottom, float left, float right){
		final float innerLeftX = left + (upx-left)/2;
		final float innerLeftY = bottom + (upy-bottom)/2;
		final float innerRightX = upx + (upx-left)/2;
		final float innerRightY = innerLeftY;
		final float innerBottomX = upx;
		final float innerBottomY = bottom;
		float color = 1 - _steps*0.9f/(Shape.steps);
		gl2.glColor3f( color, 0f, color );
		gl2.glVertex2f(innerLeftX, innerLeftY);
		gl2.glVertex2f(innerRightX, innerRightY);
		gl2.glVertex2f(innerBottomX, innerBottomY);
		if (_steps --> 0){
			fractal(gl2, _steps, upx, upy, innerLeftY, innerLeftX, innerRightX);
			fractal(gl2, _steps, innerLeftX, innerLeftY, bottom, left, innerBottomX);
			fractal(gl2, _steps, innerRightX, innerLeftY, bottom, innerBottomX, right);
		}
	}
}
