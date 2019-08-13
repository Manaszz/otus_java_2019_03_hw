package ru.otus.kchu;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.otus.kchu.dao.*;
import ru.otus.kchu.services.cache.CacheEngine;
import ru.otus.kchu.services.cache.CacheEngineSoftImpl;
import ru.otus.kchu.repository.dbrepository.DBRepository;
import ru.otus.kchu.repository.dbrepository.DBServiceFactory;
import ru.otus.kchu.repository.dbrepository.DbRepositoryHiber;
import ru.otus.kchu.repository.dbrepository.HiberCfgBuilder;

@Configuration
@ComponentScan
@EnableWebMvc
public class ServiceConfig {

    @Bean
    public DBRepository dbRepository(SessionFactory sessionFactory , @Qualifier("cashIni") CacheEngine cash) throws IllegalAccessException {
        DBRepository dbServ = new DbRepositoryHiber<>();
        dbServ.init(sessionFactory, cash);
        DBServiceFactory.dataIni(dbServ);
        return dbServ;
    }
    @Bean
    public SessionFactory sessionFactory() {
        String cfgPath = "hibernate.cfg.xml";
        Class[] classes ={
                User.class,
                Account.class,
                AddressDataSet.class,
                Phone.class};
        HiberCfgBuilder hiberCfgBuilder = new HiberCfgBuilder(cfgPath);

        for(Class c:classes){
            hiberCfgBuilder.addClass(c);
        }

        return hiberCfgBuilder.buildFactory();
    };

    @Bean
    @Qualifier("cashIni")
    public CacheEngine cacheEngine(@Value("10") int maxElements,@Value("1000") long lifeTimeMs,@Value("0") long idleTimeMs, @Value("false")boolean isEternal) {
//        int maxElements = 10;
//        long lifeTimeMs =1000;
//        long idleTimeMs = 0;
//        boolean isEternal = false;
        return new CacheEngineSoftImpl<>(maxElements, lifeTimeMs, idleTimeMs, isEternal);
    }

}
