package com.fansipan.callcolor.calltheme.model

data class CallThemeScreenModel(
    var id: Int = 1,
    var index: Int = 1,
    var background: String = "1",
    var avatar: String = "1",
    var buttonIndex: String = "1",
    var category: String = "",
    var isSetReward: Boolean = false
)