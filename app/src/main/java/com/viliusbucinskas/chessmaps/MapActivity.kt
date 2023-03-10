package com.viliusbucinskas.chessmaps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.adevinta.leku.locale.SearchZoneRect
import com.viliusbucinskas.chessmaps.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.viliusbucinskas.chessmaps.model.Place
import com.viliusbucinskas.chessmaps.adapter.PlaceRenderer
import com.viliusbucinskas.chessmaps.model.ChessboardLocationAddressInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.maps.android.clustering.ClusterManager
import com.viliusbucinskas.chessmaps.viewmodel.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.maps.android.ktx.addCircle
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.google.firebase.auth.FirebaseAuth

class MapActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var fulladdress: EditText
    private lateinit var description: EditText
    private lateinit var textAdd: EditText

    private var selected: ChessboardLocationAddressInfo = ChessboardLocationAddressInfo()

    private var address : String = ""

    var selectedLong : Double = 0.0
    var selectedLat : Double = 0.0

    private val productViewModel: ProductViewModel by viewModels()

    private lateinit var list: ArrayList<ChessboardLocationAddressInfo>
    private lateinit var places: ArrayList<Place>


    private val nav = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.mapsFragment -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.clockFragment -> {
                val intent = Intent(this, ClockActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.mainActivity -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    private fun showWindow() {
        val dialogView = layoutInflater.inflate(R.layout.activity_pop_up_window, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()

        textAdd = dialogView.findViewById(R.id.fulladdress)


        val findAddress = dialogView.findViewById<Button>(R.id.findAddress)
        findAddress.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(51.450808,
                    5.481276)
                .withSearchZone("es_ES")
                .withSearchZone(
                    SearchZoneRect(
                        LatLng(
                            51.450808,
                            5.481276
                        ), LatLng(43.906271, 5.394197)
                    )
                )
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(applicationContext)

            resultLauncher.launch(locationPickerIntent)
        }

        val submit = dialogView.findViewById<Button>(R.id.submit)
        submit.setOnClickListener {
            name = dialogView.findViewById(R.id.name)
            fulladdress = dialogView.findViewById(R.id.fulladdress)
            description = dialogView.findViewById(R.id.description)
            create(name.text.toString(),description.text.toString(),fulladdress.text.toString())

            customDialog.dismiss()
        }

    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val fullAddress = result.data?.getStringExtra(LOCATION_ADDRESS)
            address = fullAddress.toString()
            textAdd.setText(address)
            selectedLat = result.data?.getDoubleExtra(LATITUDE, 0.0)!!
            selectedLong = result.data?.getDoubleExtra(LONGITUDE, 0.0)!!
        }
    }
    private fun create(name : String,description: String, fullAdd: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val product = ChessboardLocationAddressInfo(
            selected.id,
            name,
            description,
            fullAdd,
            selected.create_date ?: Timestamp.now(),
            selectedLong,
            selectedLat,
            user?.email)
        if (product.id != null) {
            productViewModel.update(product)
        } else {
            productViewModel.create(product)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val add = findViewById<FloatingActionButton>(R.id.fab)
        add.setOnClickListener{
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                showWindow()
            } else {
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        bottomNavigation.setOnNavigationItemSelectedListener(nav)

        productViewModel.getList()
        initViewModel()


    }
    private fun initMap(p : List<Place>){

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        lifecycleScope.launchWhenCreated {

            val googleMap = mapFragment.awaitMap()

            addClusteredMarkers(googleMap)

            // Wait for map to finish loading
            googleMap.awaitMapLoad()

            // Ensure all places are visible in the map
            val bounds = LatLngBounds.builder()
            p.forEach {  bounds.include(LatLng(it.lat, it.long))  }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))

        }

    }

    private fun initViewModel() {
        productViewModel.createLiveData.observe(this, {
            onCreate(it)
        })

        productViewModel.updateLiveData.observe(this, {
            onUpdate(it)
        })

        productViewModel.deleteLiveData.observe(this, {
            onDelete(it)
        })

        productViewModel.getListLiveData.observe(this, {
            onGetList(it)
        })
    }

    private fun onCreate(it: Boolean) {
        if (it) {
            productViewModel.getList()
        }
    }

    private fun onUpdate(it: Boolean) {
        if (it) {
            productViewModel.getList()
        }
    }

    private fun onDelete(it: Boolean) {
        if (it) {
            productViewModel.getList()
        }
    }

    private fun onGetList(it: List<ChessboardLocationAddressInfo>) {
        list = ArrayList()
        places = ArrayList()
        list.addAll(it)


        if(it.isNotEmpty())
        {
            list.forEachIndexed { i, e ->
                places.add(Place(e.title!!,e.fulladdress!!,e.long!!,e.lat!!))
            }
        }
        initMap(places)

    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer
        val clusterManager = ClusterManager<Place>(this, googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                this,
                googleMap,
                clusterManager
            )

        clusterManager.addItems(places)
        clusterManager.cluster()



        clusterManager.setOnClusterItemClickListener { item ->

            addCircle(googleMap, item)

            val loc = LatLng(item.lat, item.long)
            val zoomLevel = 18f
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel))
            selectedLat = item.lat
            selectedLong= item.long
            return@setOnClusterItemClickListener false


        }
        clusterManager.setOnClusterClickListener {
            //do code
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom + 2f))
            return@setOnClusterClickListener false
        }


        // When the camera starts moving, change the alpha value of the marker to translucent
        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }

        googleMap.setOnCameraIdleListener {
            // When the camera stops moving, change the alpha value back to opaque
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
            // can be performed when the camera stops moving
            clusterManager.onCameraIdle()
        }

    }
    private var circle: Circle? = null

    private fun addCircle(googleMap: GoogleMap, item: Place) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(LatLng(item.lat,item.long))
                .radius(25.0)
                .fillColor(ContextCompat.getColor(this, R.color.colorPrimaryTranslucent))
                .strokeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        )
    }
}
