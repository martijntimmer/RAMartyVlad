package robotrace;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 *
 * @author Niels Rood
 */
public class Textures {
    
    public static Texture head = null;
    public static Texture torso = null;
    public static Texture[] track = null;   //0=Top, 1=Outer wall, 2=Inner wall of track
    public static Texture brick = null;
    public static Texture grass = null;
    public static Texture water = null;
    public static Texture leaf = null;
        
    public static void loadTextures() {
        leaf = loadTexture("textures/leaf.png");
        grass = loadTexture("textures/grass.jpg");
        head = loadTexture("textures/head.jpg");
        torso = loadTexture("textures/torso.jpg");       
        brick = loadTexture("textures/brick.jpg");
        water = loadTexture("textures/water.jpg");
        track = new Texture[] {loadTexture("textures/track.jpg"), brick, brick };
    }
    
    /**
    * Try to load a texture from the given file. The file
    * should be located in the same folder as RobotRace.java.
    */
    private static Texture loadTexture(String file) {
        Texture result = null;

        try {
            // Try to load from local folder.
            result = TextureIO.newTexture(Textures.class.getResource(file), false, null);
        } catch(Exception e1) {
            e1.printStackTrace();
        }       
        return result;
    }
}
