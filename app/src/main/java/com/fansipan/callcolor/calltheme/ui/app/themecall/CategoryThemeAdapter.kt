package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.databinding.ItemRingToneV2Binding
import com.fansipan.callcolor.calltheme.model.RingtoneModel
import com.fansipan.callcolor.calltheme.utils.showOrGone
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemCategoryThemeCallBinding
import com.fansipan.callcolor.calltheme.model.CategoryThemeModel


class CategoryThemeAdapter : BaseAdapterRecyclerView<CategoryThemeModel, ItemCategoryThemeCallBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemCategoryThemeCallBinding {
        return ItemCategoryThemeCallBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemCategoryThemeCallBinding, item: CategoryThemeModel, position: Int) {
        binding.txtName.text = item.name
        binding.imgBackground.setImageResource(item.image)

    }

}