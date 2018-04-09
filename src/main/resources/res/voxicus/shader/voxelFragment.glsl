#version 330 core

in vec2 tileUv;
in vec2 otexCoord;
in vec2 tileOffset;
in float otileSize;

out vec4 fragColor;

uniform sampler2D sampler;

vec4 fourTapSample() {
	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	float totalWeight = 0.0;
	
	for (int x = 0; x < 2; x ++) {
		for (int y = 0; y < 2; y ++) {
			vec2 tileCoord = 2.0 * fract(0.5 * (tileUv + vec2(x, y)));
			float w = pow(1.0 - max(abs(tileCoord.x - 1.0), abs(tileCoord.y - 1.0)), 16);
			vec2 atlasUv = tileOffset + otileSize * tileCoord;
			color += w * texture(sampler, atlasUv);
			totalWeight += w;
		}
	}
	
	return color / totalWeight;
}

void main() {
	fragColor = fourTapSample();
}