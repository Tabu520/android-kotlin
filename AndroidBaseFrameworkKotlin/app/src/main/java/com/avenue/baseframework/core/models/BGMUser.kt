package com.avenue.baseframework.core.models

import android.util.JsonReader
import android.util.Log
import com.avenue.baseframework.core.helpers.EString
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.charset.StandardCharsets

class BGMUser {

    val TAG = "BGMUser"

    var userId = 0
    var username: String? = null
    var password: String? = null
    var personID: String? = null
    var displayName: String? = null
    var email: String? = null
    var loginTime: String? = null
    var loginStatus: Boolean? = null
    var deviceId: String? = null
    var token: String? = null
    var isActive = 0
    var startYear = 0
    var endYear = 0


    var emailTemplate: JSONObject? = null

    init {
        token = null
    }

    fun getYears(): Array<String?> {
        val years = arrayOfNulls<String>(endYear - startYear + 1)
        for (yy in years.indices) {
            years[yy] = (yy + startYear).toString()
        }
        return years
    }


    fun getYearIndex(year: String): Int {
        val years = getYears()
        var index = years.size - 1
        for (i in years.indices) {
            if (years[i] == year) {
                index = i
                break
            }
        }
        return index
    }


    fun parseBGMUser(response: String): BGMUser {
        val user = BGMUser()
        try {
            val inputStream: InputStream =
                ByteArrayInputStream(response.toByteArray(StandardCharsets.UTF_8))
            val reader = JsonReader(InputStreamReader(inputStream))
            user.parse(reader)
        } catch (ex: Exception) {
            Log.e(TAG, "parseBGMUser: $ex")
        }
        return user
    }

    fun parseBGUser(response: JSONObject): BGMUser? {
        val user = BGMUser()
        try {
            val message = response.getString("Message")
            if (message == EString.LOGIN_SUCCESS) {
                parseUserLoginObject(user, response)
            } else if (message.isNullOrEmpty()) {
                try {
                    parseUserLoginObject(user, response) //  for reset password message is null
                } catch (ex: Exception) {
                    Log.e(TAG, ex.toString())
                }
            } else {
                user.token = null
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return user
    }

    @Throws(JSONException::class)
    private fun parseUserLoginObject(user: BGMUser, response: JSONObject) {
        val objUserLogin = response.getJSONObject("UserLogin")
        user.token = if (objUserLogin.isNull("Token")) null else objUserLogin.getString("Token")
        if (user.token != null) {
            val objMaxUser = objUserLogin.getJSONObject("MaxUser")
            val objPerson = objMaxUser.getJSONObject("Person")
            user.startYear = objUserLogin.getInt("StartYear")
            user.endYear = objUserLogin.getInt("EndYear")
            user.userId = objMaxUser.getInt("BGMTUserId")
            user.username = objMaxUser.getString("BGMUsername")
            user.password = objMaxUser.getString("BGMPassword")
            user.personID = objPerson.getString("PersonID")
            user.displayName = objPerson.getString("DisplayName")
            user.email = objPerson.getString("PrimaryEmail")
            user.isActive = objMaxUser.getInt("BGMActive")
            user.loginTime = objUserLogin.getString("LoginTime")
            user.loginStatus = objUserLogin.getBoolean("LoginStatus")
            user.deviceId = objUserLogin.getString("DeviceID")
        }
    }

    @Throws(IOException::class)
    private fun parse(reader: JsonReader) {
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            if (name == "UserLogin") {
                Log.d(TAG, "reader object name: $name")
                parseMaxUser(reader)
            }
            Log.d(TAG, "reader object name: $name")
        }
        reader.endObject()
    }

    @Throws(IOException::class)
    private fun parseMaxUser(reader: JsonReader) {
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            when(name) {
                "MaxUser" -> parseMaxUserObject(reader)
                "LoginTime" -> {
                    loginTime = reader.nextString()
                    Log.d(TAG, "reader object name: $name for value: $loginTime")
                }
                "LoginStatus" -> {
                    loginStatus = reader.nextBoolean()
                    Log.d(TAG, "reader object name: $name for value: $loginStatus")
                }
                "DeviceID" -> reader.skipValue()
                "Token" -> token = reader.nextString()
                "StartYear" -> startYear = reader.nextInt()
                "EndYear" -> endYear = reader.nextInt()
                "IdPays" -> reader.skipValue()
            }
        }
        reader.endObject()
    }

    @Throws(IOException::class)
    private fun parseMaxUserObject(reader: JsonReader) {
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            when(name) {
                "BGMTUserId" -> {
                    userId = reader.nextInt()
                    Log.d(TAG, "reader object name: $name for value: $userId")
                }
                "BGMUsername" -> {
                    username = reader.nextString()
                    Log.d(TAG, "reader object name: $name for value: $username")
                }
                "BGMPassword" -> {
                    password = reader.nextString()
                    Log.d(TAG, "reader object name: $name for value: $password")
                }
                "Person" -> parsePerson(reader)
                "BGMActive" -> {
                    isActive = reader.nextInt()
                    Log.d(TAG, "reader object name: $name for value: $isActive")
                }
                "IdPays" -> reader.skipValue()
            }
        }
        reader.endObject()
    }

    @Throws(IOException::class)
    private fun parsePerson(reader: JsonReader) {
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            when (name) {
                "PersonID" -> {
                    personID = reader.nextString()
                    Log.d(TAG, "reader object name: $name for value: $personID")
                }
                "DisplayName" -> {
                    displayName = reader.nextString()
                    Log.d(TAG, "reader object name: $name for value: $displayName")
                }
                "PrimaryEmail" -> {
                    email = reader.nextString()
                    Log.d(TAG, "reader object name: $name for value: $email")
                }
                "IdPays" -> reader.skipValue()
            }
        }
        reader.endObject()
    }
}