//
//  EurekaVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/15.
//

import UIKit
import CoreLocation
import GeoFire
import FirebaseFirestore
import SnapKit

class EurekaVC: UIViewController {
    
    @IBOutlet var backgroundView: UIView!
    @IBOutlet var textFieldView: UIView!
    @IBOutlet var eurekaTextField: UITextField!
    @IBOutlet var myChatLabel: UILabel!
    @IBOutlet var myChatImage: UIImageView!
    @IBOutlet var myChatBubbleView: UIView!
    
    var locationManager = CLLocationManager()
    let user = UserDefaults.getLoginedUserInfo()
    
    let yumyumYellow: ColorSet = .yumyumYellow
    
    
    //위도와 경도
    var latitude: Double?
    var longitude: Double?
    
    var neighbor: [Chat]?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // 델리게이트 설정
        locationManager.delegate = self
        eurekaTextField.delegate = self
        
        // 거리 정확도 설정
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        // 사용자에게 허용 받기 alert 띄우기
        locationManager.requestWhenInUseAuthorization()
        
        // 아이폰 설정에서의 위치 서비스가 켜진 상태라면
        if CLLocationManager.locationServicesEnabled() {
//            print("위치 서비스 On 상태")
            locationManager.startUpdatingLocation() //위치 정보 업데이트
            // 위도 경도 가져오기
            let coor = locationManager.location?.coordinate
            latitude = coor?.latitude
            longitude = coor?.longitude
        } else {
            print("위치 서비스 Off 상태")
        }
        
        // Subscribe to Keyboard Will Show notifications
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(keyboardWillShow(_:)),
            name: UIResponder.keyboardWillShowNotification,
            object: nil
        )
        
        // Subscribe to Keyboard Will Hide notifications
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(keyboardWillHide(_:)),
            name: UIResponder.keyboardWillHideNotification,
            object: nil
        )
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        setLayout()
        FirestoreManager.shared.getNeighbors(myId: user!["id"].intValue, latitude: latitude!, longitude: longitude!) { neighbor in
            self.backgroundView.subviews.map({ $0.removeFromSuperview() })
            self.neighbor = []
            self.neighbor = neighbor
            self.neighbor?.forEach({ chat in
                self.showOtherMessage(chat: chat)
            })
        }
    }
    
    func setLayout() {
        myChatLabel.textColor = yumyumYellow.toColor()
        
        myChatBubbleView.isHidden = true
        myChatBubbleView.layer.borderWidth = 2
        myChatBubbleView.backgroundColor = yumyumYellow.toColor().withAlphaComponent(0.1)
        myChatBubbleView.layer.borderColor = yumyumYellow.toColor().cgColor
        myChatBubbleView.layer.cornerRadius = 16
        
        myChatImage.layer.cornerRadius = 50
        myChatImage.clipsToBounds = true
        imageMakeRouded(imageview: myChatImage)
        if let url = URL(string: user!["profilePath"].stringValue) {
            var image: UIImage?
            
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: url)
                DispatchQueue.main.async {
                    image = UIImage(data: data!)
                    self.myChatImage.image = image
                }
            }
        } else {
            var image: UIImage?
            image = UIImage(named: "ic_profile")
            self.myChatImage.image = image
        }
        
        myChatImage.layer.borderWidth = 3
    }
    
    func showOtherMessage(chat: Chat) {
        let width = Int(self.view.frame.size.width) - 100
        let height  = Int(self.view.frame.size.height) - Int(self.tabBarController?.tabBar.frame.size.height ?? 0) - 100
        
        let topOffset = Int.random(in: 20..<height)
        let leadingOffset = Int.random(in: 20..<width)
        
        // container
        let container = UIView()
        backgroundView.addSubview(container)
        container.snp.makeConstraints { (make) -> Void in
            make.height.equalTo(50)
            make.top.equalToSuperview().offset(topOffset)
            make.leading.equalToSuperview().offset(leadingOffset)
        }
        
        // profileImage
        let yourProfileImage: UIImageView = {
            let imageView = UIImageView()
            imageView.layer.borderWidth = 2
            imageView.layer.cornerRadius = 20
            imageView.layer.borderColor = UIColor.gray.cgColor
            imageView.clipsToBounds = true
            return imageView
        }()
        
        if let url = URL(string: chat.profilePath!) {
            var image: UIImage?
            
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: url)
                DispatchQueue.main.async {
                    image = UIImage(data: data!)
                    yourProfileImage.image = image
                }
            }
        } else {
            var image: UIImage?
            image = UIImage(named: "ic_profile")
            yourProfileImage.image = image
        }
        container.addSubview(yourProfileImage)
        yourProfileImage.snp.makeConstraints { (make) -> Void in
            make.width.height.equalTo(40)
            make.leading.equalToSuperview()
            make.centerY.equalToSuperview()
        }
        
        
        //stackview
        var stackView: UIStackView = {
          let stackView = UIStackView()
          stackView.translatesAutoresizingMaskIntoConstraints = false
          stackView.axis = .vertical
          stackView.distribution = .fillEqually
          return stackView
        }()
        container.addSubview(stackView)
        stackView.snp.makeConstraints { make -> Void in
            make.leading.equalTo(yourProfileImage.snp.trailing).offset(8)
            make.centerY.equalTo(yourProfileImage)
            make.trailing.equalToSuperview()
        }
        // messageLabel
        let messageLabel: UILabel = {
            let label = UILabel()
            label.font = UIFont.systemFont(ofSize: 10)
            label.textColor = .gray
            return label
        }()
        if let message = chat.message {
            messageLabel.text = message
        }
        stackView.addArrangedSubview(messageLabel)

        // nickname
        let nicknameLabel: UILabel = {
            let label = UILabel()
            label.font = UIFont.systemFont(ofSize: 10)
            label.textColor = .gray
            return label
        }()
        if let nickname = chat.nickname {
            nicknameLabel.text = "@\(nickname)"
        }
        stackView.addArrangedSubview(nicknameLabel)
        
    }
    
    // 키보드 내리기
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?){
        self.view.endEditing(true)
    }
    
    func createChat(message: String) {
        // 아이폰 설정에서의 위치 서비스가 켜진 상태라면
        if CLLocationManager.locationServicesEnabled() {
//            print("위치 서비스 On 상태")
            locationManager.startUpdatingLocation() //위치 정보 업데이트
            // 위도 경도 가져오기
            let coor = locationManager.location?.coordinate
            latitude = coor?.latitude
            longitude = coor?.longitude
        } else {
            print("위치 서비스 Off 상태")
        }
        
        let location = CLLocationCoordinate2D(latitude: latitude!, longitude: longitude!)
        let hash = GFUtils.geoHash(forLocation: location)
        let userId = user!["id"].intValue
        
        

        let chat: Chat = Chat(userId: userId, message: message, geohash: hash, lat: latitude, lng: longitude, profilePath: user!["profilePath"].stringValue, nickname: user!["nickname"].stringValue
        )
        
        FirestoreManager.shared.createChat(userId: userId, chat: chat)
        
        neighbor?.forEach({ chat in
            FirestoreManager.shared.sendMessage(to: chat.userId!, message: message) { res in
                print(res)
            } failure: { err in
                print(err)
            }
        })

        
        myChatLabel.text = message
        myChatBubbleView.isHidden = false
        eurekaTextField.text = ""
        self.view.endEditing(true)
        
    }
    
    @IBAction func didTapEurekaButton(_ sender: Any) {
        let message = eurekaTextField.text!
        if message.count > 0 {
            createChat(message: message)
        }
    }
    
    @objc
    dynamic func keyboardWillShow(
        _ notification: NSNotification
    ) {        
        animateWithKeyboard(notification: notification) {
            (keyboardFrame) in
            let constant = -keyboardFrame.height + (self.tabBarController?.tabBar.frame.size.height)!
            self.view.frame.origin.y = constant
        }
    }
    
    @objc
    dynamic func keyboardWillHide(
        _ notification: NSNotification
    ) {
        animateWithKeyboard(notification: notification) {
            (keyboardFrame) in
            self.view.frame.origin.y = 0
        }
    }
    
    func animateWithKeyboard(
        notification: NSNotification,
        animations: ((_ keyboardFrame: CGRect) -> Void)?
    ) {
        // Extract the duration of the keyboard animation
        let durationKey = UIResponder.keyboardAnimationDurationUserInfoKey
        let duration = notification.userInfo![durationKey] as! Double
        
        // Extract the final frame of the keyboard
        let frameKey = UIResponder.keyboardFrameEndUserInfoKey
        let keyboardFrameValue = notification.userInfo![frameKey] as! NSValue
        
        // Extract the curve of the iOS keyboard animation
        let curveKey = UIResponder.keyboardAnimationCurveUserInfoKey
        let curveValue = notification.userInfo![curveKey] as! Int
        let curve = UIView.AnimationCurve(rawValue: curveValue)!
        
        // Create a property animator to manage the animation
        let animator = UIViewPropertyAnimator(
            duration: duration,
            curve: curve
        ) {
            // Perform the necessary animation layout updates
            animations?(keyboardFrameValue.cgRectValue)
            
            // Required to trigger NSLayoutConstraint changes
            // to animate
            self.view?.layoutIfNeeded()
        }
        
        // Start the animation
        animator.startAnimation()
    }
    
    
}


extension EurekaVC: CLLocationManagerDelegate {
    // 위치 정보 계속 업데이트 -> 위도 경도 받아옴
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
//        print("didUpdateLocations")
        if let location = locations.first {
//            print("위도: \(location.coordinate.latitude)")
//            print("경도: \(location.coordinate.longitude)")
        }
    }
    
    // 위도 경도 받아오기 에러
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error)
    }
}

extension EurekaVC: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField.text!.count > 0 {
            createChat(message: textField.text!)
            return true
        }
        return false
    }
    
}
