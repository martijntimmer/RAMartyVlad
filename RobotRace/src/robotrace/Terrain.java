package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

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
        Textures.grass.bind(gl);
        // terrain is 20x20, but for now scaling is applied because everything is too big in relation to the terrain :S        
        gl.glPushMatrix();
        gl.glScaled(3,3,3);
        util.drawPlane(20, 20, 64, 64, gl, glu, glut);       
        gl.glPopMatrix();
    }
    
}