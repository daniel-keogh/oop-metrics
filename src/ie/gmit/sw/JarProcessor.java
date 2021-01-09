package ie.gmit.sw;

import ie.gmit.sw.db.MetricStore;
import ie.gmit.sw.metrics.Metric;
import ie.gmit.sw.metrics.NOC;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

/**
 * This class is used to process a JAR file.
 */
public class JarProcessor {

	/**
	 * Processes a JAR file and returns a {@link Collection} of {@link Metric} objects.
	 * Each Metric in the Collection corresponds to one of the classes in the JAR file.
	 *
	 * If the JAR has already been processed before, the saved data is returned from the object
	 * store. Otherwise, a new set of Metric objects is created and returned.
	 *
	 * @param path Path of the JAR file that is to be analysed.
	 * @return A Collection of the Metrics for each class in the JAR.
	 * @throws IOException If there's an error while reading in the JAR.
	 */
	public Collection<Metric> process(String path) throws IOException {
		List<Class<?>> classes = getClasses(path);

		List<String> strings = classes
				.stream()
				.map(Class::getName)
				.collect(Collectors.toList());

		Collection<Metric> cached = MetricStore.getInstance()
				.filter(m -> strings.contains(m.getClassName()));

		if (!cached.isEmpty()) {
			return cached;
		} else {
			Collection<Metric> metrics = classes.stream()
					.map(cls -> new NOC(cls, classes))
					.collect(Collectors.toList());

			MetricStore.getInstance().addAll(metrics);
			return metrics;
		}
	}

	/**
	 * Reads a JAR file and returns a {@link List} of all the classes it contains.
	 *
	 * @param path Path of the JAR file that is to be analysed.
	 * @return A List of all the classes read from the JAR file.
	 * @throws IOException If there's an error while reading in the JAR.
	 */
	private List<Class<?>> getClasses(String path) throws IOException {
		List<Class<?>> classes = new ArrayList<>();

		try (JarInputStream in = new JarInputStream(new FileInputStream(path))) {
			JarEntry next = in.getNextJarEntry();

			while (next != null) {
				if (next.getName().endsWith(".class")) {
					String name = next.getName().replaceAll("/", "\\.");
					
					if (!name.contains("$")) {
						name = name.replaceAll(".class", "");

						Class<?> cls = Class.forName(name);
						classes.add(cls);
					}
				}

				next = in.getNextJarEntry();
			}
		} catch (ClassNotFoundException ignored) { }

		return classes;
	}
}
