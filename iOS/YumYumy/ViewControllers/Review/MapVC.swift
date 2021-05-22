//
//  MapVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/04.
//

import UIKit



protocol PlaceToSearchVCDelegate: class {
    func setPlaceToSearchVC(_ controller: MapVC, place: Place)
}

class MapVC: UIViewController, MTMapViewDelegate {

    static func instance(place: Place) -> MapVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "MapVC") as! MapVC
        vc.place = place
        vc.navigationItem.title = place.name

        
        vc.poiItem = MTMapPOIItem()
        vc.poiItem!.itemName = "Marker"
        vc.poiItem!.showDisclosureButtonOnCalloutBalloon = true
        vc.poiItem!.markerSelectedType = .redPin
        vc.poiItem!.mapPoint = MTMapPoint(geoCoord: MTMapPointGeo(latitude: place.locationY, longitude:  place.locationX))

        return vc
    }
    
    var place: Place?
    var poiItem: MTMapPOIItem?
    
    var delegate: PlaceToSearchVCDelegate?
    
    @IBOutlet var map: MTMapView!
    
    @IBOutlet var placeNameLabel: UILabel!
    
    @IBAction func didTapNextButton(_ sender: Any) {
        delegate?.setPlaceToSearchVC(self, place: place!)
        
        let viewControllers: [UIViewController] = self.navigationController!.viewControllers as [UIViewController]
        self.navigationController?.popToViewController(viewControllers[viewControllers.count - 3], animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        map.showCurrentLocationMarker = true
        
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        map.add(poiItem)
        map.fitAreaToShowAllPOIItems()
        
        placeNameLabel.text = place?.name
        
    }
    
}
