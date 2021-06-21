/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import banana.Injector;
import banana.InjectorInterface;
import banana.exceptions.ClassNotInjectable;
import banana.exceptions.InterfaceNotImplemented;
import banana.exceptions.UnresolvableDependency;
import interfaces.Interfaces;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Manel
 */
public class DriverExample {

    public DriverExample() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UnresolvableDependency, InterfaceNotImplemented, ClassNotInjectable {
        InjectorInterface injector = new Injector(new HashMap<>(), new HashMap<>(), new ArrayList<>());
        injector.addDependency(Interfaces.Dependency1Interface.class, Dependency1.class)
                .addDependency(Interfaces.Dependency2Interface.class, Dependency2.class)
                .addDependency(Interfaces.Dependency3Interface.class, Dependency3.class);
        injector.initialise();

        Formular f = injector.resolveDependencies(Formular.class);
        f.setVisible(true);
        f.setSize(300, 300);

        Formular f1 = injector.resolveDependencies(Formular.class);

        assert f == f1 : "Fail";
    }
}
