/*
 * File : MainProgram.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #3
 * Purpose : A class that creates the Display, and begins the OpenGL program
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

class MainProgram
{
    private static final int _HEIGHT = 480;
    private static final int _WIDTH = 640;

    private FloatBuffer _lightPosition;
    private FloatBuffer _whiteLight;

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
            new Camera ( 0, 0, -5 ).cameraLoop ();
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
        Display.setTitle ( "Final Assignment by \"Things that really make you go Hmmm...\"" );
        Display.create ();
    }

    //  Method : initGL
    // Purpose : Sets the background color, matrix mode, perspective, and other important OpenGL features
    private void initGL ()
    {
//        glClearColor ( 0.0f, 0.5f, 1.0f, 0f );

        glMatrixMode ( GL_PROJECTION );
        glLoadIdentity ();
        gluPerspective ( 100.0f, _WIDTH / _HEIGHT, 0.1f, 300.0f );
        glMatrixMode ( GL_MODELVIEW );
        glHint ( GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST );
        glEnable ( GL_DEPTH_TEST );
        glEnableClientState ( GL_VERTEX_ARRAY );
        glEnableClientState ( GL_COLOR_ARRAY );
        glEnable ( GL_TEXTURE_2D );
        glEnableClientState ( GL_TEXTURE_COORD_ARRAY );

        initLightArrays ();
        glLight ( GL_LIGHT0, GL_POSITION, _lightPosition );
        glLight ( GL_LIGHT0, GL_SPECULAR, _whiteLight );
        glLight ( GL_LIGHT0, GL_DIFFUSE, _whiteLight );
        glLight ( GL_LIGHT0, GL_AMBIENT, _whiteLight );
        glEnable ( GL_LIGHTING );
        glEnable ( GL_LIGHT0 );
    }

    //  Method : initLightArrays
    // Purpose : Initializes white light and source
    private void initLightArrays ()
    {
        _lightPosition = BufferUtils.createFloatBuffer ( 4 );
        _lightPosition.put ( 0.0f ).put ( 0.0f ).put ( 0.0f ).put ( 1.0f ).flip ();

        _whiteLight = BufferUtils.createFloatBuffer ( 4 );
        _whiteLight.put ( 1.0f ).put ( 1.0f ).put ( 1.0f ).put ( 0.0f ).flip ();
    }
}
