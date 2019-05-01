package go.id.mygis

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class MAPS(
    @SerializedName("icon")
    var icon: String?= null,
    @SerializedName("name")
    var name: String?= null,
    @SerializedName("geometry")
    var geometry: JSONObject? = null

)