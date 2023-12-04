package com.fansipan.callcolor.calltheme.ui.app.diy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemAvatarV2Binding

class AvatarAdapterV2 : BaseAdapterRecyclerView<Int, ItemAvatarV2Binding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAvatarV2Binding {
        return ItemAvatarV2Binding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemAvatarV2Binding, item: Int, position: Int) {
        binding.imgThumbnail.setImageResource(item)
    }
}