package com.fansipan.callcolor.calltheme.model

data class CallThemeScreenModel(
    var id: Int = 1,
    var index: Int = 1,
    var background: String = "1",
    var avatar: String = "",
    var buttonIndex: String = "",
    var category: String = "",
    var isSetReward: Boolean = false
)