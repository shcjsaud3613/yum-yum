//
//  PageCell.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/12.
//

import UIKit


class PageCell: UICollectionViewCell {
    
    @IBOutlet var tableView: UITableView!
    @IBOutlet var collectionView: UICollectionView!
    
    var feedResult: [Feed]?
    var placeResult: [Place]?
    var delegate: pageCellDelegate?
    var placedelegate : pageCellPlaceDelegate?

    override func awakeFromNib() {
        super.awakeFromNib()
        self.backgroundColor = .gray
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: PlaceTableViewCell.reusableIdentifier, bundle: nil), forCellReuseIdentifier: PlaceTableViewCell.reusableIdentifier)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(UINib(nibName: FeedCollectionViewCell.reusableIdentifier, bundle: nil), forCellWithReuseIdentifier:FeedCollectionViewCell.reusableIdentifier)
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

extension PageCell: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("you tab \(indexPath.row)")
        self.placedelegate?.sendToSearchPlaceFeed(itemId: indexPath.row, place:(placeResult?[indexPath.row])!)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return placeResult?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: PlaceTableViewCell.reusableIdentifier, for: indexPath) as! PlaceTableViewCell
        cell.placeNameLabel.text = placeResult?[indexPath.row].name
        cell.placeAddressLabel.text =  placeResult?[indexPath.row].address
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    
}

extension PageCell: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("you tap \(indexPath.item)")
        self.delegate?.sendToSearchFeed(itemId: indexPath.item, feedList: feedResult!)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return feedResult?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FeedCollectionViewCell.reusableIdentifier, for: indexPath) as! FeedCollectionViewCell
        
        cell.reviewLabel.text = feedResult?[indexPath.item].content
        cell.userNameLabel.text = feedResult?[indexPath.item].user?.nickname
        cell.titleLabel.text = feedResult?[indexPath.item].title
        
        if let profileUrl:URL = URL(string: (feedResult?[indexPath.item].user?.profilePath!)!) {
            let imageUrl:URL = (feedResult?[indexPath.item].thumbnailPath)!
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
            let imageUrl:URL = (feedResult?[indexPath.item].thumbnailPath)!
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

extension PageCell: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let CvRect = self.collectionView.frame
    
        return CGSize(width: (CvRect.width/2)-3,
                      height: (CvRect.width/1.25)-3)
    }
    
}

protocol pageCellDelegate{
    func sendToSearchFeed(itemId:Int, feedList:[Feed])
}

protocol pageCellPlaceDelegate {
    func sendToSearchPlaceFeed(itemId: Int, place:Place)
}
