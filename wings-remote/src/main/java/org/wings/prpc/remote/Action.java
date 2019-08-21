package org.wings.prpc.remote;

import java.io.Serializable;
import java.util.function.Supplier;

public interface Action<R extends Serializable> extends Supplier<R>, Serializable {
}
