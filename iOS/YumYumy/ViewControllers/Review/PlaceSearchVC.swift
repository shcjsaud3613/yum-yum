//
//  PlaceSearchVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/06.
//

import UIKit
import CoreLocation

protocol PlaceDelegate {
    func setPlace(_ viewController: PlaceSearchVC, place: Place)
}

class PlaceSearchVC: UIViewController, CLLocationManagerDelegate {
    
    static func instance() -> PlaceSearchVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "PlaceSearchVC") as! PlaceSearchVC
        return vc
    }
    
    var results: [Place] = []
    var delegate: PlaceDelegate?
    var locationManager = CLLocationManager()
    
    var latitude : Double?
    var longitude  : Double?
    var distance : [Double] = []

    @IBOutlet var resultTableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.resultTableView.delegate = self
        self.resultTableView.dataSource = self
    
//        self.navigationItem.hidesBackButton = true
//        self.navigationItem.leftBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: self, action: #selector(leftBarButtonAction))
//
        
        //locationManager 인스턴스 생성 및 델리게이트 생성
        locationManager = CLLocationManager()
        locationManager.delegate = self
        
        //포그라운드 상태에서 위치 추적 권한 요청
        locationManager.requestWhenInUseAuthorization()
        
        //배터리에 맞게 권장되는 최적의 정확도
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        
        //위치업데이트
        locationManager.startUpdatingLocation()
        
        //위도 경도 가져오기
        let coor = locationManager.location?.coordinate
        latitude = coor?.latitude
        longitude = coor?.longitude
        setSearchBar()
    }
    
    @objc func leftBarButtonAction() {
        self.navigationController?.popViewController(animated: true)
    }
    
    func setSearchBar() {
        let searchBar = UISearchBar()
        self.navigationItem.titleView = searchBar
        searchBar.placeholder = "장소검색"
        searchBar.delegate = self
        
        self.navigationItem.titleView = searchBar
    }

}


extension PlaceSearchVC: UISearchBarDelegate {
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.resultTableView.reloadData()
        let searchKey = searchBar.text!
        WebApiManager.shared.searchPlace(searchKey: searchKey, logitudeX: longitude!, latitudeY: latitude!) { (result) in
            dump(result["documents"])
            self.distance = []
            self.results = result["documents"].arrayValue.compactMap({Place(json: $0)})
            self.results.map({(place:Place) -> Place in
                var userLocation = CLLocation(latitude: self.latitude!, longitude: self.longitude!)
                var secondLocation = CLLocation(latitude: place.locationY, longitude: place.locationX)
                let distancePlace:CLLocationDistance = userLocation.distance(from: secondLocation)
                self.distance.append(distancePlace)
                return place
            })
            self.resultTableView.reloadData()
        } failure: { (error) in
            print(#function, "error: \(error)")
        }
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchBar.text = ""
    }
    
}

extension PlaceSearchVC: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PlaceSearchCell") as! PlaceSearchCell
        
        cell.nameLabel.text = self.results[indexPath.row].name
        cell.addressLabel.text = self.results[indexPath.row].address
        cell.reviewLabel.text = self.results[indexPath.row].phone
        cell.distanceLabel.text = String(ceil(self.distance[indexPath.row])) + "m"
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = MapVC.instance(place: results[indexPath.row])
        vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
}

extension PlaceSearchVC: PlaceToSearchVCDelegate {
    func setPlaceToSearchVC(_ controller: MapVC, place: Place) {
        delegate?.setPlace(self, place: place)
    }
}
