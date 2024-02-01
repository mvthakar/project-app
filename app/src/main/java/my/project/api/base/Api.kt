package my.project.api.base

import com.google.gson.Gson
import my.project.api.refreshtoken.RefreshTokenRequest
import my.project.api.refreshtoken.RefreshTokenResponse
import my.project.state.Prefs
import java.net.HttpURLConnection
import java.net.URL

class Api
{
    companion object
    {
        class Methods
        {
            companion object
            {
                const val GET = "GET"
                const val POST = "POST"
            }
        }

        fun <TResponse : Response>
        get(path: String, responseType: Class<TResponse>, refreshTokensIfNecessary: Boolean = true): TResponse
        {
            val connection = createHttpConnection(path, Methods.GET)
            if (connection.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED && refreshTokensIfNecessary)
            {
                refreshTokens()
                return get(path, responseType)
            }

            if (connection.responseCode != HttpURLConnection.HTTP_OK)
            {
                val errorReader = connection.errorStream.bufferedReader()
                val error = Gson().fromJson(errorReader.readText(), ApiError::class.java)

                error.statusCode = connection.responseCode
                errorReader.close()

                connection.disconnect()
                throw error
            }

            val reader = connection.inputStream.bufferedReader()
            val response = Gson().fromJson(reader.readText(), responseType)
            reader.close()

            connection.disconnect()
            return response
        }

        fun <TRequest : Request, TResponse : Response>
        post(path: String, request: TRequest, responseType: Class<TResponse>, refreshTokensIfNecessary: Boolean = true): TResponse
        {
            val connection = createHttpConnection(path, Methods.POST)
            val json = Gson().toJson(request)

            val writer = connection.outputStream.bufferedWriter()
            writer.write(json)
            writer.close()

            if (connection.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED && refreshTokensIfNecessary)
            {
                refreshTokens()
                return post(path, request, responseType)
            }

            if (connection.responseCode != 200)
            {
                val errorReader = connection.errorStream.bufferedReader()
                val error = Gson().fromJson(errorReader.readText(), ApiError::class.java) ?: ApiError()

                error.statusCode = connection.responseCode
                errorReader.close()

                connection.disconnect()
                throw error
            }

            val reader = connection.inputStream.bufferedReader()
            val response = Gson().fromJson(reader.readText(), responseType) ?: Response()

            response.statusCode = connection.responseCode
            reader.close()

            connection.disconnect()

            @Suppress("UNCHECKED_CAST")
            return response as TResponse
        }

        private fun refreshTokens()
        {
            val request = RefreshTokenRequest(Prefs.refreshToken, Prefs.clientId)
            val response = post(
                Urls.REFRESH_TOKEN,
                request,
                RefreshTokenResponse::class.java,
                refreshTokensIfNecessary = false
            )

            Prefs.saveTokens(response.accessToken, response.refreshToken, response.clientId)
        }

        private fun createHttpConnection(path: String, method: String): HttpURLConnection
        {
            val url = URL(path)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = method
            connection.doInput = true
            connection.doOutput = method != Methods.GET
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accepts", "application/json")

            if (Prefs.accessToken != null)
                connection.setRequestProperty("Authorization", "Bearer ${Prefs.accessToken}")

            return connection
        }
    }
}