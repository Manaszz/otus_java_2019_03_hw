package ru.otus.kchu.HW3.annotations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.otus.l6.reflection.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class TestRunner {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        run(AnnotationsTest.class);
    }

    private static void run(Class<?> testClass) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        Class<?> clazz = testClass; // Person.class;
//        clazz.getAnnotations();
        Method[] declMethods = clazz.getDeclaredMethods();
        ArrayList<Method> methodsBeforeEach = new ArrayList<>();
        ArrayList<Method> methodsAfterEach = new ArrayList<>();
        ArrayList<Method> methodsTest = new ArrayList<>();
        ArrayList<Method> methodsBeforeAll = new ArrayList<>();
        ArrayList<Method> methodsAfterAll = new ArrayList<>();

        for (Method method:declMethods ){
            if(method.isAnnotationPresent(BeforeEach.class)) {methodsBeforeEach.add(method); continue;}
            if(method.isAnnotationPresent(AfterEach.class)) {methodsAfterEach.add(method); continue;}
            if(method.isAnnotationPresent(Test.class)) {methodsTest.add(method); continue;}
            if(method.isAnnotationPresent(BeforeAll.class)) {methodsBeforeAll.add(method); continue;}
            if(method.isAnnotationPresent(AfterAll.class)) methodsAfterAll.add(method);

        }

        for (Method method:methodsBeforeAll ){
            method.invoke(Class.forName(testClass.getName()));
            }

        System.out.println(".....................................");

        for (Method method : methodsTest) {
            runMethods(testClass, methodsBeforeEach);
//            ReflectionHelper.callMethod(Class.forName(testClass.getName()).newInstance(), method.getName());
            method.invoke(Class.forName(testClass.getName()).newInstance(), null);
            runMethods(testClass, methodsAfterEach);
            System.out.println(".....................................");
        }

        for (Method method:methodsAfterAll ){
            method.invoke(Class.forName(testClass.getName()));
        }

    }


    private static void runMethods(Class<?> testClass, ArrayList<Method> methods ) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        for (Method method : methods) {
            ReflectionHelper.callMethod(Class.forName(testClass.getName()).newInstance(), method.getName());
        }
    }
}
