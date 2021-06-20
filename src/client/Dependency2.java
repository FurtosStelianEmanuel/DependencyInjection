/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import annotations.Injectable;
import interfaces.Interfaces;
import interfaces.Interfaces.Dependency2Interface;

/**
 *
 * @author Manel
 */
@Injectable
public class Dependency2 implements Dependency2Interface{

    private final Interfaces.Dependency1Interface dep1;

    public Dependency2(Interfaces.Dependency1Interface dep1) {
        this.dep1 = dep1;
    }
}
