package com.omnyom.yumyum.model.maps


// 일단은 안쓰는 중
class KeywordSearchRequest (private val query: String, private val x : Double, private val y: Double, private val page: Int, private val size : Int) {
    fun get() : HashMap<String, Any> {
        return hashMapOf(
            "query" to query,
            "x" to x,
            "y" to y,
            "page" to page,
            "size" to size
        )
    }
}
