package com.avenue.baseframework.core.models

data class MenuModel(
    val menuId: Int,
    val webscreen: String,
    val webscreenName: String,
    val iconMenu: Int,
    val parentID: Int,
    val level: Int
) {
    var listChild: MutableList<MenuModel> = mutableListOf()
}
