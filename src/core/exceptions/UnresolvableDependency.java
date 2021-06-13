/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.exceptions;

/**
 *
 * @author Manel
 */
public class UnresolvableDependency extends Exception {

    public UnresolvableDependency() {
        super();
    }

    public UnresolvableDependency(String nameOfClass) {
        super(String.format("%s couldn't be resolved", nameOfClass));
    }

    public UnresolvableDependency(String c1, String c2) {
        super(String.format("%s couldn't be resolved for %s", c1, c2));
    }
}
