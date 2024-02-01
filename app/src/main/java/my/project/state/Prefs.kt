package my.project.state

import android.content.Context
import android.content.SharedPreferences

class Prefs
{
    companion object
    {
        private lateinit var prefs: SharedPreferences

        var accessToken: String? = null
            private set
        var refreshToken: String? = null
            private set
        var clientId: String? = null
            private set

        fun load(context: Context)
        {
            prefs = context.getSharedPreferences("tokens", Context.MODE_PRIVATE)

            accessToken = prefs.getString("accessToken", null)
            refreshToken = prefs.getString("refreshToken", null)
            clientId = prefs.getString("clientId", null)
        }

        fun saveTokens(accessToken: String, refreshToken: String, clientId: String)
        {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
            this.clientId = clientId

            val editor = prefs.edit()

            editor.putString("accessToken", accessToken)
            editor.putString("refreshToken", refreshToken)
            editor.putString("clientId", clientId)

            editor.apply()
        }
    }
}