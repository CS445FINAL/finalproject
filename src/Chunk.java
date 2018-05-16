/*
 * File : Chunk.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #2
 * Purpose : A class that renders a random assortment of 3D Chunks
 */

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.nio.FloatBuffer;
import java.util.Objects;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

class Chunk
{

    private static final int _CHUNK_SIZE = 30;
    private static final int _CUBE_LENGTH = 2;

    private final Block[][][] _blocks;
    private final int _vboVertexHandle;
    private final int _vboColorHandle;
    private final int _vboTextureHandle;
    private final Random _random;

    //  Method : Chunk
    // Purpose : Initializes the Texture object, Blocks 3D array, and Handles that will be used for creating Chunks
    Chunk ( int originX, int originY, int originZ )
    {
        // Initialize the Texture
        try
        {
            Texture _texture = TextureLoader.getTexture ( "PNG", ResourceLoader.getResourceAsStream ( "/resources/terrain.png" ) );
        }
        catch ( Exception e )
        {
            System.err.println ( "Textures at \"/resources/terrain.png\" could not be loaded" );
            System.exit ( -1 );
        }

        this._random = new Random ();
        this._blocks = new Block[ _CHUNK_SIZE ][ _CHUNK_SIZE ][ _CHUNK_SIZE ];

        this._vboColorHandle = glGenBuffers ();
        this._vboVertexHandle = glGenBuffers ();
        this._vboTextureHandle = glGenBuffers ();

        this.rebuildMesh ( originX, originY, originZ );
    }

    //  Method : createCube
    // Purpose : Creates and returns an Array holding the Vertices of a new Cube
    private static float[] createCube ( float x, float y, float z )
    {
        int offset = _CUBE_LENGTH / 2;

        return new float[] {
                //Top quad
                x + offset, y + offset, z,
                x - offset, y + offset, z,
                x - offset, y + offset, z - _CUBE_LENGTH,
                x + offset, y + offset, z - _CUBE_LENGTH,
                //Bottom quad
                x + offset, y - offset, z - _CUBE_LENGTH,
                x - offset, y - offset, z - _CUBE_LENGTH,
                x - offset, y - offset, z,
                x + offset, y - offset, z,
                //Front quad
                x + offset, y + offset, z - _CUBE_LENGTH,
                x - offset, y + offset, z - _CUBE_LENGTH,
                x - offset, y - offset, z - _CUBE_LENGTH,
                x + offset, y - offset, z - _CUBE_LENGTH,
                //Back quad
                x + offset, y - offset, z,
                x - offset, y - offset, z,
                x - offset, y + offset, z,
                x + offset, y + offset, z,
                //Left Quad
                x - offset, y + offset, z - _CUBE_LENGTH,
                x - offset, y + offset, z,
                x - offset, y - offset, z,
                x - offset, y - offset, z - _CUBE_LENGTH,
                //Right Quad
                x + offset, y + offset, z,
                x + offset, y + offset, z - _CUBE_LENGTH,
                x + offset, y - offset, z - _CUBE_LENGTH,
                x + offset, y - offset, z
        };
    }

    //  Method : createCubeTexture
    // Purpose : Based on the BlockType, will call a function to fetch the correct Texture
    private static float[] createCubeTexture ( Block block )
    {
        float offset = ( 1024f / 16 ) / 1024.0f;

        switch ( block.getBlockTypeID () )
        {
            // Grass Block
            case 0:
            {
                return getCubeTexture ( offset, 3, 2, 10, 9 );
            }
            // Sand Block
            case 1:
            {
                return getCubeTexture ( offset, 3, 2, 2, 1 );
            }
            // Water Block
            case 2:
            {
                return getCubeTexture ( offset, 16, 15, 12, 13 );
            }
            // Dirt Block
            case 3:
            {
                return getCubeTexture ( offset, 3, 2, 0, 1 );
            }
            // Stone Block
            case 4:
            {
                return getCubeTexture ( offset, 2, 1, 0, 1 );
            }
            // Bedrock Block
            case 5:
            {
                return getCubeTexture ( offset, 2, 1, 1, 2 );
            }
            default:
            {
                return null;
            }
        }
    }

    //  Method : getCubeTexture
    // Purpose : Based on the BlockType, will fetch the correct Texture
    private static float[] getCubeTexture ( float offset, int offsetX1, int offsetX2, int offsetY1, int offsetY2 )
    {
        return new float[] {
                // BOTTOM QUAD ( DOWN =+ Y )
                offset * offsetX1, offset * offsetY1,
                offset * offsetX2, offset * offsetY1,
                offset * offsetX2, offset * offsetY2,
                offset * offsetX1, offset * offsetY2,
                // TOP!
                offset * offsetX1, offset * offsetY1,
                offset * offsetX2, offset * offsetY1,
                offset * offsetX2, offset * offsetY2,
                offset * offsetX1, offset * offsetY2,
                // FRONT QUAD
                offset * offsetX1, offset * offsetY1,
                offset * offsetX2, offset * offsetY1,
                offset * offsetX2, offset * offsetY2,
                offset * offsetX1, offset * offsetY2,
                // BACK QUAD
                offset * offsetX1, offset * offsetY1,
                offset * offsetX2, offset * offsetY1,
                offset * offsetX2, offset * offsetY2,
                offset * offsetX1, offset * offsetY2,
                // LEFT QUAD
                offset * offsetX1, offset * offsetY1,
                offset * offsetX2, offset * offsetY1,
                offset * offsetX2, offset * offsetY2,
                offset * offsetX1, offset * offsetY2,
                // RIGHT QUAD
                offset * offsetX1, offset * offsetY1,
                offset * offsetX2, offset * offsetY1,
                offset * offsetX2, offset * offsetY2,
                offset * offsetX1, offset * offsetY2, };
    }

    //  Method : render
    // Purpose : Renders the Chunk
    void render ()
    {
        glPushMatrix ();
        glBindBuffer ( GL_ARRAY_BUFFER, _vboVertexHandle );
        glVertexPointer ( 3, GL_FLOAT, 0, 0L );
        glBindBuffer ( GL_ARRAY_BUFFER, _vboColorHandle );
        glColorPointer ( 3, GL_FLOAT, 0, 0L );
        glBindBuffer ( GL_ARRAY_BUFFER, _vboTextureHandle );
        glBindTexture ( GL_TEXTURE_2D, 1 );
        glTexCoordPointer ( 2, GL_FLOAT, 0, 0L );
        glDrawArrays ( GL_QUADS, 0, _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE * 24 );
        glPopMatrix ();
    }

    //  Method : rebuildMesh
    // Purpose : Generating the terrain using the Simplex Noise Classes. Also, draws the Chunks which create the world
    private void rebuildMesh ( float startX, float startY, float startZ )
    {
        float persistence = 0.6f * ( _random.nextFloat () % 0.4f );

        int seed = _random.nextInt ( 50 ) + 1;
        System.out.println ( persistence );
        System.out.println ( seed );

        SimplexNoise noise = new SimplexNoise ( _CHUNK_SIZE, persistence, seed );
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );

        for ( int x = 0; x < _CHUNK_SIZE; x++ )
        {
            for ( int z = 0; z < _CHUNK_SIZE; z++ )
            {
                for ( int y = 0; y < _CHUNK_SIZE; y++ )
                {
                    int height = ( int ) ( startY + ( int ) ( _CHUNK_SIZE * noise.getNoise ( x, y, z ) ) * _CUBE_LENGTH );

                    if ( y >= height )
                    {
                        break;
                    }

                    _blocks[ x ][ y ][ z ] = new Block ( Block.BlockType.values ()[ _random.nextInt ( 6 ) ] );
                    VertexPositionData.put ( createCube ( startX + x * _CUBE_LENGTH, y * _CUBE_LENGTH + ( int ) ( _CHUNK_SIZE * 0.8 ), startZ + z * _CUBE_LENGTH ) );
                    VertexColorData.put ( createCubeVertexColor ( getCubeColor () ) );
                    VertexTextureData.put ( Objects.requireNonNull ( createCubeTexture ( _blocks[ x ][ y ][ z ] ) ) );
                }
            }
        }

        VertexColorData.flip ();
        VertexPositionData.flip ();
        VertexTextureData.flip ();
        glBindBuffer ( GL_ARRAY_BUFFER, _vboVertexHandle );
        glBufferData ( GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW );
        glBindBuffer ( GL_ARRAY_BUFFER, 0 );
        glBindBuffer ( GL_ARRAY_BUFFER, _vboColorHandle );
        glBufferData ( GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW );
        glBindBuffer ( GL_ARRAY_BUFFER, 0 );
        glBindBuffer ( GL_ARRAY_BUFFER, _vboTextureHandle );
        glBufferData ( GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW );
        glBindBuffer ( GL_ARRAY_BUFFER, 0 );
    }

    //  Method : createCubeVertexColor
    // Purpose : Creates the color data for a Chunk
    private float[] createCubeVertexColor ( float[] cubeColorArray )
    {
        float[] cubeColors = new float[ cubeColorArray.length * 4 * 6 ];
        for ( int i = 0; i < cubeColors.length; i++ )
        {
            cubeColors[ i ] = cubeColorArray[ i % cubeColorArray.length ];
        }

        return cubeColors;
    }

    // Method: getCubeColor
    // Purpose: Returns an array describing a static color
    private float[] getCubeColor ()
    {
        return new float[] { 1, 1, 1 };
    }
}