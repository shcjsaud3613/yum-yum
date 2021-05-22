package com.omnyom.yumyum.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    // 현재는 마땅한 처리가 없어 로직 처리가 없지만, 필요 시에 사용!!
}