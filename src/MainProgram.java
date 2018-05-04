/**
 *
 * 
 */

import java.util.*;
import java.io.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;

public class MainProgram {
    
    private static int HEIGHT = 480;
    private static int WIDTH = 640;
    
    // method: start
    // purpose: links/binds to the dll in the natives folder in the lib folder, calls createWindow, initGL, and render
    public void start() {
        try {
            // Using command line args instead 
            // System.setProperty("org.lwjgl.librarypath", new File("lib/lwjgl-2.9.2/native/windows").getAbsolutePath());
            createWindow();
            initGL();
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method: createWindow
    // purpose: creates a display window, not fullscreen, with a given height and width
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
        Display.setTitle("Program 2");
        Display.create();
    }

    // method: initGL
    // purpose: sets the background color, camera, id matrix, and model view
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(-320, 320, -240, 240, 1, -1);

        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    // method: render
    // purpose: reads in the coordinates.txt file and renders the approriate shapes 
    // such as lines, ellipses, circles
    private void render() {
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            try{
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
                glLoadIdentity();
                
                

                Display.update();
                Display.sync(60);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        Display.destroy();
    }
    
    
    public static void main(String[] args)
    {
        
    }
}
