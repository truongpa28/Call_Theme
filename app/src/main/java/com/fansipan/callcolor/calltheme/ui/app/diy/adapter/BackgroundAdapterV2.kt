package com.fansipan.callcolor.calltheme.ui.app.diy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemBackgroundV2Binding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone

class BackgroundAdapterV2 : BaseAdapterRecyclerView<CallThemeScreenModel, ItemBackgroundV2Binding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemBackgroundV2Binding {
        return ItemBackgroundV2Binding.inflate(inflater, parent, false)
    }

    override fun bindData(
        binding: ItemBackgroundV2Binding,
        item: CallThemeScreenModel,
        position: Int
    ) {
        val context = binding.root.context

        Glide.with(context)
            .asBitmap()
            .load("https://batterycharger.lutech.vn/app/calltheme/theme3/theme${item.id}/bgtheme${item.id}.png")
            .into(binding.imgThumbnail)
        binding.imgDownload.showOrGone(!SharePreferenceUtils.isThemeDownload("${item.category}_${item.id}.png"))
    }
}