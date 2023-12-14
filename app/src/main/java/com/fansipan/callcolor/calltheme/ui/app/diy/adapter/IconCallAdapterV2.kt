package com.fansipan.callcolor.calltheme.ui.app.diy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemAvatarBinding
import com.fansipan.callcolor.calltheme.databinding.ItemIconCallBinding
import com.fansipan.callcolor.calltheme.databinding.ItemIconCallV2Binding
import com.fansipan.callcolor.calltheme.model.IconCallModel

class IconCallAdapterV2: BaseAdapterRecyclerView<IconCallModel, ItemIconCallV2Binding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemIconCallV2Binding {
        return ItemIconCallV2Binding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemIconCallV2Binding, item: IconCallModel, position: Int) {
        val context = binding.root.context
        binding.imgIcon1.setImageResource(item.icon1)
        binding.imgIcon2.setImageResource(item.icon2)
    }
}