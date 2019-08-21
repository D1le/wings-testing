package org.wings.prpc.remote;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Created by sp00x on 8/29/2019.
 * Project: wings-testing
 */


public class TestClassDependency {

    @Test
    void method() {
        String o;
        Mockito m = new Mockito();
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return null;
            }
        };
        Supplier<String> supplier2 = (Supplier<String> & Serializable) () -> "zz";
//        Supplier<String> supplier2 = () -> "zz";
    }


    class InnerClass1 {


        class InnerInnerClass {

        }

    }

    class InnerClass2 {

    }


}
