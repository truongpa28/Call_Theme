package com.fansipan.callcolor.calltheme.utils.data

import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.model.CategoryThemeModel

object ThemeCallUtils {

    val listCategory = listOf<CategoryThemeModel>(
        CategoryThemeModel("Flower", "Flower", R.drawable.bg_category_flower),
        CategoryThemeModel("Cat", "Cat", R.drawable.bg_category_cat),
        CategoryThemeModel("Halloween", "Halloween", R.drawable.bg_category_halloween),
        CategoryThemeModel("3D", "3D", R.drawable.bg_category_3d),
        CategoryThemeModel("Neon", "Neon", R.drawable.bg_category_noen),
        CategoryThemeModel("Marvel", "Marvel", R.drawable.bg_category_marvel),
        CategoryThemeModel("CarMoto", "CarMoto", R.drawable.bg_category_car_moto),
        CategoryThemeModel("Nature", "Nature", R.drawable.bg_category_nature),
        CategoryThemeModel("Anime", "Anime", R.drawable.bg_category_anime),
        CategoryThemeModel("K-Pop", "K-Pop", R.drawable.bg_category_kpop),
        CategoryThemeModel("Korea", "Korea", R.drawable.bg_category_korea),
        CategoryThemeModel("Vintage", "Vintage", R.drawable.bg_category_vintage)
    )

    val listCategoryOfBackground = listOf<CategoryThemeModel>(
        CategoryThemeModel("All", "All", R.drawable.bg_category_flower),
        CategoryThemeModel("Flower", "Flower", R.drawable.bg_category_flower),
        CategoryThemeModel("Cat", "Cat", R.drawable.bg_category_cat),
        CategoryThemeModel("Halloween", "Halloween", R.drawable.bg_category_halloween),
        CategoryThemeModel("3D", "3D", R.drawable.bg_category_3d),
        CategoryThemeModel("Neon", "Neon", R.drawable.bg_category_noen),
        CategoryThemeModel("Marvel", "Marvel", R.drawable.bg_category_marvel),
        CategoryThemeModel("CarMoto", "CarMoto", R.drawable.bg_category_car_moto),
        CategoryThemeModel("Nature", "Nature", R.drawable.bg_category_nature),
        CategoryThemeModel("Anime", "Anime", R.drawable.bg_category_anime),
        CategoryThemeModel("K-Pop", "K-Pop", R.drawable.bg_category_kpop),
        CategoryThemeModel("Korea", "Korea", R.drawable.bg_category_korea),
        CategoryThemeModel("Vintage", "Vintage", R.drawable.bg_category_vintage)
    )


    fun getThemeOfCategory(position : Int) : ArrayList<CallThemeScreenModel> {
        val key = listCategory[position].key
        val data = ArrayList<CallThemeScreenModel>()
        data.addAll(DataUtils.listDataCallThemScreen.filter {
            it.category == key
        })
        return data
    }
}