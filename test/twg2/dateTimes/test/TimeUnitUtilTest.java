package twg2.dateTimes.test;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import twg2.dateTimes.TimeUnitUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-7-4
 */
public class TimeUnitUtilTest {
	private static TimeUnit SEC = TimeUnit.SECONDS;
	private static TimeUnit MIN = TimeUnit.MINUTES;
	private static TimeUnit MS = TimeUnit.MILLISECONDS;
	private static TimeUnit NS = TimeUnit.NANOSECONDS;


	@Test
	public void convertTest() {
		TimeUnit[] units = TimeUnit.values();
		// try various time values
		long[] values = { 3, 5, 20 };
		// slight multiplication errors, equal to ~0.1 nanos per day
		double delta = 1.0e-15;

		for(long value : values) {
			for(TimeUnit unit1 : units) {
				for(TimeUnit unit2 : units) {
					double custom = TimeUnitUtil.convert(unit1, value, unit2);
					double calced = TimeUnitUtil.getSrcToDstMultiplier(unit1, unit2) * value;
					Assert.assertEquals(value + " srcUnit=" + unit1 + ", dstUnit=" + unit2, calced, custom, delta);

					// ensure unit1 is always the larger unit (NOTE: this only works because TimeUnit's enums (and thus enum ordinals) are in order of magnitude)
					// this ensures we always multiply to larger units
					TimeUnit unitLarger = unit1;
					TimeUnit unitSmaller = unit2;
					if(unitLarger.compareTo(unitSmaller) < 0) {
						TimeUnit tmp = unitLarger;
						unitLarger = unitSmaller;
						unitSmaller = tmp;
					}

					long timeUnitRatio = unitSmaller.convert(1, unitLarger);
					long timeUnitRes = value * timeUnitRatio;
					double timeUnitRev = (double)value / timeUnitRatio;
					double customRes = TimeUnitUtil.convert(unitLarger, value, unitSmaller);
					double customRev = TimeUnitUtil.convert(unitSmaller, value, unitLarger);
					Assert.assertEquals(value + " srcUnit=" + unitLarger + ", dstUnit=" + unitSmaller, timeUnitRes, customRes, delta);
					Assert.assertEquals(value + " srcUnit=" + unitSmaller + ", dstUnit=" + unitLarger, timeUnitRev, customRev, delta);
				}
			}
		}
	}


	@Test
	public void toStringTest() {
		Assert.assertEquals("0.3", TimeUnitUtil.format(SEC, 20, MIN, 1));
		Assert.assertEquals("0.33", TimeUnitUtil.format(SEC, 20, MIN, 2));
		Assert.assertEquals("0.333", TimeUnitUtil.format(SEC, 20, MIN, 3));

		Assert.assertEquals("150", TimeUnitUtil.format(NS, 150_000_000, MS, 3));
		Assert.assertEquals("1.5", TimeUnitUtil.format(NS, 1_500_000, MS, 3));
		Assert.assertEquals("0.002", TimeUnitUtil.format(NS, 1_500, MS, 3));
		Assert.assertEquals("0.0015", TimeUnitUtil.format(NS, 1_500, MS, 4));
		Assert.assertEquals("0.0015", TimeUnitUtil.format(NS, 1_500, MS, 5));

		Assert.assertEquals("0", TimeUnitUtil.format(NS, 12_345, MS, 1));
		Assert.assertEquals("0.0123", TimeUnitUtil.format(NS, 12_345, MS, 4));
		Assert.assertEquals("0.0123", TimeUnitUtil.format(NS, 12_345, MS, 4)); // check that format reuse doesn't cause issue
		Assert.assertEquals("0.01235", TimeUnitUtil.format(NS, 12_345, MS, 5));
		Assert.assertEquals("0.012345", TimeUnitUtil.format(NS, 12_345, MS, 6));
		Assert.assertEquals("0.012345", TimeUnitUtil.format(NS, 12_345, MS, 7));
		Assert.assertEquals("0.012345", TimeUnitUtil.format(NS, 12_345, MS, 7)); // check that format reuse doesn't cause issue
	}


	@Test
	public void toStringWithAbbreviationTest() {
		Assert.assertEquals("0.58 min", TimeUnitUtil.format(SEC, 35, MIN, 2, true));
		Assert.assertEquals("0.583 min", TimeUnitUtil.format(SEC, 35, MIN, 3, true));
		Assert.assertEquals("0.5833 min", TimeUnitUtil.format(SEC, 35, MIN, 4, true));

		Assert.assertEquals("0.58 mins", TimeUnitUtil.format(SEC, 35, MIN, 2, true, true));
		Assert.assertEquals("0.583 mins", TimeUnitUtil.format(SEC, 35, MIN, 3, true, true));
		Assert.assertEquals("0.5833 mins", TimeUnitUtil.format(SEC, 35, MIN, 4, true, true));

		Assert.assertEquals("15,000,000,000 ns", TimeUnitUtil.format(SEC, 15, TimeUnit.NANOSECONDS, 2, true));
		Assert.assertEquals("15,000,000 micro", TimeUnitUtil.format(SEC, 15, TimeUnit.MICROSECONDS, 2, true));
		Assert.assertEquals("15,000 ms", TimeUnitUtil.format(SEC, 15, TimeUnit.MILLISECONDS, 2, true));
		Assert.assertEquals("15 sec", TimeUnitUtil.format(SEC, 15, TimeUnit.SECONDS, 3, true));
		Assert.assertEquals("0.25 min", TimeUnitUtil.format(SEC, 15, TimeUnit.MINUTES, 4, true));
		Assert.assertEquals("0.00417 hr", TimeUnitUtil.format(SEC, 15, TimeUnit.HOURS, 5, true));
		Assert.assertEquals("0.000174 day", TimeUnitUtil.format(SEC, 15, TimeUnit.DAYS, 6, true));
	}

}
