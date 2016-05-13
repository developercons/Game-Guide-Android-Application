package com.example.martha.gameguide.anotation;

/**
 * Created by Martha on 4/17/2016.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Request {

    String SerializedName() default "";

    boolean sign_up() default false;
    boolean login() default false;
    boolean update() default false;

    boolean change_password() default false;
    boolean recover_password() default false;
    boolean profile_get() default false;


    boolean game_request() default false;

}
