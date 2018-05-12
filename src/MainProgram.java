/*
 * File : MainProgram.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #2
 * Purpose : A class that creates the Display, and begins the OpenGL program
 */

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

class MainProgram
{
    private static final int _HEIGHT = 480;
    private static final int _WIDTH = 640;

    // Method  : main
    // Purpose : Begins the OpenGL program
    public static void main ( String[] args )
    {
        new MainProgram ().start ();
    }

    //  Method : start
    // Purpose : (Optionally) Links / Binds to the dll in the natives folder 
    // in the lib folder, calls createWindow, initGL, and finally starts the 
    // Camera loop which will process movements and rendering.
    private void start ()
    {
        try
        {
            // Using command line args instead
            // System.setProperty("org.lwjgl.librarypath", new File("lib/lwjgl-2.9.2/native/windows").getAbsolutePath());
            createWindow ();
            initGL ();
            new Camera ( 0, 0, 0 ).cameraLoop ();
        }
        catch ( Exception exception )
        {
            exception.printStackTrace ();
        }
    }

    //  Method : createWindow
    // Purpose : Creates a display window using our constant sizes, disables 
    // fullscreen, centers the Display and adds a title to our
    private void createWindow () throws Exception
    {
        Display.setFullscreen ( false );
        Display.setDisplayMode ( new DisplayMode ( _WIDTH, _HEIGHT ) );
        Display.setLocation ( -1, -1 );
        Display.setTitle ( "Final Assignment by {Our Team Name}" );
        Display.create ();
    }

    //  Method : initGL
    // Purpose : Sets the background color, matrix mode, perspective, and other
    // important OpenGL features
    private void initGL ()
    {
        glClearColor ( 0.5f, 0.8f, .97f, 0f );
        glMatrixMode ( GL_PROJECTION );
        glLoadIdentity ();
        gluPerspective ( 100.0f, _WIDTH / _HEIGHT, 0.1f, 300.0f );
        glMatrixMode ( GL_MODELVIEW );
        glHint ( GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST );
        glEnable ( GL_DEPTH_TEST );
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}
