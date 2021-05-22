package com.omnyom.yumyum.helper

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.omnyom.yumyum.model.login.LoginResponse
import com.omnyom.yumyum.model.login.LoginData
import java.lang.reflect.Type

class LoginJsonAdapter : JsonDeserializer<LoginResponse> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): LoginResponse {
        val gson = Gson()
        lateinit var response : LoginResponse
        val jsonObject = json!!.asJsonObject

        if (jsonObject.get("status").asString == "200") { // status code가 200일경우 body가 jsonArray형태로 반환
            response = LoginResponse(
                    jsonObject.get("status").asString,
                    null,
                    null,
                    jsonObject.get("message").asString,
                    gson.fromJson(jsonObject.get("data").asJsonObject.toString(), object : TypeToken<LoginData>(){}.type)
            )
        } else { // status code가 http status Error code 일 경우, body가 String형식으로 반환 된다.
            response = LoginResponse(
                    jsonObject.get("status").asString,
                    jsonObject.get("error").asString,
                    jsonObject.get("code").asString,
                    jsonObject.get("message").asString,
                    null
            )
        }

        return response
    }
}