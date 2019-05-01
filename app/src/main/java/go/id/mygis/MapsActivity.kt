package go.id.mygis

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import go.id.kominfo.ApiRepository.ApiReposirtory
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener,MainView {
    override fun showDataMap(listMap: List<MAPS>) {
        list.clear()
        list.addAll(listMap)

    }

    var marker: Marker? = null

    override fun onLocationChanged(p0: Location?) {
        lastLocation = p0!!
        if ( marker != null ){
            marker!!.remove()
        }


        latLng  = LatLng(lastLocation.latitude, lastLocation.longitude)
        val latLang = "${lastLocation.latitude},${lastLocation.longitude}"
        presenter.getMaps(latLang)
        println("Latlang : $latLang")
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("My Locations")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        for (i in list.indices){
            val maps = list[i]
            var markerOps = MarkerOptions()
            val posisiLat = maps.geometry?.getJSONObject("location")?.getJSONArray("lat") as String
            val posisiLang = maps.geometry?.getJSONObject("location")?.getJSONArray("lng") as String
            println("posisi :$posisiLat,$posisiLang")
            println(maps.name)



        }

        marker = mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12F))

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
    }

    override fun onConnected(p0: Bundle?) {
        locationRequest = LocationRequest()
        locationRequest.interval = 1100
        locationRequest.fastestInterval = 1100
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
        }


    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    fun build() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()

    }

    lateinit var googleApiClient: GoogleApiClient
    lateinit var locationRequest: LocationRequest
    lateinit var lastLocation: Location
    lateinit var presenter: Presenter
    lateinit var list: MutableList<MAPS>
    lateinit var gson: Gson
    lateinit var apiReposirtory: ApiReposirtory
    lateinit var latLng:LatLng


    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager

            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        list = mutableListOf()
        gson = Gson()
        apiReposirtory = ApiReposirtory()
        presenter = Presenter(this,gson,apiReposirtory)



    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            build()
            mMap.isMyLocationEnabled = true
        }
    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            }
            return false
        } else {
            return true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            99 ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (googleApiClient == null) {
                            build()
                        }
                        mMap.isBuildingsEnabled = true
                    }
                }
        }
    }


}
