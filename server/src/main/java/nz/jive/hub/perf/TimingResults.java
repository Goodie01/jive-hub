package nz.jive.hub.perf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;


public class TimingResults extends ThreadLocal<Map<String, TimingResults.TimedInvocation>> {
    private static final TimingResults INSTANCE = new TimingResults();
    private static final Logger LOGGER = LoggerFactory.getLogger("Timing results");
    private static final String TIME_FORMAT_PATTERN = "%11.2f ms";
    private static final String TOTAL_CALL_LOG_MESSAGE = "Sum total: {0}ms; over: {1} calls";

    @Override
    protected Map<String, TimedInvocation> initialValue() {
        return new HashMap<>();
    }

    public void addInvocation(final String method, final long dt) {
        Map<String, TimedInvocation> invocations = get();
        invocations.compute(method, (s, timedInvocation) -> {
            if (timedInvocation == null) {
                return new TimedInvocation(method, dt);
            } else {
                timedInvocation.anotherCall(dt);
                return timedInvocation;
            }
        });
    }

    public void dumpCurrent() {
        List<TimedInvocation> sortedInvocations = new ArrayList<>(get().values());
        Collections.sort(sortedInvocations);

        long sum = sortedInvocations.stream().mapToLong(timedInvocation -> timedInvocation.dt).sum();
        int count = sortedInvocations.stream().mapToInt(value -> value.calls).sum();

        sortedInvocations.forEach(timedInvocation -> LOGGER.info(timedInvocation.toString()));
        LOGGER.info(MessageFormat.format(TOTAL_CALL_LOG_MESSAGE, sum / 1e6, count));

        get().clear();
    }

    public static TimingResults getInstance() {
        return INSTANCE;
    }

    public static class TimedInvocation implements Comparable<TimedInvocation> {
        private final String method;
        private long dt;
        private int calls = 1;

        public TimedInvocation(final String method, final long dt) {
            this.method = method;
            this.dt = dt;
        }

        @Override
        public String toString() {
            String duration = String.format(TIME_FORMAT_PATTERN, dt / 1e6);
            String avgDuration = String.format(TIME_FORMAT_PATTERN, dt / (1e6 * calls));
            String numCallsStr = String.format("%8d", calls);
            return duration + avgDuration + numCallsStr + "   " + method;
        }

        public void anotherCall(final long dt) {
            this.dt += dt;
            ++calls;
        }

        @Override
        public int compareTo(final TimedInvocation o) {
            return Long.compare(dt, o.dt);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TimedInvocation that = (TimedInvocation) o;
            return dt == that.dt
                    && calls == that.calls
                    && Objects.equals(method, that.method);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dt, calls, method);
        }
    }
}