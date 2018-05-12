/*
 * File : Block.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #2
 * Purpose : A class that describes a block along with its various functions such as type, activeness, etc..
 */

import static org.lwjgl.opengl.GL11.*;

class Block
{

    private BlockType _blockType;

    //  Method : Block
    // Purpose : No actual purpose
    Block () { }


    //  Method : Block
    // Purpose : Creates a Block based on the Type
    public Block ( BlockType blockType )
    {
        this._blockType = blockType;
    }

    //  Method : renderSampleBlock
    // Purpose : Renders a 3D Block with a different color for each face at the
    // origin of our coordinate system
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

        //Block outline in the same order
        glBegin ( GL_LINE_LOOP );

        glColor3f ( 0.0f, 0.0f, 0.0f );
        glVertex3f ( 1.0f, 1.0f, -1.0f );
        glVertex3f ( -1.0f, 1.0f, -1.0f );
        glVertex3f ( -1.0f, 1.0f, 1.0f );
        glVertex3f ( 1.0f, 1.0f, 1.0f );
        glEnd ();

        glBegin ( GL_LINE_LOOP );

        glVertex3f ( 1.0f, -1.0f, 1.0f );
        glVertex3f ( -1.0f, -1.0f, 1.0f );
        glVertex3f ( -1.0f, -1.0f, -1.0f );
        glVertex3f ( 1.0f, -1.0f, -1.0f );
        glEnd ();

        glBegin ( GL_LINE_LOOP );

        glVertex3f ( 1.0f, 1.0f, 1.0f );
        glVertex3f ( -1.0f, 1.0f, 1.0f );
        glVertex3f ( -1.0f, -1.0f, 1.0f );
        glVertex3f ( 1.0f, -1.0f, 1.0f );
        glEnd ();

        glBegin ( GL_LINE_LOOP );

        glVertex3f ( 1.0f, -1.0f, -1.0f );
        glVertex3f ( -1.0f, -1.0f, -1.0f );
        glVertex3f ( -1.0f, 1.0f, -1.0f );
        glVertex3f ( 1.0f, 1.0f, -1.0f );
        glEnd ();
        glBegin ( GL_LINE_LOOP );

        glVertex3f ( -1.0f, 1.0f, 1.0f );
        glVertex3f ( -1.0f, 1.0f, -1.0f );
        glVertex3f ( -1.0f, -1.0f, -1.0f );
        glVertex3f ( -1.0f, -1.0f, 1.0f );
        glEnd ();

        glBegin ( GL_LINE_LOOP );

        glVertex3f ( 1.0f, 1.0f, -1.0f );
        glVertex3f ( 1.0f, 1.0f, 1.0f );
        glVertex3f ( 1.0f, -1.0f, 1.0f );
        glVertex3f ( 1.0f, -1.0f, -1.0f );
        glEnd ();
    }


    //  Method : getBlockTypeID
    // Purpose : returns the underlying BlockType ID
    int getBlockTypeID ()
    {
        return this._blockType.getBlockTypeID ();
    }

    public enum BlockType
    {
        Grass ( 0 ), Sand ( 1 ), Water ( 2 ), Dirt ( 3 ), Stone ( 4 ), Bedrock ( 5 ), Default ( 6 );

        private int _blockID;

        //  Method : BlockType
        // Purpose : Creates a BlockType based on the integer passed to the constructor
        BlockType ( int identifier )
        {
            this.setBlockTypeID ( identifier );
        }

        //  Method : randomMiddleLayerIdentifier
        // Purpose : Creates a Middle Layer Block ID ( Dirt, Stone, or Bedrock )
        public static BlockType randomMiddleLayerIdentifier ()
        {
            return values () [ 3 + ( int ) ( Math.random () * 2.0f ) ];
        }

        //  Method : getBlockTypeID
        // Purpose : Returns the ID of the BlockType
        int getBlockTypeID ()
        {
            return this._blockID;
        }

        //  Method : setBlockTypeID
        // Purpose : Changes the type of the Block based on the new identifier passed to the function
        public void setBlockTypeID ( int identifier )
        {
            this._blockID = identifier >= 0 && identifier <= 5 ? identifier : 6;
        }
    }

}
