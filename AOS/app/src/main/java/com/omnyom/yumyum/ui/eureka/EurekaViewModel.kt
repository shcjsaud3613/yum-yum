package com.omnyom.yumyum.ui.eureka

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.messengerapp.Notifications.EurekaData
import com.example.messengerapp.Notifications.Sender
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.RemoteMessage
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.interfaces.APISerivce
import com.omnyom.yumyum.model.eureka.Chat
import com.omnyom.yumyum.model.eureka.Client
import com.omnyom.yumyum.model.eureka.EurekaResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.feed.SingleFeedResponse
import com.omnyom.yumyum.model.place.GetPlaceDataResponse
import com.omnyom.yumyum.model.place.PlaceData
import com.omnyom.yumyum.model.userInfo.UserData
import com.omnyom.yumyum.model.userInfo.UserResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EurekaViewModel(application: Application) : BaseViewModel(application) {
    val db = Firebase.firestore
    var positions : List<Double>? = null
    var geoHash : String? = null

    private val _feedData = MutableLiveData<FeedData>().apply {
    }
    val feedData : LiveData<FeedData> = _feedData

    private val _myFeedData = MutableLiveData<List<FeedData>>().apply {
    }
    val myFeedData : LiveData<List<FeedData>> = _myFeedData

    private val _authorData = MutableLiveData<UserData>().apply {

    }
    val authorData : LiveData<UserData> = _authorData

    private val _myData = MutableLiveData<UserData>().apply {

    }
    val myData : LiveData<UserData> = _myData

    private val _matchingDocs = MutableLiveData<List<Chat>>().apply {
        value = arrayListOf()
    }
    val matchingDocs : LiveData<List<Chat>> = _matchingDocs

    fun getLocation() {
        positions = KakaoMapUtils.getMyPosition(getApplication())
    }

    fun sendMessage(message: String, userId: Int) {
        var Call = retrofitService.getUserData(userId.toString().toLong())
        Call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    getLocation()
                    val lng = positions!![0]
                    val lat = positions!![1]
                    val geoLocation = GeoLocation(lat, lng)
                    geoHash = GeoFireUtils.getGeoHashForLocation(geoLocation)
                    val messageHashMap = HashMap<String, Any?>()
                    messageHashMap["userId"] = userId
                    messageHashMap["lat"] = lat
                    messageHashMap["lng"] = lng
                    messageHashMap["message"] = message
                    messageHashMap["geohash"] = geoHash
                    messageHashMap["feedId"] = -1
                    messageHashMap["thumbnailPath"] = ""
                    messageHashMap["profilePath"] = response.body()!!.data.profilePath
                    messageHashMap["nickname"] = response.body()!!.data.nickname

                    val locationRef: DocumentReference = db.collection("Chats").document("$userId")
                    locationRef.set(messageHashMap).addOnCompleteListener {
                        getGeoHashes()
                    }
                    val docs = matchingDocs.value!!
                    for (doc in docs) {
                        if (doc.userId != userId) {
                            sendNoti(doc.userId.toString(), message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t
            }
        })
    }

    fun getGeoHashes() {
        getLocation()
        val center = GeoLocation(positions!![1], positions!![0])
        val radiusInM = ((PreferencesManager.eurekaDistance+1) * 1000).toDouble()

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q: Query = db.collection("Chats")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                val matchingDocs: MutableList<Chat> = ArrayList()
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap!!.documents) {
                        val lat = doc.getDouble("lat")!!
                        val lng = doc.getDouble("lng")!!
                        val docLocation = GeoLocation(lat, lng)
                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                        if (distanceInM <= radiusInM) {
                            val inner = doc.getData() as Map<String, Any>
                            if (inner["feedId"] == null) {
                                val chat = Chat(inner["userId"].toString().toInt(), inner["geohash"] as String, inner["lat"] as Double, inner["lng"] as Double, inner["message"] as String, "".toUri(), -1, inner["profilePath"].toString().toUri(), inner["nickname"] as String)
                                matchingDocs.add(chat)
                            } else {
                                val chat = Chat(inner["userId"].toString().toInt(), inner["geohash"] as String, inner["lat"] as Double, inner["lng"] as Double, inner["message"] as String, inner["thumbnailPath"].toString().toUri(), inner["feedId"].toString().toInt(), inner["profilePath"].toString().toUri(), inner["nickname"] as String)
                                matchingDocs.add(chat)
                            }
                        }
                    }
                }
                _matchingDocs.postValue(matchingDocs)
            }

    }

    private fun sendNoti(receiverId: String, message:String) {
        val apiService = Client.Client.getClient("https://fcm.googleapis.com/")!!.create(APISerivce::class.java)
        val eurekaData = EurekaData(message, "YUM? YUM!")
        val sender = Sender(eurekaData, "/topics/$receiverId")
        apiService.sendNotification(sender)
                .enqueue(object : Callback<EurekaResponse> {
                    override fun onResponse(call: Call<EurekaResponse>, response: Response<EurekaResponse>) {
                    }
                    override fun onFailure(call: Call<EurekaResponse>, t: Throwable) {
                    }
                })
    }

    fun getFeedData(feedId : Long, userId: Long) {
        d("author", "오고1")
        retrofitService.getSingleFeeds(feedId, userId).enqueue(object : Callback<SingleFeedResponse> {
            override fun onResponse(call: Call<SingleFeedResponse>, response: Response<SingleFeedResponse>) {
                _feedData.postValue(response.body()!!.data)
            }

            override fun onFailure(call: Call<SingleFeedResponse>, t: Throwable) {
                d("author", "$t")
            }
        })
    }


    fun getMessageAuthor(authorId:Long) {
        var Call = retrofitService.getUserData(authorId)
        Call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _authorData.postValue(response.body()?.data!!)
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t
            }
        })
    }

    fun getMyFeed(userId: Long) {
        retrofitService.getUserFeeds(userId, userId).enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                _myFeedData.postValue(response.body()!!.data)
            }
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
            }
        })
    }

    fun getMyData(userId: Long) {
        retrofitService.getUserData(userId).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _myData.postValue(response.body()!!.data)
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            }
        })
    }




}
