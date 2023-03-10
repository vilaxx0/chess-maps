package com.viliusbucinskas.chessmaps.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.viliusbucinskas.chessmaps.R
import com.viliusbucinskas.chessmaps.model.ChessboardLocationAddressInfo

class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val nameText: TextView = itemView.findViewById(R.id.nameText)
    private val fulladdressText: TextView = itemView.findViewById(R.id.addressText)
    private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
    private val createDateText: TextView = itemView.findViewById(R.id.createDateText)
    private val descriptionL: LinearLayout = itemView.findViewById(R.id.desLayout)
    fun bindItem(chessboardLocationAddressInfo: ChessboardLocationAddressInfo) {
        itemView.apply {
            nameText.text = chessboardLocationAddressInfo.title
            fulladdressText.text = chessboardLocationAddressInfo.fulladdress
            if(chessboardLocationAddressInfo.description ==  "" )
            {
                descriptionL.visibility = View.GONE
            }else{
                descriptionText.text = chessboardLocationAddressInfo.description
            }

            createDateText.text = chessboardLocationAddressInfo.create_date!!.toDate().toString()
        }
    }
}