package ie.gmit.sw.metrics;

import java.util.Collection;

/**
 * Object that computes the Number of Children in Tree (NOC) {@link Metric} for a provided class.
 *
 * The NOC refers to the number of direct subclasses of a given class and provides
 * a measure of the breadth of a class hierarchy.
 *
 * A high NOC value denotes high reuse of a base class.
 */
public class NOC extends Metric {
    private final Collection<Class<?>> classes;

    /**
     * Constructs a new NOC instance.
     * @param cls The class on which this metric will be calculated.
     * @param classes A {@link Collection} of classes that will be checked to see if they subclass <code>cls</code>.
     */
    public NOC(Class<?> cls, Collection<Class<?>> classes) {
        super(cls);
        this.classes = classes;
    }

    /**
     * Computes the Number of Children in Tree (NOC) {@link Metric} for <code>cls</code>.
     *
     * The NOC refers to the number of direct subclasses of a given class and provides
     * a measure of the breadth of a class hierarchy.
     *
     * The method will iterate over each class in <code>classes</code> and if one of the classes
     * is found to be a subclass of <code>cls</code>, the depth of <code>cls</code>'s inheritance
     * tree will be incremented by one.
     *
     * @return The NOC of <code>cls</code>.
     */
    @Override
    public Integer measure() {
        int depth = 0;

		for (Class<?> c : classes) {
			Class<?> spr = c.getSuperclass();

			if (spr != null) {
				if (spr.getName().equals(getCls().getName())) {
					depth++;
				}
			}
		}

		return depth;
    }
}
