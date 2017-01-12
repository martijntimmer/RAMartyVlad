
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
    final int NUM_VERT = 40;        //Number of quads used to draw the track
    
    @Override
    protected Vector getPoint(double t) {        
        return new Vector(WIDTH*Math.cos(2*Math.PI*t), HEIGHT*Math.sin(2*Math.PI*t), 1);
    }
    
    // WIP  lane ID's range from 0 to NUM_LANES-1
    @Override
    public Vector getLanePoint(int lane, double t){
        Vector tangent = getTangent(t);
        Vector normal = new Vector(tangent.y, -tangent.x, 0);
        Vector offset = normal.scale(LANE_WIDTH*(lane - (NUM_LANES-1)/2) - 0.6f);
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
    
    @Override
    public void draw(GL2 gl, GLU glu, GLUT glut) {  
        double dt = 1.0/NUM_VERT;      
        for(int i = 0; i < 3; i++)
        {
            Textures.track[i].bind(gl);
            gl.glBegin(gl.GL_QUAD_STRIP);
            for(int j = 0; j <= NUM_VERT; j++)
            {
                drawSegmentOfTrack(gl, j,j*dt, i);
            }      
            gl.glEnd();
        }      
    }  
  
     private void drawSegmentOfTrack(GL2 gl, int j, double t, int drawingObject) {
        double halfTrackWidth = NUM_LANES * LANE_WIDTH * 0.5;
        Vector tangent = getTangent(t);
        Vector normal = new Vector(tangent.y, -tangent.x, 0); 
        Vector point = getPoint(t);
        
        Vector pointInner = point.subtract(normal.scale(halfTrackWidth));
        Vector pointOuter = point.add(normal.scale(halfTrackWidth));
        
        switch (drawingObject) {       
            case 0:     //Draws the top of the track
                gl.glNormal3f(0,0,1);
               // gl.glColor3d(1, 0, 0);
                gl.glTexCoord2d(0, j%2==0?0:1);                             
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z);
                gl.glTexCoord2d(1,j%2==0?0:1);
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z);                
                break;        
            case 1:     //Draws the outer walls of the track
                gl.glNormal3f((float)normal.x,(float)normal.y,(float)normal.z);
                gl.glColor3d(0, 1, 0);
                gl.glTexCoord2d(j%2==0?0:1,0); 
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z);
                gl.glTexCoord2d(j%2==0?0:1,1);
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z - 2);
               
                break;       
            case 2:     //Draws the innter walls of the track
                gl.glNormal3f((float)-normal.x,(float)-normal.y,(float)-normal.z);
                gl.glColor3d(0, 0, 1);
                gl.glTexCoord2d(j%2==0?0:1,0); 
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z);
                 gl.glTexCoord2d(j%2==0?0:1,1);
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z - 2);
               
                break;      
        }
    }
}
