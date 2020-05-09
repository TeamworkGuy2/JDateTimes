package twg2.dateTimes.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import twg2.dateTimes.TimeParser;
import twg2.junitassist.checks.CheckTask;
import twg2.parser.textParser.TextCharsParser;
import twg2.parser.textParser.TextParser;

/**
 * @author TeamworkGuy2
 * @since 2015-6-20
 */
public class TimeParserTest {

	@Test
	public void parseDateTimeTest() {
		double milli = TimeUnit.SECONDS.toMillis(1);
		String[] strs = new String[] {
				" 	123",
				"13:30",
				"3:20.0008",
				"	1:40:2.0005",
				"1:00:00",
				"0.0130"
		};
		Long[] expect = new Long[] {
				(long)(123 * milli),
				(long)((13 * 60 + 30) * milli),
				(long)((3 * 60) * milli + (20.0008d * milli)),
				(long)((1 * 60 * 60) * milli + (40 * 60 + 2.0005d) * milli),
				(long)((1 * 60 * 60) * milli),
				(long)(0.0130d * milli)
		};
		CheckTask.assertTests(strs, expect, (str) -> TimeParser.parseTimeMilli(parser(str), true));
	}


	@Test
	public void parseClockLikeToMillisTest() {
		String[] input = {
				"15:02:29",
				"2:40:31.050",
				"13:52.289",
				"1:08",
				"105",
		};

		Long[] expect = new Long[] {
				TimeParser.toMs(15, 2, 29, 00),
				TimeParser.toMs(2, 40, 31, 50),
				TimeParser.toMs(0, 13, 52, 289),
				TimeParser.toMs(0, 1, 8, 0),
				TimeParser.toMs(0, 0, 105, 0),
		};

		// parse string to millisecond value
		CheckTask.assertTests(input, expect, (str) -> {
			return TimeParser.parseClockLikeToMillis(str);
		});

		// parse string to millisecond value
		CheckTask.assertTests(input, expect, (String str) -> {
			return TimeParser.parseClockLikeToMillis(parser(str));
		});

		CheckTask.assertException(() -> TimeParser.parseClockLikeToMillis("1:2:"));
		CheckTask.assertException(() -> TimeParser.parseClockLikeToMillis("(12)"));
		CheckTask.assertException(() -> TimeParser.parseClockLikeToMillis(parser("1:2:")));
		CheckTask.assertException(() -> TimeParser.parseClockLikeToMillis(parser("(12)")));
	}


	@Test
	public void formatMsToClockLikeTest() {
		Long[] input = new Long[] {
				TimeParser.toMs(15, 2, 29, 00),
				TimeParser.toMs(2, 40, 31, 50),
				TimeParser.toMs(0, 13, 52, 289),
				TimeParser.toMs(0, 1, 8, 0),
				TimeParser.toMs(0, 0, 343, 0)
		};

		String[] expect = {
				"15:02:29",
				"2:40:31.050",
				"13:52.289",
				"1:08",
				"5:43",
		};

		String[] expectNoMillis = {
				"15:02:29",
				"2:40:31",
				"13:52",
				"1:08",
				"5:43",
		};

		String[] expectNoSecs = {
				"15:02:00",
				"2:40:00",
				"13:00",
				"1:00",
				"5:00",
		};

		// converting milliseconds to string
		CheckTask.assertTests(input, expect, (Long ms) -> {
			return TimeParser.formatMsToClockLike(ms, true, true);
		});

		// converting milliseconds to string (no milliseconds)
		CheckTask.assertTests(input, expectNoMillis, (Long ms) -> {
			return TimeParser.formatMsToClockLike(ms);
		});

		// converting milliseconds to string (no seconds and no milliseconds)
		CheckTask.assertTests(input, expectNoSecs, (Long ms) -> {
			return TimeParser.formatMsToClockLike(ms, false, false);
		});
	}


	@Test
	public void testParsingTimeUnit() {
		String[] input = {
				"1.5 hours",
				"45 minute",
				"30 mins.",
				"30.12 min",
				"0.00005556 hrs.",
				"42 seconds",
				"16.667 secs",
		};

		Long[] expect = new Long[] {
				TimeParser.toMs(1, 30, 0, 0),
				TimeParser.toMs(0, 45, 0, 0),
				TimeParser.toMs(0, 30, 0, 0),
				TimeParser.toMs(0, 30, 7, 200),
				TimeParser.toMs(0, 0, 0, 200),
				TimeParser.toMs(0, 0, 42, 0),
				TimeParser.toMs(0, 0, 16, 667),
		};

		CheckTask.assertTests(input, expect, (str) -> {
			return TimeParser.parseNamedTimeUnitToMillis(str);
		});

		CheckTask.assertTests(input, expect, (String str) -> {
			return TimeParser.parseNamedTimeUnitToMillis(parser(str));
		});

		CheckTask.assertException(() -> TimeParser.parseNamedTimeUnitToMillis("2 days"));
		CheckTask.assertException(() -> TimeParser.parseNamedTimeUnitToMillis(parser("2 days")));
	}


	private static TextParser parser(String src) {
		return TextCharsParser.of(src);
	}
}
