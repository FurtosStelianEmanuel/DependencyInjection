/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import interfaces.Interfaces;

/**
 *
 * @author Manel
 */
public class Dependency3 implements Interfaces.Dependency3Interface {

    private final Interfaces.Dependency1Interface dep1;

    private final Interfaces.Dependency2Interface dep2;

    public Dependency3(Interfaces.Dependency1Interface dep1, Interfaces.Dependency2Interface dep2) {
        this.dep1 = dep1;
        this.dep2 = dep2;
    }

    @Override
    public void shout() {
        System.out.println("NIGGERed");
    }
}
