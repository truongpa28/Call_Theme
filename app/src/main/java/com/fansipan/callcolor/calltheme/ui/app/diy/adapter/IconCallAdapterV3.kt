package com.fansipan.callcolor.calltheme.ui.app.diy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemAvatarBinding
import com.fansipan.callcolor.calltheme.databinding.ItemIconCallBinding
import com.fansipan.callcolor.calltheme.model.IconCallModel
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone

class IconCallAdapterV3: BaseAdapterRecyclerView<IconCallModel, ItemIconCallBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemIconCallBinding {
        return ItemIconCallBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemIconCallBinding, item: IconCallModel, position: Int) {
        val context = binding.root.context
        binding.imgIcon1.setImageResource(item.icon1)
        binding.imgIcon2.setImageResource(item.icon2)
        binding.llViewIcon.showOrGone(position != dataList.size - 1)
        binding.imgSeeMore.showOrGone(position == dataList.size - 1)
    }
}