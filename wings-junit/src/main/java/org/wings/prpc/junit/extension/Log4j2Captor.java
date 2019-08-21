package org.wings.prpc.junit.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class Log4j2Captor implements BeforeEachCallback, AfterEachCallback {

    private static final ExtensionContext.Namespace LOG4J2CAPTOR = ExtensionContext.Namespace.create("log4j2-captor");

    private static final String METHOD_CAPTOR = "method-captor";
    private static final String LOGS_ENTRY = "logs";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        TestEventsAppender appender = new TestEventsAppender();
        appender.start();

        context.getStore(LOG4J2CAPTOR).put(METHOD_CAPTOR, appender);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        TestEventsAppender appender = context.getStore(LOG4J2CAPTOR)
                .remove(METHOD_CAPTOR, TestEventsAppender.class);
        appender.stop();

        String logs = appender.getLines().toString();
        if (!logs.isEmpty()) {
            context.publishReportEntry(LOGS_ENTRY, logs);
        }
    }
}