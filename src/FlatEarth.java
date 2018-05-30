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

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/*
 * Volume = ( PI * R * R * H ) / 3
 */

class FlatEarth
{
    private static final int _CHUNK_SIZE = 30;
    private static final int _CUBE_LENGTH = 2;
    private final Block[][][] _blocks;
    private final int _vboVertexHandle;
    private final int _vboColorHandle;
    private final int _vboTextureHandle;

    /* Flat Earth Variables */
    private int _CENTERX = _CHUNK_SIZE / 2;
    private int _CENTERZ = _CHUNK_SIZE / 2;

    private static String commaRegEx = ",\\p{Space}*";
    private static String dashRegEx = "\\p{Space}*-\\p{Space}*";

    // X , Z
    private HashMap< Integer, ArrayList< Integer > > northAmerica = new HashMap<> ();
    private HashMap< Integer, ArrayList< Integer > > southAmerica = new HashMap<> ();
    private HashMap< Integer, ArrayList< Integer > > europe = new HashMap<> ();
    private HashMap< Integer, ArrayList< Integer > > africa = new HashMap<> ();
    private HashMap< Integer, ArrayList< Integer > > asia = new HashMap<> ();
    private HashMap< Integer, ArrayList< Integer > > australia = new HashMap<> ();

    //initialize Texture
    private Texture texture;

    //  Method : Chunk
    // Purpose : Initializes the Texture object, Blocks 3D array, and Handles that will be used for creating Chunks
    FlatEarth ( int originX, int originY, int originZ )
    {
        // Load Continent Data
        populateContinentResourcesFor ( Continent.NorthAmerica );
        populateContinentResourcesFor ( Continent.SouthAmerica );
        populateContinentResourcesFor ( Continent.Europe );
        populateContinentResourcesFor ( Continent.Asia );
        populateContinentResourcesFor ( Continent.Africa );
        populateContinentResourcesFor ( Continent.Australia );

        // Initialize the Texture
        texture = loadTexture ();

        this._blocks = new Block[_CHUNK_SIZE][_CHUNK_SIZE][_CHUNK_SIZE];

        this._vboColorHandle = glGenBuffers ();
        this._vboVertexHandle = glGenBuffers ();
        this._vboTextureHandle = glGenBuffers ();

        this.rebuildMesh ( originX, originY, originZ );
    }

    private void populateContinentResourcesFor ( final Continent continent )
    {
        String continentString = continent.toString ();

        try
        {
            for ( String line : Files.readAllLines ( Paths.get ( "./resources/Continents/" + continentString + ".txt" ) ) )
            {
                String[] components = line.split ( commaRegEx );
                Integer key = -1;
                ArrayList< Integer > values = new ArrayList<> ();

                for ( int index = 0; index < components.length; ++index )
                {
                    if ( index <= 0 )
                    {
                        key = Integer.parseInt ( components[index] );
                        System.out.println ( "Key : " + key );
                    }
                    else
                    {
                        String[] dashComponents = components[index].split ( dashRegEx );
                        System.out.println ( "Dash Components : " + Arrays.toString ( dashComponents ) );

                        if ( dashComponents.length >= 2 )
                        {
                            Integer leftBound = Integer.parseInt ( dashComponents[0] );
                            Integer rightBound = Integer.parseInt ( dashComponents[dashComponents.length - 1] );
                            while ( leftBound <= rightBound )
                            {
                                values.add ( leftBound++ );
                            }
                        }
                        else
                        {
                            values.add ( Integer.parseInt ( dashComponents[0] ) );
                        }
                    }
                }

                switch ( continent )
                {
                    case NorthAmerica:
                    {
                        System.out.println ( "North America : Key : " + key + " Values : " + values.toString () );
                        northAmerica.put ( key, values );
                        break;
                    }
                    case SouthAmerica:
                    {
                        System.out.println ( "South America : Key : " + key + " Values : " + values.toString () );
                        southAmerica.put ( key, values );
                        break;
                    }
                    case Europe:
                    {
                        System.out.println ( "Europe : Key : " + key + " Values : " + values.toString () );
                        europe.put ( key, values );
                        break;
                    }
                    case Africa:
                    {
                        System.out.println ( "Africa : Key : " + key + " Values : " + values.toString () );
                        africa.put ( key, values );
                        break;
                    }
                    case Asia:
                    {
                        System.out.println ( "Asia : Key : " + key + " Values : " + values.toString () );
                        asia.put ( key, values );
                        break;
                    }
                    case Australia:
                    {
                        System.out.println ( "Australia : Key : " + key + " Values : " + values.toString () );
                        australia.put ( key, values );
                        break;
                    }
                }
            }
        }
        catch ( IOException exception )
        {
            exception.printStackTrace ();
        }
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
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer ( ( _CHUNK_SIZE * _CHUNK_SIZE * _CHUNK_SIZE ) * 6 * 12 );

        /**
         * This approach places Sand/Water in predictable locations rather than
         * random locations
         */
        for ( int y = 0, radius = y + 1; radius <= _CHUNK_SIZE / 2; y++, radius++ )
        {
            for ( int x = 0; x < _CHUNK_SIZE; x++ )
            {
                for ( int z = 0; z < _CHUNK_SIZE; z++ )
                {
                    int currentX = Math.abs ( x - _CENTERX );
                    int currentZ = Math.abs ( z - _CENTERZ );

                    final int currentRadius = Math.round ( ( float ) ( Math.sqrt ( currentX * currentX + currentZ * currentZ ) ) );

                    System.out.println ( "X : " + x + " Y : " + y + " Z : " + z );
                    System.out.println ( "Current Radius : " + currentRadius + " vs. Radius : " + radius );

                    if ( currentRadius <= radius )
                    {
                        _blocks[x][y][z] = findAppropriateBlockTypeUsingCoordinate ( x, z, radius >= _CHUNK_SIZE / 2 );
                        VertexPositionData.put ( createCube ( startX + x * _CUBE_LENGTH, ( y ) * _CUBE_LENGTH + ( int ) ( _CHUNK_SIZE * 0.8 ), startZ + z * _CUBE_LENGTH ) );
                        VertexColorData.put ( createCubeVertexColor ( getCubeColor () ) );
                        VertexTextureData.put ( Objects.requireNonNull ( createCubeTexture ( _blocks[x][y][z] ) ) );

                        /*
                        int maxHeight = Math.round ( ( float ) ( Math.random () * 2.0 ) );

                        for ( int height = 0; height < maxHeight; height++ )
                        {
                            Block block = findAppropriateBlockTypeUsingCoordinate ( x, z,radius >= _CHUNK_SIZE / 2 );

                            if ( block.getBlockTypeID () == Block.BlockType.Water.getBlockTypeID () && height > 0 )
                            {
                                break;
                            }
                            else
                            {
                                _blocks[x][y + height][z] = findAppropriateBlockTypeUsingCoordinate ( x, z,radius >= _CHUNK_SIZE / 2 );
                                VertexPositionData.put ( createCube ( startX + x * _CUBE_LENGTH, ( y + height ) * _CUBE_LENGTH + ( int ) ( _CHUNK_SIZE * 0.8 ), startZ + z * _CUBE_LENGTH ) );
                                VertexColorData.put ( createCubeVertexColor ( getCubeColor () ) );
                                VertexTextureData.put ( Objects.requireNonNull ( createCubeTexture ( _blocks[x][y + height][z] ) ) );
                            }
                        }
                        */
                    }
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

    //  Method : findBlockTypeUsingCoordinate
    // Purpose : Builds the world as layers. Bottom layer is Bedrock, followed by Dirt/Stone, and ultimately Water
    // Sand or Grass. While at the topmost layer, based on the location of the block, if it is inside the radius
    // of our Lake/Pond, then return a Water Block Type, but if we are outside the radius, then generate a random
    // chance and either fill using Grass or Sand
    private Block findAppropriateBlockTypeUsingCoordinate ( int x, int z, boolean haveReachedHeightLimit )
    {
        if ( haveReachedHeightLimit )
        {
            Continent continent = continentForCoordinate ( x, z );

            if ( continent == null )
            {
                return new Block ( Block.BlockType.Water );
            }
            else
            {
                switch ( continent )
                {
                    case NorthAmerica:
                    {
                        return new Block ( Block.BlockType.Grass );
                    }
                    case SouthAmerica:
                    {
                        return new Block ( Block.BlockType.Grass );
                    }
                    case Europe:
                    {
                        return new Block ( Block.BlockType.Stone );
                    }
                    case Africa:
                    {
                        return new Block ( Block.BlockType.Sand );
                    }
                    case Asia:
                    {
                        return new Block ( Block.BlockType.Bedrock );
                    }
                    case Australia:
                    {
                        return new Block ( Block.BlockType.Dirt );
                    }
                    default:
                    {
                        return new Block ( Block.BlockType.Water );
                    }
                }
            }
        }
        else
        {
            int coinFlip = Math.round ( ( float ) Math.random () );
            return new Block ( coinFlip <= 0 ? Block.BlockType.Stone : Block.BlockType.Dirt );
        }
    }

    private Continent continentForCoordinate ( final int x, final int z )
    {
        if ( northAmerica.containsKey ( x ) )
        {
            if ( Collections.binarySearch ( northAmerica.get ( x ), z ) >= 0 )
            {
                return Continent.NorthAmerica;
            }
        }

        if ( southAmerica.containsKey ( x ) )
        {
            if ( Collections.binarySearch ( southAmerica.get ( x ), z ) >= 0 )
            {
                return Continent.SouthAmerica;
            }
        }

        if ( europe.containsKey ( x ) )
        {
            if ( Collections.binarySearch ( europe.get ( x ), z ) >= 0 )
            {
                return Continent.Europe;
            }
        }

        if ( africa.containsKey ( x ) )
        {
            if ( Collections.binarySearch ( africa.get ( x ), z ) >= 0 )
            {
                return Continent.Africa;
            }
        }

        if ( asia.containsKey ( x ) )
        {
            if ( Collections.binarySearch ( asia.get ( x ), z ) >= 0 )
            {
                return Continent.Asia;
            }
        }

        if ( australia.containsKey ( x ) )
        {
            if ( Collections.binarySearch ( australia.get ( x ), z ) >= 0 )
            {
                return Continent.Australia;
            }
        }

        return null;
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
