//
//  ReviewVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import UIKit
import SwiftyJSON
import Lottie
import NVActivityIndicatorView

enum Score: Int {
    case one = 1
    case two = 2
    case three = 3
    case four = 4
    case five = 5
}

class ReviewVC: UIViewController{
    
    static func instance(videoUrl: URL) -> ReviewVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "ReviewVC") as! ReviewVC
        vc.videoUrl = videoUrl
        return vc
    }
    
    let indicator = NVActivityIndicatorView(frame: CGRect(x: 185, y: 450 , width: 50, height: 50), type: .ballRotateChase, color: .black, padding: 0)
    
    var videoUrl: URL!
    var feed: Feed = Feed()
    var score: Score = .five
    var place: Place?
    var tempfeed: Feed?
    var updatefeed: Feed?
    var foodAiData : [String] = []
    
    @IBOutlet var collectionView: UICollectionView!
    
    @IBOutlet var scoreOneView: AnimationView!
    @IBOutlet var scoreTwoView: AnimationView!
    @IBOutlet var scoreThreeView: AnimationView!
    @IBOutlet var scoreFourView: AnimationView!
    @IBOutlet var scoreFiveView: AnimationView!
    
    let animationview = AnimationView(name: "ic_vomited")
    let animationview2 = AnimationView(name: "ic_confused")
    let animationview3 = AnimationView(name: "ic_neutral")
    let animationview4 = AnimationView(name: "ic_lol")
    let animationview5 = AnimationView(name: "ic_inloveface")
    
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var contentTextField: UITextField!
    @IBOutlet var locationTextField: UITextField!
    @IBOutlet weak var emojiLabel: UILabel!
    
    @IBAction func didTabLocationButton(_ sender: Any) {
        let vc = PlaceSearchVC.instance()
        vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let flowLayout : UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        flowLayout.scrollDirection = .horizontal
        self.collectionView.collectionViewLayout = flowLayout
        self.view.addSubview(indicator)
        setUpAnimation()
        setAnimationGesture()
        setLayout()
        titleTextField.delegate = self
        contentTextField.delegate = self
        titleTextField.addTarget(self, action: #selector(textFieldDidChange(_sender:)), for: .editingChanged)
        contentTextField.addTarget(self, action: #selector(textFieldDidChange(_sender:)), for: .editingChanged)
        locationTextField.addTarget(self, action: #selector(textFieldDidChange(_sender:)), for: .valueChanged)
        
        // 피드가 임시저장 피드일떄랑 완성본 수정일떄랑 두개로 나눈다.
        if tempfeed != nil {
            loadTempData()
        } else if updatefeed != nil {
            loadUpdateData()
        }
        
        //MARK: - TODO 나중에 호출하는거 확인할것~
        if videoUrl != nil {
            loadAIVideo()
            indicator.startAnimating()
        }
    }
    
    private func loadAIVideo() {
        WebApiManager.shared.postAiVdieo(mediaUrl: self.videoUrl){ (result) in
            if result["success"] == true {
                self.foodAiData = (result["predictions"].arrayObject! as? [String])!
                self.collectionView.reloadData()
                self.indicator.stopAnimating()
            }
        } failure: { (error) in
            print(error.localizedDescription)
        }
    }
    func setUpAnimation() {
        
        animationview.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview.contentMode = .scaleAspectFit
        scoreOneView.addSubview(animationview)
    
        animationview2.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview2.contentMode = .scaleAspectFit
        scoreTwoView.addSubview(animationview2)
        
        animationview3.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview3.contentMode = .scaleAspectFit
        scoreThreeView.addSubview(animationview3)
        
        animationview4.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview4.contentMode = .scaleAspectFit
        scoreFourView.addSubview(animationview4)

        animationview5.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview5.contentMode = .scaleAspectFit
        scoreFiveView.addSubview(animationview5)

    }
    
    
    func setAnimationGesture() {
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture2 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture3 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture4 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture5 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        animationview.tag = 1
        animationview2.tag = 2
        animationview3.tag = 3
        animationview4.tag = 4
        animationview5.tag = 5
        animationview.addGestureRecognizer(tapGesture)
        animationview2.addGestureRecognizer(tapGesture2)
        animationview3.addGestureRecognizer(tapGesture3)
        animationview4.addGestureRecognizer(tapGesture4)
        animationview5.addGestureRecognizer(tapGesture5)
        
    }
    
    @objc func handleTap(sender: UITapGestureRecognizer) {
        let yumyumYellow = Color(r: (246/255), g: (215/255), b: (5/255), a: 1)
        let yellowColorValueProvider = ColorValueProvider(yumyumYellow)
        let keyPath = AnimationKeypath(keypath: "**.Stroke 1.Color")
        let keyPathEyes = AnimationKeypath(keypath: "**.Fill 1.Color")
        let black = Color(r: (0/255), g: (0/255), b: (0/255), a: 1)
        let blackColorValueProvider = ColorValueProvider(black)
        
        
        switch sender.view?.tag {
        case 1:
            animationview.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview.setValueProvider(yellowColorValueProvider, keypath: keyPath)
            animationview.setValueProvider(yellowColorValueProvider, keypath: keyPathEyes)
            animationview.play()
            animationview.loopMode = .loop
            animationview2.stop()
            animationview3.stop()
            animationview4.stop()
            animationview5.stop()

            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            setEmoji(value: .one)
        case 2:
            animationview2.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview2.setValueProvider(yellowColorValueProvider, keypath: keyPath)
            animationview2.setValueProvider(yellowColorValueProvider, keypath: keyPathEyes)
            animationview.stop()
            animationview3.stop()
            animationview4.stop()
            animationview5.stop()
            animationview2.play()
            animationview2.loopMode = .loop
            
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            setEmoji(value: .two)
            
            
        case 3:
            animationview3.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview3.setValueProvider(yellowColorValueProvider, keypath: keyPath)
            animationview3.setValueProvider(yellowColorValueProvider, keypath: keyPathEyes)
            animationview3.loopMode = .loop
            animationview2.stop()
            animationview.stop()
            animationview4.stop()
            animationview5.stop()
            animationview3.play()
            
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            setEmoji(value: .three)
        case 4:
            animationview4.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview4.setValueProvider(yellowColorValueProvider, keypath: keyPath)
            animationview4.setValueProvider(yellowColorValueProvider, keypath: keyPathEyes)
            animationview4.loopMode = .loop
            animationview4.play()
            animationview2.stop()
            animationview3.stop()
            animationview.stop()
            animationview5.stop()
            
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview5.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            
            setEmoji(value: .four)
        case 5:
            animationview5.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview5.setValueProvider(yellowColorValueProvider, keypath: keyPath)
            animationview5.setValueProvider(yellowColorValueProvider, keypath: keyPathEyes)
            animationview5.play()
            animationview5.loopMode = .loop
            animationview2.stop()
            animationview3.stop()
            animationview4.stop()
            animationview.stop()
            
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview2.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview3.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPath)
            animationview4.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
            
            setEmoji(value: .five)
        default:
            break
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        checkNowReviewData()
    }
    
    func checkNowReviewData() {
        if titleTextField.text == "" || contentTextField.text == "" || locationTextField.text == "" || emojiLabel.text == "Label"{
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "임시저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
        } else {
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
            self.feed.isCompleted = true
        }
    }
    
    //임시피드를 받아오는 부분
    private func loadTempData() {
        feed = tempfeed!
        feed.userId = tempfeed?.user?.id
        setEmoji(value: Score(rawValue: feed.score!)!)
        titleTextField.text = feed.title
        contentTextField.text = feed.content
        locationTextField.text = feed.place?.name
    }
    
    private func loadUpdateData() {
        feed = updatefeed!
        feed.userId = updatefeed?.user?.id
        setEmoji(value: Score(rawValue: feed.score!)!)
        titleTextField.text = feed.title
        contentTextField.text = feed.content
        locationTextField.text = feed.place?.name
    }
    
    @objc func textFieldDidChange(_sender: UITextField) {
        if titleTextField.text == "" || contentTextField.text == "" || locationTextField.text == "" || emojiLabel.text == "Label"{
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "임시저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
        } else {
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
            self.feed.isCompleted = true
        }
    }
    
    func setLayout() {
        // navigationBar
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.title = "리뷰쓰기"
        self.navigationItem.leftBarButtonItem = UIBarButtonItem(title: "취소", style: .plain, target: self, action: #selector(leftBarButtonAction))
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "임시저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
        
    }
    
    @objc
    func leftBarButtonAction() {
        self.dismiss(animated: true, completion: nil)
        self.navigationController?.popViewController(animated: true)
    }
    
    @objc
    func rightBarButtonAction() {
        if self.videoUrl != nil {
            WebApiManager.shared.createMediaPath(mediaUrl: self.videoUrl) { (result) in
                print("json: \(result)")
                self.feed.thumbnailPath = URL(string: result["data"]["thumbnailPath"].stringValue)
                self.feed.videoPath = URL(string: result["data"]["videoPath"].stringValue)
                let user = UserDefaults.getLoginedUserInfo()
                self.feed.userId = user!["id"].intValue
                self.feed.content = self.contentTextField.text
                self.feed.title = self.titleTextField.text
                self.feed.score = self.score.rawValue
                self.feed.place = self.place
                
                WebApiManager.shared.createFeed(feed: self.feed) { (result) in
                    print("feed생성: \(result)")
                    if result["status"] == "200" {
                        self.dismiss(animated: true, completion: nil)
                    }

                } failure: { (error) in
                    print("feed생성에러: \(error)")
                }
            } failure: { (error) in
                print("error: \(error)")
            }
        // 임시저장으로 들어왔을떄 실행되는 로직
        } else if self.videoUrl == nil && tempfeed != nil {
            print("임시저장시 들아오는 피드 생성부분")
            WebApiManager.shared.updateFeed(feed: self.feed) { (result) in
                print("feed 임시저장 수정!: \(result)")
                if result["status"] == "200" {
                    self.alertConfirm("수정 완료")
                    self.navigationController?.popViewController(animated: true)
                }
            } failure: { (error) in
                print("feed생성에러: \(error)")
            }
        //
        } else if self.videoUrl == nil && updatefeed != nil {
            print("업데이트 피드 수정해요!!")
            self.feed.content = self.contentTextField.text
            self.feed.title = self.titleTextField.text
            self.feed.score = self.score.rawValue
            dump(feed)
            WebApiManager.shared.updateFeed(feed: self.feed) { (result) in
                print("feed 업데이트 피드 수정!: \(result)")
                if result["status"] == "200" {
                    self.alertConfirm("수정 완료")
                    let myFeedVC = UIStoryboard(name: "MyPage", bundle: nil).instantiateViewController(withIdentifier: "MyFeedVC") as! MyFeedVC
                    self.navigationController?.popViewController(animated: true)
                }
            } failure: { (error) in
                print("feed생성에러: \(error)")
            }
        }
    }

    func setEmoji(value: Score) {
        self.score = value
        switch value {
        case .one:
            emojiLabel.text = "우웩"
        case .two:
            emojiLabel.text = "노맛"
        case .three:
            emojiLabel.text = "쏘쏘"
        case .four:
            emojiLabel.text = "마싰다"
        case .five:
            emojiLabel.text = "쫀맛"
        }
    }

}


extension ReviewVC: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == titleTextField {
            self.feed.title = titleTextField.text ?? ""

        } else if textField == contentTextField {
            self.feed.content = contentTextField.text ?? ""
        }
    }
}


extension ReviewVC: PlaceDelegate {
    func setPlace(_ controller: PlaceSearchVC, place: Place) {
        print("띵동place도착: \(place)")
        self.locationTextField.text = place.name
        self.place = place
        self.feed.place = place
    }
}

extension ReviewVC: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.foodAiData.count
        
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell: AiFoodNameCell = collectionView.dequeueReusableCell(withReuseIdentifier: "AiFoodNameCell", for: indexPath) as! AiFoodNameCell
        
        cell.foodNameLabel.text = foodAiData[indexPath.item]
        cell.wrapFoodName.layer.borderWidth = 2
        cell.wrapFoodName.layer.cornerRadius = 10
        cell.wrapFoodName.layer.masksToBounds = true
        cell.wrapFoodName.layer.borderColor = UIColor.systemYellow.cgColor
        
        
        
        return cell
    }
    
}

extension ReviewVC: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("You tepped me \(indexPath.item)")
        self.titleTextField.text = foodAiData[indexPath.item]
    }
}

extension ReviewVC: UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let CvRect = collectionView.frame
        
        return CGSize(width: (CvRect.width/3)-3,
                      height: (CvRect.height)-3)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 2
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
}
