#version 120
// simple vertex shader
varying vec3 P;
varying vec3 p;
varying vec3 N;

void main() {
	// apply height function
   	vec4 res = gl_Vertex;
	res.z = 0.6*cos(0.3*res.x+0.2*res.y) + 0.4*cos(res.x-0.5*res.y);
        float nx = 0.18*sin(0.3*res.x+0.2*res.y) + 0.4*sin(res.x-0.5*res.y);
        float ny = 0.12*sin(0.3*res.x+0.2*res.y) - 0.2*sin(res.x-0.5*res.y);
        N = vec3(nx,ny,1);

    p = vec3(gl_Vertex.x,gl_Vertex.y,gl_Vertex.z);
    N = normalize(gl_NormalMatrix*gl_Normal); // calculate normal in modelView
    vec4 pos = gl_ModelViewMatrix*gl_Vertex;
    P = pos.xyz/pos.w; // calculate position of vertex in modelView

	// output of vertex shader
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_FrontColor  = gl_Color; // calculate and apply lighting
    gl_Position    = gl_ModelViewProjectionMatrix * res;
} 