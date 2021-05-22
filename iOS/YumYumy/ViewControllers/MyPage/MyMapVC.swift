//
//  MyMapVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/17.
//

import UIKit

class MyMapVC: UIViewController, MTMapViewDelegate {
    
    static func instance() -> MyMapVC {
        let vc = UIStoryboard.init(name: "MyPage", bundle: nil).instantiateViewController(withIdentifier: "MyMapVC") as! MyMapVC
        return vc
    }
    
    @IBOutlet var myMapView:MTMapView!
    
    var poitItems = [Any]()
    
    var feedList : [Feed]?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = "리뷰 지도"
        markOnMap()
        myMapView.delegate = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        myMapView.showCurrentLocationMarker = true
        myMapView.addPOIItems(poitItems)
        // 모든 지도 마커 기준으로 레벨 및 위치 정렬
        myMapView.fitAreaToShowAllPOIItems()
    }
    
    //마커 저장해서 넣기
    func markOnMap() {
        let mapfeedList = feedList?.map({ (feed:Feed) -> Feed in
            var poitItem = MTMapPOIItem()
            poitItem.itemName = feed.place?.name
            poitItem.showDisclosureButtonOnCalloutBalloon = true
            poitItem.markerType = .redPin
            poitItem.mapPoint = MTMapPoint(geoCoord: MTMapPointGeo(latitude: (feed.place?.locationY)!, longitude: (feed.place?.locationX)!))
            poitItems.append(poitItem)
            return feed
        })
    }

    func mapView(_ mapView: MTMapView!, selectedPOIItem poiItem: MTMapPOIItem!) -> Bool {
        myMapView.setZoomLevel(3, animated: true)
        myMapView.setMapCenter(poiItem.mapPoint, animated: true)
        return true
    }

    
}
