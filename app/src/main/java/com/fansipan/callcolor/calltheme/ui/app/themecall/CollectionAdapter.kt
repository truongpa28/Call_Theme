package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemCollectionBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils


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
            //.apply(requestOption)
            .load("https://batterycharger.lutech.vn/app/calltheme/theme3/theme${item.id}/bgtheme${item.id}.png")
            .into(binding.imgThumbnail)
        binding.imgDownload.showOrGone(!SharePreferenceUtils.isThemeDownload("${item.category}_${item.id}.png"))
        val posButton = item.buttonIndex.toInt() - 1
        binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)
        binding.imgAvatar.setImageResource(AvatarUtils.listAvatar[item.avatar.toInt()])
    }

}