package com.mobiai.base.basecode.storage

import com.mobiai.app.App

object SharedPreferenceUtils {
    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    private const val LANGUAGE = "LANGUAGE"
    private const val EMAIL = "EMAIL"

    var emailLogin: String?
        get() = App.instanceSharePreference.getValue(EMAIL, null)
        set(value) = App.instanceSharePreference.setValue(EMAIL, value)

    var firstOpenApp: Boolean
        get() = App.instanceSharePreference.getValueBool(FIRST_OPEN_APP, true)
        set(value) = App.instanceSharePreference.setValueBool(FIRST_OPEN_APP, value)

    var languageCode: String?
        get() = App.instanceSharePreference.getValue(LANGUAGE, null)
        set(value) = App.instanceSharePreference.setValue(LANGUAGE, value)

}