# Change Log
All notable changes to this project will be documented in this file.
This project does its best to adhere to [Semantic Versioning](http://semver.org/).


--------
###[0.2.0](N/A) - 2016-10-01
#### Added
* TimeUnitFormatter class
  * containing instanced (no longer static) format() and abbreviation() methods moved/renamed from TimeUnitUtil
  * the constructor provides overloads for Locale or DecimalFormatSymbols to customize the formatted numbers
#### Changed
Refactored TimeUnitUtil
* Renamed TimeUnitUtil toString() -> format()
* Moved TimeUnitUtil toString() and abbreviation() methods to new instanced class TimeUnitFormatter
* Setup TimeUnitUtil to mirror the nte TimeUnitFormatter API with proxy methods to forward to a default instance of TimeUnitFormatter


--------
###[0.1.2](https://github.com/TeamworkGuy2/JDateTimes/commit/cf049156200ee5b20aad02f5329cda28812b8546) - 2016-09-26
#### Added
* Added TimeUnitUtil.toString() with source and destination TimeUnits and decimal point arguments


--------
###[0.1.1](https://github.com/TeamworkGuy2/JDateTimes/commit/078bee11a506dfadda24c652a32a1416e3022478) - 2016-09-02
#### Changed
* Updated dependency, switched jparser-data-type-like (now deprecated/removed) for jparse-json-lite and jparse-primitive


--------
###[0.1.0](https://github.com/TeamworkGuy2/JDateTimes/commit/8567a885cb93dddf3cdd8d6e26b391b4b8e3a166) - 2016-08-27
#### Added
* Initial versioning of existing code, including date-time parsing/formatting, and time unit conversion.
