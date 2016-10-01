JDateTimes
==========
version: 0.2.0

Includes DateTime and TimeUnit parsing and formatting.

Packages:
####dateTimes/
Ease of use date/time conversion methods to supplement the Java standard libraries
  * DateTimeConverter - static date time parsing and formatting methods for common date formats
  * TimeParser - parsing strings such as "## seconds", "## mins.", or "HH:mm:ss" into milliseconds
  * TimeUnitUtil - convert between time units using 'double', instead of 'long' like java.util.concurrent.TimeUnit uses

####tests/
JUnit tests and example code
