package com.learning;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        accessTypeInstance();
        accessTypeAndMemberInfo();
        interactWithObjectInstance();
        instanceCreation();
    }

    // region Demos

    private static void accessTypeInstance() {
        try {
            BankAccount account = new BankAccount("1020");
            Class<?> theClass = account.getClass();
            logger.log(Level.INFO, "the account variable is type: {0}", theClass.getName());
            theClass = Class.forName("com.learning.BankAccount");
            logger.log(Level.INFO, "the account variable is type: {0}", theClass.getName());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    private static void accessTypeAndMemberInfo() {
        HighVolumeAccount highVolumeAccount = new HighVolumeAccount("1030");
        classInfo(highVolumeAccount);
        typeModifiers(highVolumeAccount);
        fieldInfo(highVolumeAccount);
        methodInfo(highVolumeAccount);
    }

    private static void interactWithObjectInstance() {
        try {
            // use constructor to create new instance
            Constructor<?> constructor = BankAccount.class.getDeclaredConstructor(String.class, int.class);
            Object account = constructor.newInstance("1040", 500);
            Class<?> theClass = account.getClass();

            // call deposit method
            Method depositMethod = theClass.getMethod("deposit", int.class);
            depositMethod.invoke(account, 10);

            // call get balance
            Method getBalanceMethod = theClass.getMethod("getBalance");
            var balance = (int) getBalanceMethod.invoke(account);
            logger.log(Level.INFO, "Current balance is: {0}", balance);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    private static void instanceCreation() {
        try {
            // create new AccountWorker instance
            Class<?> workerType = Class.forName("com.learning.AccountWorker");
            Constructor<?> workerConstructor = workerType.getDeclaredConstructor();
            TaskWorker worker = (TaskWorker) workerConstructor.newInstance();

            // create new BankAccount instance
            Constructor<?> accountConstructor = BankAccount.class.getDeclaredConstructor(String.class, int.class);
            Object account = accountConstructor.newInstance("1040", 500);

            // execute doWork method
            worker.setTarget(account);
            worker.doWork();

            // create new HighVolumeAccount instance
            accountConstructor = HighVolumeAccount.class.getDeclaredConstructor(String.class, int.class);
            account = accountConstructor.newInstance("1050", 1000);

            // execute doWork method
            worker.setTarget(account);
            worker.doWork();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    // endregion

    // region Helpers

    private static void classInfo(Object obj) {
        Class<?> theClass = obj.getClass();
        logger.log(Level.INFO, "Class Name: {0}", theClass.getSimpleName());

        Class<?> superClass = theClass.getSuperclass();
        logger.log(Level.INFO, "Super Class Name: {0}", superClass.getSimpleName());

        Class<?>[] interfaces = theClass.getInterfaces();
        for (Class<?> itf : interfaces) {
            logger.log(Level.INFO, "Interface Name: {0}", itf.getSimpleName());
        }
    }

    private static void typeModifiers(Object obj) {
        Class<?> theClass = obj.getClass();
        int modifiers = theClass.getModifiers();

        if ((modifiers & Modifier.FINAL) > 0) {
            logger.log(Level.INFO, "Bitwise check - final");
        }

        if (Modifier.isFinal(modifiers)) {
            logger.log(Level.INFO, "Method check - final");
        }
        if (Modifier.isProtected(modifiers)) {
            logger.log(Level.INFO, "Method check - protected");
        }
        if (Modifier.isPrivate(modifiers)) {
            logger.log(Level.INFO, "Method check - private");
        }
        if (Modifier.isPublic(modifiers)) {
            logger.log(Level.INFO, "Method check - public");
        }
    }

    private static void fieldInfo(Object obj) {
        Class<?> theClass = obj.getClass();
        Field[] fields = theClass.getFields();
        displayFields(fields);
        Field[] declaredFields = theClass.getDeclaredFields();
        displayFields(declaredFields);
    }

    private static void methodInfo(Object obj) {
        Class<?> theClass = obj.getClass();
        Method[] methods = theClass.getMethods();
        displayMethods(methods);
        displayMethodsExcludeObject(methods);
        Method[] declaredMethods = theClass.getDeclaredMethods();
        displayMethods(declaredMethods);
    }

    /**
     * display method information
     * 
     * @param methods
     */
    private static void displayMethods(Method[] methods) {
        for (Method method : methods) {
            logger.log(Level.INFO, "{0} : {1} parameters",
                    new Object[] { method.getName(), method.getParameterCount() });
        }
    }

    /**
     * display method information if it not belong to Object class
     * 
     * @param methods
     */
    private static void displayMethodsExcludeObject(Method[] methods) {
        for (Method method : methods) {
            if (method.getDeclaringClass() != Object.class)
                logger.log(Level.INFO, "{0} : {1} parameters",
                        new Object[] { method.getName(), method.getParameterCount() });
        }
    }

    /**
     * display field information
     * 
     * @param fields
     */
    private static void displayFields(Field[] fields) {
        for (Field field : fields) {
            logger.log(Level.INFO, "{0} : {1}", new Object[] { field.getName(), field.getType() });
        }
    }

    // endregion
}