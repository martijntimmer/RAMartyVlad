
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
    final int NUM_VERT = 20;        //Number of quads used to draw the track
    
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
    
    @Override
  public void draw(GL2 gl, GLU glu, GLUT glut) {      
      double dt = 1.0/NUM_VERT;
      
      gl.glBegin(gl.GL_QUAD_STRIP);
      for(double t = 0; t <= 1; t+=dt)
      {
          drawSegment(gl, t);
      }      
      drawSegment(gl, 0); //Closes the loop
       gl.glEnd();
  }
  
  private void drawSegment(GL2 gl, double t) {
      double halfTrackWidth = NUM_LANES * LANE_WIDTH * 0.5;
          Vector tangent = getTangent(t);
          Vector normal = new Vector(tangent.y, -tangent.x, 0);      
          Vector pointInner = getPoint(t).subtract(normal.scale(halfTrackWidth));
          Vector pointOuter = getPoint(t).add(normal.scale(halfTrackWidth));
          gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z);
          gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z);
  }  
}
