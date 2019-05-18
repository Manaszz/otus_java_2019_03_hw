package ru.otus.kchu.HW3.annotations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.otus.l6.reflection.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TestRunner {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        run(AnnotationsTest.class);
    }

    private static void run(Class<?> testClass) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        Class<?> clazz = testClass; // Person.class;
//        clazz.getAnnotations();
        Method[] declMethods = clazz.getDeclaredMethods();
/**
 * Вариант1: распределения методов в мапу  */
        Map<String,ArrayList> methodsMap = getMethods(declMethods);
/**
* Вариант2: распределения методов возвращает лист с заданной аннотацией. Красиво, но для каждого бегать цикл по методам
        ArrayList<Method> methodsBeforeEach = getMethods(declMethods,BeforeEach.class);
*/
/**
 * Вариант3: распределения методов в листы
        getMethods(declMethods, methodsBeforeEach, methodsAfterEach, methodsTest, methodsBeforeAll, methodsAfterAll);
 */
/**
* Вариант4: Вынести листы в поля класса и распределять в них без передачи параметрами метода. */

        try {
            for (Method method : (ArrayList<Method>) methodsMap.get("BeforeAll")) {
                method.invoke(Class.forName(testClass.getName()));
            }

            System.out.println(".....................................");

            for (Method method : (ArrayList<Method>) methodsMap.get("Test")) {

                Object testObject = Class.forName(testClass.getName()).newInstance();
                try {
                    runMethods(testObject, methodsMap.get("BeforeEach"));
                    ReflectionHelper.callMethod(testObject, method.getName());

                } catch (Exception e) {
                    System.out.println("Exception before Each");
                    e.printStackTrace();
                } finally {
                    try {
                        runMethods(testObject, methodsMap.get("AfterEach"));
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

            for (Method method : (ArrayList<Method>) methodsMap.get("AfterAll")) {
                method.invoke(Class.forName(testClass.getName()));
            }
        }

    }


    private static Map<String,ArrayList>  getMethods(Method[] declMethods) {

        ArrayList<Method> methodsBeforeEach = new ArrayList<>();
        ArrayList<Method> methodsAfterEach = new ArrayList<>();
        ArrayList<Method> methodsTest = new ArrayList<>();
        ArrayList<Method> methodsBeforeAll = new ArrayList<>();
        ArrayList<Method> methodsAfterAll = new ArrayList<>();
        Map<String,ArrayList> methodsMap = new HashMap<>();

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

    private static void getMethods(Method[] declMethods, ArrayList<Method> methodsBeforeEach, ArrayList<Method> methodsAfterEach, ArrayList<Method> methodsTest, ArrayList<Method> methodsBeforeAll, ArrayList<Method> methodsAfterAll) {
        for (Method method:declMethods ){
            if(method.isAnnotationPresent(BeforeEach.class)) {methodsBeforeEach.add(method); continue;}
            if(method.isAnnotationPresent(AfterEach.class)) {methodsAfterEach.add(method); continue;}
            if(method.isAnnotationPresent(Test.class)) {methodsTest.add(method); continue;}
            if(method.isAnnotationPresent(BeforeAll.class)) {methodsBeforeAll.add(method); continue;}
            if(method.isAnnotationPresent(AfterAll.class)) methodsAfterAll.add(method);

        }
    }

    private static ArrayList<Method>  getMethods(Method[] declMethods,Class Annotation ) {

        ArrayList<Method> methodsList = new ArrayList<>();

        for (Method method:declMethods ){
            if(method.isAnnotationPresent(Annotation)) {methodsList.add(method); continue;}
        }

        return  methodsList;

    }

    private static void runMethods(Class<?> testClass, ArrayList<Method> methods ) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (Method method : methods) {
            ReflectionHelper.callMethod(Class.forName(testClass.getName()).newInstance(), method.getName());
        }
    }

    private static void runMethods(Object testClass, ArrayList<Method> methods ) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
                method.invoke(testClass, null);
            }
    }
}
