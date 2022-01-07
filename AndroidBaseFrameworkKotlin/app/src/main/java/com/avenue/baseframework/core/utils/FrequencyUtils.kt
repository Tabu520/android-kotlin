package com.avenue.baseframework.core.utils

import com.avenue.baseframework.core.helpers.EString
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FrequencyUtils {

    private const val splitCharacter = ","

    fun getHourMinute(time: String): HashMap<Int, Int> {
        val hashMap = HashMap<Int, Int>()
        val array = time.split(":").toTypedArray()
        hashMap[Calendar.HOUR_OF_DAY] = array[0].toInt()
        hashMap[Calendar.MINUTE] = array[1].toInt()
        if (array.size > 2) {
            hashMap[Calendar.SECOND] = array[2].toInt()
        }
        return hashMap
    }

    fun getStringHourFrameArray(value: String): List<String>? {
        return if (value != EString.EMPTY) {
            StringUtils.trimAllWhiteSpace(value).split(splitCharacter)
        } else null
    }

    fun getStringHourFrameList(value: String): List<String>? {
        if (value != EString.EMPTY) {
            return StringUtils.trimAllWhiteSpace(value).split(splitCharacter)
        }
        return null
    }

    fun getIntFrequencyArray(value: String): List<Int>? {
        if (value != EString.EMPTY) {
            val stringArray = getStringHourFrameArray(value)
            val intArray: MutableList<Int> = ArrayList()
            stringArray?.let { sArr ->
                for (s in sArr) {
                    if (s == EString.EMPTY) {
                        continue
                    }
                    intArray.add(s.trim { it <= ' ' }.replace(":00", "").toInt())
                }
                return intArray
            }
        }
        return null
    }

    fun getLongFrequencyArray(value: String): List<Long>? {
        if (value != EString.EMPTY) {
            val stringArray = getStringHourFrameArray(value)
            val intArray: MutableList<Long> = ArrayList()
            stringArray?.let { sArr ->
                for (s in sArr) {
                    if (s == EString.EMPTY) {
                        continue
                    }
                    intArray.add(s.trim { it <= ' ' }.replace(":00", "").toLong())
                }
                return intArray
            }
        }
        return null
    }

    fun isGreaterThanOrEqualToHour(hour: Int, dateNow: Date?, tomorrowMinutes: Int): Boolean {
        dateNow?.let {
            val calNow = Calendar.getInstance()
            calNow.time = it
            calNow.add(Calendar.MINUTE, tomorrowMinutes)
            val calHour = Calendar.getInstance()
            calHour.time = it
            calHour[Calendar.HOUR_OF_DAY] = hour
            calHour[Calendar.MINUTE] = 0
            calHour[Calendar.SECOND] = 0
            return calNow.time.time > calHour.time.time || calNow.time.time == calHour.time.time
        }
        return false
    }

    fun isLessThanOrEqualToHour(hour: Int, dateNow: Date?): Boolean {
        dateNow?.let {
            val calNow = Calendar.getInstance()
            calNow.time = it
            val calHour = Calendar.getInstance()
            calHour.time = it
            calHour[Calendar.HOUR_OF_DAY] = hour
            calHour[Calendar.MINUTE] = 0
            calHour[Calendar.SECOND] = 0
            return calNow.time.time < calHour.time.time || calNow.time.time == calHour.time.time
        }
        return false
    }

    fun isGreaterThanOrEqualToHour(hh_mm: String, dateNow: Date?, tomorrowMinutes: Int): Boolean {
        dateNow?.let {
            val calNow = Calendar.getInstance()
            calNow.time = dateNow
            calNow.add(Calendar.MINUTE, tomorrowMinutes)
            val mapTime = getHourMinute(hh_mm)
            val calHour = Calendar.getInstance()
            calHour.time = dateNow
            calHour[Calendar.HOUR_OF_DAY] = mapTime[Calendar.HOUR_OF_DAY]!!
            calHour[Calendar.MINUTE] = mapTime[Calendar.MINUTE]!!
            calHour[Calendar.SECOND] = 0
            return calNow.time.time > calHour.time.time || calNow.time.time == calHour.time.time
        }
        return false
    }

    fun isGreaterThanOrEqualToHour(firstTime: String, secondTime: String): Boolean {
        val firstMapTime = getHourMinute(firstTime)
        val firstCal = Calendar.getInstance()
        firstCal[Calendar.HOUR_OF_DAY] = firstMapTime[Calendar.HOUR_OF_DAY]!!
        firstCal[Calendar.MINUTE] = firstMapTime[Calendar.MINUTE]!!
        if (firstMapTime.size > 2) firstCal[Calendar.SECOND] = firstMapTime[Calendar.SECOND]!!
        val secondMapTime = getHourMinute(secondTime)
        val secondCal = Calendar.getInstance()
        secondCal[Calendar.HOUR_OF_DAY] = secondMapTime[Calendar.HOUR_OF_DAY]!!
        secondCal[Calendar.MINUTE] = secondMapTime[Calendar.MINUTE]!!
        if (secondMapTime.size > 2) secondCal[Calendar.SECOND] = secondMapTime[Calendar.SECOND]!!
        return firstCal.time.time >= secondCal.time.time
    }

    fun isLessThanOrEqualToHour(hh_mm: String, dateNow: Date?): Boolean {
        dateNow?.let {
            val calNow = Calendar.getInstance()
            calNow.time = dateNow
            val mapTime = getHourMinute(hh_mm)
            val calHour = Calendar.getInstance()
            calHour.time = dateNow
            calHour[Calendar.HOUR_OF_DAY] = mapTime[Calendar.HOUR_OF_DAY]!!
            calHour[Calendar.MINUTE] = mapTime[Calendar.MINUTE]!!
            calHour[Calendar.SECOND] = 0
            return calNow.time.time < calHour.time.time || calNow.time.time == calHour.time.time
        }
        return false
    }

    fun isLessThanOrEqualToHour(firstTime: String, secondTime: String): Boolean {
        val firstMapTime = getHourMinute(firstTime)
        val firstCal = Calendar.getInstance()
        firstCal[Calendar.HOUR_OF_DAY] = firstMapTime[Calendar.HOUR_OF_DAY]!!
        firstCal[Calendar.MINUTE] = firstMapTime[Calendar.MINUTE]!!
        if (firstMapTime.size > 2) firstCal[Calendar.SECOND] = firstMapTime[Calendar.SECOND]!!
        val secondMapTime = getHourMinute(secondTime)
        val secondCal = Calendar.getInstance()
        secondCal[Calendar.HOUR_OF_DAY] = secondMapTime[Calendar.HOUR_OF_DAY]!!
        secondCal[Calendar.MINUTE] = secondMapTime[Calendar.MINUTE]!!
        if (secondMapTime.size > 2) secondCal[Calendar.SECOND] = secondMapTime[Calendar.SECOND]!!
        return firstCal.time.time <= secondCal.time.time
    }

    fun getTimeBefore(hh_mm: String, dateNow: Date?, timeRange: Int): Long {
        dateNow?.let {
            val mapTime = getHourMinute(hh_mm)
            val cal = Calendar.getInstance()
            cal.time = it
            cal[Calendar.HOUR_OF_DAY] = mapTime[Calendar.HOUR_OF_DAY]!!
            cal[Calendar.MINUTE] = mapTime[Calendar.MINUTE]!!
            cal.add(Calendar.MINUTE, -timeRange)
            cal[Calendar.SECOND] = 0
            return cal.time.time
        }
        return -1L
    }

    fun getTimeAfter(hh_mm: String, dateNow: Date?, timeRange: Int): Long {
        dateNow?.let {
            val mapTime = getHourMinute(hh_mm)
            val cal = Calendar.getInstance()
            cal.time = it
            cal[Calendar.HOUR_OF_DAY] = mapTime[Calendar.HOUR_OF_DAY]!!
            cal[Calendar.MINUTE] = mapTime[Calendar.MINUTE]!!
            cal.add(Calendar.MINUTE, timeRange)
            cal[Calendar.SECOND] = 0
            return cal.time.time
        }
        return -1L
    }

    fun addTime(hh_mm: String, dateNow: Date?): Long {
        dateNow?.let {
            val mapTime = getHourMinute(hh_mm)
            val cal = Calendar.getInstance()
            cal.time = dateNow
            cal[Calendar.HOUR_OF_DAY] = mapTime[Calendar.HOUR_OF_DAY]!!
            cal[Calendar.MINUTE] = mapTime[Calendar.MINUTE]!!
            cal[Calendar.SECOND] = 0
            return cal.time.time
        }
        return -1L
    }
}