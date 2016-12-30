package robotrace;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_LESS;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_NORMALIZE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static robotrace.ShaderPrograms.*;
import static robotrace.Textures.*;

/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards; (Not required in this assignment)
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the folder textures. 
 * These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * Textures.track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
    
    private final double startTime;
        
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        // determine starting time of program
        // used to determine how long the race is running
        startTime = System.currentTimeMillis();
        //startTime = 0;
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[2];
        
        // Track 1
        raceTracks[0] = new ParametricTrack();
        
        // Track 2
        float g = 3.5f;
        float width = 10;
        float length = 15;
        float height = 5;
        raceTracks[1] = new BezierTrack(                
            new Vector[] { new Vector(0,0,1), new Vector(-width,0,1), new Vector(-width,length,1), new Vector(0, length, 1), //Lower left C
                           new Vector(0,length,1), new Vector(width,length,1), new Vector(width,length*2,1), new Vector(0, length*2, 1), //Upper right C 
                           new Vector(0,length*2,1), new Vector(-width,length*2,1), new Vector(-width,length,1+height), new Vector(0, length, 1+height), //Upper left C
                           new Vector(0, length, 1+height), new Vector(width,length,1+height), new Vector(width,0,0), new Vector(0, 0, 1), //Lower right C
            }       
        );
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD,gs,raceTracks,0
                
        );
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER,gs,raceTracks,1
              
        );
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD,gs,raceTracks,2
              
        );

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE,gs,raceTracks,3
                
        );
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the terrain
        terrain = new Terrain();
        util.gs = gs;
    } 
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {
		
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
        // Enable face culling for improved performance
        // gl.glCullFace(GL_BACK);
        // gl.glEnable(GL_CULL_FACE);
        
	    // Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
	// Try to load four textures, add more if you like in the Textures class         
        Textures.loadTextures();
        reportError("reading textures");
        
        // Try to load and set up shader programs
        ShaderPrograms.setupShaders(gl, glu);
        reportError("shaderProgram");
        
    }
   
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        glu.gluPerspective(45, (float)gs.w / (float)gs.h, 0.1*gs.vDist, 10*gs.vDist);
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        
        gl.glMatrixMode(GL_PROJECTION);
        // Add light source
        gl.glLightfv(GL_LIGHT0, GL_POSITION, new float[]{0,0,0,1f}, 0); // right,up,along front
        gl.glMatrixMode(GL_MODELVIEW);
               
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        glu.gluLookAt(camera.eye.x(),    camera.eye.y(),    camera.eye.z(),
                      camera.center.x(), camera.center.y(), camera.center.z(),
                      camera.up.x(),     camera.up.y(),     camera.up.z());
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        
        gl.glUseProgram(defaultShader.getProgramID());
        reportError("program");
        
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        

    // Draw hierarchy example.
        //drawHierarchy();
        drawAxisFrame();
        
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
        
        // Draw the (first) robot.
        gl.glUseProgram(defaultShader.getProgramID());
        for (int i = 0; i < 4; i++) {
        robots[i].draw(gl, glu, glut, (float)((System.currentTimeMillis()-startTime)/1000.0));
        }
        //robots[0].draw(gl, glu, glut, (float)((gs.sliderA-startTime)*1000.0/1000.0));
        
        /*
        gl.glUseProgram(defaultShader.getProgramID());
        gl.glPushMatrix();
        gl.glTranslated(0,-2,0);
        robots[0].draw(gl, glu, glut, (float)(System.currentTimeMillis()%1000.0/1000.0));
        gl.glTranslated(0,2,0);
        robots[1].draw(gl, glu, glut, (float)(System.currentTimeMillis()%1000.0/1000.0));
        gl.glTranslated(0,2,0);
        robots[2].draw(gl, glu, glut, (float)(System.currentTimeMillis()%1000.0/1000.0));
        gl.glTranslated(0,2,0);
        robots[3].draw(gl, glu, glut, (float)(System.currentTimeMillis()%1000.0/1000.0));
        gl.glPopMatrix();*/
        // Draw the race track.
        gl.glUseProgram(defaultShader.getProgramID());
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        
        
        // Draw the terrain.
        gl.glUseProgram(terrainShader.getProgramID());
       // terrain.draw(gl, glu, glut);
        reportError("terrain:");
        
        
    }
    
    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        gl.glPushMatrix();
        gl.glColor3d(1, 1 ,0);
        glut.glutSolidSphere(0.2, 8, 8);
        gl.glColor3d(1, 0, 0);
        drawArrow(); // x axis
        gl.glRotated(90, 0, 0, 1);
        gl.glColor3d(0, 1, 0);
        drawArrow(); // y axis
        gl.glRotated(-90, 0, 1, 0);
        gl.glColor3d(0, 0, 1);
        drawArrow(); // z axis
        gl.glPopMatrix();
    }
    
    /**
     * Draws a single arrow aligned with the x-axis
     */
    public void drawArrow() {
        gl.glPushMatrix();
            // draw arrow body
            gl.glScaled(1, 0.1, 0.1);
            gl.glTranslated(0.5, 0, 0);
            glut.glutSolidCube(1);
            gl.glTranslated(-0.5, 0, 0);
            gl.glScaled(1, 10, 10);
            // draw arrow head
            gl.glTranslated(1, 0, 0);
            gl.glRotated(90.0, 0, 1, 0);
            glut.glutSolidCone(0.2, 0.3, 8, 1);
        gl.glPopMatrix();
    }
 
    /**
     * Drawing hierarchy example.
     * 
     * This method draws an "arm" which can be animated using the sliders in the
     * RobotRace interface. The A and B sliders rotate the different joints of
     * the arm, while the C, D and E sliders set the R, G and B components of
     * the color of the arm respectively. 
     * 
     * The way that the "arm" is drawn (by calling {@link #drawSecond()}, which 
     * in turn calls {@link #drawThird()} imposes the following hierarchy:
     * 
     * {@link #drawHierarchy()} -> {@link #drawSecond()} -> {@link #drawThird()}
     */
    private void drawHierarchy() {
        gl.glColor3d(gs.sliderC, gs.sliderD, gs.sliderE);
        gl.glPushMatrix(); 
            gl.glScaled(2, 1, 1);
            glut.glutSolidCube(1);
            gl.glScaled(0.5, 1, 1);
            gl.glTranslated(1, 0, 0);
            gl.glRotated(gs.sliderA * -90.0, 0, 1, 0);
            drawSecond();
        gl.glPopMatrix();
    }
    
    private void drawSecond() {
        gl.glTranslated(1, 0, 0);
        gl.glScaled(2, 1, 1);
        glut.glutSolidCube(1);
        gl.glScaled(0.5, 1, 1);
        gl.glTranslated(1, 0, 0);
        gl.glRotated(gs.sliderB * -90.0, 0, 1, 0);
        drawThird();
    }
    
    private void drawThird() {
        gl.glTranslated(1, 0, 0);
        gl.glScaled(2, 1, 1);
        glut.glutSolidCube(1);
    }
    
    
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    }
}
