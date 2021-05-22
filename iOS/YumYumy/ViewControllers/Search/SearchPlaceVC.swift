//
//  SearchPlaceVC.swift
//  YumYumy
//
//  Created by 염성훈 on 2021/05/20.
//

import UIKit

class SearchPlaceVC: UIViewController {
    
    @IBOutlet var collectionView: UICollectionView!
    
    var placeFeedList : [Feed] = []
    var place: Place?
    var delegate : SearchPlaceDelegate?

    override func viewDidLoad() {
        super.viewDidLoad()
        let nTitle = UILabel(frame:CGRect(x:0, y:0, width: 200, height: 40))
        nTitle.numberOfLines = 1
        nTitle.textAlignment = .center
        nTitle.font = UIFont.systemFont(ofSize: 25) // 폰트크기
        nTitle.text = place?.name
        self.navigationItem.titleView = nTitle
        self.navigationController?.navigationBar.tintColor = .black
        
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(UINib(nibName: SearchPlaceCell.reusableIdentifier, bundle: nil), forCellWithReuseIdentifier:SearchPlaceCell.reusableIdentifier)
        
        setupCollectionView()
    }
    func setupCollectionView(){
        let flowLayout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        flowLayout.minimumLineSpacing = 10
        flowLayout.minimumInteritemSpacing = 0
        flowLayout.sectionInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        flowLayout.estimatedItemSize = .zero
        collectionView.collectionViewLayout = flowLayout
        collectionView.isPagingEnabled = true
        collectionView.reloadData()
    }
}

extension SearchPlaceVC : UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("you tap \(indexPath.item)")
        let searchPlaceDetailVC = UIStoryboard(name: "Search", bundle: nil).instantiateViewController(identifier: "SearchPlaceDetailVC") as! SearchPlaceDetailVC
        
        searchPlaceDetailVC.feedList = placeFeedList
        searchPlaceDetailVC.itemId = indexPath.item
        searchPlaceDetailVC.modalPresentationStyle = .fullScreen
        self.present(searchPlaceDetailVC, animated: true)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.placeFeedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell : SearchPlaceCell = collectionView.dequeueReusableCell(withReuseIdentifier: SearchPlaceCell.reusableIdentifier, for: indexPath) as! SearchPlaceCell
        
        cell.reviewLabel.text = placeFeedList[indexPath.item].content
        cell.userNameLabel.text = placeFeedList[indexPath.item].user?.nickname
        cell.titleLabel.text = placeFeedList[indexPath.item].title
        
        if let profileUrl:URL = URL(string: (placeFeedList[indexPath.item].user?.profilePath!)!) {
            let imageUrl:URL = (placeFeedList[indexPath.item].thumbnailPath)!
            var image: UIImage?
            var profileImg : UIImage?
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: imageUrl)
                let profileData = try? Data(contentsOf: profileUrl)
                DispatchQueue.main.async {
                    let beforeimage = UIImage(data: data!)
                    image = beforeimage?.fixedOrientation().imageRotatedByDegrees(degrees: 90.0)
                    profileImg = UIImage(data: profileData!)
                    cell.thumbnailView.image = image
                    cell.userProfileView.image = profileImg
                }
            }
        } else {
            let imageUrl:URL = (placeFeedList[indexPath.item].thumbnailPath)!
            var image: UIImage?
            var profileImg : UIImage?
            profileImg = UIImage(named: "ic_profile")
            cell.userProfileView.image = profileImg
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: imageUrl)
                DispatchQueue.main.async {
                    let beforeimage = UIImage(data: data!)
                    image = beforeimage?.fixedOrientation().imageRotatedByDegrees(degrees: 90.0)
                    cell.thumbnailView.image = image
                }
            }
        }
        
        
        return cell
    }
}

extension SearchPlaceVC : UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let CvRect = self.collectionView.frame
    
        return CGSize(width: (CvRect.width/2)-3,
                      height: (CvRect.width/1.25)-3)
    }
}

protocol SearchPlaceDelegate{
    func SendToPlaceDetailFeed(itemId: Int, feedList:[Feed])
}
