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
					double calced = TimeUnitUtil.srcToDstMultiple(unit1, unit2) * value;
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
		TimeUnit SEC = TimeUnit.SECONDS;
		TimeUnit MIN = TimeUnit.MINUTES;
		TimeUnit MS = TimeUnit.MILLISECONDS;
		TimeUnit NS = TimeUnit.NANOSECONDS;

		Assert.assertEquals("0.3", TimeUnitUtil.toString(SEC, 20, MIN, 1));
		Assert.assertEquals("0.33", TimeUnitUtil.toString(SEC, 20, MIN, 2));
		Assert.assertEquals("0.333", TimeUnitUtil.toString(SEC, 20, MIN, 3));

		Assert.assertEquals("150", TimeUnitUtil.toString(NS, 150_000_000, MS, 3));
		Assert.assertEquals("1.5", TimeUnitUtil.toString(NS, 1_500_000, MS, 3));
		Assert.assertEquals("0.002", TimeUnitUtil.toString(NS, 1_500, MS, 3));
		Assert.assertEquals("0.0015", TimeUnitUtil.toString(NS, 1_500, MS, 4));
		Assert.assertEquals("0.0015", TimeUnitUtil.toString(NS, 1_500, MS, 5));

		Assert.assertEquals("0", TimeUnitUtil.toString(NS, 12_345, MS, 1));
		Assert.assertEquals("0.0123", TimeUnitUtil.toString(NS, 12_345, MS, 4));
		Assert.assertEquals("0.0123", TimeUnitUtil.toString(NS, 12_345, MS, 4)); // check that format reuse doesn't cause issue
		Assert.assertEquals("0.01235", TimeUnitUtil.toString(NS, 12_345, MS, 5));
		Assert.assertEquals("0.012345", TimeUnitUtil.toString(NS, 12_345, MS, 6));
		Assert.assertEquals("0.012345", TimeUnitUtil.toString(NS, 12_345, MS, 7));
		Assert.assertEquals("0.012345", TimeUnitUtil.toString(NS, 12_345, MS, 7)); // check that format reuse doesn't cause issue
	}

}
