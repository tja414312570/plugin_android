package com.yanan.util.xml;

public class NodeStack {
    private Object instance;
    private EntryMapping entryMapping;
    private String name;

    public Object getInstance() {
        return instance;
    }

    public NodeStack(Object instance, EntryMapping entryMapping, String name) {
        this.instance = instance;
        this.entryMapping = entryMapping;
        this.name = name;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public EntryMapping getEntryMapping() {
        return entryMapping;
    }

    public void setEntryMapping(EntryMapping entryMapping) {
        this.entryMapping = entryMapping;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NodeStack{" +
                "instance=" + instance +
                ", entryMapping=" + entryMapping +
                ", name='" + name + '\'' +
                '}';
    }
}
