package twg2.dateTimes;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/** A formatter for {@link TimeUnit} time periods with various {@code toString()} overrides and {@code abbreviation()} to get a time unit's abbreviation
 * @author TeamworkGuy2
 * @since 2016-10-01
 */
public class TimeUnitFormatter {
	// TODO we do not yet use constructor locale information for abbreviations
	private final DecimalFormat format2DecimalPlaces;
	private final DecimalFormat format3DecimalPlaces;
	private final DecimalFormatSymbols formatSymbols;
	private volatile DecimalFormat lastCustomFormat;


	public TimeUnitFormatter() {
		this((DecimalFormatSymbols)null);
	}


	public TimeUnitFormatter(Locale locale) {
		this(locale != null ? DecimalFormatSymbols.getInstance(locale) : null);
	}


	public TimeUnitFormatter(DecimalFormatSymbols formatSymbols) {
		this.formatSymbols = formatSymbols;
		this.format2DecimalPlaces = newFormat(2);
		this.format3DecimalPlaces = newFormat(3);
	}


	/** Get a time unit's English name abbreviation<br>
	 * Example: {@link TimeUnit#DAYS} is 'days'<br>
	 * or: {@link TimeUnit#NANOSECONDS} is 'nano', 'nanos', or 'ns'<br>
	 * The version of abbreviation returned depends on the arguments
	 * @param unit the time unit to abbreviate
	 * @param shortest if true 'millis' is shortened to 'ms', 'nanos' to 'ns', etc. If false, the longer abbreviation is returned
	 * @param plural if true 'secs' instead of 'sec' or 'millis' instead of 'millis'.  If false, the singular version is returned
	 * @return the abbreviation
	 */
	public final String abbreviation(TimeUnit unit, boolean shortest, boolean plural) {
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
			throw TimeUnitUtil.throwUnknownUnit(unit, null);
		}
	}


	/** Convert a time measurement from one unit to another and then to a decimal number with a certain number of decimal places
	 * @param srcUnit the units of the {@code srcDuration} (i.e. milliseconds or minutes)
	 * @param srcDuration the time period to convert
	 * @param dstUnit the units to convert {@code srcDuration} to (i.e. hours or nanoseconds)
	 * @param decimalPlaces the number of decimal places to display the converted value as (note: currently optimized for 2 and 3 decimal places)
	 * @return a string representing the source time period in {@code dstUnit} units to n-{@code decimalPlaces} of accuracy
	 */
	public final String format(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit, int decimalPlaces) {
		double res = TimeUnitUtil.getSrcToDstMultiplier(srcUnit, dstUnit) * srcDuration;
		return decimalPlaces == 3 ? this.format3DecimalPlaces.format(res) : (decimalPlaces == 2 ? this.format2DecimalPlaces.format(res) : newFormat(decimalPlaces).format(res));
	}


	/** Convert a time unit and output it as a decimal to n-{@code decimalPlaces} with a units abbreviation (i.e. 'ms' or 'hours').<br>
	 * Equivalent to {@link #format(TimeUnit, double, TimeUnit, int) toString()} + " " + {@link #abbreviation(TimeUnit, boolean, boolean) abbreviation()}
	 * @see #format(TimeUnit, double, TimeUnit, int)
	 * @see #abbreviation(TimeUnit, boolean, boolean)
	 */
	public final String format(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit, int decimalPlaces, boolean shortest) {
		return format(srcUnit, srcDuration, dstUnit, decimalPlaces, shortest, false);
	}


	/** Convert a time unit and output it as a decimal to n-{@code decimalPlaces} with a units abbreviation (i.e. 'ms' or 'hours').<br>
	 * Equivalent to {@link #format(TimeUnit, double, TimeUnit, int) toString()} + " " + {@link #abbreviation(TimeUnit, boolean, boolean) abbreviation()}
	 * @see #format(TimeUnit, double, TimeUnit, int)
	 * @see #abbreviation(TimeUnit, boolean, boolean)
	 */
	public final String format(TimeUnit srcUnit, double srcDuration, TimeUnit dstUnit, int decimalPlaces, boolean shortest, boolean plural) {
		return format(srcUnit, srcDuration, dstUnit, decimalPlaces) + " " + abbreviation(dstUnit, shortest, plural);
	}


	/** Create a {@code '#,###.##...'} {@link DecimalFormat} with n-{@code decimalPlaces}.<br>
	 * NOTE: a cached format may be returned, treat the returned format as immutable, do not modify the returned format.
	 * @param decimalPlaces the number of decimal places of the returned format
	 * @return a decimal format in the above described format
	 */
	DecimalFormat createOrReuseFormat(int decimalPlaces) {
		DecimalFormat tmpFormat = this.lastCustomFormat;

		if(tmpFormat != null && tmpFormat.getMaximumFractionDigits() == decimalPlaces) {
			return tmpFormat;
		}
		else {
			DecimalFormat newFormat = newFormat(decimalPlaces);
			this.lastCustomFormat = newFormat;
			return newFormat;
		}
	}


	DecimalFormat newFormat(int decimalPlaces) {
		DecimalFormat format = (this.formatSymbols != null ? new DecimalFormat("#,###.#", this.formatSymbols) : new DecimalFormat("#,###.#"));
		format.setRoundingMode(RoundingMode.HALF_UP);
		format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(decimalPlaces);
		return format;
	}

}
