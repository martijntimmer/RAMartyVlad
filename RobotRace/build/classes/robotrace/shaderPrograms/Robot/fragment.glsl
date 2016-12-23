# version 120
/*
 * Implementation of fragment shading in GLSL
 * Questions: Dark edges around the light circles - better seen with multiple light sources
 * Why use "max" below? Can dot product be negative? Isn't min more logical?
 */

uniform bool ambient, diffuse, specular;
uniform float n;
uniform int lightAmnt;
uniform sampler2D texture;

varying vec3 N;
varying vec3 P;

// compute contribution of each kind of lighting
vec4 shading(vec3 P, vec3 N, gl_LightSourceParameters light, gl_MaterialParameters mat) {
	vec4 result  = vec4(0,0,0,1); // opaque black is initial color
    if (ambient) { result += mat.ambient*light.ambient; } // compute and add ambient contribution
    vec3 L = normalize(light.position.xyz/light.position.w - P); // compute direction of light
	float diffIntensity = max(dot(N, L), 0); // compute dffuse intensity
    if (diffuse) { result += (diffIntensity*mat.diffuse*light.diffuse); }  // compute and add diffuse contribution
    vec3 V = normalize(-P); // direction towards viewer
	float specularIntensity = 0;
	if (dot(N, L) >= -0.1) specularIntensity = pow(max(dot(N, normalize(V+L)),0),n); // compute specular intensity
	//specularIntensity = pow(max(dot(N, normalize(V+L)),0),n);
    if (specular) { result += specularIntensity * light.specular * mat.specular; } // compute and add specular contribution
    return result;
}

void main() {	gl_FragColor = gl_Color;
    gl_MaterialParameters mat      = gl_FrontMaterial;
	// output of vertex shader
	vec4 color = texture2D(texture,gl_TexCoord[0].st);
	vec4 res = vec4(0, 0, 0, 1); // default color is opaque black
	for (int i = 0; i < lightAmnt; i++) {
		res += shading(P, N, gl_LightSource[i], mat);
	}
	gl_FragColor = res*color;
} 
