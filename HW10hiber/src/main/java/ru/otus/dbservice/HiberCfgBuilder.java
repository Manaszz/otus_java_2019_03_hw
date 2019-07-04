package ru.otus.dbservice;


import java.util.ArrayList;
import java.util.List;

public class HiberCfgBuilder {
    private String configPath;
    private List<Class> annotatedClasses;
    public HiberCfgBuilder(String cfgPath) {
    this.configPath = cfgPath;
    annotatedClasses = new ArrayList<>();
    }

    public String getConfigPath() {
        return configPath;
    }

    public List<Class> getAnnotatedClasses() {
        return annotatedClasses;
    }

    public HiberCfgBuilder addClass(Class<?>  clazz) {
        annotatedClasses.add(clazz);
        return this;

    }

}
