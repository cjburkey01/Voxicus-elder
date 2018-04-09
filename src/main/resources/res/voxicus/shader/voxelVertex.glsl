#version 330 core

in layout(location = 0) vec3 pos;
in layout(location = 1) vec3 normal;
in layout(location = 2) vec2 offset;
in layout(location = 3) vec3 qpos;

out vec2 tileUv;
out vec2 otexCoord;
out vec2 tileOffset;
out float otileSize;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform float tileSize;

void main() {
	gl_Position = projectionMatrix * modelViewMatrix * vec4(pos, 1.0);
	
	tileUv = vec2(dot(normal.zxy, qpos), dot(normal.yzx, qpos));
	otexCoord = offset + tileSize * fract(tileUv);
	tileOffset = offset;
	otileSize = tileSize;
}