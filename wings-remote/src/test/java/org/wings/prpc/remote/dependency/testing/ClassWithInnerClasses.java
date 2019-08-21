package org.wings.prpc.remote.dependency.testing;

public class ClassWithInnerClasses {

    class NonStaticInnerClass {

        class NonStaticInnerClass2 {
        }
    }

    static class StaticInnerClass {
    }

    private class PrivateNonStaticClass {
    }
}
