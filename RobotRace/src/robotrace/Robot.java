package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {
    
    GlobalState gs;             // provides sliders for easier debugging
    int maxTailSegAmnt = 5;     // the amount of segments the raptor's tail consists of
    
    /** The position of the robot. */
    public Vector position = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, GlobalState gs, float speedFactor, RaceTrack[] raceTracks) {
        this.speedFactor = speedFactor;
        this.raceTracks = raceTracks;
        progress = 0; // start race at start
        this.material = material;
            this.gs = gs;
    }
    
    float modelScale = 0.7f; // scales whole model (ensures that the feet touch the ground)
    float animSpeed = 1; // animation speed in Hz
    boolean onFours = false; // if true, then the raptor has bigger arms and runs on all fours
    boolean inPhase = false; // if true, then left an right limbs are in phase (the raptors have air-time)
    float headScale = 1f; // scale of the head-size. Bobble-heads look funny. A maximum size of 1.5 is advised.
    
    RaceTrack[] raceTracks; // array of possible racetracks
    float speedFactor = 0.1f; // how much % of the track the robot covers in 1 second
    float progress; // perecentage progress in the track
    float prevTime;
    
    /**
     * Draws this robot-raptor. The anchor-point is assumed to be on the ground below the raptor.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float time) {
        gl.glColor3d(0, 1 ,0);
        Vector pos = raceTracks[gs.trackNr].getLanePoint(GL_ONE, progress);
        Vector tan = raceTracks[gs.trackNr].getLaneTangent(GL_ONE, progress);
        float rot = (float)(Math.atan2(tan.y,tan.x)/Math.PI*180-90);
        progress += ((time-prevTime)*speedFactor)%1.0f;
        prevTime = time;
        gl.glPushMatrix();
        gl.glTranslated(pos.x, pos.y, pos.z); // position robot on track
        gl.glRotated(rot,0,0,1); // align robot with track
        drawBody(gl, glu, glut, (float)((time%1.0f)*animSpeed));
        gl.glPopMatrix();
    }
    
    // draws the robot's body. The anchor-point is assumed to be on the ground below the raptor.
    // Based on movement type: apply the appropriate forward-inclination of body, as well as appropriate jump-height and frequency.
    public void drawBody(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        gl.glRotated(90,0,0,1); // to respect the orientation-convention
        gl.glRotated(10 + (onFours ? 12 : 0) + (inPhase ? -6 : 0),0,1,0);
        float jumpAnim;
        if (inPhase) jumpAnim = (float)Math.cos(tAnim*2*Math.PI);
        else jumpAnim = (float)Math.cos(tAnim*4*Math.PI);
        gl.glTranslated(0, 0, (3 + (inPhase ? -0.8 : 0) + (inPhase ? 2 : 0.5)*(1+1*jumpAnim))*modelScale);
        gl.glScaled(modelScale,modelScale,modelScale); // global scale of model
        drawTorso(gl, glu, glut, (float)Math.cos(tAnim*2*Math.PI));
        gl.glPopMatrix();
    }
    
    // Draws the torso. The torso of the root bodypart.
    public void drawTorso(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        //glut.glutSolidCube(1); // actual torso block
        gl.glRotated(90,0,1,0);
        gl.glScaled(1.5,1.5,2.5);
        gl.glTranslated(0,0,-0.5);
        glut.glutSolidCylinder(0.6,1,16,1);
        gl.glTranslated(0,0,0.5);
        gl.glScaled(1/1.5,1/1.5,1/2.5);
        gl.glRotated(-90,0,1,0);
        gl.glTranslated(0.8,0,0.3);
        drawNeck(gl, glu, glut, tAnim); // draw neck (with a head connected)
        gl.glTranslated(-1.5,0,-0.25);
        drawTail(gl, glu, glut, tAnim, maxTailSegAmnt); // top-level call for drawing the tail
        gl.glTranslated(0.7,0,-0.5); // "recenter"  matrix
        gl.glTranslated(-0.6,0.8,0.2);
        drawLeg(gl, glu, glut, tAnim); // draw left leg
        gl.glTranslated(0,-1.6,0);
        drawLeg(gl, glu, glut, inPhase ? tAnim : -tAnim); // draw right leg
        gl.glTranslated(0.4,0.8,-0.2); // center
        gl.glTranslated(1.4,0.8,0.4);
        drawArm(gl, glu, glut, inPhase ? tAnim : -tAnim); // draw left arm
        gl.glTranslated(0,-1.6,0);
        drawArm(gl,glu,glut, tAnim); // draw right arm
        // gl.glTranslated(-1.4,0.4,-0.4); // return to center
        gl.glPopMatrix();
    }
    
    public void drawLeg(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        gl.glRotated(75 + tAnim*(inPhase ? 23 : 40),0,1,0);
        gl.glScaled(2,1,1);
        gl.glTranslated(0.5,0,0);
        glut.glutSolidCube(1);
        gl.glTranslated(0.4,0,0);
        gl.glScaled(0.5,1,1);
        drawLowerLeg(gl, glu, glut, tAnim);
        gl.glPopMatrix();
    }
    
    // Applies appropriate rotation and scaling, then draws the "arm" using the leg-drawing procedure
    public void drawArm(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        gl.glRotated(180,0,0,1);
        gl.glRotated(50,0,1,0);
        if (!onFours) gl.glScaled(0.5,0.5,0.5);
        drawLeg(gl, glu, glut, tAnim);
        gl.glPopMatrix();
    }
    
    // The draw-tail method recursively calls itself to extend the tail with additional segments of decreasing size
    public void drawTail(GL2 gl, GLU glu, GLUT glut, float tAnim, int tailSegAmnt) {
        gl.glPushMatrix();
        // if this is the first segment, rotae and scale tail appropriately
        if (tailSegAmnt == maxTailSegAmnt) {
            gl.glRotated(-90,0,1,0);
            gl.glScaled(1.2,1.2,1.2);
        }
        gl.glTranslated(0,0,0.4);
        gl.glRotated(tAnim*10, 1 ,0, 0);
        glut.glutSolidCone(0.6, 2.2, 16, 1); // draw tail cone
        gl.glTranslated(0,0,0.4);
        gl.glScaled(0.6,0.6,1);
        tailSegAmnt--;
        if (tailSegAmnt > 0) // if more tail-segments remain to be drawn
            drawTail(gl, glu, glut, tAnim, tailSegAmnt); // recurse
        gl.glPopMatrix();
    }
    
    public void drawNeck(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        gl.glScaled(0.9,0.8,0.8); // global neck scale
        gl.glRotated(-10 + tAnim*10,0,1,0);
        gl.glScaled(2,1,1);
        gl.glTranslated(0.5,0,0);
        glut.glutSolidCube(1);
        gl.glTranslated(0.4,0,0);
        gl.glScaled(0.5,1,1);
        drawHead(gl, glu, glut, tAnim);
        gl.glPopMatrix();
    }
    
    public void drawHead(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        gl.glRotated(10 - tAnim*10,0,1,0);
        gl.glScaled(2,1.3,1.3);
        gl.glScaled(headScale, headScale, headScale);
        gl.glTranslated(0.5,0,0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
    }
    
    public void drawLowerLeg(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        gl.glRotated(90 - tAnim*50,0,1,0);
        gl.glScaled(1.5,0.5,0.5);
        gl.glTranslated(0.5,0,0);
        glut.glutSolidCube(1);
        gl.glTranslated(0.4,0,0);
        gl.glScaled(1/1.5,2,2);
        drawFoot(gl, glu, glut, tAnim);
        gl.glPopMatrix();
    }
    
    private void drawFoot (GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
        gl.glRotated(-90+50*tAnim,0,1,0);
        gl.glTranslated(0.5,0,0);
        gl.glScaled(1.4,0.6,0.2);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
    }
}
