
package com.viliusbucinskas.chessmaps.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Place(
    var name: String,
    var address: String,
    var long: Double,
    var lat: Double,
) : ClusterItem {
    override fun getPosition(): LatLng =
        LatLng(lat,long)

    override fun getTitle(): String =
        name

    override fun getSnippet(): String =
        address
}