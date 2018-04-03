#version 330 core

in layout(location = 0) vec3 pos;
in layout(location = 1) vec3 col;

out vec3 color;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(pos, 1.0);
	color = col;
}