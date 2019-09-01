package org.wings.prpc.junit.extension;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Log4j2 Appender which could collect LogEvents from all loggers
 * sending log events in thread created this appender.
 *
 * @author Alexey Lapin
 */
public class TestEventsAppender extends AbstractAppender {

    private static final Logger logger = LogManager.getLogger(TestEventsAppender.class);

    private String id;
    private StringBuilder lines;
    private CompletableFuture future;

    public TestEventsAppender() {
        this(UUID.randomUUID().toString(), new StringBuilder(), new CompletableFuture());
    }

    TestEventsAppender(String id, StringBuilder lines, CompletableFuture future) {
        this(id, createFilter(id, future), createLayout(), false);
        this.id = id;
        this.lines = lines;
        this.future = future;
    }

    TestEventsAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                       boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        // needed for completion event
        Configurator.setLevel(logger.getName(), Level.INFO);
    }

    public String getId() {
        return id;
    }

    public StringBuilder getLines() {
        return lines;
    }

    private static Filter createFilter(String id, CompletableFuture future) {
        return CompositeFilter.createFilters(new Filter[]{
                new CompletionFilter(future, id),
                new ThreadFilter(Thread.currentThread().getName())
        });
    }

    private static Layout<String> createLayout() {
        String colored = "\u001b[" + 0 + ';' + 35 + 'm' + "%d %-5p - %m%n" + "\u001b[" + 'm';
        return PatternLayout.newBuilder().withPattern(colored).build();
    }

    @Override
    public void append(LogEvent event) {
        lines.append(getLayout().toSerializable(event));
    }

    @Override
    public void start() {
        super.start();
        getRootLogger().addAppender(this);
    }

    private static org.apache.logging.log4j.core.Logger getRootLogger() {
        return (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
    }

    @Override
    public void stop() {
        // send stop event
        logger.info(id);
        waitStopEvent();
        super.stop();
        getRootLogger().removeAppender(this);
    }

    private void waitStopEvent() {
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (Throwable t) {
            //
        }
    }

    /**
     * {@code Filter} implementation to accept events only for specific thread
     */
    public static class ThreadFilter extends AbstractFilter {
        private String thread;

        ThreadFilter(String thread) {
            this.thread = Objects.requireNonNull(thread);
        }

        @Override
        public Result filter(LogEvent event) {
            if (thread.equals(event.getThreadName())) {
                return Result.ACCEPT;
            } else {
                return Result.DENY;
            }
        }
    }

    /**
     * {@code Filter} implementation to finish appender collection
     */
    public static class CompletionFilter extends AbstractFilter {
        private CompletableFuture<?> future;
        private String id;

        CompletionFilter(CompletableFuture<?> future, String id) {
            this.future = future;
            this.id = id;
        }

        @Override
        public Result filter(LogEvent event) {
            if (id.equals(event.getMessage().toString())) {
                future.complete(null);
                return Result.DENY;
            }
            return Result.NEUTRAL;
        }
    }
}
