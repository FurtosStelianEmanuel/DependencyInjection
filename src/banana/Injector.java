/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banana;

import annotations.Injectable;
import banana.exceptions.ClassNotInjectable;
import banana.exceptions.InterfaceNotImplemented;
import banana.exceptions.UnresolvableDependency;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Manel
 */
public class Injector implements InjectorInterface {

    private final Map<Class, Class> skeletonPool;
    private final Map<Class, Object> objectPool;

    public Injector(Map<Class, Class> skeletonPool, Map<Class, Object> objectPool) {
        this.skeletonPool = skeletonPool;
        this.objectPool = objectPool;
    }

    @Override
    public Injector addDependency(Class interfaceClass, Class implementationClass) throws InterfaceNotImplemented, ClassNotInjectable {
        boolean classImplementsInterface = Arrays.asList(implementationClass.getInterfaces()).stream().filter(p -> p.getName().equals(interfaceClass.getName())).findAny().orElse(null) != null;
        boolean classIsInjectable = isInjectable(implementationClass);
        if (!classImplementsInterface) {
            throw new InterfaceNotImplemented(String.format("%s doesn't implement interface %s", implementationClass.getName(), interfaceClass.getName()));
        }
        if (!classIsInjectable){
            throw new ClassNotInjectable(String.format("%s Is not injectable", implementationClass.getName()));
        }
        skeletonPool.put(interfaceClass, implementationClass);
        return this;
    }

    @Override
    public void initialise() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnresolvableDependency {
        final List<Class> sorted = new ArrayList<>();

        skeletonPool.keySet().forEach((c) -> {
            sorted.add(c);
        });
        Collections.sort(sorted, (c1, c2) -> getConstructorParameterCount(skeletonPool.get(c1)) - getConstructorParameterCount(skeletonPool.get(c2)));

        for (Class skeletonInterface : sorted) {
            objectPool.put(skeletonInterface, resolve(skeletonInterface));
        }
    }

    @Override
    public <T> T resolveDependencies(Class classToResolve) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnresolvableDependency {
        boolean requiresNewInstance = requiresNewInstance(classToResolve);
        if (!requiresNewInstance) {
            for (Class c : objectPool.keySet()) {
                if (c.equals(classToResolve)) {
                    return (T) objectPool.get(c);
                }
            }
        }
        Constructor[] constructors = classToResolve.getConstructors();
        List<Object> resolvedDependencies = new ArrayList<>();
        for (Class c : constructors[0].getParameterTypes()) {
            if (objectPool.containsKey(c)) {
                resolvedDependencies.add(objectPool.get(c));
            } else {
                throw new UnresolvableDependency(c.getName(), classToResolve.getName());
            }
        }
        T instance = (T) constructors[0].newInstance(resolvedDependencies.toArray());
        if (!requiresNewInstance) {
            objectPool.put(classToResolve, instance);
        }
        return instance;
    }

    private Object[] resolveAll(Class[] classes) {
        List<Object> resolvedObjects = new ArrayList<>();
        for (Class c : classes) {
            Class o = objectPool.keySet().stream().filter(objectClass -> objectClass.equals(c)).findFirst().orElse(null);
            if (o == null) {
                continue;
            }
            resolvedObjects.add(objectPool.get(o));
        }
        return resolvedObjects.toArray();
    }

    private <T> T resolve(Class c) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnresolvableDependency {
        Class implementation = skeletonPool.get(skeletonPool.keySet().stream().filter(implementationKey -> implementationKey == c).findFirst().orElse(null));
        Constructor[] constructors = implementation.getConstructors();
        Constructor constructor = implementation.getConstructor(constructors[0].getParameterTypes());
        Object[] parameters = resolveAll(constructor.getParameterTypes());
        if (parameters.length != constructor.getParameterCount()) {
            throw new UnresolvableDependency(c.getName());
        }
        return (T) constructor.newInstance(parameters);
    }

    private boolean isInjectable(Class c){
        Injectable injectableAnnotation = (Injectable) c.getAnnotation(Injectable.class);
        return injectableAnnotation != null;
    }
    
    private boolean requiresNewInstance(Class c) {
        Injectable injectableAnnotation = (Injectable) c.getAnnotation(Injectable.class);
        return injectableAnnotation != null ? injectableAnnotation.ResolveWithNewInstance() : false;
    }

    private int getConstructorParameterCount(Class c) {
        return c.getConstructors()[0].getParameterCount();
    }
}
