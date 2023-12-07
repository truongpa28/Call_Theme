package com.fansipan.callcolor.calltheme.ui.app.diy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemAvatarBinding
import com.fansipan.callcolor.calltheme.databinding.ItemBackgroundBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone

class BackgroundAdapter: BaseAdapterRecyclerView<CallThemeScreenModel, ItemBackgroundBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemBackgroundBinding {
        return ItemBackgroundBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemBackgroundBinding, item: CallThemeScreenModel, position: Int) {
        val context = binding.root.context

        Glide.with(context)
            .asBitmap()
            .load("https://batterycharger.lutech.vn/app/calltheme/theme3/theme${item.id}/bgtheme${item.id}.png")
            .into(binding.imgThumbnail)
        binding.imgDownload.showOrGone(!SharePreferenceUtils.isBackgroundDownload("${item.category}_${item.id}.png"))
    }
}