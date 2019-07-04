package ru.otus.dbservice;


import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class HiberCfgBuilder {
    private String configPath;
    private List<Class> annotatedClasses;
    private SessionFactory sessionFactory;

    public HiberCfgBuilder(String cfgPath) {
    this.configPath = cfgPath;
    annotatedClasses = new ArrayList<>();
    sessionFactory =null;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public List<Class> getAnnotatedClasses() {
        return annotatedClasses;
    }

    public HiberCfgBuilder addClass(Class<?>  clazz) {
        annotatedClasses.add(clazz);
        return this;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public SessionFactory  build(){
        Configuration configuration = new Configuration()
                .configure(configPath);

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        MetadataSources metaSource= new MetadataSources(serviceRegistry);
        for(Class c:annotatedClasses){
            metaSource.addAnnotatedClass(c);
        }

        this.sessionFactory = metaSource.getMetadataBuilder().build().getSessionFactoryBuilder().build();

        return this.sessionFactory;
    }

}
