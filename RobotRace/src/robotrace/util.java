/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotrace;

/**
 *
 * @author Vladimir
 */
public class util {
    
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
}
