package com.omnyom.yumyum.ui.markermap

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityMarkerMapBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.model.feed.Place
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MarkerMapActivity : BaseBindingActivity<ActivityMarkerMapBinding>(R.layout.activity_marker_map) {

    private lateinit var placeData : ArrayList<Place>

    override fun extraSetupBinding() {
    }

    override fun setup() {
        requirePermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                KakaoMapUtils.PERM_FINE_LOCATION
        )
        placeData = intent.getSerializableExtra("placeData") as ArrayList<Place>
    }

    override fun setupViews() {
        var mapViewInput = MapView(this).apply {
            currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
            setShowCurrentLocationMarker(true)
            val myPosition = KakaoMapUtils.getMyPosition(this@MarkerMapActivity)
            val mapPoint = MapPoint.mapPointWithGeoCoord(myPosition[1], myPosition[0])
            setMapCenterPointAndZoomLevel(mapPoint, 0, true)
        }
        binding.mapView.addView(mapViewInput)

        placeData.mapIndexed { idx, place ->
            val mapPoint = MapPoint.mapPointWithGeoCoord(place.locationY, place.locationX)
            mapViewInput.addPOIItem(getMarker(mapPoint, place.name, idx))
        }

        binding.btnMyLocation.setOnClickListener {
            val myPosition = KakaoMapUtils.getMyPosition(this)
            val uNowPosition = MapPoint.mapPointWithGeoCoord(myPosition[1], myPosition[0])
            mapViewInput.setMapCenterPoint(uNowPosition, true)
        }

        // bar 백그라운드 색상 변경
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // 타이틀 색상 변경
        val text: Spannable = SpannableString(getString(R.string.title_my_feed))
        text.setSpan(ForegroundColorSpan(getColor(R.color.mainText)), 0, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        supportActionBar?.setTitle(text)
    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            KakaoMapUtils.PERM_FINE_LOCATION -> {
                Toast.makeText(baseContext, "위치 권한을 승인해야 지도를 사용할 수 있습니다!?", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getMarker(mapPoint: MapPoint, placeName: String, idx: Int): MapPOIItem {
        return MapPOIItem().apply {
            itemName = placeName
            tag = idx
            this.mapPoint = mapPoint
            markerType = MapPOIItem.MarkerType.YellowPin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
    }
}