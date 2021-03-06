/*
 * File : Camera.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #3
 * Purpose : A class responsible for managing the camera movements and rendering
 * the content. As a user presses buttons on the keyboard, the camera will
 * update itself to reflect these changes
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

class Camera
{
    private final Vector3f _position;
    private float _yaw;
    private float _pitch;
    private Chunk _chunk;

    private boolean shouldShowFlatEarth = false;
    private FlatEarth _flatEarth;

    //  Method : Camera
    // Purpose : Camera constructor. Creates our _position Vectors and
    // initializes the _yaw and _pitch to zero
    Camera ( float xCoordinate, float yCoordinate, float zCoordinate )
    {
        this._position = new Vector3f ( xCoordinate, yCoordinate, zCoordinate );
        this._yaw = 0.0f;
        this._pitch = 0.0f;
    }

    //  Method : _yaw
    // Purpose : Updates the Yaw, or the camera rotation about the Y-Axis
    private void yaw ( float amount )
    {
        this._yaw += amount;
    }

    //  Method : _pitch
    // Purpose : Updates the _pitch, or the camera rotation about the X-Axis
    private void pitch ( float amount )
    {
        this._pitch -= amount;
    }

    //  Method : moveInDirectionWithDistance
    // Purpose : Moves the camera in the direction and distance as requested by 
    // the parameter. The _position is also updated depending on the direction
    // requested. 
    private void moveInDirectionWithDistance ( MovementDirection direction, float distance )
    {
        if ( Debug.isDebugging () )
        {
            System.out.println ( "Direction : " + direction + " and Distance : " + distance + " and Position : " + _position );
        }

        switch ( direction )
        {
            case UP:
            {
                this._position.y -= distance;
                break;
            }
            case DOWN:
            {
                this._position.y += distance;
                break;
            }
            case NORTH:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( _yaw ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( _yaw ) );
                this._position.x -= xOffset;
                this._position.z += zOffset;
                break;
            }
            case EAST:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( _yaw - 90 ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( _yaw - 90 ) );
                this._position.x -= xOffset;
                this._position.z += zOffset;
                break;
            }
            case SOUTH:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( _yaw ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( _yaw ) );
                this._position.x += xOffset;
                this._position.z -= zOffset;
                break;
            }
            case WEST:
            {
                float xOffset = distance * ( float ) Math.sin ( Math.toRadians ( _yaw + 90 ) );
                float zOffset = distance * ( float ) Math.cos ( Math.toRadians ( _yaw + 90 ) );
                this._position.x -= xOffset;
                this._position.z += zOffset;
                break;
            }
        }
    }

    //  Method : lookThrough
    // Purpose : Translates and rotates the matrix so that it looks through the 
    // camera.
    private void lookThrough ()
    {
        //Rotates the _pitch around the X axis
        glRotatef ( _pitch, 1.0f, 0.0f, 0.0f );
        //Rotates the _yaw around the Y axis
        glRotatef ( _yaw, 0.0f, 1.0f, 0.0f );
        //Translates to the _position vector's location
        glTranslatef ( this._position.x, this._position.y, this._position.z );

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer ( 4 );
        lightPosition.put ( _position.x ).put ( _position.y ).put ( _position.z ).put ( 1.0f ).flip ();
        glLight ( GL_LIGHT0, GL_POSITION, lightPosition );
    }

    //  Method : updateCameraForInput
    // Purpose : Manages all camera movements. Also, sets and maintains the 
    // distance of the movement, movement speed, and mouse sensitivity. 
    // The mouse is locked in the window and the cursor is removed.
    void cameraLoop ()
    {
        float dx, dy;
        float mouseSensitivity = 0.15f;
        float movementSpeed = 0.35f;

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
                if ( Debug.isDebugging () )
                {
                    System.out.println ( "Goodbye!" );
                }
            }
            else if ( Keyboard.isKeyDown ( Keyboard.KEY_UP ) || Keyboard.isKeyDown ( Keyboard.KEY_W ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.NORTH, movementSpeed );
            }
            else if ( Keyboard.isKeyDown ( Keyboard.KEY_LEFT ) || Keyboard.isKeyDown ( Keyboard.KEY_A ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.EAST, movementSpeed );
            }
            else if ( Keyboard.isKeyDown ( Keyboard.KEY_DOWN ) || Keyboard.isKeyDown ( Keyboard.KEY_S ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.SOUTH, movementSpeed );
            }
            else if ( Keyboard.isKeyDown ( Keyboard.KEY_RIGHT ) || Keyboard.isKeyDown ( Keyboard.KEY_D ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.WEST, movementSpeed );
            }
            else if ( Keyboard.isKeyDown ( Keyboard.KEY_SPACE ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.UP, movementSpeed );
            }
            else if ( Keyboard.isKeyDown ( Keyboard.KEY_LSHIFT ) || Keyboard.isKeyDown ( Keyboard.KEY_RSHIFT ) )
            {
                this.moveInDirectionWithDistance ( MovementDirection.DOWN, movementSpeed );
            }
            else if (  Keyboard.isKeyDown ( Keyboard.KEY_F1 ) )
            {
                shouldShowFlatEarth = !shouldShowFlatEarth;
            }

            glLoadIdentity ();
            this.lookThrough ();

            glClear ( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

            if ( shouldShowFlatEarth )
            {
                glClearColor ( 0.5f, 0.4f, 0.9f, 0f );

                if ( _flatEarth == null )
                {
                    if ( Debug.isDebugging () )
                    {
                        System.out.println ( "Now Loading Flat Earth" );
                    }

                    _flatEarth = new FlatEarth ( 0, 8, -60 );
                }

                _flatEarth.render ();
            }
            else
            {
                glClearColor ( 0.4f, 0.5f, 1.0f, 0f );

                new Block ().renderSampleBlock ();

                if ( _chunk == null )
                {
                    if ( Debug.isDebugging () )
                    {
                        System.out.println ( "Now Loading World" );
                    }

                    _chunk = new Chunk ( 0, 8, -60 );
                }

                _chunk.render ();
            }

            Display.update ();
            Display.sync ( 60 );
        }

        Display.destroy ();
    }

}
