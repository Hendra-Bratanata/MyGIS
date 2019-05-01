package go.id.mygis.ApiRepository

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import go.id.mygis.BuildConfig
import go.id.mygis.R
import java.net.URLEncoder

object PromoAPI {
    fun getHospital(latlang: String): String {

        val Url = BuildConfig.BASE_URL + "$latlang&rankby=distance&type=hospital&key=${BuildConfig.API_KEY}"
        Log.d("data", Url)
        return Url
    }

}