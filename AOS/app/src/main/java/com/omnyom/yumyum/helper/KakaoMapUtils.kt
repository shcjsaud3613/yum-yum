package com.omnyom.yumyum.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

class KakaoMapUtils {
    companion object {
        val PERMISSIONS_REQUEST_CODE = 109
        var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION)
        val PERM_FINE_LOCATION = 110 // 외부 저장소 권한 처리
        val PERM_COARSE_LOCATION = 111 // 카메라 권한처리

        var locationManager: LocationManager? = null

        fun initLocationManager(activity: Activity) {
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        fun initLocationFragmentManager(context: Context?) {
            locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        fun getMyPosition(context: Context): List<Double> {
            var latitude = ""
            var longitude = ""
            val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                try {
                    val userNowLocation: Location? = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    latitude = userNowLocation?.latitude.toString()
                    longitude = userNowLocation?.longitude.toString()
                } catch (e: java.lang.NullPointerException) {

                }
            }

            return listOf( longitude.toDouble(), latitude.toDouble() )
        }
    }
}