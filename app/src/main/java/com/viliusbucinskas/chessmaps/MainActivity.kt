package com.viliusbucinskas.chessmaps

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adevinta.leku.*
import com.viliusbucinskas.chessmaps.R
import com.viliusbucinskas.chessmaps.adapter.ProductAdapter
import com.viliusbucinskas.chessmaps.model.ChessboardLocationAddressInfo
import com.viliusbucinskas.chessmaps.viewmodel.ProductViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    private lateinit var rvList: RecyclerView

    private lateinit var productAdapter: ProductAdapter
    private lateinit var list: ArrayList<ChessboardLocationAddressInfo>

    private var selected: ChessboardLocationAddressInfo = ChessboardLocationAddressInfo()

    private val productViewModel: ProductViewModel by viewModels()


    private val nav = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.mapsFragment -> {
                val intent = Intent(this@MainActivity, MapActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.clockFragment -> {
                val intent = Intent(this@MainActivity, ClockActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.mainActivity -> {
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        bottomNavigation.setOnNavigationItemSelectedListener(nav)
        val auth = findViewById<Button>(R.id.authorization)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            auth.setText("Sign Out")
            auth.setBackgroundColor(Color.parseColor("#3700B3"))
        } else {
            auth.setText("Log In")
            auth.setBackgroundColor(Color.parseColor("#3700B3"))
        }
        auth.setOnClickListener{
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                FirebaseAuth.getInstance().signOut()
                finish()
                overridePendingTransition(0, 0)
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        initElement()
        initViewModel()
    }

    private fun initElement() {
        rvList = findViewById(R.id.rvList)
        list = ArrayList()
        // Get list
        productViewModel.getList()
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
        list.addAll(it)
        list.reverse()

        productAdapter = ProductAdapter(list, this)

        rvList.adapter = productAdapter
        rvList.layoutManager = LinearLayoutManager(baseContext)

        productAdapter.notifyDataSetChanged()
    }

    override fun onClick(item: ChessboardLocationAddressInfo, position: Int) {
        selected = item

        val dialogView = layoutInflater.inflate(R.layout.activity_pop_up_window_directions, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()

        val buttonGo = dialogView.findViewById<Button>(R.id.directions)

        val buttonCancel = dialogView.findViewById<Button>(R.id.cancel)

        buttonGo.setOnClickListener{
            customDialog.dismiss()
            val gmmIntentUri = Uri.parse("google.navigation:q="+selected.lat+","+selected.long + "&mode=b")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        buttonCancel.setOnClickListener{
            customDialog.dismiss()
        }


    }

    override fun onDelete(item: ChessboardLocationAddressInfo, position: Int) {
        productViewModel.delete(item.id!!)
    }
}


