package com.viliusbucinskas.chessmaps.viewmodel

import android.location.Address
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.viliusbucinskas.chessmaps.model.ChessboardLocationAddressInfo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductViewModel: ViewModel() {

    private var db = Firebase.firestore
    private val products = "chesslocations"

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<ChessboardLocationAddressInfo>> by lazy {
        MutableLiveData<List<ChessboardLocationAddressInfo>>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(chessboardLocationAddressInfo: ChessboardLocationAddressInfo) {
        val docRef = db.collection(products)
        docRef.add(chessboardLocationAddressInfo.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }

    fun update(chessboardLocationAddressInfo: ChessboardLocationAddressInfo) {
        val docRef = db.collection(products)
        docRef.document(chessboardLocationAddressInfo.id!!).update(chessboardLocationAddressInfo.toMap()).addOnSuccessListener {
            updateLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(products)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }

    fun getList() {
        val docRef = db.collection(products)
        docRef.get().addOnSuccessListener {
            val products = ArrayList<ChessboardLocationAddressInfo>()
            for (item in it.documents) {
                val product = ChessboardLocationAddressInfo()
                product.id = item.id
                product.title = item.data!!["title"] as String?
                product.fulladdress = item.data!!["fulladdress"] as String?
                product.description = item.data!!["description"] as String?
                product.create_date = item.data!!["create_date"] as Timestamp?
                product.long = item.data!!["long"] as Double?
                product.lat = item.data!!["lat"] as Double?
                product.author = item.data!!["author"] as String?
                products.add(product)
            }

            getListLiveData.postValue(products)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getListLiveData.postValue(null)
        }
    }
}