package go.id.mygis

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import go.id.kominfo.ApiRepository.ApiReposirtory
import go.id.mygis.ApiRepository.PromoAPI
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class Presenter(val view: MainView,val gson: Gson,val apiReposirtory: ApiReposirtory){
    fun getMaps(latLang:String){
        doAsync {
            val data = gson.fromJson(apiReposirtory.doRequest(PromoAPI.getHospital(latLang)),MAPSResponse::class.java)
            uiThread {
                view.showDataMap(data.results)
                println(data.results)
            }
        }
    }
}