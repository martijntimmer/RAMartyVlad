#version 120
// simple vertex shader
varying vec3 P;
varying vec3 N;
varying vec3 p;
varying float wf; // waterfront height
varying float maxwf; // max waterfront height
uniform float n;
uniform float t;

void main() {
     // apply height function
    vec4 res = gl_Vertex;
    res.z = 0.6*cos(0.3*res.x+0.2*res.y) + 0.4*cos(res.x-0.5*res.y)-0.5;
    p = res.xyz/res.w;
    float posScale = 4;
    float hScale = 0.2;
    wf = (0.6*cos((0.3*res.x+0.2*res.y)*posScale + t) + 0.4*cos((res.x-0.5*res.y)*posScale + t))*hScale - 0.25; // waterfront
    maxwf = hScale - 0.25;
    N = normalize(gl_NormalMatrix*gl_Normal); // calculate normal in modelView

    vec4 pos = gl_ModelViewMatrix*res;
    P = pos.xyz/pos.w; // calculate position of vertex in modelView

	// output of vertex shader
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_FrontColor  = gl_Color; // calculate and apply lighting
    gl_Position    = gl_ModelViewProjectionMatrix * res;
} 