package robotrace;

/**
 * Implementation of a camera with a position and orientation. 
 */
class Camera {

    /** The position of the camera. */
    public Vector eye = new Vector(3f, 6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;

    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(GlobalState gs, Robot focus) {

        switch (gs.camMode) {
            
            // First person mode    
            case 1:
                setFirstPersonMode(gs, focus);
                break;
                
            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
        Vector dir = new Vector(Math.cos(-gs.theta)*Math.cos(gs.phi), Math.sin(-gs.theta)*Math.cos(gs.phi), Math.sin(gs.phi));
        Vector delta = dir.scale(gs.vDist);
        Vector E = gs.cnt.add(delta);
    
        /** The position of the camera. */
        eye = E;

        /** The point to which the camera is looking. */
        center = gs.cnt;

        /** The up vector. */
        up = Vector.Z;
    }

    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        eye = focus.position;
        eye.z += 3;
        center = eye.add(focus.direction);
        //center.z += 2;
        
        up = Vector.Z;
    }
}
