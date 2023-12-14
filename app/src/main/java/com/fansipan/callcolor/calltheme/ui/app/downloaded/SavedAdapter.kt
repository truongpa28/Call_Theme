package com.fansipan.callcolor.calltheme.ui.app.downloaded

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemCollectionBinding
import com.fansipan.callcolor.calltheme.model.ItemSavedModel
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.show


class SavedAdapter(
    val context: Context
) : BaseAdapterRecyclerView<ItemSavedModel, ItemCollectionBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemCollectionBinding {
        return ItemCollectionBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemCollectionBinding, item: ItemSavedModel, position: Int) {
        Glide.with(context)
            //.apply(requestOption)
            .load(item.background)
            .into(binding.imgThumbnail)
        binding.imgDownload.gone()
        binding.imgDelete.show()
        val posButton = item.buttonIndex.toInt() - 1
        binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)

        try {
            val posAvt = item.avatar.toInt()
            binding.imgAvatar.setImageResource(AvatarUtils.listAvatar[posAvt])
        } catch (e: Exception) {
            Glide.with(context)
                .load(item.avatar)
                .into(binding.imgAvatar)
        }
        binding.imgIconCall1.showOrGone(!item.isBackground)
        binding.imgIconCall2.showOrGone(!item.isBackground)
        binding.imgAvatar.showOrGone(!item.isBackground)

        binding.imgDelete.clickSafe {
            setOnClickDelete?.invoke(dataList.getOrNull(position), position)
        }
    }

    fun setOnClickDelete(listener: ((item: ItemSavedModel?, position: Int) -> Unit)? = null) {
        setOnClickDelete = listener
    }

    private var setOnClickDelete: ((item: ItemSavedModel?, position: Int) -> Unit)? = null

}