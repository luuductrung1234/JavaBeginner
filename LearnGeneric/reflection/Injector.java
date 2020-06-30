package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import util.exception.ExceptionHelper;

public class Injector {
    private Map<Class<?>, Object> objectGraph = new HashMap<>();

    public Injector with(Object value) {
        objectGraph.put(value.getClass(), value);
        return this;
    }

    public <T> T newInstance(final Class<T> type) {
        return (T) objectGraph.computeIfAbsent(type, this::instantiate);
    }

    private Object instantiate(Class<?> type) {
        try {
            Constructor<?>[] constructors = type.getConstructors();
            if (constructors.length != 1) {
                throw new IllegalArgumentException(type + " must only has 1 constructor");
            }

            Constructor<?> constructor = constructors[0];
            Object[] args = Stream.of(constructor.getParameterTypes()).map(paramType -> newInstance(paramType))
                    .toArray();
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ExceptionHelper.throwException(e);
        }
        return null;
    }
}