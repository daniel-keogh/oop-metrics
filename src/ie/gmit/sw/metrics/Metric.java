package ie.gmit.sw.metrics;

/**
 * Class that serves as the base class of all metric types.
 * The class contains an abstract method called <code>measure()</code> that will be used to calculate a
 * metric and return a numerical result.
 */
public abstract class Metric {
    private final Class<?> cls;

    /**
     * Constructs a new {@link Metric} instance.
     *
     * @param cls The class to which this metric applies.
     */
    public Metric(Class<?> cls) {
        this.cls = cls;
    }

    /**
     * Get the {@link Class} associated with this Metric object.
     *
     * @return The class associated with this Metric object
     */
    public Class<?> getCls() {
        return cls;
    }

    /**
     * Get the name of the class associated with this Metric object
     *
     * @return The name of the class as a {@link String}
     */
    public String getClassName() {
        return cls.getName();
    }

    /**
     * Compute a value for this metric.
     *
     * @return The result of the metric calculation.
     */
    public abstract Number measure();

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", getClass().getName(), getClassName(), measure());
    }
}
