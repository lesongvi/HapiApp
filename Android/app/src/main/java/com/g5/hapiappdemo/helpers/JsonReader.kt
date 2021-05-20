package com.g5.hapiappdemo.helpers

import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

object JsonReader {
    @Throws(IOException::class)
    fun readAll(rd: Reader): String {
        val sb = StringBuilder()
        var cp: Int
        while (rd.read().also { cp = it } != -1) {
            sb.append(cp.toChar())
        }
        return sb.toString()
    }

    /**
     * Hàm trả về JSONObject - MaxMines for Mobile
     * @param url - Truyền link URL có định dạng JSON
     * @return - Trả về JSONOBject
     * @throws IOException
     * @throws JSONException
     */
    @Throws(IOException::class, JSONException::class)
    fun readJsonFromUrl(url: String?): JSONObject {
        val `is` = URL(url).openStream()
        return try {
            //đọc nội dung Unicode:
            val rd = BufferedReader(
                InputStreamReader(
                    `is`,
                    Charset.forName("UTF-8")
                )
            )
            val jsonText = readAll(rd)
            JSONObject(jsonText)
        } finally {
            `is`.close()
        }
    }
}