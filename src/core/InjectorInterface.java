/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.exceptions.ClassNotInjectable;
import core.exceptions.InterfaceNotImplemented;
import core.exceptions.UnresolvableDependency;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Manel
 */
public interface InjectorInterface {

    Injector addDependency(Class c, Class o) throws InterfaceNotImplemented, ClassNotInjectable;

    void initialise() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnresolvableDependency;

    <T> T resolveDependencies(Class c) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnresolvableDependency;
}
