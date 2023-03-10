package com.viliusbucinskas.chessmaps.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.viliusbucinskas.chessmaps.BitmapHelper
import com.viliusbucinskas.chessmaps.R
import com.viliusbucinskas.chessmaps.model.Place
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/**
 * A custom cluster renderer for Place objects.
 */
class PlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Place>
) : DefaultClusterRenderer<Place>(context, map, clusterManager) {

    /**
     * The icon to use for each cluster item
     */
    private val icon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context,
            R.color.colorPrimary
        )
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.ic_chess_board_svgrepo_com,
            color
        )
    }

    /**
     * Method called before the cluster item (i.e. the marker) is rendered. This is where marker
     * options should be set
     */
    override fun onBeforeClusterItemRendered(item: Place, markerOptions: MarkerOptions) {
        markerOptions.title(item.name)
            .position(LatLng(item.lat,item.long ))
            .icon(icon)
    }

    /**
     * Method called right after the cluster item (i.e. the marker) is rendered. This is where
     * properties for the Marker object should be set.
     */
    override fun onClusterItemRendered(clusterItem: Place, marker: Marker) {
        marker.tag = clusterItem
    }
}