//
//  SearchPlaceDetailVC.swift
//  YumYumy
//
//  Created by 염성훈 on 2021/05/20.
//

import UIKit
import Lottie

class SearchPlaceDetailVC: UIViewController {

    @IBOutlet var collectionView: UICollectionView!
    var itemId : Int?
    var feedList : [Feed] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        collectionView.delegate = self
        collectionView.dataSource = self
        setLayout()
    }

    func setLayout() {
        collectionView.isPagingEnabled = true
        let flowLayout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        flowLayout.minimumLineSpacing = 0
        flowLayout.minimumInteritemSpacing = 0
        collectionView.collectionViewLayout = flowLayout
        collectionView.contentInsetAdjustmentBehavior = .never
        collectionView.scrollToItem(at: IndexPath(item: itemId!, section: 0), at: UICollectionView.ScrollPosition.centeredHorizontally , animated: false)
    }
}

extension SearchPlaceDetailVC: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.feedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let feed = feedList[indexPath.item]
        let yumyumYellow = Color(r: (246/255), g: (215/255), b: (5/255), a: 1)
        let yumyumColorValueProvider = ColorValueProvider(yumyumYellow)
        let keyPath = AnimationKeypath(keypath: "**.Stroke 1.Color")
        let keyPathEyes = AnimationKeypath(keypath: "**.Fill 1.Color")
        let cell : SearchPlaceDetailCell = collectionView.dequeueReusableCell(withReuseIdentifier: "SearchPlaceDetailCell", for: indexPath) as! SearchPlaceDetailCell
        
        cell.deleagte = self
        
        switch feed.score! {
        case 1:
            cell.animationview.play()
            cell.animationview.loopMode = .loop
            cell.scoreOneView.addSubview(cell.animationview)
            cell.animationview.setValueProvider(yumyumColorValueProvider ,keypath: keyPath)
            cell.animationview.setValueProvider(yumyumColorValueProvider ,keypath: keyPathEyes)
            break
        case 2:
            cell.animationview2.play()
            cell.animationview2.loopMode = .loop
            cell.scoreOneView.addSubview(cell.animationview2)
            cell.animationview2.setValueProvider(yumyumColorValueProvider ,keypath: keyPath)
            cell.animationview2.setValueProvider(yumyumColorValueProvider ,keypath: keyPathEyes)
            break
        case 3:
            cell.animationview3.play()
            cell.animationview3.loopMode = .loop
            cell.scoreOneView.addSubview(cell.animationview3)
            cell.animationview3.setValueProvider(yumyumColorValueProvider ,keypath: keyPath)
            cell.animationview3.setValueProvider(yumyumColorValueProvider ,keypath: keyPathEyes)
            break
        case 4:
            cell.animationview4.play()
            cell.animationview4.loopMode = .loop
            cell.scoreOneView.addSubview(cell.animationview4)
            cell.animationview4.setValueProvider(yumyumColorValueProvider ,keypath: keyPath)
            cell.animationview4.setValueProvider(yumyumColorValueProvider ,keypath: keyPathEyes)
            break
        case 5:
            cell.animationview5.play()
            cell.animationview5.loopMode = .loop
            cell.scoreOneView.addSubview(cell.animationview5)
            cell.animationview5.setValueProvider(yumyumColorValueProvider ,keypath: keyPath)
            cell.animationview5.setValueProvider(yumyumColorValueProvider ,keypath: keyPathEyes)
            break
        default:
            print("평점 값이 없습니다.")
            cell.animationview.pause()
            cell.animationview2.pause()
            cell.animationview3.pause()
            cell.animationview4.pause()
            cell.animationview5.pause()
        }
        
        cell.configureVideo(with: feed)
        
        
        
        return cell
    }
}

extension SearchPlaceDetailVC: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cvRect = self.collectionView.frame
        return CGSize(width: cvRect.width, height: cvRect.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
}

extension SearchPlaceDetailVC: searchPlaceCellDelegate {
    func backToSearchVC() {
        self.dismiss(animated: true)
    }
}

extension SearchPlaceDetailVC: SearchPlaceDelegate {
    func SendToPlaceDetailFeed(itemId: Int, feedList: [Feed]) {
        self.itemId = itemId
        self.feedList = feedList
    }
}
