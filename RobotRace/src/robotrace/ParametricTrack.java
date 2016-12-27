
package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Implementation of RaceTrack, creating a track from a parametric formula
 */
public class ParametricTrack extends RaceTrack {
    
    final int WIDTH = 10;           //Distance of center line to center line horizontally
    final int HEIGHT = 14;          //Distance of center line to center line vertically
    final double LANE_WIDTH = 1.22; //Width of lane
    final int NUM_LANES = 4;        //Number of lanes
    final int NUM_VERT = 25;        //Number of quads used to draw the track
    
    @Override
    protected Vector getPoint(double t) {        
        return new Vector(WIDTH*Math.cos(2*Math.PI*t), HEIGHT*Math.sin(2*Math.PI*t), 1);
    }
    
    // WIP  lane ID's range from 0 to NUM_LANES-1
    @Override
    public Vector getLanePoint(int lane, double t){
        Vector tangent = getTangent(t);
        Vector normal = new Vector(tangent.y, -tangent.x, 0);
        Vector offset = normal.scale(LANE_WIDTH*(lane - (NUM_LANES-1)/2));
        return getPoint(t).add(offset);
    }
    
    // WIP
    public Vector getLaneTangent(int lane, double t){        
        return getTangent(t);
    }

    @Override
    protected Vector getTangent(double t) {
        Vector tan = new Vector(-WIDTH*2*Math.PI*Math.sin(2*Math.PI*t), HEIGHT*2*Math.PI*Math.cos(2*Math.PI*t), 0);
        return tan.normalized();
    }
    
    // WIP: Colours have strange transition when going from one drawing object to next :S 
    // TODO: Add normals
    @Override
    public void draw(GL2 gl, GLU glu, GLUT glut) {      
        double dt = 1.0/NUM_VERT;      
        for(int i = 0; i < 3; i++)
        {
            gl.glBegin(gl.GL_QUAD_STRIP);
            for(double t = 0; t <= 1+dt; t+=dt) //+dt because sometimes the loop is not closed :S
            {
                drawSegmentOfTrack(gl, t, i);
            }      
            gl.glEnd();
        }      
    }  
  
    private void drawSegmentOfTrack(GL2 gl, double t, int drawingObject) {
        double halfTrackWidth = NUM_LANES * LANE_WIDTH * 0.5;
        Vector tangent = getTangent(t);
        Vector normal = new Vector(tangent.y, -tangent.x, 0); 
        Vector point = getPoint(t);
        
        Vector pointInner = point.subtract(normal.scale(halfTrackWidth));
        Vector pointOuter = point.add(normal.scale(halfTrackWidth));
        
        switch (drawingObject) {       
            case 0:     //Draws the top of the track               
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z);
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z);
                gl.glColor3d(0.8, 0.4, 1);
                break;        
            case 1:     //Draws the outer walls of the track
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z);
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z - 2);
                gl.glColor3d(1, 0.2, 0.6);
                break;       
            case 2:     //Draws the innter walls of the track
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z);
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z - 2);
                gl.glColor3d(0, 1, 1);
                break;      
        }
    }
}
