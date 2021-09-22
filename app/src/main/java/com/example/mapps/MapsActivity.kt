package com.example.mapps

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mapps.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    lateinit var btn: Button
    lateinit var input: EditText
    lateinit var ip: TextView
    lateinit var country: TextView
    lateinit var city: TextView
    lateinit var isp: TextView
    lateinit var card_view: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btn = findViewById(R.id.btn)
        input = findViewById(R.id.input)
        input.hint = "Enter IP Address"
        input.setHintTextColor(resources.getColor(R.color.dark_grey))
        card_view = findViewById(R.id.card_view)
        Toast.makeText(this,"Enter Ip Address",Toast.LENGTH_LONG).show()

        btn.setOnClickListener {

            var str = input.text.toString().trim()
            card_view.isVisible = true

            closeKeyboard(input)
            getDetails(str)

        }
    }

    private fun closeKeyboard(view: View) {
        val st: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        st.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getDetails(str: String) {
        var API_KEY = "at_QlQabVBZrzc8G8i3k0MzGLkQqFdAR"
        var API_URL = "https://geo.ipify.org/api/v1?"
        var url = API_URL + "&apiKey=" + API_KEY + "&ipAddress=" + str

        ip = findViewById(R.id.ip)
        ip.text = str
        country = findViewById(R.id.country)
        city = findViewById(R.id.city)
        isp = findViewById(R.id.isp)

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.i("typed", "${response.getJSONObject("location").get("country")} and $url")
                var jsonObject = response.getJSONObject("location")
                country.text = jsonObject.get("country").toString()
                city.text = jsonObject.get("city").toString()
                isp.text = response.get("isp").toString()
                var lat:Double = jsonObject.get("lat") as Double
                var lng:Double = jsonObject.get("lng") as Double
                var loc = LatLng(lat, lng)

                mMap.addMarker(MarkerOptions().position(loc).title("Marker near ${city.text}"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15f))
            },
            {
                Toast.makeText(this, "Error Occured, Input correct IPv4 or IPv6 address.", Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
        Toast.makeText(this,"For more click on Marker", Toast.LENGTH_LONG).show()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}