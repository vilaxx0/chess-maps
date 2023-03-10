package com.viliusbucinskas.chessmaps.model

import android.location.Address
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import android.os.Parcel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp

data class ChessboardLocationAddressInfo(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var fulladdress: String? = null,
    var create_date: Timestamp? = null,
    var long: Double? = null,
    var lat: Double? = null,
    var author : String? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "fulladdress" to fulladdress,
            "create_date" to create_date,
            "long" to long,
            "lat" to lat,
            "author" to author,

        )
    }
}