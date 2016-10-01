package twg2.dateTimes;

import java.util.concurrent.TimeUnit;

/**
 * @author TeamworkGuy2
 * @since 2015-7-4
 */
public class TimeUnitUtil {
	static final TimeUnitFormatter defaultFormatter = new TimeUnitFormatter();

	static double C1 = 1;
	static double C2 = C1 * 1000;
	static double C3 = C2 * 1000;
	static double C4 = C3 * 1000;
	static double C5 = C4 * 60;
	static double C6 = C5 * 60;
	static double C7 = C6 * 24;

	static double[] nsPer = { C1, C2, C3, C4, C5, C6, C7 };

	static double[] microPer = { C1/C2, C2/C2, C3/C2, C4/C2, C5/C2, C6/C2, C7/C2 };

	static double[] milliPer = { C1/C3, C2/C3, C3/C3, C4/C3, C5/C3, C6/C3, C7/C3 };

	static double[] secPer = { C1/C4, C2/C4, C3/C4, C4/C4, C5/C4, C6/C4, C7/C4 };

	static double[] minPer = { C1/C5, C2/C5, C3/C5, C4/C5, C5/C5, C6/C5, C7/C5 };

	static double[] hrPer = { C1/C6, C2/C6, C3/C6, C4/C6, C5/C6, C6/C6, C7/C6 };

	static double[] dayPer = { C1/C7, C2/C7, C3/C7, C4/C7, C5/C7, C6/C7, C7/C7 };


	static double[][] APerB = {
		nsPer,
		microPer,
		milliPer,
		secPer,
		minPer,
		hrPer,
		dayPer
	};


	private TimeUnitUtil() { throw new AssertionError("cannot instantiate static class TimeUnitUtil"); }


	public static final double getSrcToDstMultiplier(TimeUnit srcUnit, TimeUnit dstUnit) {
		return APerB[dstUnit.ordinal()][srcUnit.ordinal()];
	}


	/** Convert a time measurement from one unit to another.<br>
	 * Example: {@code convert(TimeUnit.HOURS, 2.55, TimeUnit.MINUTES)}<br>
	 * returns: {@code 153.0}
	 * @param srcUnit the units of the {@code srcDuration} (i.e. milliseconds or minutes)
	 * @param srcDuration the time period to convert
	 * @param dstUnit the units to convert {@code srcDuration} to (i.e. hours or nanoseconds)
	 * @return a decimal number represented the converted units as accurately as possible
	 */
	public static final double convert(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit) {
		return getSrcToDstMultiplier(srcUnit, dstUnit) * srcDuration;
	}


	// ==== mirror TimeUnitFormatter's API ====
	/** @see TimeUnitFormatter#abbreviation(TimeUnit, boolean, boolean)
	 */
	public static final String abbreviation(TimeUnit unit, boolean shortest, boolean plural) {
		return defaultFormatter.abbreviation(unit, shortest, plural);
	}


	/** @see TimeUnitFormatter#format(TimeUnit, double, TimeUnit, int)
	 */
	public static final String format(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit, int decimalPlaces) {
		return defaultFormatter.format(srcUnit, srcDuration, dstUnit, decimalPlaces);
	}


	/** @see TimeUnitFormatter#format(TimeUnit, double, TimeUnit, int, boolean)
	 */
	public static final String format(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit, int decimalPlaces, boolean shortest) {
		return defaultFormatter.format(srcUnit, srcDuration, dstUnit, decimalPlaces, shortest);
	}


	/** @see TimeUnitFormatter#format(TimeUnit, double, TimeUnit, int, boolean, boolean)
	 */
	public static final String format(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit, int decimalPlaces, boolean shortest, boolean plural) {
		return defaultFormatter.format(srcUnit, srcDuration, dstUnit, decimalPlaces, shortest, plural);
	}


	static RuntimeException throwUnknownUnit(TimeUnit unit, String name) {
		return new IllegalArgumentException("unknown " + (name != null && name.length() > 0 ? name + " " : "") + "time unit '" + unit + "'");
	}
}
