//
//  SearchPlaceDetailCell.swift
//  YumYumy
//
//  Created by 염성훈 on 2021/05/20.
//

import UIKit
import Lottie
import AVFoundation

class SearchPlaceDetailCell: UICollectionViewCell {
    @IBOutlet var userNameLabel: UILabel!
    @IBOutlet var foodNameLabel: UILabel!
    @IBOutlet var placeLabel: UILabel!
    @IBOutlet var reviewLabel: UILabel!
    @IBOutlet var mapIconView: UIImageView!
    @IBOutlet var likeBtn: UIButton!
    @IBOutlet var backBtn: UIButton!
    @IBOutlet var videoView: UIView!
    @IBOutlet var likeCountLabel : UILabel!
    @IBOutlet var scoreOneView : AnimationView!
    var checkLike: Bool = false
    var deleagte: searchPlaceCellDelegate?
    
    let yumyumYellow: ColorSet = .yumyumYellow
    var player: AVPlayer?
    var feedInfo : Feed = Feed()
    var nowUserData = UserDefaults.getLoginedUserInfo()!
    
    let animationview = AnimationView(name: "ic_vomited")
    let animationview2 = AnimationView(name: "ic_confused")
    let animationview3 = AnimationView(name: "ic_neutral")
    let animationview4 = AnimationView(name: "ic_lol")
    let animationview5 = AnimationView(name: "ic_inloveface")
    
    @IBAction func backSearchVC(_ sender: Any) {
        self.deleagte?.backToSearchVC()
    }
    
    @IBAction func likeBtnPress(_ sender: Any) {
        if checkLike == true {
            checkLike = false
            likeBtn.tintColor = .white
            let userId = nowUserData["id"].intValue
            let feedId = feedInfo.id!
            
            WebApiManager.shared.cancleLikeFeed(feedId: feedId, userId: userId){ (result) in
                if result["status"] == "200"{
                    print("싫어요요청을 보냈습니다.")
                }
            } failure: { (error) in
                print(error.localizedDescription)
                print("좋아요 서버 호출 에러")
            }
            likeCountLabel.text = String(Int(likeCountLabel.text!)! - 1)
        } else {
            checkLike = true
            likeBtn.tintColor = yumyumYellow.toColor()
    
            var likeInfo = userLike()
            likeInfo.userId = nowUserData["id"].intValue
            likeInfo.feedId = feedInfo.id!
            
            WebApiManager.shared.postLikeFeed(likeInfo: likeInfo){ (result) in
                if result["status"] == "200"{
                    print("좋아요 포스트 요청을 보냈습니다.")
                }
            } failure: { (error) in
                print(error.localizedDescription)
                print("좋아요 서버 호출 에러")
            }
            likeCountLabel.text = String(Int(likeCountLabel.text!)! + 1)
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setUpAnimation()
    }
    
    public func setUpAnimation() {
        animationview.frame = CGRect(x: 0, y: 0, width: 30, height: 30)
        animationview.contentMode = .scaleAspectFit
    }
    
    public func configureVideo(with feed:Feed) {
        self.feedInfo = feed
        self.player = AVPlayer(url: feed.videoPath!)
        NotificationCenter.default.addObserver(self, selector: #selector(playerItemDidReachEnd), name: NSNotification.Name.AVPlayerItemDidPlayToEndTime, object: self.player?.currentItem)
        let playerView = AVPlayerLayer()
        playerView.player = self.player
        playerView.frame = videoView.bounds
        playerView.videoGravity = .resize
        videoView.layer.addSublayer(playerView)
        self.player!.volume = 0
        self.player?.play()
        loadData(feed: feed)
    }
    
    @objc func playerItemDidReachEnd(notification: NSNotification) {
        self.player?.seek(to: CMTime.zero)
        self.player?.play()
    }
    
    func loadData(feed: Feed) {
        foodNameLabel.text = feed.title
        userNameLabel.text = "@" + (feed.user?.nickname)! as String
        reviewLabel.text = feed.content
        checkLike = feed.isLike!
        if let arr = feed.place?.address.components(separatedBy: " ") {
            print(arr)
            if arr.count > 1 {
                placeLabel.text = String(feed.place?.name ?? " ") +
                    " | \(String(arr[0])) \(String(arr[1]))"
            }
        }
        
        likeCountLabel.text = String(feed.likeCount!)
        
        if checkLike == true {
            likeBtn.tintColor = yumyumYellow.toColor()
        } else {
            likeBtn.tintColor = .white
        }
        
    }
}

protocol searchPlaceCellDelegate {
    func backToSearchVC()
}
