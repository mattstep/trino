/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.tracing;

import com.google.inject.Provider;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.trino.spi.tracing.OpenTelemetryFactory;

import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;

public class OpenTelemetryManager
{
    public static final OpenTelemetry NO_OP = OpenTelemetry.noop();
    private final AtomicReference<OpenTelemetry> openTelemetryAtomicReference = new AtomicReference<>(NO_OP);

    public void addOpenTelemetryFactory(OpenTelemetryFactory openTelemetryFactory) {
        if (!openTelemetryAtomicReference.compareAndSet(NO_OP, openTelemetryFactory.create())) {
            throw new IllegalArgumentException(format("An OpenTelemetryFactory has already been registered. Unable to register additional factory %s",
                    openTelemetryFactory.getName()));
        }
    }

    public Provider<Tracer> getTracerProvider() {
        return getTracerProvider("trino");
    }

    public Provider<Tracer> getTracerProvider(String scopeName) {
        return new Provider<Tracer>() {
            @Override
            public Tracer get() {
                return openTelemetryAtomicReference.get().getTracer(scopeName);
            }
        };
    }
}
