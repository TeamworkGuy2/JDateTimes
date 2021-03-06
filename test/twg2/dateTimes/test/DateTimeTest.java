package twg2.dateTimes.test;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Test;

import twg2.dateTimes.DateTimeConverter;
import twg2.junitassist.checks.CheckTask;

/**
 * @author TeamworkGuy2
 * @since 2014-12-2
 */
public class DateTimeTest {

	@Test
	public void testDateTimeParsing() {
		String[] strs = {
			"2015-3-30",
			"1899-12-13",
			"5020-2-5"
		};

		ZoneId zone = ZoneId.of("Z");

		@SuppressWarnings("deprecation")
		ZonedDateTime[] expected = new ZonedDateTime[] {
				ZonedDateTime.ofInstant(new Date(2015 - 1900, 3 - 1, 30).toInstant(), zone),
				ZonedDateTime.ofInstant(new Date(1899 - 1900, 12 - 1, 13).toInstant(), zone),
				ZonedDateTime.ofInstant(new Date(5020 - 1900, 2 - 1, 5).toInstant(), zone)
		};

		SimpleDateFormat formatter = new SimpleDateFormat(DateTimeConverter.ISO_8601_DATE_FORMAT_STRING);
		CheckTask.assertTests(strs, expected, (String str, Integer idx) -> {
			try {
				ZonedDateTime zdt = ZonedDateTime.ofInstant(formatter.parse(str).toInstant(), zone);
				//System.out.println(zdt + " | " + expected[idx]);
				return zdt;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}


	/*public static void formatParseDateTimeTest() throws ParseException {
		Date[] dates = new Date[] { Date.from(Instant.now()), new Date((long)(Math.random() * 1_000_000_000_000L)) };
		DateTimeConverter dateFormatter = DateTimeConverter.getDefaultInstance();
		SimpleDateFormat[] formatters = new SimpleDateFormat[] {
				dateFormatter.getIso8601DateFormatter(),
				dateFormatter.getIso8601DateTimeFormatter(),
				dateFormatter.getRfc822Formatter()
		};
		String[] formatterNames = new String[] { "ISO 8601 Date", "ISO 8601 Date/Time", "RFC 822" };

		for(Date date : dates) {
			System.out.println("date [" + date + "]");
			int j = 0;
			for(SimpleDateFormat formatter : formatters) {
				String str = formatter.format(date);
				Date d = formatter.parse(str);
				System.out.println(formatterNames[j] + " format [" + str + "], parse [" + d + "]");
				j++;
			}
			System.out.println();
		}
	}*/

}
