package com.yanan.framework.javascript;

import java.io.Reader;

public interface ScriptEngine  {
    public Object eval(String script) throws ScriptException;
    public Object eval(Reader reader) throws ScriptException;
    public Object eval(String script, Bindings n) throws ScriptException;
    public Object eval(Reader reader , Bindings n) throws ScriptException;
    public void put(String key, Object value);
    public Object get(String key);
    public Bindings getBindings();
    public void setBindings(Bindings bindings);
    public Bindings createBindings();
    public ScriptEngineFactory getFactory();
}
