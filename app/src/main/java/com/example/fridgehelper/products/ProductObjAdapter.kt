package com.example.fridgehelper.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgehelper.Functions
import com.example.fridgehelper.R
import java.util.*
import kotlin.collections.ArrayList

class ProductObjAdapter(
    private val arrayList: ArrayList<Functions.ProductData>,
    private val onClickListener: OnProductObjClickListener
) :
    RecyclerView.Adapter<ProductObjAdapter.ProductObjHolder>() {


    interface OnProductObjClickListener {
        fun onProductClick(product: Functions.ProductData, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductObjHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_obj_item, parent, false)
        return ProductObjHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ProductObjHolder, position: Int) {
        val item = arrayList[position]
        if (item.date == (-1).toLong()){
            holder.Date.text = "-"
        }
        else holder.Date.text = Functions.dateOutput(Functions.longToCalendar(item.date))
        if (item.quantity == (0).toFloat()) {
            holder.Quantity.text = "-"
        } else holder.Quantity.text = item.quantity.toString()
        holder.itemView.setOnClickListener { onClickListener.onProductClick(item, position) }
    }

    class ProductObjHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Date: TextView
        var Quantity: TextView

        init {
            this.Date = itemView.findViewById(R.id.itemDateObj)
            this.Quantity = itemView.findViewById(R.id.itemQuantityObj)
        }

    }
}