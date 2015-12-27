package ch.stue.transmitter.websocket.domain;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.camel.RuntimeCamelException;
import org.apache.commons.collections4.ComparatorUtils;

/**
 * Contains a getComparator method which returns a comparator for a given Object
 * @author stue
 *
 */
public class ComparatorFactory {

	private static final Comparator<Double> DOUBLE_COMPARATOR = ComparatorUtils.nullHighComparator(new DoubleComparator());
	private static final Comparator<Float> FLOAT_COMPARATOR = ComparatorUtils.nullHighComparator(new FloatComparator());
	private static final Comparator<Long> LONG_COMPARATOR = ComparatorUtils.nullHighComparator(new LongComparator());
	private static final Comparator<Integer> INTEGER_COMPARATOR = ComparatorUtils.nullHighComparator(new IntegerComparator());
	private static final Comparator<Short> SHORT_COMPARATOR = ComparatorUtils.nullHighComparator(new ShortComparator());
	private static final Comparator<String> STRING_COMPARATOR = ComparatorUtils.nullHighComparator(new StringComparator());

	/**
	 * Private constructor since this is a utility class
	 */
	private ComparatorFactory(){}

	/**
	 * Returns a comparator for the given object
	 *
	 * @param value the object
	 * @return the comparator
	 * @throws RuntimeCamelException is no comparator is found
	 */
	public static Comparator<?> getComparator(Object value) {
		if (value instanceof String) {
			return STRING_COMPARATOR;
		} else if (value instanceof Number) {
			if (value instanceof Integer) {
				return INTEGER_COMPARATOR;
			} else if (value instanceof Long) {
				return LONG_COMPARATOR;
			} else if (value instanceof Float) {
				return FLOAT_COMPARATOR;
			} else if (value instanceof Double) {
				return DOUBLE_COMPARATOR;
			} else if (value instanceof Short) {
				return SHORT_COMPARATOR;
			}
		}
		throw new RuntimeCamelException(String.format("No comparator found for Object '%s'", value));
	}

	/**
	 * Can be used to compare long values
	 *
	 * @author stue
	 */
	protected static class LongComparator implements Comparator<Long>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Long first, Long second) {
			return Long.compare(second, first);
		}
	}

	/**
	 * Can be used to compare integer values
	 *
	 * @author stue
	 */
	protected static class IntegerComparator implements Comparator<Integer>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Integer first, Integer second) {
			return Integer.compare(second, first);
		}
	}

	/**
	 * Can be used to compare double values
	 *
	 * @author stue
	 */
	protected static class DoubleComparator implements Comparator<Double>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Double first, Double second) {
			return Double.compare(second, first);
		}
	}

	/**
	 * Can be used to compare float values
	 *
	 * @author stue
	 */
	protected static class FloatComparator implements Comparator<Float>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Float first, Float second) {
			return Float.compare(second, first);
		}
	}

	/**
	 * Can be used to compare short values
	 *
	 * @author stue
	 */
	protected static class ShortComparator implements Comparator<Short>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Short first, Short second) {
			return Float.compare(second, first);
		}
	}

	/**
	 * Can be used to compare string values
	 *
	 * @author stue
	 */
	protected static class StringComparator implements Comparator<String>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(String first, String second) {
			return second.compareTo(first);
		}
	}
}