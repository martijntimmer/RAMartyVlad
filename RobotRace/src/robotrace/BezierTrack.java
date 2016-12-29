
package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Implementation of RaceTrack, creating a track from control points for a 
 * cubic Bezier curve
 */
public class BezierTrack extends RaceTrack {
    
    private Vector[] controlPoints;
    final double LANE_WIDTH = 1; //Width of lane
    final int NUM_LANES = 1;        //Number of lanes
    final int NUM_VERT = 4;        //Number of quads used to draw the track
    
    BezierTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
    }
    
    private double B(int i, int n, double t)
    {
        return util.choose(n, i)*Math.pow(t, i)*Math.pow(1-t,n-i);
    }
    Vector[] points = new Vector[] { (new Vector(3,3,0)), (new Vector(5,6,0)), (new Vector(10,6,0)), (new Vector(15, 2, 0)) };
    @Override
    protected Vector getPoint(double t) {
        Vector ans = Vector.O;
       
        int n = 3;
        for(int i = 0; i <= n; i++)
            ans = ans.add(points[i].scale(B(i,n,t)));
        return ans;
    }

    @Override
    protected Vector getTangent(double t) {
        int n = 3;
        Vector ans = Vector.O;
        for(int i = 0; i <= n-1; i++)
        {
            Vector Q = (points[i+1].subtract(points[i])).scale(n);
            ans = ans.add(Q.scale(B(i,n-1,t)));
        }
        return ans.normalized();
    }
       
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
                gl.glNormal3f(0,0,1);
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z);
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z);
                gl.glColor3d(0.8, 0.4, 1);
                break;        
            case 1:     //Draws the outer walls of the track
                gl.glNormal3f((float)normal.x,(float)normal.y,(float)normal.z);
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z);
                gl.glVertex3d(pointOuter.x, pointOuter.y, pointOuter.z - 2);
                gl.glColor3d(1, 0.2, 0.6);
                break;       
            case 2:     //Draws the innter walls of the track
                gl.glNormal3f((float)-normal.x,(float)-normal.y,(float)-normal.z);
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z);
                gl.glVertex3d(pointInner.x, pointInner.y, pointInner.z - 2);
                gl.glColor3d(0, 1, 1);
                break;      
        }
    }
}
