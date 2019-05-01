package go.id.kominfo.ApiRepository

import android.util.Log
import java.net.URL

class ApiReposirtory{
    fun doRequest(url: String):String{
        val data = URL(url).readText()
        Log.d("LOG", data)

        return data
    }
}