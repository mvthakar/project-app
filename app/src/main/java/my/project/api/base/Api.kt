package my.project.api.base

import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class Api
{
    companion object
    {
        fun <T> get(path: String, type: Class<T>): T
        {
            val url = URL(path)

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accepts", "application/json")

            val reader = connection.inputStream.bufferedReader()
            val response = Gson().fromJson(reader.readText(), type)
            reader.close()

            connection.disconnect()
            return response
        }

        fun <T : Response> post(path: String, request: Request, responseType: Class<T>): T
        {
            val url = URL(path)
            val json = Gson().toJson(request)

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doInput = true
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accepts", "application/json")

            val writer = connection.outputStream.bufferedWriter()
            writer.write(json)
            writer.close()

            if (connection.responseCode != 200)
            {
                val errorStream = connection.errorStream.bufferedReader()
                val error = Gson().fromJson(errorStream.readText(), ApiError::class.java)
                error.statusCode = connection.responseCode

                connection.disconnect()
                throw error
            }

            val reader = connection.inputStream.bufferedReader()
            val response = Gson().fromJson(reader.readText(), responseType)

            response.statusCode = connection.responseCode
            reader.close()

            connection.disconnect()
            return response
        }
    }
}