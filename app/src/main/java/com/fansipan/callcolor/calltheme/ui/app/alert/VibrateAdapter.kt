package com.fansipan.callcolor.calltheme.ui.app.alert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView
import com.fansipan.callcolor.calltheme.databinding.ItemAvatarBinding
import com.fansipan.callcolor.calltheme.databinding.ItemFlashBinding
import com.fansipan.callcolor.calltheme.model.SpeedFlashModel
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.ex.setTint
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone

class VibrateAdapter: BaseAdapterRecyclerView<Int, ItemFlashBinding>() {


    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemFlashBinding {
        return ItemFlashBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemFlashBinding, item: Int, position: Int) {
        val context = binding.root.context
        val choose = SharePreferenceUtils.getVibrateRingtone() == position

        binding.imgStateSelect.showOrGone(choose)
        binding.viewBorder.showOrGone(choose)

        if (choose) {
            binding.imgRingTone.setColorFilter(ContextCompat.getColor(context, R.color.main_color))
        } else {
            binding.imgRingTone.setColorFilter(ContextCompat.getColor(context, R.color.color_type_flash))
        }

        binding.imgRingTone.setImageResource(item)
    }
}