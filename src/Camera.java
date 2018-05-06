/**
 * File : Camera.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #1
 * Purpose : A class responsible for managing the camera movements and rendering
 * the content. As a user presses buttons on the keyboard, the camera will
 * update itself to reflect these changes
 */

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

class Camera
{
    private final Vector3f position;
    private float yaw;
    private float pitch;

    //  Method : Camera
    // Purpose : Camera constructor. Creates our position Vectors and
    // initializes the yaw and pitch to zero
    Camera ( float xCoord, float yCoord, float zCoord )
    {
        this.position = new Vector3f ( xCoord, yCoord, zCoord );
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    //  Method : yaw
    // Purpose : Updates the Yaw, or the camera rotation about the Y-Axis
    private void yaw ( float amount )
    {
        this.yaw += amount;
    }

    //  Method : pitch
    // Purpose : Updates the pitch, or the camera rotation about the X-Axis
    private void pitch ( float amount )
    {
        this.pitch -= amount;
    }

    //  Method : moveInDirectionWithDistance
    // Purpose : Moves the camera in the direction and distance as requested by 
    // the parameter. The position is also updated depending on the direction
    // requested. 
    private void moveInDirectionWithDistance ( MovementDirection direction, float distance )
    {
        System.out.println ( "Direction : " + direction + " and Distance : " + distance + " and Position : " + position );

        switch ( direction )
        {
            case UP:
            {
                this.position.y -= distance;
                break;
            }
            case DOWN:
            {
                this.position.y += distance;
                break;
            }
            case NORTH:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( yaw ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( yaw ) );
                this.position.x -= xOffset;
                this.position.z += zOffset;
                break;
            }
            case EAST:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( yaw - 90 ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( yaw - 90 ) );
                this.position.x -= xOffset;
                this.position.z += zOffset;
                break;
            }
            case SOUTH:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( yaw ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( yaw ) );
                this.position.x += xOffset;
                this.position.z -= zOffset;
                break;
            }
            case WEST:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( yaw + 90 ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( yaw + 90 ) );
                this.position.x -= xOffset;
                this.position.z += zOffset;
                break;
            }
        }
    }

    //  Method : lookThrough
    // Purpose : Translates and rotates the matrix so that it looks through the 
    // camera.
    private void lookThrough ()
    {
        //Rotates the pitch around the X axis
        glRotatef ( pitch, 1.0f, 0.0f, 0.0f );
        //Rotates the yaw around the Y axis
        glRotatef ( yaw, 0.0f, 1.0f, 0.0f );
        //Translates to the position vector's location
        glTranslatef ( this.position.x, this.position.y, this.position.z );
    }

    //  Method : updateCameraForInput
    // Purpose : Manages all camera movements. Also, sets and maintains the 
    // distance of the movement, movement speed, and mouse sensitivity. 
    // The mouse is locked in the window and the cursor is removed.
    void cameraLoop ()
    {
        float dx, dy;
        float mouseSensitivity = 0.15f;
        float movementSpeed = 0.10f;

        // Hide the Mouse
        Mouse.setGrabbed ( true );

        while ( !Display.isCloseRequested () && !Keyboard.isKeyDown ( Keyboard.KEY_ESCAPE ) )
        {
            dx = Mouse.getDX ();
            dy = Mouse.getDY ();

            // Adjust camera with movements from the mouse
            this.yaw ( dx * mouseSensitivity );
            this.pitch ( dy * mouseSensitivity );


            if ( Keyboard.isKeyDown ( Keyboard.KEY_ESCAPE ) )
            {
                System.out.println ( "Goodbye!" );
            }
            if ( Keyboard.isKeyDown ( Keyboard.KEY_UP ) || Keyboard.isKeyDown ( Keyboard.KEY_W ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.NORTH, movementSpeed );
            }
            if ( Keyboard.isKeyDown ( Keyboard.KEY_LEFT ) || Keyboard.isKeyDown ( Keyboard.KEY_A ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.EAST, movementSpeed );
            }
            if ( Keyboard.isKeyDown ( Keyboard.KEY_DOWN ) || Keyboard.isKeyDown ( Keyboard.KEY_S ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.SOUTH, movementSpeed );
            }
            if ( Keyboard.isKeyDown ( Keyboard.KEY_RIGHT ) || Keyboard.isKeyDown ( Keyboard.KEY_D ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.WEST, movementSpeed );
            }
            if ( Keyboard.isKeyDown ( Keyboard.KEY_SPACE ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.UP, movementSpeed );
            }
            if ( Keyboard.isKeyDown ( Keyboard.KEY_LSHIFT ) || Keyboard.isKeyDown ( Keyboard.KEY_RSHIFT ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.DOWN, movementSpeed );
            }

            glLoadIdentity ();
            this.lookThrough ();
            glClear ( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

            new Block ().renderSampleBlock ();

            Display.update ();
            Display.sync ( 60 );
        }

        Display.destroy ();
    }

}
