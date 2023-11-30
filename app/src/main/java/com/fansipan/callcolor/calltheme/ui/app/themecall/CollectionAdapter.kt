package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.databinding.ItemRingToneV2Binding
import com.fansipan.callcolor.calltheme.model.RingtoneModel
import com.fansipan.callcolor.calltheme.utils.showOrGone
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemCategoryThemeCallBinding
import com.fansipan.callcolor.calltheme.databinding.ItemCollectionBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.model.CategoryThemeModel


class CollectionAdapter : BaseAdapterRecyclerView<CallThemeScreenModel, ItemCollectionBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemCollectionBinding {
        return ItemCollectionBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemCollectionBinding, item: CallThemeScreenModel, position: Int) {
        val context = binding.root.context
        Glide.with(context)
            .asBitmap()
            //.apply(requestOption)
            .load("https://batterycharger.lutech.vn/app/calltheme/background/background${item.index}.webp")
            .into(binding.imgThumbnail)
    }

}