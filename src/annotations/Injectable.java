/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Manel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {
    boolean ResolveWithNewInstance() default false;
}
