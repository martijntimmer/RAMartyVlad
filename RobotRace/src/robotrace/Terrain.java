package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static robotrace.ShaderPrograms.terrainShader;
import static robotrace.ShaderPrograms.waterShader;
import static robotrace.ShaderPrograms.defaultShader;

/**
 * Represents the terrain, to be implemented according to the Assignments.
 */
class Terrain {

    
    
    public Terrain() {
        
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        gl.glColor3d(0,1,0);
        // terrain is 20x20, but for now scaling is applied because everything is too big in relation to the terrain :S        
        gl.glPushMatrix();
        gl.glScaled(3,3,3);
        gl.glUseProgram(terrainShader.getProgramID());
        Textures.grass.bind(gl);
        terrainShader.setUniform(gl, "t", (float)(util.gs.tAnim)/2f);
        util.drawPlane(20, 20, 64, 64, gl, glu, glut);
        gl.glTranslated(0,0,-0.25f);
        gl.glUseProgram(waterShader.getProgramID());
        Textures.water.bind(gl);
        waterShader.setUniform(gl, "time", (float)(util.gs.tAnim)/2f);
        util.drawPlane(20, 20, 64, 64, gl, glu, glut);
        gl.glUseProgram(defaultShader.getProgramID());
        drawTree(gl, glu, glut);
        gl.glPopMatrix();
    }
    
    
    int maxTrunkSegAmnt = 3;
    int leafAmnt = 6;
        
    void drawTree (GL2 gl, GLU glu, GLUT glut) {
        gl.glColor3d(0.3, 0 ,0);
        drawTrunk(gl, glu, glut, 0, maxTrunkSegAmnt);
    }
   
    // The draw-tail method recursively calls itself to extend the trunk with additional segments of decreasing size
    void drawTrunk(GL2 gl, GLU glu, GLUT glut, float tAnim, int trunkSegAmnt) {
        gl.glPushMatrix();
        // if this is the first segment, scale trunk appropriately
        if (trunkSegAmnt == maxTrunkSegAmnt) {
            gl.glScaled(1.2,1.2,1.2);
        }
        gl.glTranslated(0,0,0.4);
        gl.glRotated(tAnim*10, 1 ,0, 0);
        glut.glutSolidCone(0.6, 2.2, 16, 1); // draw trunk cone
        if(trunkSegAmnt == 1) { // last segment
            gl.glTranslated(0,0,1.8);
            drawLeaves(gl, glu, glut);
        }
        gl.glTranslated(0,0,0.4);
        gl.glScaled(0.6,0.6,1);
        trunkSegAmnt--;
        if (trunkSegAmnt > 0) // if more trunk-segments remain to be drawn
            drawTrunk(gl, glu, glut, tAnim, trunkSegAmnt); // recurse
        gl.glPopMatrix();
    }
    
        // The draw-tail method recursively calls itself to extend the trunk with additional segments of decreasing size
    void drawLeaves(GL2 gl, GLU glu, GLUT glut) {
        Textures.grass.bind(gl);
        gl.glPushMatrix();
        float dr = 360/(float)leafAmnt/2f;
        for(int i = 0; i < leafAmnt; i++) {
            // lower leaf
            gl.glRotated(dr,0,0,1);
            gl.glTranslated(2,0,0);
            util.drawPlane(4, 1.5, 8, 8, gl, glu, glut);
            gl.glTranslated(-2,0,0);
            // upper leaf
            gl.glRotated(dr,0,0,1);
            gl.glRotated(-10,0,1,0);
            gl.glTranslated(1.5,0,0);
            util.drawPlane(4, 1.5, 8, 8, gl, glu, glut);
            gl.glTranslated(-1.5,0,0);
            gl.glRotated(10,0,1,0);
        }
        gl.glPopMatrix();
    }
}