package ru.otus.kchu.HW3.annotations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.otus.l6.reflection.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestRunner {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        run(AnnotationsTest.class);
    }

    private static void run(Class<?> testClass) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        Class<?> clazz = testClass; // Person.class;
        Method[] declMethods = clazz.getDeclaredMethods();
        TestContext ctx = new TestContext(clazz.getDeclaredMethods());

    /**Вариант1: распределения методов в мапу  */
//        Map<String,List<Method>> methodsMap = getMethods(declMethods);

        try {
            for (Method method : ctx.getMethsBeforeAll()) {
                method.invoke(Class.forName(testClass.getName()));
            }

            System.out.println(".....................................");

            for (Method method :ctx.getMethsTest()) {

                Object testObject = Class.forName(testClass.getName()).newInstance();
                try {
                    runMethods(testObject, ctx.getMethsBeforeEach());
                    method.invoke(testObject, null);

                } catch (Exception e) {
                    System.out.println("Exception before Each");
                    e.printStackTrace();
                } finally {
                    try {
                        runMethods(testObject, ctx.getMethsAfterEach());
                    } catch (Exception e) {
                        System.out.println("Exception after Each:");
                        e.printStackTrace();
                    }
                }

                System.out.println(".....................................");
            }

        } catch (Exception e) {
            System.out.println("Exception in BeforeAll:");
            e.printStackTrace();
        } finally {

            for (Method method : ctx.getMethsAfterAll()) {
                method.invoke(Class.forName(testClass.getName()));
            }
        }

    }

    private static Map<String,List<Method>>  getMethods(Method[] declMethods) {

        ArrayList<Method> methodsBeforeEach = new ArrayList<>();
        ArrayList<Method> methodsAfterEach = new ArrayList<>();
        ArrayList<Method> methodsTest = new ArrayList<>();
        ArrayList<Method> methodsBeforeAll = new ArrayList<>();
        ArrayList<Method> methodsAfterAll = new ArrayList<>();
        Map<String,List<Method>> methodsMap = new HashMap<>();

        for (Method method:declMethods ){
            if(method.isAnnotationPresent(BeforeEach.class)) {methodsBeforeEach.add(method); continue;}
            if(method.isAnnotationPresent(AfterEach.class)) {methodsAfterEach.add(method); continue;}
            if(method.isAnnotationPresent(Test.class)) {methodsTest.add(method); continue;}
            if(method.isAnnotationPresent(BeforeAll.class)) {methodsBeforeAll.add(method); continue;}
            if(method.isAnnotationPresent(AfterAll.class)) methodsAfterAll.add(method);

        }
        methodsMap.put("BeforeEach", methodsBeforeEach);
        methodsMap.put("AfterEach", methodsAfterEach);
        methodsMap.put("Test", methodsTest);
        methodsMap.put("BeforeAll", methodsBeforeAll);
        methodsMap.put("AfterAll", methodsAfterAll);
        return  methodsMap;
    }

    private static void runMethods(Class<?> testClass, ArrayList<Method> methods ) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (Method method : methods) {
            ReflectionHelper.callMethod(Class.forName(testClass.getName()).newInstance(), method.getName());
        }
    }

    private static void runMethods(Object testClass, List<Method> methods ) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
                method.invoke(testClass, null);
            }
    }

    private static class TestContext {
        private ArrayList<Method> methodsBeforeEach = new ArrayList<>();
        private ArrayList<Method> methodsAfterEach = new ArrayList<>();
        private ArrayList<Method> methodsTest = new ArrayList<>();
        private ArrayList<Method> methodsBeforeAll = new ArrayList<>();
        private ArrayList<Method> methodsAfterAll = new ArrayList<>();

        public TestContext(Method[] declMethods )
        {
            for (Method method:declMethods ){
                if(method.isAnnotationPresent(BeforeEach.class)) {methodsBeforeEach.add(method); continue;}
                if(method.isAnnotationPresent(AfterEach.class)) {methodsAfterEach.add(method); continue;}
                if(method.isAnnotationPresent(Test.class)) {methodsTest.add(method); continue;}
                if(method.isAnnotationPresent(BeforeAll.class)) {methodsBeforeAll.add(method); continue;}
                if(method.isAnnotationPresent(AfterAll.class)) methodsAfterAll.add(method);

            }
        }
        List<Method> getMethsBeforeEach(){ return methodsBeforeEach;}
        List<Method> getMethsAfterEach(){ return methodsAfterEach;}
        List<Method> getMethsBeforeAll(){ return methodsBeforeAll;}
        List<Method> getMethsAfterAll(){ return methodsAfterAll;}
        List<Method> getMethsTest(){ return methodsTest;}
    }
}
