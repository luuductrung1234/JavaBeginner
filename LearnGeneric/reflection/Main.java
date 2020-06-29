package reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.logging.CustomLogger;

public class Main {
    private static Logger logger = CustomLogger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        int choice = 0;
        try {
            choice = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Please choose a specific demo to run!");
        }

        switch (choice) {
            case 1:
                buildSampleInjectorWithGeneric();
                break;
            case 2:
                reifiableTypesDemo();
                break;
            case 3:
                nonReifiableTypesDemo();
                break;
            case 4:
                reflectingGenericInformation();
                break;
            default:
                buildSampleInjectorWithGeneric();
                break;
        }

    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    /**
     * Build a sample Injector (DI pattern) using Java Generic
     */
    private static void buildSampleInjectorWithGeneric() {
        Injector injector = new Injector().with("Hello World");
        MyLogger logger = injector.newInstance(MyLogger.class);
        logger.log();
    }

    /**
     * Reifiable Types:
     * 
     * - Primitives (e.g. int, long)
     * 
     * - Non Parameterized Class or Interface (e.g. String, ActionListener)
     * 
     * - All type arguments are unbounded Wildcards (e.g. List<?>, Map<?,?>)
     * 
     * - Raw Types (e.g. List, Map)
     * 
     * - Arrays of reifiable components (e.g. int[][], List<?>[])
     */
    private static void reifiableTypesDemo() {
        System.out.println(int.class);

        System.out.println(String.class);

        List<?> wildcards = new ArrayList();
        System.out.println(wildcards.getClass());

        List raw = new ArrayList();
        System.out.println(raw.getClass());
        System.out.println(raw.getClass() == wildcards.getClass());

        System.out.println(int[].class);
        System.out.println(List[].class);
        // System.out.println(List[].class == int[].class);
    }

    /**
     * Non Reifiable Types:
     * 
     * - Type Variables (e.g. T)
     * 
     * - Parameterized Type with Parameters (e.g. ArrayList<String>, Map<Integer,
     * String>)
     * 
     * - Parameterized Type with Bounds (e.g. List<? extends Number>, Consumer<?
     * super String>)
     */
    private static <T> void nonReifiableTypesDemo() {
        // System.out.println(T.class);

        List<String> strings = new ArrayList<>();
        List<Integer> intergers = new ArrayList<>();
        System.out.println(strings.getClass());
        System.out.println(intergers.getClass());
        System.out.println(intergers.getClass() == strings.getClass());

        List<? extends Number> numbers = new ArrayList<>();
        System.out.println(numbers.getClass());
        System.out.println(intergers.getClass() == numbers.getClass());

    }

    /**
     * Reify type information in certain circumstances and extract useful
     * information about that
     */
    private static void reflectingGenericInformation() {
        List<String> strings = new ArrayList<>();
        Class<?> arrayList = strings.getClass();

        final TypeVariable<? extends Class<?>>[] typeParamters = arrayList.getTypeParameters();
        System.out.println(Arrays.toString(typeParamters));

        System.out.println(arrayList.toString());
        System.out.println(arrayList.toGenericString());

        final ParameterizedType arrayListOfString = (ParameterizedType) StringList.class.getGenericSuperclass();
        System.out.println(Arrays.toString(arrayListOfString.getActualTypeArguments()));
    }
}