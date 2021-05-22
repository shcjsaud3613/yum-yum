//
//  PeopleVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/13.
//

import UIKit
import SwiftyJSON

class PeopleVC: UIViewController {
    let cellIdentifire : String  = "peopleCell"
    var feedList : [Feed] = []
    var peopleFeedList: [Feed] = []
    var peopleLikeList: [Feed] = []
    var userId: Int?
    var username : String?
    var userData: User?
    var likeFeedCheck = false
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var ProfileImgView: UIImageView!
    @IBOutlet weak var IntroduceLabel : UILabel!
    @IBOutlet var userNameLabel: UILabel!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.rightBarButtonItem = nil
        let flowLayout : UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        self.collectionView.collectionViewLayout = flowLayout
        imageMakeRouded(imageview: ProfileImgView)
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.initTitle()
        loadData()
        presentUserData()
    }
    
    @IBAction func changeLikeSegment(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            self.likeFeedCheck = false
            self.feedList = peopleFeedList
            self.collectionView.reloadData()
        } else if sender.selectedSegmentIndex == 1 {
            self.likeFeedCheck = true
            self.feedList = peopleLikeList
            self.collectionView.reloadData()
        }
    }
    
    func initTitle() {
        let nTitle = UILabel(frame:CGRect(x:0, y:0, width: 200, height: 40))
        nTitle.numberOfLines = 1
        nTitle.textAlignment = .center
        nTitle.font = UIFont.systemFont(ofSize: 20) // 폰트크기
//        nTitle.text = "유저페이지"
        self.navigationItem.titleView = nTitle
    }
    
    func loadData() {
        WebApiManager.shared.getMyFeedList(userId: userId!, authorId: userId!) {(result) in
            if result["status"] == "200" {
                let results = result["data"]
                let list = results.arrayValue.compactMap({Feed(feedJson: $0)})
                self.peopleFeedList = list.filter { $0.isCompleted == true }
                self.feedList = self.peopleFeedList
                self.collectionView.reloadData()
            }
            
        } failure: { (error) in
            print("유저 피드리스트 에러 : \(error)")
        }
        
        WebApiManager.shared.getMyLikeFeed(userId: userId!) {
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.peopleLikeList = results.arrayValue.compactMap({Feed(feedJson: $0)})
                self.collectionView.reloadData()
            }
            
        } faliure: { (error) in
            print(error.localizedDescription)
        }
    }
    
    func presentUserData() {
        self.IntroduceLabel.text = userData?.introduction!
        
        guard let nickname = userData?.nickname else {
            return
        }
        self.userNameLabel.text = "@\(nickname)"
        userNameLabel.font = .boldSystemFont(ofSize: 20)
        
        if let url = URL(string: (userData?.profilePath)!) {
            var image : UIImage?
            
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: url)
                DispatchQueue.main.async {
                    image = UIImage(data:data!)
                    self.ProfileImgView.image = image
                }
            }
        } else {
            var image: UIImage?
            image = UIImage(named: "ic_profile")
            self.ProfileImgView.image = image
        }
        
    }
}

extension PeopleVC: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("You tepped me \(indexPath.item)")
        
        let peopleFeedVC = UIStoryboard(name: "Home", bundle: nil).instantiateViewController(identifier: "PeopleFeedVC") as! PeopleFeedVC
        
        peopleFeedVC.peopleFeedList = feedList
        peopleFeedVC.itemId = indexPath.item
        peopleFeedVC.modalPresentationStyle = .fullScreen
        self.present(peopleFeedVC, animated: true)
    }
}

extension PeopleVC: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.feedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let reversePeopleFeedList = Array(self.feedList.reversed())
        let peolplefeed = reversePeopleFeedList[indexPath.item]
        let imageurl:URL = peolplefeed.thumbnailPath!
        
        let cell: PeopleCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifire, for: indexPath) as! PeopleCell
        
        var image: UIImage?
        
        DispatchQueue.global().async {
            let data = try? Data(contentsOf: imageurl)
            DispatchQueue.main.async {
                let beforeimage = UIImage(data: data!)
                image = beforeimage?.fixedOrientation().imageRotatedByDegrees(degrees: 90.0)
                
                cell.foodImgView.image = image
            }
        }
        
        return cell
    }
}

extension PeopleVC: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let CvRect = collectionView.frame
        
        return CGSize(width: (CvRect.width/3)-3,
                      height: (CvRect.width/2)-3)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 2
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
}
