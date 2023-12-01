package com.fansipan.callcolor.calltheme.ui.app.downloaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentSavedBinding
import com.fansipan.callcolor.calltheme.utils.data.DataSaved

class SavedFragment(val position: Int) : BaseFragment() {


    private lateinit var binding: FragmentSavedBinding

    private val adapter by lazy {
        SavedAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        val data = if (position == 0) {
            DataSaved.getAllDownloaded(requireContext())
        } else {
            DataSaved.getAllCreated(requireContext())
        }

        adapter.setDataList(data)
    }

    private fun initView() {
        binding.rcyCollection.adapter = adapter
    }

    private fun initListener() {

    }
}