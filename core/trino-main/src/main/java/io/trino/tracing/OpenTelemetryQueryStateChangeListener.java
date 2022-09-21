package io.trino.tracing;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.trino.execution.QueryState;
import io.trino.execution.StateMachine;

import java.util.concurrent.atomic.AtomicReference;

public class OpenTelemetryQueryStateChangeListener implements StateMachine.StateChangeListener<QueryState> {
    private final Tracer tracer;
    private final Span parentSpan;
    private final String baseSpanName;
    private final AtomicReference<Span> previousStateSpan = new AtomicReference<>();

    public OpenTelemetryQueryStateChangeListener(Tracer tracer, Span parentSpan, String baseSpanName) {
        this.tracer = tracer;
        this.parentSpan = parentSpan;
        this.baseSpanName = baseSpanName;
    }

    @Override
    public void stateChanged(QueryState newState) {
        Span previous = previousStateSpan.getAndSet(tracer.spanBuilder(baseSpanName + "-" + newState.toString().toLowerCase()).setParent(Context.current().with(parentSpan)).startSpan());
        if (previous != null) {
            previous.end();
        }
    }
}
