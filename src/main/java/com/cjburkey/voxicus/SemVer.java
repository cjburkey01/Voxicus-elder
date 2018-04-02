package com.cjburkey.voxicus;

import org.joml.Vector3i;

public class SemVer {
	
	private final Vector3i ver = new Vector3i();
	private final String extra;
	
	public SemVer(int major, int minor, int patch) {
		this(major, minor, patch, null);
	}
	
	public SemVer(int major, int minor, int patch, String extra) {
		ver.set(major, minor, patch);
		this.extra = (extra == null) ? null : extra.trim().replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "").toLowerCase();
	}
	
	public int getMajor() {
		return ver.x;
	}
	
	public int getMinor() {
		return ver.y;
	}
	
	public int getPatch() {
		return ver.z;
	}
	
	public String getExtra() {
		return extra;
	}
	
	public boolean hasExtra() {
		return extra != null && !extra.isEmpty();
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extra == null) ? 0 : extra.hashCode());
		result = prime * result + ((ver == null) ? 0 : ver.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SemVer other = (SemVer) obj;
		if (extra == null) {
			if (other.extra != null) {
				return false;
			}
		} else if (!extra.equals(other.extra)) {
			return false;
		}
		if (ver == null) {
			if (other.ver != null) {
				return false;
			}
		} else if (!ver.equals(other.ver)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return ver.x + "." + ver.y + "." + ver.z + ((extra == null) ? ("") : ("-" + extra));
	}
	
}