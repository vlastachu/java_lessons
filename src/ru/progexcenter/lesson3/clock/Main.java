

import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Main {

	public static void main( String [] args ) {
		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities glcapabilities = new GLCapabilities(glprofile);
		glcapabilities.setHardwareAccelerated(true);
		glcapabilities.setDoubleBuffered(true);
		final GLCanvas glcanvas = new GLCanvas( glcapabilities );
		final FPSAnimator animator = new FPSAnimator(glcanvas, 40);
		glcanvas.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
				Clock.setup(glautodrawable.getGL().getGL2(), width, height);
			}

			@Override
			public void init(GLAutoDrawable glautodrawable) {
			}

			@Override
			public void dispose(GLAutoDrawable glautodrawable) {
			}

			@Override
			public void display(GLAutoDrawable glautodrawable) {
				Clock.render(glautodrawable.getGL().getGL2(), glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
			}
		});


		final JFrame jframe = new JFrame( "My own clock" );

		jframe.addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent windowevent ) {
				jframe.dispose();
				System.exit( 0 );
			}
		});

		jframe.getContentPane().add( glcanvas, BorderLayout.CENTER );
		jframe.setSize( 378, 378 );
		jframe.setResizable(false);
		jframe.setVisible( true );
		animator.start();
	}
}
