package ies.quevedo.chardat.data.utils

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromTimeStamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToTimeStamp(date: LocalDate?): String? {
        return date?.toString()
    }
}
