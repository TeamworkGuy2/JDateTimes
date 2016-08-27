package twg2.dateTimes;

import java.util.Arrays;

import twg2.parser.io.jsonLite.JsonLiteNumber;
import twg2.parser.primitive.ParseInt;
import twg2.parser.textParser.TextParser;
import twg2.parser.textParserUtils.ReadNumber;
import twg2.parser.textParserUtils.ReadWhitespace;
import twg2.text.stringUtils.StringCheck;
import twg2.text.stringUtils.StringSplit;

/**
 * @author TeamworkGuy2
 * @since 2015-6-20
 */
public class TimeParser {


	/** Parse a time value in the format {@code hh:mm:ss[.ms]}, {@code mm:ss[.ms]}, or {@code ss[.ms]}.
	 * @param in
	 * @param readWhitespaceBefore
	 * @return the number of milliseconds
	 */
	public static final long parseTimeMilli(TextParser in, boolean readWhitespaceBefore) {
		if(readWhitespaceBefore) {
			ReadWhitespace.readWhitespaceCustom(in, StringCheck.SIMPLE_WHITESPACE_NOT_NEWLINE);
		}
		int val1 = ParseInt.readInt(in, false, 10);
		int val2 = 0;
		int val3 = 0;
		long valMilli = 0;
		JsonLiteNumber number = new JsonLiteNumber();

		if(in.nextIf(':')) {
			val2 = ParseInt.readInt(in, false, 10);
			if(in.nextIf(':')) {
				val3 = ParseInt.readInt(in, false, 10);
				if(in.nextIf('.')) {
					JsonLiteNumber.readNumberCustom(in, false, false, true, false, false, number);
					valMilli = (long)(number.asDouble() * 1000);
				}
				// hh:mm:ss.ms
				return val1*3600*1000 + val2*60*1000 + val3*1000 + valMilli;
			}
			// mm:ss.ms
			else if(in.nextIf('.')) {
				JsonLiteNumber.readNumberCustom(in, false, false, true, false, false, number);
				valMilli = (long)(number.asDouble() * 1000);
				return val1*60*1000 + val2*1000 + valMilli;
			}
			// mm:ss
			return val1*60*1000 + val2*1000;
		}
		// ss.ms
		else if(in.nextIf('.')) {
			JsonLiteNumber.readNumberCustom(in, false, false, true, false, false, number);
			valMilli = (long)(number.asDouble() * 1000);
			return val1*1000 + valMilli;
		}
		// ss
		return val1*1000;
	}


	/** Convert an hour, minute, second string to milliseconds.
	 * For example, parses {@code "1:19:10.575"]}
	 * and returns: {@code 4,750,575}
	 * @param hrMinSecMillis a string representing hours, minutes, and second of a time value.
	 * If the string is in the format "#", the value is assumed to be seconds.
	 * If the string is in the format "#:#", the first value is assumed to be minutes and
	 * the second value is assumed to be seconds.
	 * If the string is in the format "#:#:#", the first value is assumed to be hours, the
	 * second value is assumed to be minutes and the third value is assumed to be seconds.<br>
	 * All values can be followed by ".#" which represents a millisecond value
	 * @return the hour:minute:second value in milliseconds
	 */
	public static final long parseClockLikeToMillis(String hrMinSecMillis) {
		String[] hrMinSecMillisAry = new String[4];
		StringSplit.split(hrMinSecMillis, ':', hrMinSecMillisAry);

		// hours, minutes, seconds
		if(hrMinSecMillisAry[2] != null) {
			int msIndex = hrMinSecMillisAry[2].indexOf('.');
			// hours, minutes, seconds, milliseconds
			if(msIndex > 0) {
				String secs = hrMinSecMillisAry[2].substring(0, msIndex);
				String ms = hrMinSecMillisAry[2].substring(msIndex);
				hrMinSecMillisAry[2] = secs;
				hrMinSecMillisAry[3] = ms;

				return (long)(((long)Integer.parseInt(hrMinSecMillisAry[0]) * 60 * 60 +
						Integer.parseInt(hrMinSecMillisAry[1]) * 60 +
						Integer.parseInt(hrMinSecMillisAry[2]) +
						Double.parseDouble(hrMinSecMillisAry[3])) * 1000);
			}
			else {
				return ((long)Integer.parseInt(hrMinSecMillisAry[0]) * 60 * 60 +
						Integer.parseInt(hrMinSecMillisAry[1]) * 60 +
						Integer.parseInt(hrMinSecMillisAry[2])) * 1000;
			}
		}
		// minutes and seconds
		else if(hrMinSecMillisAry[1] != null) {
			return ((long)Integer.parseInt(hrMinSecMillisAry[0]) * 60 +
					Integer.parseInt(hrMinSecMillisAry[1])) * 1000;
		}
		// seconds only
		else if(hrMinSecMillisAry[0] != null) {
			return (long)Integer.parseInt(hrMinSecMillisAry[0]) * 1000;
		}
		else {
			throw new IllegalArgumentException("an hour minute second string can contain from 1 to 4 strings, " +
					"input strings " + Arrays.toString(hrMinSecMillisAry) + " are not valid");
		}
	}


	public static final long parseClockLikeToMillis(TextParser in) {
		StringBuilder strB = new StringBuilder();
		long res = 0;

		if(in.nextBetween('0', '9', 0, strB) > 0) {
			String valA = strB.toString();
			strB.setLength(0);

			if(in.nextIf(':')) {
				if(in.nextBetween('0', '9', 0, strB) > 0) {
					String valB = strB.toString();
					strB.setLength(0);

					if(in.nextIf(':')) {
						if(in.nextBetween('0', '9', 0, strB) > 0) {
							String valC = strB.toString();
							strB.setLength(0);

							// hours, minutes, seconds
							res = ((long)Integer.parseInt(valA) * 60 * 60 +
									Integer.parseInt(valB) * 60 +
									Integer.parseInt(valC)) * 1000;
						}
						else {
							throw new IllegalArgumentException("hour minute second string cannot end with ambiguous ':'");
						}
					}
					// minutes and seconds
					else {
						res = ((long)Integer.parseInt(valA) * 60 +
								Integer.parseInt(valB)) * 1000;
					}
				}
				else {
					throw new IllegalArgumentException("minute second string cannot end with ambiguous ':'");
				}
			}
			else {
				// seconds only
				res = (long)Integer.parseInt(valA) * 1000;
			}
		}
		else {
			throw new IllegalArgumentException("an hour minute second string can contain from 1 to 4 strings, " +
					"input strings '" + strB.toString() + "' are not valid");
		}

		// optional 
		if(in.nextIf('.')) {
			strB.append('.');

			if(in.nextBetween('0', '9', 0, strB) > 0) {
				String milliStr = strB.toString();
				strB.setLength(0);

				res += Double.parseDouble(milliStr) * 1000;
			}
		}

		return res;
	}


	/** A time unit with a name, valid time units are [sec, secs, second, seconds, min, mins, minute, minutes, hr, hrs, hour, hours].<br>
	 * For example {@code "3 hours"} or {@code "15 mins."} or {@code "112 second"}
	 * @param namedTimeUnitVal
	 * @return the millisecond value of the input time units
	 */
	public static final long parseNamedTimeUnitToMillis(String namedTimeUnitVal) {
		int timeUnitIndex = namedTimeUnitVal.indexOf(' ');
		// the time value, like 30 or 1.5
		String time = namedTimeUnitVal.substring(0, timeUnitIndex);
		// the time units, like 'hours' or 'secs.'
		String units = namedTimeUnitVal.substring(timeUnitIndex + 1).toLowerCase();

		double timeVal = Double.parseDouble(time);

		if(units.startsWith("hr") || units.startsWith("hour")) {
			return (long)(timeVal * 60 * 60 * 1000);
		}
		else if(units.startsWith("min")) {
			return (long)(timeVal * 60 * 1000);
		}
		else if (units.startsWith("sec")) {
			return (long)(timeVal * 1000);
		}

		throw new IllegalArgumentException("unknown time units '" + units + "' from '" + namedTimeUnitVal + "'");
	}


	public static final long parseNamedTimeUnitToMillis(TextParser in) {
		StringBuilder strB = new StringBuilder();
		int startPos = in.getPosition();

		ReadNumber.readNumberInto(in, strB);

		if(!in.nextIf(' ')) {
			throw new IllegalArgumentException("incorrect time value-unit format, time value must be proceeded by a space and a time unit");
		}

		int read = in.getPosition() - startPos;
		double timeVal = Double.parseDouble(strB.toString());
		strB.setLength(0);

		if(in.nextIf('h')) {
			if(in.nextIf('r')) {
				if(in.nextIf('s')) {
					in.nextIf('.');
				}
				else if(in.nextIf('.')) {
				}
			}
			else if(in.nextIf("our")) {
				in.nextIf('s');
			}

			return (long)(timeVal * 60 * 60 * 1000);
		}
		else if(in.nextIf("min")) {
			if(in.nextIf('s')) {
				in.nextIf('.');
			}
			else if(in.nextIf("ute")) {
				in.nextIf('s');
			}
			else if(in.nextIf('.')) {
			}

			return (long)(timeVal * 60 * 1000);
		}
		else if(in.nextIf("sec")) {
			if(in.nextIf('s')) {
				in.nextIf('.');
			}
			else if(in.nextIf("ond")) {
				in.nextIf('s');
			}
			else if(in.nextIf('.')) {
			}

			return (long)(timeVal * 1000);
		}

		// unread the time value
		in.unread(read);

		throw new IllegalArgumentException("unknown time units");
	}


	public static final String formatMsToClockLike(long ms) {
		return formatMsToClockLike(ms, true, false);
	}


	/** Converts a millisecond time span to a hr:min:sec[.millis] string.<br>
	 * For example {@code 3702000} milliseconds would be converted to {@code "1:01:42"}.
	 * Or {@code 381000} milliseconds would be converted to {@code "6:21"}
	 * @param ms the millisecond time interval/period to convert
	 * @return a string in the format {@code hr:min:sec} or {@code min:sec}
	 * depending on whether the time span is longer or shorter than one hour
	 */
	public static final String formatMsToClockLike(long ms, boolean includeSecs, boolean includeMillis) {
		long remainder = ms;
		long hours = (long)(remainder / (1000 * 60 * 60));
		remainder %= (1000 * 60 * 60);
		long minutes = (long)(remainder / (1000 * 60));
		remainder %= (1000 * 60);
		long seconds = (long)(remainder / 1000);
		remainder %= 1000;
		long millis = remainder;

		if(minutes > 59 || seconds > 59 || millis > 999) {
			throw new IllegalStateException("error parsing '" + ms + "' milliseconds to hr:min:sec " +
					hours + ":" + minutes + ":" + seconds);
		}

		return (hours > 0 ? (hours + ":") : "") +
				(minutes > 0 ? (minutes < 10 && hours > 0 ? "0" + minutes + ":" : minutes + ":") : ( hours > 0 ? "00:" : "0:")) +
				(includeSecs && seconds > 0 ? (seconds > 0 ? (seconds < 10 ? "0" + seconds : seconds) : "00") : "00") +
				(includeMillis && millis > 0 ? (millis < 100 ? (millis < 10 ? ".00" + millis : ".0" + millis) : "." + millis) : "");
	}


	public static final long hrToMs(int hours) {
		return minToMs(hours * 60);
	}


	public static final long minToMs(int min) {
		return secToMs(min * 60);
	}


	public static final long secToMs(int sec) {
		return sec * 1000;
	}


	public static final long toMs(int hours, int mins, int secs, int ms) {
		return hrToMs(hours) + minToMs(mins) + secToMs(secs) + ms;
	}

}
