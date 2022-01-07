package com.avenue.baseframework.core.parser

import com.avenue.baseframework.core.BaseApplication
import com.avenue.baseframework.core.db.entity.XMLTemplateEntity
import com.avenue.baseframework.core.helpers.EString
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException
import java.util.ArrayList

class XmlTemplateParser(content: String): XMLParser(content) {

    override fun read(parser: XmlPullParser): List<*> {
        val templates: MutableList<XMLTemplateEntity> = ArrayList<XMLTemplateEntity>()

        parser.require(XmlPullParser.START_TAG, ns, "OPL_XML_TEMPLATEMboSet")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "OPL_XML_TEMPLATE") {
                templates.add(readTemplate(parser))
            } else {
                skip(parser)
            }
        }
        return templates
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTemplate(parser: XmlPullParser): XMLTemplateEntity {
        var OPL_XML_TEMPLATEID: String = EString.EMPTY
        var ORGID: String? = EString.EMPTY
        var SITEID: String? = EString.EMPTY
        var ZONEID: String? = EString.EMPTY
        var AREAID: String? = EString.EMPTY
        var TEMPLATEID: String = EString.EMPTY
        var TEMPLATE_DESCRIPTION: String? = EString.EMPTY
        var PATH: String? = EString.EMPTY
        var LOCAL_PATH: String
        var FREQUENCY: String? = EString.EMPTY
        var VERSION: String? = EString.EMPTY
        parser.require(XmlPullParser.START_TAG, ns, "OPL_XML_TEMPLATE")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "OPL_XML_TEMPLATEID" -> OPL_XML_TEMPLATEID = parser.nextText()
                "ORGID" -> ORGID = parser.nextText()
                "SITEID" -> SITEID = parser.nextText()
                "ZONEID" -> ZONEID = parser.nextText()
                "AREAID" -> AREAID = parser.nextText()
                "TEMPLATEID" -> TEMPLATEID = parser.nextText()
                "TEMPLATE_DESCRIPTION" -> TEMPLATE_DESCRIPTION = parser.nextText()
                "PATH" -> PATH = parser.nextText()
                "FREQUENCY" -> FREQUENCY = parser.nextText()
                "VERSION" -> VERSION = parser.nextText()
                else -> skip(parser)
            }
        }
        val directory: File = BaseApplication.getContext().filesDir
        val file = File(directory, "$TEMPLATEID.xml")
        LOCAL_PATH = file.absolutePath
        return XMLTemplateEntity(
            OPL_XML_TEMPLATEID.toLong(),
            ORGID,
            SITEID,
            ZONEID,
            AREAID,
            TEMPLATEID,
            TEMPLATE_DESCRIPTION,
            PATH,
            LOCAL_PATH,
            FREQUENCY,
            VERSION
        )
    }
}