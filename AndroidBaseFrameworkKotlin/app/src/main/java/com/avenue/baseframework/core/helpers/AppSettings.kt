package com.avenue.baseframework.core.helpers

import javax.inject.Inject

class AppSettings @Inject constructor() {

    var APP_VERSION_CODE = 0
    var HEADER_COLUMNS = 2
    var SECTION_COLUMNS = 2
    var OFFLINE_PERIOD = 30
    var NETWORK_TIMEOUT = 0
    var SHIFT_DAY_START = 7
    var SHIFT_DAY_END = 19
    var SUPPORT_EMAIL = "support_bsr@avenue-net.com"
    var SUPPORT_CC_EMAIL = EString.EMPTY
    var ZONE_CC_EMAIL = "KCTV;KGD"

    var CLIENT_TIMEOUT =
        30 * 60 * 1000 // 30 min -- get from file app_setting.xml but chua có property

    var DATA_OVER_DAY = 3 // 3 days -- get from file app_setting.xml but chua có property

    var STORED_DAYS = 15 // get from file app_setting.xml
}