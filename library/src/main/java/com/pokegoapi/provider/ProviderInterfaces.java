package com.pokegoapi.provider;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by chris on 2/27/2017.
 */
public class ProviderInterfaces {

    /**
     * Throws an IllegalStateException if the caller Class has not been Initialized
     * @param caller the SpiDelegate calling this check
     */
    public static void requireInitialized(ProviderInterface caller){
        if(!caller.hasBeenInitialized()){
            String className = caller.getClass().getSimpleName();
            String methodName = getInvokingMethodName(caller.getClass());
            throw new IllegalStateException(className + " has not be initialized. Please initialize " 
                    + className + " before calling: " + className + methodName);
        }
    }

    public static <T> void requireInitializedAndNonNull(ProviderInterface caller, T object, Class objectClass){
        if(!caller.hasBeenInitialized()){
            String className = caller.getClass().getSimpleName();
            String methodName = getInvokingMethodName(caller.getClass());
            throw new IllegalStateException(className + " has not be initialized. Please initialize "
                    + className + " before calling: " + className + methodName);
        }

        Objects.requireNonNull(object, objectClass.getSimpleName() + " is Null");
    }

    /**
     * Gets the name of the invoking method's name
     * @param caller the class that the invoking method is part of.
     * @return the name of the invoking method and its parameter types, ie. ".getInvokingMethodName(Class)"
     */
    private static String getInvokingMethodName(Class caller){
        String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
        Method[] methods = caller.getMethods();

        Method method = null;
        for(Method m : methods){
            if(m.getName().equalsIgnoreCase(methodName)){
                method = m;
                break;
            }
        }

        if(method == null){
            return null;
        }

        methodName = method.toGenericString();
        return methodName.substring(methodName.lastIndexOf('.'));
    }
}
