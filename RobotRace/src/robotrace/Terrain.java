package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static robotrace.ShaderPrograms.terrainShader;
import static robotrace.ShaderPrograms.waterShader;
import static robotrace.ShaderPrograms.defaultShader;
import static robotrace.ShaderPrograms.leafShader;

/**
 * Represents the terrain, to be implemented according to the Assignments.
 */
class Terrain {
    
    float[] phases;
    
    public Terrain() {
        phases = new float[7];
        for (int i = 0; i < 7; i++)
            phases[i] = (float)(Math.random()*Math.PI);
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
        gl.glTranslated((0.9-0.5)*20, (0.1-0.5)*20,0);
        drawTree(gl, glu, glut,0);
        gl.glTranslated(-(0.9-0.5)*20, -(0.1-0.5)*20,0);
        gl.glTranslated((0.6-0.5)*20, (0.15-0.5)*20,0);
        drawTree(gl, glu, glut,1);
        gl.glTranslated(-(0.6-0.5)*20, -(0.15-0.5)*20,0);
        gl.glTranslated((0.75-0.5)*20, (0.3-0.5)*20,0);
        drawTree(gl, glu, glut,2);
        gl.glTranslated(-(0.75-0.5)*20, -(0.3-0.5)*20,0);
        gl.glTranslated((0.51-0.5)*20, (0.40-0.5)*20,0);
        drawTree(gl, glu, glut,3);
        gl.glTranslated(-(0.51-0.5)*20, -(0.37-0.5)*20,0);
        gl.glTranslated((0.47-0.5)*20, (0.57-0.5)*20,0);
        drawTree(gl, glu, glut,4);
        gl.glTranslated(-(0.47-0.5)*20, -(0.52-0.5)*20,0);
        gl.glTranslated((0.1-0.5)*20, (0.9-0.5)*20,0);
        drawTree(gl, glu, glut,5);
        gl.glTranslated(-(0.1-0.5)*20, -(0.9-0.5)*20,0);
        gl.glTranslated((0.30-0.5)*20, (0.7-0.5)*20,0);
        drawTree(gl, glu, glut,6);
        gl.glTranslated(-(0.30-0.5)*20, -(0.7-0.5)*20,0);
        gl.glPopMatrix();
    }
    
    
    int maxTrunkSegAmnt = 3;
    int leafAmnt = 6;
    int cocosAmnt = 5;
        
    void drawTree (GL2 gl, GLU glu, GLUT glut, int phaseIndex) {
        gl.glPushMatrix();
        gl.glUseProgram(defaultShader.getProgramID());
        gl.glScaled(0.5,0.5,0.5);
        gl.glColor3d(0.5, 0.2 ,0);
        drawTrunk(gl, glu, glut, (float)Math.cos((util.gs.tAnim)/2f + phases[phaseIndex])/4f, maxTrunkSegAmnt);
        gl.glPopMatrix();
    }
   
    // The draw-tail method recursively calls itself to extend the trunk with additional segments of decreasing size
    void drawTrunk(GL2 gl, GLU glu, GLUT glut, float tAnim, int trunkSegAmnt) {
        gl.glPushMatrix();
        // if this is the first segment, scale trunk appropriately
        if (trunkSegAmnt == maxTrunkSegAmnt) {
            gl.glScaled(1.2,1.2,1.2);
            gl.glRotated(45,0,0,1);
        }
        gl.glTranslated(0,0,0.4);
        gl.glRotated(tAnim*10, 1 ,0, 0);
        glut.glutSolidCone(0.6, 2.2, 16, 1); // draw trunk cone
        if(trunkSegAmnt == 1) { // last segment
            gl.glTranslated(0,0,1.8);

            gl.glScaled(1,1,0.8);
            gl.glTranslated(0,0,-0.5);
            drawCocos(gl, glu, glut);
            gl.glTranslated(0,0,0.5);
            gl.glScaled(1,1,(1.0f/0.8f));
            drawLeaves(gl, glu, glut);
        }
        gl.glTranslated(0,0,0.4);
        gl.glScaled(0.6,0.6,1);
        trunkSegAmnt--;
        if (trunkSegAmnt > 0) // if more trunk-segments remain to be drawn
            drawTrunk(gl, glu, glut, tAnim, trunkSegAmnt); // recurse
        gl.glPopMatrix();
    }
    
    void drawLeaves(GL2 gl, GLU glu, GLUT glut) {
        gl.glUseProgram(leafShader.getProgramID());
        Textures.leaf.bind(gl);
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
    
    void drawCocos(GL2 gl, GLU glu, GLUT glut) {
        gl.glUseProgram(defaultShader.getProgramID());
        gl.glColor3d(0.3, 0 ,0);
        gl.glPushMatrix();
        float dr = 360/(float)leafAmnt;
        for(int i = 0; i < leafAmnt; i++) {
            // lower leaf
            gl.glRotated(dr,0,0,1);
            gl.glTranslated(0.5,0,0);
            gl.glRotated(15,0,1,0);
            glut.glutSolidSphere(0.5, 8, 8);
            gl.glRotated(-15,0,1,0);
            gl.glTranslated(-0.5,0,0);
        }
        gl.glPopMatrix();
    }
}