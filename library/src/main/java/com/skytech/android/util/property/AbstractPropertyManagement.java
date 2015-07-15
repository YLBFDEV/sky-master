package com.skytech.android.util.property;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPropertyManagement {
	protected Map<String,String> properties = new HashMap<String,String>();
	
	public String getString(String name) {
		return properties.get(name);
	}
}
