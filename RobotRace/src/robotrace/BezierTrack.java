
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
    final double LANE_WIDTH = 1.22; //Width of lane
    final int NUM_LANES = 4;        //Number of lanes
    final int NUM_VERT = 201;        //Number of quads used to draw the track
    
    BezierTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
    }
    
     // WIP  lane ID's range from 0 to NUM_LANES-1
    @Override
    public Vector getLanePoint(int lane, double t){
        t = t % 1.0;
        Vector tangent = getTangent(t);
        Vector normal = new Vector(tangent.y, -tangent.x, 0);
        Vector offset = normal.scale(LANE_WIDTH*(lane - (NUM_LANES-1)/2) - 0.6);
        return getPoint(t).add(offset);
    }
    
    // WIP
    @Override
    public Vector getLaneTangent(int lane, double t){  
        t = t % 1.0;
        return getTangent(t);
    }
    
    private double B(int i, int n, double t)
    {
        return util.choose(n, i)*Math.pow(t, i)*Math.pow(1-t,n-i);
    }

    @Override
    protected Vector getPoint(double t) {
        t = Math.min(t, 0.99999);               //1 is technically a new curve instead of the last
        Vector ans = Vector.O;       
        int n = 3;
        int numCurves = controlPoints.length/4;  //Amount of bezier curves
        double dt = 1.0/numCurves;                 //How much 't' per curve
        int iCurve =  Math.min((int)(t*numCurves), numCurves -1);        //Which bezier curve it is
        double newT = (t % (dt))/dt;
        for(int i = 0; i <= n; i++)
            ans = ans.add(controlPoints[iCurve*4 + i].scale(B(i,n,newT)));
        return ans;
    }

    @Override
    protected Vector getTangent(double t) {
        t = Math.min(t, 0.99999);               //1 is technically a new curve instead of the last
        int n = 3;
        Vector ans = Vector.O;
        
        int numCurves = controlPoints.length/4;  //Amount of bezier curves
        double dt = 1.0/numCurves;                 //How much 't' per curve
        int iCurve =  Math.min((int)(t*numCurves), numCurves -1);        //Which bezier curve it is
        double newT = (t % (dt))/dt;
        
        if(iCurve*4+2+1 > controlPoints.length)
        {
            int abc=123;            
        }
        for(int i = 0; i <= n-1; i++)
        {
            Vector Q = (controlPoints[iCurve*4+i+1].subtract(controlPoints[iCurve*4+i])).scale(n);
            ans = ans.add(Q.scale(B(i,n-1,newT)));
        }
        return ans.normalized();
    }
       
    @Override
    public void draw(GL2 gl, GLU glu, GLUT glut) {      
        double dt = 1.0/NUM_VERT; 
        int j = 0;
        for(int i = 0; i < 3; i++)
        {
            Textures.track[i].bind(gl);
            gl.glBegin(gl.GL_QUAD_STRIP);
            for(double t = 0; t <= 1; t+=dt) 
            {
                drawSegmentOfTrack(gl,j, t, i);
                j++;
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
