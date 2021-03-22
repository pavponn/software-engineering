package common.utils

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor
import java.util.*

object DateTimeFormatter {

    @JvmStatic
    private val formatter: DateTimeFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(Locale.UK)
        .withZone(ZoneId.systemDefault())

    @JvmStatic
    fun format(time: TemporalAccessor): String = formatter.format(time)

}