package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemCategoryThemeCallBinding
import com.fansipan.callcolor.calltheme.model.CategoryThemeModel
import com.fansipan.callcolor.calltheme.utils.ex.setOnTouchScale


class CategoryThemeAdapterV2(
    var context: Context,
    private var listData: List<CategoryThemeModel>,
    private val listener: CategoryThemeListener
) : RecyclerView.Adapter<CategoryThemeAdapterV2.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var viewAll: ConstraintLayout = view.findViewById(R.id.viewAll)
        var imgBackground: ImageView = view.findViewById(R.id.imgBackground)
        var txtName: TextView = view.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category_theme_call, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = listData[position]
        viewHolder.txtName.text = item.name
        viewHolder.imgBackground.setImageResource(item.image)
        viewHolder.viewAll.setOnTouchScale({
            listener.clickCategoryTheme(position, item)
        }, 0.9f, true)
    }
    override fun getItemCount() = listData.size
}
interface CategoryThemeListener {
    fun clickCategoryTheme(position: Int, languageModel: CategoryThemeModel)
}