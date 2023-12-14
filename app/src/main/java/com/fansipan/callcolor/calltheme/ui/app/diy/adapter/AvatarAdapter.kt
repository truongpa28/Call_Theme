package com.fansipan.callcolor.calltheme.ui.app.diy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemAvatarBinding

class AvatarAdapter: BaseAdapterRecyclerView<Int, ItemAvatarBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAvatarBinding {
        return ItemAvatarBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemAvatarBinding, item: Int, position: Int) {
        val context = binding.root.context
        binding.imgThumbnail.setImageResource(item)
    }
}