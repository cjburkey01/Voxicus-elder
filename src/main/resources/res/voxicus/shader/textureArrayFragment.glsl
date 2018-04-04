#version 330 core

in vec2 uv;
flat in int textureId;

out vec4 fragColor;

uniform int textureTotal;
uniform sampler2DArray samplerArr;

void main() {
	fragColor = texture(samplerArr, vec3(uv, textureId));
}