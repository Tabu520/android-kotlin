package com.avenue.baseframework.core.parser

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

open class XMLParser(content: String) {

    val ns: String? = null

    var dataSet: List<*>? = null

    init {
        val stream: InputStream = ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8))
        try {
            dataSet = parse(stream)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    open fun parse(inputStream: InputStream): List<*>? {
        return inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            read(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    open fun read(parser: XmlPullParser): List<*>? {
        return null
    }

    @Throws(XmlPullParserException::class, IOException::class)
    open fun skip(parser: XmlPullParser) {
        check(parser.eventType == XmlPullParser.START_TAG)
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}