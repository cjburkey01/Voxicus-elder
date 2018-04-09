#version 330 core

in layout(location = 0) vec3 pos;
in layout(location = 1) vec2 texCoord;

out vec2 uv;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(pos, 1.0);
	uv = texCoord;
}