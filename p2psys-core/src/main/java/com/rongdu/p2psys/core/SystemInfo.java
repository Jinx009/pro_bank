package com.rongdu.p2psys.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rongdu.p2psys.core.domain.SystemConfig;

public class SystemInfo {
	private Map<String, SystemConfig> map;

	public SystemInfo() {
		map = Collections.synchronizedMap(new HashMap<String, SystemConfig>());
	}

	public void addConfig(SystemConfig sys) {
		map.put(sys.getNid().replace("con_", ""), sys);
	}

	private SystemConfig getConfig(String key) {
		SystemConfig sys = (SystemConfig) map.get(key);
		return sys;
	}

	public String getValue(String key) {
		SystemConfig c = getConfig(key);
		if (c == null)
			return null;
		return c.getValue();
	}

	public String getStatus(String key) {
		SystemConfig c = getConfig(key);
		if (c == null)
			return null;
		return getConfig(key).getStatus() + "";
	}

}
