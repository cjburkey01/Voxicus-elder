#version 330 core

in layout(location = 0) vec3 pos;
in layout(location = 1) vec2 texCoord;
in layout(location = 2) int tid;

out vec2 uv;
flat out int textureId;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(pos, 1.0);
	uv = texCoord;
	textureId = tid;
}