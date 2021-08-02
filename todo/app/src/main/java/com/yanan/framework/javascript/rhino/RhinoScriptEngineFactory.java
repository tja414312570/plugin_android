package com.yanan.framework.javascript.rhino;

import com.yanan.framework.javascript.ScriptEngine;
import com.yanan.framework.javascript.ScriptEngineFactory;
import com.yanan.framework.javascript.ScriptEngineManager;

public class RhinoScriptEngineFactory implements ScriptEngineFactory {
	public static final String ENGINE_NAME = "Rhino";
	static {
		ScriptEngineManager.register(ENGINE_NAME,new RhinoScriptEngineFactory());
	}
	@Override
	public ScriptEngine getScriptEngine() {
		return new RhinoScriptEngine();
	}

}
