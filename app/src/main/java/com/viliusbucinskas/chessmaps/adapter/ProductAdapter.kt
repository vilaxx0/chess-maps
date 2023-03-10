package com.viliusbucinskas.chessmaps.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.viliusbucinskas.chessmaps.R
import com.viliusbucinskas.chessmaps.adapter.viewholder.ProductViewHolder
import com.viliusbucinskas.chessmaps.model.ChessboardLocationAddressInfo
import com.google.firebase.auth.FirebaseAuth

class ProductAdapter(
    var list: List<ChessboardLocationAddressInfo>,
    var onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)


        return ProductViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = list[position]
        holder.bindItem(item)
        val r = holder.itemView.findViewById<LinearLayout>(R.id.deleteLayout)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            if (user.email == item.author)
            { r.visibility = View.VISIBLE}
            else
                r.visibility = View.GONE

        } else {
            r.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(item, position)
        }
        holder.itemView.findViewById<Button>(R.id.delete).setOnClickListener {
            onItemClickListener.onDelete(item, position)
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onClick(item: ChessboardLocationAddressInfo, position: Int)
        fun onDelete(item: ChessboardLocationAddressInfo, position: Int)
    }
}