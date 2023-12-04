package com.fansipan.callcolor.calltheme.ui.app.diy.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemCategoryBackgroundBinding
import com.fansipan.callcolor.calltheme.databinding.ItemCategoryThemeCallBinding
import com.fansipan.callcolor.calltheme.model.CategoryThemeModel


class CategoryBackgroundAdapter : BaseAdapterRecyclerView<CategoryThemeModel, ItemCategoryBackgroundBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemCategoryBackgroundBinding {
        return ItemCategoryBackgroundBinding.inflate(inflater, parent, false)
    }

    private var positionChoose = 0


    fun getPositionChoose() = positionChoose

    override fun bindData(binding: ItemCategoryBackgroundBinding, item: CategoryThemeModel, position: Int) {
        val context = binding.root.context
        binding.txtTitle.text = item.name
        if (positionChoose == position) {
            binding.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.viewAll.setBackgroundResource(R.drawable.bg_category_background_selected)
        } else {
            binding.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.black_app))
            binding.viewAll.setBackgroundResource(R.drawable.bg_category_background_no_selected)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun choose(position: Int) {
        positionChoose = position
        notifyDataSetChanged()
    }

}