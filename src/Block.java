/**
 * File :
 * Author :
 * Class :
 * Assignment :
 * Purpose :
 */

import static org.lwjgl.opengl.GL11.*;

class Block
{
    //  Method : Block
    // Purpose :
    Block () { }

    //  Method : renderSampleBlock
    // Purpose :
    void renderSampleBlock ()
    {
        glBegin ( GL_QUADS );

        glColor3f ( 1.0f, 0.0f, 0.0f );
        // Top Right Of The Quad (Top)
        glVertex3f ( 1.0f, 1.0f, -1.0f );
        // Top Left Of The Quad (Top)
        glVertex3f ( -1.0f, 1.0f, -1.0f );
        // Bottom Left Of The Quad (Top)
        glVertex3f ( -1.0f, 1.0f, 1.0f );
        // Bottom Right Of The Quad (Top)
        glVertex3f ( 1.0f, 1.0f, 1.0f );

        glColor3f ( 0.0f, 1.0f, 0.0f );
        // Top Right Of The Quad (Bottom)
        glVertex3f ( 1.0f, -1.0f, 1.0f );
        // Top Left Of The Quad (Bottom)
        glVertex3f ( -1.0f, -1.0f, 1.0f );
        // Bottom Left Of The Quad (Bottom)
        glVertex3f ( -1.0f, -1.0f, -1.0f );
        // Bottom Right Of The Quad (Bottom)
        glVertex3f ( 1.0f, -1.0f, -1.0f );

        glColor3f ( 0.0f, 0.0f, 1.0f );
        // Top Right Of The Quad (Front)
        glVertex3f ( 1.0f, 1.0f, 1.0f );
        // Top Left Of The Quad (Front)
        glVertex3f ( -1.0f, 1.0f, 1.0f );
        // Bottom Left Of The Quad (Front)
        glVertex3f ( -1.0f, -1.0f, 1.0f );
        // Bottom Right Of The Quad (Front)
        glVertex3f ( 1.0f, -1.0f, 1.0f );

        glColor3f ( 0.5f, 0.5f, 0.5f );
        // Bottom Left Of The Quad (Back)
        glVertex3f ( 1.0f, -1.0f, -1.0f );
        // Bottom Right Of The Quad (Back)
        glVertex3f ( -1.0f, -1.0f, -1.0f );
        // Top Right Of The Quad (Back)
        glVertex3f ( -1.0f, 1.0f, -1.0f );
        // Top Left Of The Quad (Back)
        glVertex3f ( 1.0f, 1.0f, -1.0f );

        glColor3f ( 1.0f, 0.0f, 1.0f );
        // Top Right Of The Quad (Left)
        glVertex3f ( -1.0f, 1.0f, 1.0f );
        // Top Left Of The Quad (Left)
        glVertex3f ( -1.0f, 1.0f, -1.0f );
        // Bottom Left Of The Quad (Left)
        glVertex3f ( -1.0f, -1.0f, -1.0f );
        // Bottom Right Of The Quad (Left)
        glVertex3f ( -1.0f, -1.0f, 1.0f );

        glColor3f ( 0.0f, 1.0f, 1.0f );
        // Top Right Of The Quad (Right)
        glVertex3f ( 1.0f, 1.0f, -1.0f );
        // Top Left Of The Quad (Right)
        glVertex3f ( 1.0f, 1.0f, 1.0f );
        // Bottom Left Of The Quad (Right)
        glVertex3f ( 1.0f, -1.0f, 1.0f );
        // Bottom Right Of The Quad (Right)
        glVertex3f ( 1.0f, -1.0f, -1.0f );

        glEnd ();
    }

}
