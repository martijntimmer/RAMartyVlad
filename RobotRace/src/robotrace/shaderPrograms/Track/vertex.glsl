#version 120
// simple vertex shader
varying vec3 P;
varying vec3 N;

void main() {
    N = normalize(gl_NormalMatrix*gl_Normal); // calculate normal in modelView
    vec4 pos = gl_ModelViewMatrix*gl_Vertex;
	P = pos.xyz/pos.w; // calculate position of vertex in modelView

	// output of vertex shader
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_FrontColor  = gl_Color; // calculate and apply lighting
    gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
} 