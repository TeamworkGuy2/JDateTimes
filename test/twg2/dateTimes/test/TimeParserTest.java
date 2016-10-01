package twg2.dateTimes.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import twg2.dateTimes.TimeParser;
import twg2.parser.textParser.TextIteratorParser;
import checks.CheckTask;

/**
 * @author TeamworkGuy2
 * @since 2015-6-20
 */
public class TimeParserTest {

	@Test
	public void parseDateTimeTest() {
		double milli = TimeUnit.SECONDS.toMillis(1);
		String[] strs = new String[] {" 	123", "13:30", "3:20.0008", "	1:40:2.0005", "1:00:00", "0.0130"};
		Long[] expect = new Long[] {
				(long)(123 * milli),
				(long)((13 * 60 + 30) * milli),
				(long)((3 * 60) * milli + (20.0008d * milli)),
				(long)((1 * 60 * 60) * milli + (40 * 60 + 2.0005d) * milli),
				(long)((1 * 60 * 60) * milli),
				(long)(0.0130d * milli)
		};
		CheckTask.assertTests(strs, expect, (str) -> TimeParser.parseTimeMilli(TextIteratorParser.of(str), true));
	}


	@Test
	public void testParserClockLikeToMs() {
		String[] input = {
				"2:40:31.050",
				"13:52",
				"1:08"
		};

		String[] inputNoMillis = {
				"2:40:31",
				"13:52",
				"1:08"
		};

		String[] inputNoSecs = {
				"2:40:00",
				"13:00",
				"1:00"
		};

		Long[] expect = new Long[] {
				TimeParser.toMs(2, 40, 31, 50),
				TimeParser.toMs(0, 13, 52, 0),
				TimeParser.toMs(0, 1, 8, 0)
		};

		// parsing string into millisecond value
		CheckTask.assertTests(input, expect, (str) -> {
			long time = TimeParser.parseClockLikeToMillis(str);
			return time;
		});

		// parsing string into millisecond value
		CheckTask.assertTests(input, expect, (String str) -> {
			long time = TimeParser.parseClockLikeToMillis(TextIteratorParser.of(str));
			return time;
		});

		// converting milliseconds to string
		CheckTask.assertTests(expect, input, (Long ms) -> {
			String str = TimeParser.formatMsToClockLike(ms, true, true);
			return str;
		});

		// converting milliseconds to string (no milliseconds)
		CheckTask.assertTests(expect, inputNoMillis, (Long ms) -> {
			String str = TimeParser.formatMsToClockLike(ms);
			return str;
		});

		// converting milliseconds to string (no seconds and no milliseconds)
		CheckTask.assertTests(expect, inputNoSecs, (Long ms) -> {
			String str = TimeParser.formatMsToClockLike(ms, false, false);
			return str;
		});
	}


	@Test
	public void testParsingTimeUnit() {
		String[] input = {
				"1.5 hours",
				"30 mins.",
				"30.12 min",
				"0.00005556 hrs.",
				"16.667 secs",
		};

		Long[] expect = new Long[] {
				TimeParser.toMs(1, 30, 0, 0),
				TimeParser.toMs(0, 30, 0, 0),
				TimeParser.toMs(0, 30, 7, 200),
				TimeParser.toMs(0, 0, 0, 200),
				TimeParser.toMs(0, 0, 16, 667),
		};

		CheckTask.assertTests(input, expect, (str) -> {
			return TimeParser.parseNamedTimeUnitToMillis(str);
		});

		CheckTask.assertTests(input, expect, (String str) -> {
			return TimeParser.parseNamedTimeUnitToMillis(TextIteratorParser.of(str));
		});
	}

}
