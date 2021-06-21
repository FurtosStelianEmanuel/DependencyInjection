/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import banana.InjectorInterface;
import banana.Injector;
import annotations.Injectable;
import banana.exceptions.ClassNotInjectable;
import banana.exceptions.InterfaceNotImplemented;
import banana.exceptions.UnresolvableDependency;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author Manel
 */
public class InjectorTest {

    private final InjectorInterface injector;
    private final Map<Class, Class> skeletonPoolMock;
    private final Map<Class, Object> objectPoolMock;
    private final List<Class> sortedSkeletonMock;
    
    final String ExceptionShouldHaveBeenThrownMessage = "Exception should have been thrown";

    public InjectorTest() {
        skeletonPoolMock = Mockito.mock(Map.class, "SkeletonPoolMock");
        objectPoolMock = Mockito.mock(Map.class, "ObjectPoolMock");
        sortedSkeletonMock = Mockito.mock(List.class, "SortedSkeletonMock");
        injector = new Injector(skeletonPoolMock, objectPoolMock, sortedSkeletonMock);
    }

    //<editor-fold defaultstate="collapsed" desc="Injector test classes">
    public interface Dependency1Interface {

        void show();
    }

    public interface Dependency2Interface {

        void bow();
    }

    public interface Dependency3Interface {

    }

    public interface UninjectableInterface{
        
    }
    
    public interface Dependency4Interface{
        
    }
    
    @Injectable
    public static class Dependency1 implements Dependency1Interface {

        public Dependency1() {
        }

        @Override
        public void show() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Injectable
    public static class Dependency2 implements Dependency2Interface {

        public Dependency2() {

        }

        @Override
        public void bow() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Injectable
    public static class Dependency3 implements Dependency3Interface {

        private final Dependency1 dep1;
        private final Dependency2 dep2;

        public Dependency3(Dependency1 dep1, Dependency2 dep2) {
            this.dep1 = dep1;
            this.dep2 = dep2;
        }
    }
    
    public static class Uninjectable implements UninjectableInterface{
        
    }
    
    @Injectable(ResolveWithNewInstance = true)
    public static class Dependency4 implements Dependency4Interface{
        
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Injector addDependency">
    /**
     * Test of addDependency method, of class Injector.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInjectorAddDependency_Success() throws Exception {
        Injector result = injector.addDependency(Dependency1Interface.class, Dependency1.class);
        
        Mockito.verify(skeletonPoolMock, Mockito.times(1)).put(Dependency1Interface.class, Dependency1.class);
        assertEquals(injector, result);
    }
    
    /**
     *
     * @throws banana.exceptions.ClassNotInjectable
     */
    @Test
    public void testInjectorAddDependency_WhenClassDoesntImplementInterface_ThenExceptionThrown() throws ClassNotInjectable {
        Class c = Dependency1Interface.class;
        Class o = Dependency2.class;
        
        try {
            injector.addDependency(c, o);
            fail(ExceptionShouldHaveBeenThrownMessage);
        } catch (InterfaceNotImplemented ex) {
        }
    }
    
    @Test
    public void testInjectorAddDependency_WhenClassIsntInjectable_ThenExceptionThrown() throws InterfaceNotImplemented{
        Class c = UninjectableInterface.class;
        Class o = Uninjectable.class;
        
        try {
            injector.addDependency(c, o);
            fail(ExceptionShouldHaveBeenThrownMessage);
        } catch (ClassNotInjectable ex) {
        }
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Injector initialise">
    /**
     * Test of initialise method, of class Injector.
     * @throws java.lang.Exception
     */
    @Test
    public void testInjectorInitialise_Success() throws Exception {
        Class i1 = Dependency1Interface.class;
        Class c1 = Dependency1.class;
        
        Class i2 = Dependency2Interface.class;
        Class c2 = Dependency2.class;
        
        Map<Class, Class> skeleton = new HashMap<Class, Class>() {
            {
                put(i1, c1);
                put(i2, c2);
            }
        };
        
        List<Class> sortedSkeleton = Arrays.asList(i1,i2);
        
        Injector instance = new Injector(skeleton, objectPoolMock, sortedSkeleton);
        
        instance.initialise();
        
        Mockito.verify(objectPoolMock, Mockito.times(1)).put(Mockito.eq(Dependency1Interface.class), Mockito.any(Dependency1.class));
        Mockito.verify(objectPoolMock, Mockito.times(1)).put(Mockito.eq(Dependency2Interface.class), Mockito.any(Dependency2.class));
    }
    
    @Test
    public void testInjectorInitialise_WhenDependencyCouldntBeResolved_ThenExceptionThrown() {
        Class i3 = Dependency3Interface.class;
        Class c3 = Dependency3.class;
        
        Map<Class, Class> skeleton = new HashMap<Class, Class>() {
            {
                put(i3, c3);
            }
        };
        
        List<Class> sortedSkeleton = Arrays.asList(i3);
        
        Injector instance = new Injector(skeleton, objectPoolMock, sortedSkeleton);
        
        try {
            instance.initialise();
            fail(ExceptionShouldHaveBeenThrownMessage);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | UnresolvableDependency ex) {
            assertEquals(UnresolvableDependency.class, ex.getClass());
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Injector resolveDependencies">
    /**
     * Test of resolveDependencies method, of class Injector.
     * @throws java.lang.Exception
     */
    @Test
    public void testInjectorResolveDependencies_Success() throws Exception {
        Object classInstance = injector.resolveDependencies(Dependency1.class);
        assertNotEquals(null, classInstance);
        assertEquals(Dependency1.class, Dependency1.class);
    }
    
    @Test
    public void testInjectorResolveDependencies_WhenUnresolvableDependency_ThenExceptionThrown() {
        try {
            injector.resolveDependencies(Dependency3.class);
            fail(ExceptionShouldHaveBeenThrownMessage);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | UnresolvableDependency ex) {
            assertEquals(UnresolvableDependency.class, ex.getClass());
        }
    }
//</editor-fold>
}
