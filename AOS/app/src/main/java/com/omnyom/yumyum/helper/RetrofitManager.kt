package com.omnyom.yumyum.helper

import com.omnyom.yumyum.AiRetrofitBuilder
import com.omnyom.yumyum.KakaoRetrofitBuilder
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.interfaces.AiService
import com.omnyom.yumyum.interfaces.KakaoApiService
import com.omnyom.yumyum.interfaces.RetrofitService

class RetrofitManager {
    companion object {
        val kakaoApiService: KakaoApiService = KakaoRetrofitBuilder.buildService(KakaoApiService::class.java)
        val retrofitService: RetrofitService = RetrofitBuilder.buildService(RetrofitService::class.java)
        val aiService: AiService = AiRetrofitBuilder.buildService(AiService::class.java)
    }
}