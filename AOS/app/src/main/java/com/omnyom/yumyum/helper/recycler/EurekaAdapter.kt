package com.omnyom.yumyum.helper.recycler

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.messengerapp.Notifications.EurekaData
import com.example.messengerapp.Notifications.Sender
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ListItemEurekaBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.helper.RotateTransformation
import com.omnyom.yumyum.helper.recycler.EurekaAdapter.EurekaViewHolder
import com.omnyom.yumyum.interfaces.APISerivce
import com.omnyom.yumyum.model.eureka.Chat
import com.omnyom.yumyum.model.eureka.Client
import com.omnyom.yumyum.model.eureka.EurekaResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.userInfo.UserData
import com.omnyom.yumyum.model.userInfo.UserResponse
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.skyfishjy.library.RippleBackground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EurekaAdapter(val context: Context) : BaseRecyclerAdapter<EurekaViewHolder, FeedData>() {
    val db = Firebase.firestore
    var positions : List<Double>? = null
    var geoHash : String? = null

    private val _matchingDocs = MutableLiveData<List<Chat>>().apply {
        value = arrayListOf()
    }
    val matchingDocs : LiveData<List<Chat>> = _matchingDocs

    private val _authorData = MutableLiveData<UserData>().apply {

    }
    val authorData : LiveData<UserData> = _authorData



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EurekaViewHolder {
        val itemBinding = ListItemEurekaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EurekaViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EurekaViewHolder, position: Int) {
        holder.bind(items[position])
        if (items[position].isCompleted) {
            Glide.with(context)
                .load(items[position].thumbnailPath)
                .transform(RotateTransformation(context, 90f))
                .override(280, 280)
                .into(holder.thumbnail)
            holder.thumbnail.setOnClickListener {
                shareFeed(items[position])
            }
        }
    }

    class EurekaViewHolder(private val itemBinding: ListItemEurekaBinding) : BaseViewHolder(itemBinding.root) {
        val thumbnail = itemBinding.ivEurekaFeedThumbnail

    }

    fun shareFeed(feed: FeedData) {
        AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog)).apply {
            setTitle("피드 올리기")
            setMessage("정말 올리시겠습니까?")
            setPositiveButton("공유") { _, _ ->
                sendMessage(userId.toString().toInt(), feed)
                Toast.makeText(context, "피드가 공유되었습니다." , Toast.LENGTH_SHORT).show()
                val eurekaLayout = View.inflate(context, R.layout.fragment_eureka, null)
                val rippleBackground : RippleBackground = eurekaLayout.findViewById(R.id.eureka_circle_wave)
                rippleBackground.startRippleAnimation()
                Handler().postDelayed({
                    rippleBackground.stopRippleAnimation()
                }, 3000)
            }
            setNegativeButton("취소") { _, _ -> }
            show()
        }
    }

    fun sendMessage(userId: Int, feed: FeedData) {
        var Call = RetrofitManager.retrofitService.getUserData(userId.toString().toLong())
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
                    messageHashMap["message"] = ""
                    messageHashMap["geohash"] = geoHash
                    messageHashMap["feedId"] = feed.id
                    messageHashMap["thumbnailPath"] = feed.thumbnailPath
                    messageHashMap["profilePath"] = response.body()!!.data.profilePath
                    messageHashMap["nickname"] = response.body()!!.data.nickname


                    val locationRef: DocumentReference = db.collection("Chats").document("$userId")
                    locationRef.set(messageHashMap).addOnCompleteListener {
                        getGeoHashes()
                    }
                    val docs = matchingDocs.value!!
                    for (doc in docs) {
                        if (doc.userId != userId) {
                            sendNoti(doc.userId.toString(), "피드가 공유되었습니다.")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t
            }
        })


    }

    fun getLocation() {
        positions = KakaoMapUtils.getMyPosition(context)
    }

    fun getGeoHashes() {
        getLocation()
        val center = GeoLocation(positions!![1], positions!![0])
        val radiusInM = 5000.toDouble()

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

    fun getMessageAuthor(authorId:Long) {
        var Call = RetrofitManager.retrofitService.getUserData(authorId)
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

}
