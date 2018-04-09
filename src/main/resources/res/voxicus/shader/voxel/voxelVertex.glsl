#version 330 core

in layout(location = 0) vec3 vpos;
in layout(location = 1) vec3 vnormal;
in layout(location = 2) vec2 tileTexturePos;

out vec3 pos;
out vec3 normal;
out vec2 tilePos;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(vpos, 1.0);
	
	pos = vpos;
	normal = vnormal;
	tilePos = tileTexturePos;
}