package com.avenue.baseframework.core.helpers

import com.avenue.baseframework.R
import com.avenue.baseframework.core.BaseApplication
import javax.inject.Inject

class AppSize @Inject constructor() {

    var screenWidth = 0
        get() = field - paddingSize() * 2
    var screenHeight = 0

    fun multipleRowWidth(): Int {
        return BaseApplication.getContext().resources
            .getDimensionPixelSize(R.dimen.row_control_width)
    }

    fun buttonSize(): Int {
        return BaseApplication.getContext().resources.getDimensionPixelSize(R.dimen.button_size)
    }

    fun paddingSize(): Int {
        return BaseApplication.getContext().resources
            .getDimensionPixelSize(R.dimen.normal_margin)
    }
}