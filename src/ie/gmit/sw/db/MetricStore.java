package ie.gmit.sw.db;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ie.gmit.sw.metrics.Metric;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;

/**
 * This class is used to persist {@link Metric} objects to an object store.
 *
 * The class is implemented as a singleton and a reference to the single instance can be gotten by calling the
 * <code>getInstance()</code> method.
 */
public class MetricStore implements Store<Metric> {
	private EmbeddedStorageManager db = null;
	private final List<Metric> root = new ArrayList<>();
	
	private static Store<Metric> instance;
	
	private MetricStore() {
	}

	/**
	 * Get a reference to the {@link MetricStore} singleton object.
	 * @return the instance of this class
	 */
	public static Store<Metric> getInstance() {
		if (instance == null) {
			instance = new MetricStore();
		}
		return instance;
	}

	/**
	 * Initialise the object store.
	 *
	 * This method will create a folder called <code>data</code> for storing the persisted objects
	 * if one does not already exist.
	 */
	@Override
	public void init() {
		if (db == null) {
			db = EmbeddedStorage.start(root, Paths.get("./data"));
		}
	}

	@Override
	public void shutDown() {
		db.shutdown();
	}

	@Override
	public void add(Metric metric) {
		root.add(metric);
		db.storeRoot();
	}

	@Override
	public void addAll(Collection<? extends Metric> metrics) {
		root.addAll(metrics);
		db.storeRoot();
	}

	@Override
	public void clear() {
		root.clear();
		db.storeRoot();
	}

	@Override
	public Collection<Metric> filter(Predicate<? super Metric> predicate) {
		// Perform stream-based query
		return root.stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}
}
