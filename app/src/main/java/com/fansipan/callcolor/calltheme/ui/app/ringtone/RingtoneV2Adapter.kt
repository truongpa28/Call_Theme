package com.fansipan.callcolor.calltheme.ui.app.ringtone

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.databinding.ItemRingToneV2Binding
import com.fansipan.callcolor.calltheme.model.RingtoneModel
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.base.BaseAdapterRecyclerView


class RingtoneV2Adapter : BaseAdapterRecyclerView<RingtoneModel, ItemRingToneV2Binding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemRingToneV2Binding {
        return ItemRingToneV2Binding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemRingToneV2Binding, item: RingtoneModel, position: Int) {
        binding.tvRingToneName.text = item.nameSound
        binding.tvRingToneName.isSelected = true
        binding.tvRingToneTime.text = item.duration
        binding.imgStateSelect.showOrGone(item.isSelected)
        /*binding.cbSelected.showOrGone(position == SharePrefUtils.getSpeedFlash())*/
    }

    fun chooseRingTone(position: Int) {
        if (position !in dataList.indices) return
        val indexSelect = dataList.indexOfFirst { it.isSelected }
        if (indexSelect == position) {
            return
        }
        dataList[position].isSelected = true
        notifyItemChanged(position)
        if (indexSelect != -1) {
            dataList[indexSelect].isSelected = false
            notifyItemChanged(indexSelect)
        }
    }

    fun getDataSoundChoose(): Int {
        val indexSelect = dataList.indexOfFirst { it.isSelected }
        return dataList[indexSelect].sound
    }

}