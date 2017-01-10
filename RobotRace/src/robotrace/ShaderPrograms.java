package robotrace;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Niels Rood
 */
public class ShaderPrograms {
    
    public static ShaderProgram phongShader;
    public static ShaderProgram defaultShader;
    public static ShaderProgram robotShader;
    public static ShaderProgram trackShader;
    public static ShaderProgram terrainShader;
    public static ShaderProgram waterShader;
    public static ShaderProgram leafShader;
    
    public static void setupShaders(GL2 gl, GLU glu) {
        waterShader = new ShaderProgram(gl, glu, "shaderPrograms/Water/vertex.glsl", null, "shaderPrograms/Water/fragment.glsl");
        phongShader =  new ShaderProgram(gl, glu, "shaderPrograms/Default/vertex.glsl", null, "shaderPrograms/Default/fragment.glsl");
        defaultShader = new ShaderProgram(gl, glu, "shaderPrograms/Default/vertex.glsl", null, "shaderPrograms/Default/fragment.glsl");
        //defaultShader.setUniform(gl, "n", 30);
        robotShader = new ShaderProgram(gl, glu, "shaderPrograms/Robot/vertex.glsl", null, "shaderPrograms/Robot/fragment.glsl");
        trackShader = new ShaderProgram(gl, glu, "shaderPrograms/Track/vertex.glsl", null, "shaderPrograms/Track/fragment.glsl");
        //trackShader.setUniform(gl, "n", 30);
        terrainShader = new ShaderProgram(gl, glu, "shaderPrograms/Terrain/vertex.glsl", null, "shaderPrograms/Terrain/fragment.glsl");
        //terrainShader.setUniform(gl, "n", 30);
        leafShader = new ShaderProgram(gl, glu, "shaderPrograms/Leaf/vertex.glsl", null, "shaderPrograms/Leaf/fragment.glsl");
    } 
}
