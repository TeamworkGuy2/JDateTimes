package twg2.dateTimes;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author TeamworkGuy2
 * @since 2015-7-4
 */
public class TimeUnitUtil {
	private static final DecimalFormat format2DecimalPlaces;
	private static final DecimalFormat format3DecimalPlaces;
	private static volatile DecimalFormat lastCustomFormat;

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


	static {
		format2DecimalPlaces = newFormat(2);
		format3DecimalPlaces = newFormat(3);
	}


	private TimeUnitUtil() { throw new AssertionError("cannot instantiate static class TimeUnitUtil"); }


	public static final double srcToDstMultiple(TimeUnit srcUnit, TimeUnit dstUnit) {
		return APerB[dstUnit.ordinal()][srcUnit.ordinal()];
	}


	public static final String abbreviation(TimeUnit unit, boolean shortest, boolean plural) {
		switch(unit) {
		case DAYS:
			return (plural ? "days" : "day");
		case HOURS:
			return (plural ? "hrs" : "hr");
		case MICROSECONDS:
			return (plural ? "micros" : "micro");
		case MILLISECONDS:
			return (shortest ? "ms" : (plural ? "millis" : "milli"));
		case MINUTES:
			return (plural ? "mins" : "min");
		case NANOSECONDS:
			return (shortest ? "ns" : (plural ? "nanos" : "nano"));
		case SECONDS:
			return (plural ? "secs" : "sec");
		default:
			throw throwUnknownUnit(unit, null);
		}
	}


	public static final double convert(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit) {
		return srcToDstMultiple(srcUnit, dstUnit) * srcDuration;
	}


	public static final String toString(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit, int decimalPlaces) {
		double res = srcToDstMultiple(srcUnit, dstUnit) * srcDuration;
		return decimalPlaces == 3 ? format3DecimalPlaces.format(res) : (decimalPlaces == 2 ? format2DecimalPlaces.format(res) : newFormat(decimalPlaces).format(res));
	}


	static DecimalFormat newFormat(int decimalPlaces) {
		DecimalFormat tmpFormat = TimeUnitUtil.lastCustomFormat;

		if(tmpFormat != null && tmpFormat.getMaximumFractionDigits() == decimalPlaces) {
			return tmpFormat;
		}
		else {
			DecimalFormat format = new DecimalFormat("#.#");
			format.setRoundingMode(RoundingMode.HALF_UP);
			format.setMinimumFractionDigits(0);
			format.setMaximumFractionDigits(decimalPlaces);
			TimeUnitUtil.lastCustomFormat = format;
			return format;
		}
	}


	static RuntimeException throwUnknownUnit(TimeUnit unit, String name) {
		return new IllegalArgumentException("unknown " + (name != null && name.length() > 0 ? name + " " : "") + "time unit '" + unit + "'");
	}
}
