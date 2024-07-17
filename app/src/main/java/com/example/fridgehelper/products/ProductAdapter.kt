package com.example.fridgehelper.products

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgehelper.Functions
import com.example.fridgehelper.R
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter(
    private val arrayList: ArrayList<Product>,
    private val onClickListener: OnProductClickListener,
    private val onLongClickListener: OnProductLongClickListener
) :
    RecyclerView.Adapter<ProductAdapter.ProductAdapterHolder>() {

    interface OnProductClickListener {
        fun onProductClick(product: Product, position: Int)
    }

    interface OnProductLongClickListener {
        fun onLongProductClick(product: Product, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapterHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.products_item, parent, false)
        return ProductAdapterHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductAdapterHolder, position: Int) {
        val item = arrayList[position]
        holder.Title.text = item.title.replace("_", " ")
        if (item.date != GregorianCalendar(2999, 12, 31) && item.date.timeInMillis - GregorianCalendar.getInstance().timeInMillis <= 0){
            holder.Title.setTextColor(Color.RED)
        }else if (item.date != GregorianCalendar(2999, 12, 31) && item.date.timeInMillis - GregorianCalendar.getInstance().timeInMillis <= 259200000){
            holder.Title.setTextColor(Color.MAGENTA)
        }
        if (item.quantity == (0).toFloat()) {
            holder.Quantity.text = "-"
        } else holder.Quantity.text = item.quantity.toString() + item.unit
        if (item.date == GregorianCalendar(2999, 12, 31)) {
            holder.Date.text = "-"
        } else holder.Date.text = Functions.dateOutput(item.date)
        holder.itemView.setOnClickListener {
            onClickListener.onProductClick(item, position)
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onLongProductClick(item, position)
            holder.onLongClick(it)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ProductAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnLongClickListener, OnClickListener {
        var Title: TextView
        var Quantity: TextView
        var Date: TextView

        init {
            this.Title = itemView.findViewById(R.id.textViewProductTitle)
            this.Quantity = itemView.findViewById(R.id.textViewQuantity)
            this.Date = itemView.findViewById(R.id.textViewDate)
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(p0: View?): Boolean {
            return true
        }

        override fun onClick(p0: View?) {

        }


    }
}