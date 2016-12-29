/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Vladimir
 */
public class util {
    public static GlobalState gs;
    // rotates vector v, d degrees around the axes marked as greater than 0
    static public Vector Rotate(Vector v, float d, int x, int y, int z) {
        float r = (float)(d/180.0*Math.PI);
        if (x > 0) {
            float curr = (float)Math.atan2(v.z,v.y);
            float l = (float)Math.sqrt(v.y*v.y+v.z*v.z);
            v.y = l*Math.cos(curr + r);
            v.z = l*Math.sin(curr + r);
        }
        if (y > 0) {
            float curr = (float)Math.atan2(v.z,v.x);
            float l = (float)Math.sqrt(v.x*v.x+v.z*v.z);
            v.x = l*Math.cos(curr + r);
            v.z = l*Math.sin(curr + r);
        }
        if (z > 0) {
            float curr = (float)Math.atan2(v.y,v.x);
            float l = (float)Math.sqrt(v.y*v.y+v.x*v.x);
            v.x = l*Math.cos(curr + r);
            v.y = l*Math.sin(curr + r);
        }
        return v;
    }
    
    public static double choose(int x, int y)
    {
        double denominator = 1.0, numerator = 1.0;
        for (int i = 1; i <= y; i++) {
            denominator *= i;
            numerator *= (x + 1 - i);
        }
        return numerator / denominator;
    }
    
    // draws plane of dimensions xDim x yDim with xP points in the x direction, and yP points in the y direction
    static public void drawPlane(double xDim, double yDim, int xP, int yP, GL2 gl, GLU glu, GLUT glut) {      
        double dx = xDim/(double)xP;
        double dy = yDim/(double)yP;
        gl.glBegin(gl.GL_TRIANGLES);
        for(double x = -0.5*xDim; x < 0.5*xDim-0.5*dx; x+=dx) {
            gl.glNormal3f(0,0,1);
            for(double y = -0.5*yDim; y < 0.5*yDim-0.5*dy; y+=dy) {
                // first triangle of quad
                gl.glVertex3d(x, y, 0);
                gl.glVertex3d(x, y+dy, 0);
                gl.glVertex3d(x+dx,y,0);
                // second triangle of quad
                gl.glVertex3d(x, y+dy, 0);
                gl.glVertex3d(x+dx, y+dy, 0);
                gl.glVertex3d(x+dx,y,0);
            }   
        }
        gl.glEnd();
    }  
}
