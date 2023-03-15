package com.eman.weatherproject.utilities

import java.text.SimpleDateFormat
import java.util.*

class Converters {

    companion object {

        fun getDateFromInt(day: Int, month: Int,year:Int):String{
            val commingDate: Date = Date(year,month,day)
            val dateFormat: SimpleDateFormat = SimpleDateFormat("dd MMM")
            val formatedDate: String = dateFormat.format(commingDate)

            return formatedDate
        }

        fun getDateFormat(dtInTimeStamp: Int): String {

            val currentDate: Date = Date(dtInTimeStamp.toLong() * 1000)
            val dateFormat: SimpleDateFormat = SimpleDateFormat("EEE MMM d")
            val formatedDate: String = dateFormat.format(currentDate)

            return formatedDate

        }
        fun getDateFormattwo(txt: String): Date? {
            val formatter = SimpleDateFormat("d MMMM, yyyy HH:mm")
            formatter.timeZone = TimeZone.getTimeZone("GMT+2")
            val date = formatter.parse(txt)
            return date
        }

        fun getDayFormat(dtInTimeStamp: Int): String {

            val currentDate: Date = Date(dtInTimeStamp.toLong() * 1000)
            val dateFormat: SimpleDateFormat = SimpleDateFormat("EEE")
            val formatedDate: String = dateFormat.format(currentDate)

            return formatedDate

        }

        fun getTimeFormat(dtInTimeStamp: Int): String {

            val currentTime: Date = Date(dtInTimeStamp.toLong() * 1000)
            val timeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")
            val formatedTime: String = timeFormat.format(currentTime)

            return formatedTime

        }
    }

}
