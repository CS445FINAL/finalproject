/*
 * File : Chunk.java
 * Author : Michael Ly, Jose Garcia, Erik Huerta, Phong Trinh, Josh Montgomery
 * Class : CS 445 Computer Graphics
 * Assignment : Final Assignment Checkpoint #3
 * Purpose : A class that renders a random assortment of 3D Chunks
 */

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * World Partitioning Scheme In reality, Earth is 71 % Water and 29 % Land For
 * our Game, 70 % Land and 30 % Between Water and Sand Or, 530 Blocks of Grass
 * and 270 Blocks of Water / Sand
 */
class Chunk
{

    private static final int _CHUNK_SIZE = 30;
    private static final int _CUBE_LENGTH = 2;
    private static final int _SAND_RATIO = ( int ) ( _CHUNK_SIZE * _CHUNK_SIZE * 0.10 );
    private static final int _WATER_RATIO = ( int ) ( _CHUNK_SIZE * _CHUNK_SIZE * 0.20 );
    private final Block[][][] _blocks;
    private final int _vboVertexHandle;
    private final int _vboColorHandle;
    private final int _vboTextureHandle;
    private final Random _random;
    private final int waterLocationX = Double.valueOf ( Math.random () * _CHUNK_SIZE ).intValue ();
    private final int waterLocationZ = Double.valueOf ( Math.random () * _CHUNK_SIZE ).intValue ();
    private final int waterRadius = Math.round ( ( float ) Math.sqrt ( _WATER_RATIO / Math.PI ) );

    //initialize Texture
    private Texture texture;

    private int _sandBlockCount, _waterBlockCount;

    //  Method : Chunk
    // Purpose : Initializes the Texture object, Blocks 3D array, and Handles that will be used for creating Chunks
    Chunk ( int originX, int originY, int originZ )
    {
        _sandBlockCount = _waterBlockCount = 0;

        // Initialize the Texture
        texture = loadTexture ();

        this._random = new Random ();
        this._blocks = new Block[_CHUNK_SIZE][_CHUNK_SIZE][_CHUNK_SIZE];

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
                x + offset, y + offset, z, x - offset, y + offset, z, x - offset, y + offset, z - _CUBE_LENGTH, x + offset, y + offset, z - _CUBE_LENGTH,
                //Bottom quad
                x + offset, y - offset, z - _CUBE_LENGTH, x - offset, y - offset, z - _CUBE_LENGTH, x - offset, y - offset, z, x + offset, y - offset, z,
                //Front quad
                x + offset, y + offset, z - _CUBE_LENGTH, x - offset, y + offset, z - _CUBE_LENGTH, x - offset, y - offset, z - _CUBE_LENGTH, x + offset, y - offset, z - _CUBE_LENGTH,
                //Back quad
                x + offset, y - offset, z, x - offset, y - offset, z, x - offset, y + offset, z, x + offset, y + offset, z,
                //Left Quad
                x - offset, y + offset, z - _CUBE_LENGTH, x - offset, y + offset, z, x - offset, y - offset, z, x - offset, y - offset, z - _CUBE_LENGTH,
                //Right Quad
                x + offset, y + offset, z, x + offset, y + offset, z - _CUBE_LENGTH, x + offset, y - offset, z - _CUBE_LENGTH, x + offset, y - offset, z };
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
                return new float[] {
                        // BOTTOM QUAD(DOWN=+Y)
                        offset * 3, offset * 10, offset * 2, offset * 10, offset * 2, offset * 9, offset * 3, offset * 9,
                        // TOP!
                        offset * 3, offset * 1, offset * 2, offset * 1, offset * 2, offset * 0, offset * 3, offset * 0,
                        // FRONT QUAD
                        offset * 3, offset * 0, offset * 4, offset * 0, offset * 4, offset * 1, offset * 3, offset * 1,
                        // BACK QUAD
                        offset * 4, offset * 1, offset * 3, offset * 1, offset * 3, offset * 0, offset * 4, offset * 0,
                        // LEFT QUAD
                        offset * 3, offset * 0, offset * 4, offset * 0, offset * 4, offset * 1, offset * 3, offset * 1,
                        // RIGHT QUAD
                        offset * 3, offset * 0, offset * 4, offset * 0, offset * 4, offset * 1, offset * 3, offset * 1 };
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
                return getCubeTexture ( offset, 0, 0, 0, 0 );
            }
        }
    }

    //  Method : getCubeTexture
    // Purpose : Based on the BlockType, will fetch the correct Texture
    private static float[] getCubeTexture ( float offset, int offsetX1, int offsetX2, int offsetY1, int offsetY2 )
    {
        return new float[] {
                // BOTTOM QUAD ( DOWN =+ Y )
                offset * offsetX1, offset * offsetY1, offset * offsetX2, offset * offsetY1, offset * offsetX2, offset * offsetY2, offset * offsetX1, offset * offsetY2,
                // TOP!
                offset * offsetX1, offset * offsetY1, offset * offsetX2, offset * offsetY1, offset * offsetX2, offset * offsetY2, offset * offsetX1, offset * offsetY2,
                // FRONT QUAD
                offset * offsetX1, offset * offsetY1, offset * offsetX2, offset * offsetY1, offset * offsetX2, offset * offsetY2, offset * offsetX1, offset * offsetY2,
                // BACK QUAD
                offset * offsetX1, offset * offsetY1, offset * offsetX2, offset * offsetY1, offset * offsetX2, offset * offsetY2, offset * offsetX1, offset * offsetY2,
                // LEFT QUAD
                offset * offsetX1, offset * offsetY1, offset * offsetX2, offset * offsetY1, offset * offsetX2, offset * offsetY2, offset * offsetX1, offset * offsetY2,
                // RIGHT QUAD
                offset * offsetX1, offset * offsetY1, offset * offsetX2, offset * offsetY1, offset * offsetX2, offset * offsetY2, offset * offsetX1, offset * offsetY2, };
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
        glBindTexture ( GL_TEXTURE_2D, texture.getTextureID () );
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

        SimplexNoise noise = new SimplexNoise ( _CHUNK_SIZE, persistence, seed );
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );

        /*
         * This small chunk of code is here so that the world generation does not follow a predictable pattern such as
         * ( 0, 0, 0 ) followed by ( 0, 0, 1 ) but rather ( 0, 0, 0 ) to possibly ( 10, 0, 29 ) as an example.
         * This is to ensure that Water/Sand Blocks are placed sparsely rather than locally.
         */
        ArrayList< Integer > xValues = new ArrayList<> ();

        for ( int value = 0; value < _CHUNK_SIZE; value++ )
        {
            xValues.add ( value );
        }

        Collections.shuffle ( xValues );
        ArrayList< Integer > zValues = new ArrayList<> ( xValues );

        if ( Debug.isDebugging () )
        {
            System.out.println ( "Sand Ratio : " + _SAND_RATIO );
            System.out.println ( "Water Ratio : " + _WATER_RATIO );
        }

        for ( int x : xValues )
        {
            for ( int z : zValues )
            {
                for ( int y = 0; y < _CHUNK_SIZE; y++ )
                {
                    int height = ( int ) ( startY + ( int ) ( _CHUNK_SIZE * noise.getNoise ( x, y, z ) ) * _CUBE_LENGTH );

                    if ( Debug.isDebugging () )
                    {
                        System.out.println ( "X : " + x + " Y : " + y + " Z : " + z );
                    }

                    // Randomly selects a Block Type
                    // _blocks[x][y][z] = new Block ( Block.BlockType.values ()[_random.nextInt ( 6 )] );
                    // Based on Y-Coordinate, will find the correct Block type
                    // A Y-Coordinate <= 0 always signifies Bedrock
                    // A Y-Coordinate above 0 and below the maximum height is Stone or Dirt
                    // The topmost Y-Coordinate, the max, is Grass, Sand, or Water
                    _blocks[x][y][z] = findAppropriateBlockTypeUsingCoordinate ( x, y, z, height < y );

                    VertexPositionData.put ( createCube ( startX + x * _CUBE_LENGTH, y * _CUBE_LENGTH + ( int ) ( _CHUNK_SIZE * 0.8 ), startZ + z * _CUBE_LENGTH ) );
                    VertexColorData.put ( createCubeVertexColor ( getCubeColor () ) );
                    VertexTextureData.put ( Objects.requireNonNull ( createCubeTexture ( _blocks[x][y][z] ) ) );

                    if ( height < y )
                    {
                        break;
                    }
                }
            }
        }

        /**
         * This approach places Sand/Water in predictable locations rather than
         * random locations
         */

        /*
        System.out.println ( "Sand Ratio : " + _SAND_RATIO );
        System.out.println ( "Water Ratio : " + _WATER_RATIO );

        for ( int x = 0; x < _CHUNK_SIZE; x++ )
        {
            for ( int z = 0; z < _CHUNK_SIZE; z++ )
            {
                for ( int y = 0; y < _CHUNK_SIZE; y++ )
                {
                    int height = ( int ) ( startY + ( int ) ( _CHUNK_SIZE * noise.getNoise ( x, y, z ) ) * _CUBE_LENGTH );

                    // Randomly selects a Block Type
                    // _blocks[x][y][z] = new Block ( Block.BlockType.values ()[_random.nextInt ( 6 )] );

                    // Based on Y-Coordinate, will find the correct Block type
                    // A Y-Coordinate <= 0 always signifies Bedrock
                    // A Y-Coordinate above 0 and below the maximum height is Stone or Dirt
                    // The topmost Y-Coordinate, the max, is Grass, Sand, or Water
                    _blocks[x][y][z] = findAppropriateBlockTypeUsingCoordinate ( x, y, z, height < y );

                    VertexPositionData.put ( createCube ( startX + x * _CUBE_LENGTH, y * _CUBE_LENGTH + ( int ) ( _CHUNK_SIZE * 0.8 ), startZ + z * _CUBE_LENGTH ) );
                    VertexColorData.put ( createCubeVertexColor ( getCubeColor () ) );
                    VertexTextureData.put ( Objects.requireNonNull ( createCubeTexture ( _blocks[x][y][z] ) ) );

                    if ( height < y )
                    {
                        break;
                    }
                }
            }
        }
         */
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

    //  Method : findBlockTypeUsingCoordinate
    // Purpose : Builds the world as layers. Bottom layer is Bedrock, followed by Dirt/Stone, and ultimately Water
    // Sand or Grass. While at the topmost layer, based on the location of the block, if it is inside the radius
    // of our Lake/Pond, then return a Water Block Type, but if we are outside the radius, then generate a random
    // chance and either fill using Grass or Sand
    private Block findAppropriateBlockTypeUsingCoordinate ( int x, int y, int z, boolean haveReachedHeightLimit )
    {
        // Bedrock
        if ( y <= 0 )
        {
            return new Block ( Block.BlockType.Bedrock );
        }
        else
        {
            // We are at the Topmost Layer
            if ( haveReachedHeightLimit )
            {
                x = Math.abs ( x - waterLocationX );
                z = Math.abs ( z - waterLocationZ );

                final int radius = Math.round ( ( float ) ( Math.sqrt ( x * x + z * z ) ) );

                // If we are in the Area of the Lake/Pond, fill it with Water
                if ( radius < waterRadius && _waterBlockCount < _WATER_RATIO )
                {
                    ++_waterBlockCount;
                    return new Block ( Block.BlockType.Water );
                } // Else, flip a coin and either fill with Grass or Sand if we are under the Sand Ratio
                else
                {
                    int coinFlip = Math.round ( ( float ) Math.random () );

                    if ( _sandBlockCount < _SAND_RATIO && coinFlip <= 0 )
                    {
                        ++_sandBlockCount;
                        return new Block ( Block.BlockType.Sand );
                    }
                    else
                    {
                        return new Block ( Block.BlockType.Grass );
                    }
                }
            } // We are above Bedrock but below Topmost Layer
            else
            {
                int coinFlip = Math.round ( ( float ) Math.random () );
                return new Block ( coinFlip <= 0 ? Block.BlockType.Stone : Block.BlockType.Dirt );
            }
        }
    }

    //  Method : createCubeVertexColor
    // Purpose : Creates the color data for a Chunk
    private float[] createCubeVertexColor ( float[] cubeColorArray )
    {
        float[] cubeColors = new float[cubeColorArray.length * 4 * 6];
        for ( int i = 0; i < cubeColors.length; i++ )
        {
            cubeColors[i] = cubeColorArray[i % cubeColorArray.length];
        }

        return cubeColors;
    }

    // Method: getCubeColor
    // Purpose: Returns an array describing a static color
    private float[] getCubeColor ()
    {
        return new float[] { 1, 1, 1 };
    }

    //Method: loadTexture
    //Purpose: return texture to the variable for using more properly
    private Texture loadTexture ()
    {
        try
        {
            return TextureLoader.getTexture ( "PNG", ResourceLoader.getResourceAsStream ( "/resources/terrain.png" ) );
        }
        catch ( Exception e )
        {
            System.err.println ( "Textures at \"/resources/terrain.png\" could not be loaded" );
            System.exit ( -1 );
        }

        return null;
    }
}
