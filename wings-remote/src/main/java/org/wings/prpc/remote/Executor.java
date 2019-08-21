package org.wings.prpc.remote;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * An {@code Executor} is remote code runner for Pega PRPC platform.
 * It is responsible for sending an action and receiving a result.
 *
 * @author Alexey Lapin
 */
public interface Executor {

    <R extends Serializable> Future<Output<R>> execute(ActionContainer<R> container);
}
