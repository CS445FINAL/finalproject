/**
 * File : Block.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #1
 * Purpose : A class that renders a 3D Block at the origin
 */

import static org.lwjgl.opengl.GL11.*;

class Block {

    //  Method : Block
    // Purpose : No actual purpose
    Block() { }

    //  Method : renderSampleBlock
    // Purpose : Renders a 3D Block with a different color for each face at the
    // origin of our coordinate system
    void renderSampleBlock() {
        glBegin(GL_QUADS);

        glColor3f(1.0f, 0.0f, 0.0f);
        // Top Right Of The Quad (Top)
        glVertex3f(1.0f, 1.0f, -1.0f);
        // Top Left Of The Quad (Top)
        glVertex3f(-1.0f, 1.0f, -1.0f);
        // Bottom Left Of The Quad (Top)
        glVertex3f(-1.0f, 1.0f, 1.0f);
        // Bottom Right Of The Quad (Top)
        glVertex3f(1.0f, 1.0f, 1.0f);

        glColor3f(0.0f, 1.0f, 0.0f);
        // Top Right Of The Quad (Bottom)
        glVertex3f(1.0f, -1.0f, 1.0f);
        // Top Left Of The Quad (Bottom)
        glVertex3f(-1.0f, -1.0f, 1.0f);
        // Bottom Left Of The Quad (Bottom)
        glVertex3f(-1.0f, -1.0f, -1.0f);
        // Bottom Right Of The Quad (Bottom)
        glVertex3f(1.0f, -1.0f, -1.0f);

        glColor3f(0.0f, 0.0f, 1.0f);
        // Top Right Of The Quad (Front)
        glVertex3f(1.0f, 1.0f, 1.0f);
        // Top Left Of The Quad (Front)
        glVertex3f(-1.0f, 1.0f, 1.0f);
        // Bottom Left Of The Quad (Front)
        glVertex3f(-1.0f, -1.0f, 1.0f);
        // Bottom Right Of The Quad (Front)
        glVertex3f(1.0f, -1.0f, 1.0f);

        glColor3f(0.5f, 0.5f, 0.5f);
        // Bottom Left Of The Quad (Back)
        glVertex3f(1.0f, -1.0f, -1.0f);
        // Bottom Right Of The Quad (Back)
        glVertex3f(-1.0f, -1.0f, -1.0f);
        // Top Right Of The Quad (Back)
        glVertex3f(-1.0f, 1.0f, -1.0f);
        // Top Left Of The Quad (Back)
        glVertex3f(1.0f, 1.0f, -1.0f);

        glColor3f(1.0f, 0.0f, 1.0f);
        // Top Right Of The Quad (Left)
        glVertex3f(-1.0f, 1.0f, 1.0f);
        // Top Left Of The Quad (Left)
        glVertex3f(-1.0f, 1.0f, -1.0f);
        // Bottom Left Of The Quad (Left)
        glVertex3f(-1.0f, -1.0f, -1.0f);
        // Bottom Right Of The Quad (Left)
        glVertex3f(-1.0f, -1.0f, 1.0f);

        glColor3f(0.0f, 1.0f, 1.0f);
        // Top Right Of The Quad (Right)
        glVertex3f(1.0f, 1.0f, -1.0f);
        // Top Left Of The Quad (Right)
        glVertex3f(1.0f, 1.0f, 1.0f);
        // Bottom Left Of The Quad (Right)
        glVertex3f(1.0f, -1.0f, 1.0f);
        // Bottom Right Of The Quad (Right)
        glVertex3f(1.0f, -1.0f, -1.0f);

        glEnd();

        //Block outline in the same order
        glBegin(GL_LINE_LOOP);

        glColor3f(0.0f, 0.0f, 0.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glEnd();

        glBegin(GL_LINE_LOOP);

        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glEnd();

        glBegin(GL_LINE_LOOP);

        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glEnd();

        glBegin(GL_LINE_LOOP);

        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glEnd();
        glBegin(GL_LINE_LOOP);

        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glEnd();

        glBegin(GL_LINE_LOOP);

        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glEnd();
    }

}
