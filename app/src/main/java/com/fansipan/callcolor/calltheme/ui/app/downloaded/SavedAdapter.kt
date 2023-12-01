package com.fansipan.callcolor.calltheme.ui.app.downloaded

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemCollectionBinding
import com.fansipan.callcolor.calltheme.model.ItemSavedModel
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.gone


class SavedAdapter : BaseAdapterRecyclerView<ItemSavedModel, ItemCollectionBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemCollectionBinding {
        return ItemCollectionBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemCollectionBinding, item: ItemSavedModel, position: Int) {
        val context = binding.root.context
        Glide.with(context)
            .asBitmap()
            //.apply(requestOption)
            .load(item.background)
            .into(binding.imgThumbnail)
        binding.imgDownload.gone()
        val posButton = item.buttonIndex.toInt() - 1
        binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)
        binding.imgAvatar.setImageResource(AvatarUtils.listAvatar[item.avatar.toInt()])
        binding.imgIconCall1.showOrGone(!item.isBackground)
        binding.imgIconCall2.showOrGone(!item.isBackground)
        binding.imgAvatar.showOrGone(!item.isBackground)
    }

}