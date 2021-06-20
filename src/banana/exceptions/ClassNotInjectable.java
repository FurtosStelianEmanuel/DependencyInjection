/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banana.exceptions;

/**
 *
 * @author Manel
 */
public class ClassNotInjectable extends Exception{
    
    public ClassNotInjectable(){
        super();
    }
    
    public ClassNotInjectable(String message){
        super(message);
    }
}
