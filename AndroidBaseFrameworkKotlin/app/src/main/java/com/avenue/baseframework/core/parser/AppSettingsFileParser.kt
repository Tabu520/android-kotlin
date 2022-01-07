package com.avenue.baseframework.core.parser

import android.util.Xml
import com.avenue.baseframework.core.helpers.AppSettings
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.*
import javax.inject.Inject

class AppSettingsFileParser {

    val TAG = "AppSettingsFileParser"

    @Inject
    lateinit var appSettings: AppSettings

    private val ns: String? = null

    private val settings = "settings"
    private val apk_version_code = "apk_version_code"
    private val header_columns = "header_columns"
    private val section_columns = "section_columns"
    private val offline_period = "offline_period"
    private val network_timeout = "timeout"
    private val shift_day_start = "shift_day_start"
    private val shift_day_end = "shift_day_end"
    private val support_email = "support_email"
    private val support_cc_email = "support_cc_email"

    constructor(fileName: String) {
        try {
            val stream: InputStream = FileInputStream(File(fileName))
            parse(stream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    constructor(inputStream: InputStream) {
        try {
            parse(inputStream)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream) {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            read(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun read(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, settings)
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                apk_version_code -> appSettings.APP_VERSION_CODE = readText(parser).toInt()
                header_columns -> appSettings.HEADER_COLUMNS = readText(parser).toInt()
                section_columns -> appSettings.SECTION_COLUMNS = readText(parser).toInt()
                offline_period -> appSettings.OFFLINE_PERIOD = readText(parser).toInt()
                network_timeout -> appSettings.NETWORK_TIMEOUT = readText(parser).toInt()
                shift_day_start -> appSettings.SHIFT_DAY_START = readText(parser).toInt()
                shift_day_end -> appSettings.SHIFT_DAY_END = readText(parser).toInt()
                support_email -> appSettings.SUPPORT_EMAIL = readText(parser)
                support_cc_email -> appSettings.SUPPORT_CC_EMAIL = readText(parser)
                else -> skip(parser)
            }
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun skip(parser: XmlPullParser) {
        check(parser.eventType == XmlPullParser.START_TAG)
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    companion object {
        @Throws(Exception::class)
        fun getStringFromFile(filePath: String?): String {
            val fl = File(filePath!!)
            val fin = FileInputStream(fl)
            val ret = convertStreamToString(fin)
            fin.close()
            return ret
        }

        @Throws(Exception::class)
        fun convertStreamToString(inputStream: InputStream?): String {
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            reader.close()
            return sb.toString()
        }
    }

}