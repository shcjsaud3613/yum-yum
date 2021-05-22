//
//  MyFeedVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/11.
//

import UIKit
import Lottie

class MyFeedVC: UIViewController {
    
    static func instance() -> MyFeedVC{
        let vc = UIStoryboard.init(name: "MyPage", bundle: nil).instantiateViewController(withIdentifier: "MyFeedVC") as! MyFeedVC
        return vc
    }
    
    @IBOutlet var collectionView: UICollectionView!
    
    var myFeedList: [Feed] = []
//    var myLikeFeedList: [Feed] = []
    let userData = UserDefaults.getLoginedUserInfo()!
    var itemId: Int?
    var feedLikeOrNot:Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setLayout()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.navigationBar.isHidden = true
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

extension MyFeedVC: UICollectionViewDataSource  {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.myFeedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let feedReverse = Array(self.myFeedList.reversed())
        let feed = feedReverse[indexPath.item]
        let yumyumYellow = Color(r: (246/255), g: (215/255), b: (5/255), a: 1)
        let yumyumColorValueProvider = ColorValueProvider(yumyumYellow)
        let keyPath = AnimationKeypath(keypath: "**.Stroke 1.Color")
        let keyPathEyes = AnimationKeypath(keypath: "**.Fill 1.Color")

        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "mycell", for: indexPath) as! MyFeedCollectionViewCell

        let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(backMyPage))
        
        cell.delegate = self
        
        if feedLikeOrNot {
            cell.threeDotBtn.isHidden = true
        } else {
            cell.threeDotBtn.isHidden = false
        }
        
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

        cell.backMyPageBtn.isUserInteractionEnabled = true
        cell.backMyPageBtn.tag = indexPath.item
        cell.backMyPageBtn.addGestureRecognizer(tapGestureRecognizer)

        cell.configureVideo(with: feed)
        
        return cell
    }
    
    @objc func backMyPage() {
        self.dismiss(animated: true)
    }
}

extension MyFeedVC: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cvRect = self.collectionView.frame
        return CGSize(width: cvRect.width, height: cvRect.height)
    }
}

extension MyFeedVC: myFeedCellDelegate {
    
    func deleteFeedToMove(feedId: Int) {
        defaultalert("정말 삭제하시겠어요?"){ () in
            WebApiManager.shared.deleteMyFeed(feedId: feedId){
                (result) in
                if result["status"] == "200" {
                    print("피드가 삭제 되었습니다.")
                } else {
                    print("피드 삭제 오류")
                }
            } faliure: { (error) in
                print(error)
            }
            self.dismiss(animated: true)
        }failure: {
            return
        }
    }
    
    func updateFeedToMove(feed : Feed) {
        defaultalert("피드를 수정하시겠습니까?"){ () in
            let reviewVC = UIStoryboard(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "ReviewVC") as! ReviewVC
            reviewVC.updatefeed = feed
            self.navigationController?.pushViewController(reviewVC, animated: true)
            return
        } failure: {
            print("취소했어요!")
            
            return
        }
    }
    
    
}
