package com.example.fridgehelper.recipes

import android.content.Context
import android.content.res.Resources
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgehelper.Functions
import com.example.fridgehelper.R
import java.util.GregorianCalendar


class RecipesAdapter(
    val arrayList: ArrayList<Functions.Recipe>,
    val onClickListener: onRecipeClickListener,
    val onLongClickListener: onRecipeLongClickListener
) :
    RecyclerView.Adapter<RecipesAdapter.RecipesAdapterHolder>() {

    class RecipesAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnLongClickListener,
        View.OnClickListener {
        var name: TextView
        var products: TextView

        init {
            this.name = itemView.findViewById(R.id.textViewRecipeName)
            this.products = itemView.findViewById(R.id.textViewRecipeProducts)
        }

        override fun onLongClick(p0: View?): Boolean {
            return true
        }

        override fun onClick(p0: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipes_item, parent, false)
        return RecipesAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    interface onRecipeClickListener {
        fun onRecipeClick(recipe: Functions.Recipe, position: Int)
    }

    interface onRecipeLongClickListener {
        fun onRecipeLongClick(recipe: Functions.Recipe, position: Int)
    }

    override fun onBindViewHolder(holder: RecipesAdapterHolder, position: Int) {
        val resources = holder.itemView.resources
        val item = arrayList[position]
        holder.name.text = item.name.replace("_", " ").uppercase()
        var productText = ""
        for (i in item.products) {
            var color = "grey"
            val productsDate =
                Functions.findMinDate(Functions.getProductDB(holder.itemView.context, i.name))
            if (productsDate < GregorianCalendar.getInstance().timeInMillis && productsDate != (-1).toLong()) {
                color = "red"
            } else if (productsDate - 3 * 24 * 60 * 60 * 1000 < GregorianCalendar.getInstance().timeInMillis && productsDate != (-1).toLong()) {
                color = "magenta"
            }
            productText += "<font color='${color}'>${i.name} ${i.quantity}${
                resources.getStringArray(
                    R.array.units
                )[i.unit]
            } </font>"
        }
        holder.itemView.setOnClickListener {
            onClickListener.onRecipeClick(item, position)
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onRecipeLongClick(item, position)
            holder.onLongClick(it)
        }
        holder.products.setText(Html.fromHtml(productText))
    }

}