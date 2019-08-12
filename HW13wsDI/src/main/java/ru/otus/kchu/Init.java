package ru.otus.kchu;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class Init implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {

        System.out.println("------------------BeforeInitialization : " + beanName);
        return bean;  // you can return any other object as well
    }
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {

        System.out.println("-----------------AfterInitialization : " + beanName);
        return bean;  // you can return any other object as well
    }
}