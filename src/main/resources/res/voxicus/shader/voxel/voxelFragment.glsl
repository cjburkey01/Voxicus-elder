#version 330 core

in vec3 pos;
in vec3 normal;
in vec2 tilePos;

out vec4 fragColor;

uniform sampler2D sampler;
uniform int atlasPreDoubleWidth;

vec2 getUv() {
	return vec2(dot(normal.zxy, pos), dot(normal.yzx, pos));
}

vec4 getTexture() {
	vec2 texUv = getUv();
	vec2 tileOffset = tilePos / vec2(atlasPreDoubleWidth, atlasPreDoubleWidth);
	float tileSize = 1.0 / atlasPreDoubleWidth / 2.0;
	
	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	float weight = 0.0;
	
	for (int x = 0; x < 2; x ++) {
		for (int y = 0; y < 2; y ++) {
			vec2 tileCoord = 2.0 * fract(0.5 * (texUv + vec2(x, y)));
			float w = pow(1.0 - max(abs(tileCoord.x - 1.0), abs(tileCoord.y - 1.0)), 16.0);
			vec2 atlasUv = tileOffset + tileSize * tileCoord;
			color += w * texture(sampler, atlasUv);
			weight += w;
		}
	}
	
	return color / weight;
}

void main() {
	fragColor = getTexture();
}