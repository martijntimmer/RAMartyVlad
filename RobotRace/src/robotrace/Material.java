package robotrace;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /** 
     * Gold material properties.
     * Modify the default values to make it look like gold.
     */
    GOLD (
            
        new float[] {0.4f, 0.4f, 0.4f, 1},
        new float[] {0.9f, 0.9f, 0.9f, 1},
        100

    ),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     */
    SILVER (
            
        new float[] {0.4f, 0.4f, 0.4f, 1},
        new float[] {0.9f, 0.9f, 0.9f, 1},
        100

    ),

    /** 
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE (
            
        new float[] {0.6f, 0.6f, 0.6f, 1},
        new float[] {0.5f, 0.5f, 0.5f, 1},
        10

    ),

    /**
     * Wood material properties.
     * Modify the default values to make it look like Wood.
     */
    WOOD (

        new float[] {0.6f, 0.6f, 0.6f, 1},
        new float[] {0.5f, 0.5f, 0.5f, 1},
        5

    );

    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
