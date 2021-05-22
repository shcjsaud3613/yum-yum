//
//  SignUpViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/22.
//

import UIKit
import Alamofire
import SwiftyJSON

class SignupVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    static func instance(userEmail: String) -> SignupVC {
        let vc = UIStoryboard.init(name: "Accounts", bundle: nil).instantiateViewController(withIdentifier: "SignupVC") as! SignupVC
        vc.userEmail = userEmail
        return vc
    }
    
    @IBOutlet var profileImgView: UIImageView!
    @IBOutlet weak var nickNameLabel: UITextField!
    @IBOutlet weak var introductionLabel: UITextField!
    var userEmail: String?
    var profilePath: String? = ""

    
    // 회원가입 완료 후 동작하는 메서드
    @IBAction func completeSignUp(_ sender: UIButton) {
        let user = User(profilePath: self.profilePath!, nickname: self.nickNameLabel.text!, introduction: self.introductionLabel.text!, email: userEmail!)
        
        WebApiManager.shared.signup(user: user) { (result) in
            UserDefaults.setUserInfo(json: result["data"])
            print("result: \(result)")
            let vc = MainTabBarVC.instance()
            self.view.window?.rootViewController = vc
        } failure: { (error) in
            print("회원가입에러: \(error)")
        }

    }
    
    @IBAction func tapView(_ sender: UITapGestureRecognizer) {
        self.view.endEditing(true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(touchToPickPhoto))
        
        profileImgView.addGestureRecognizer(tapGesture)
        profileImgView.isUserInteractionEnabled = true
        self.introductionLabel.placeholder = "한줄 소개를 입력해주세요."
        self.introductionLabel.autocorrectionType = .no
        self.introductionLabel.autocapitalizationType = .none
        self.nickNameLabel.placeholder = "별명을 입력해주세요."
        self.nickNameLabel.autocorrectionType = .no
        self.nickNameLabel.autocapitalizationType = .none
        
        imageMakeRouded(imageview: profileImgView)
        
    }
    
    @objc func touchToPickPhoto(){
        let alert = UIAlertController(title: nil, message: "사진가져올 곳을 선택해주세요.", preferredStyle: .actionSheet)
        
        // 저장된 앨범을 사용할 수 있으면
        if UIImagePickerController.isSourceTypeAvailable(.savedPhotosAlbum) {
            alert.addAction(UIAlertAction(title: "저장된 앨범", style: .default){ (_) in self.imgPikcer(.savedPhotosAlbum)
            })
        }
        
        // 포토 라이브러리를 사용할 수 있으면 클로저 문법으로 뒤의 함수를 실행한다.
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
            alert.addAction(UIAlertAction(title: "포토 라이브러리", style: .default){ (_) in self.imgPikcer(.photoLibrary)
            })
        }
        // 취소 버튼 추가
        alert.addAction(UIAlertAction(title: "취소", style: .cancel, handler: nil))
        
        self.present(alert, animated: true)
        
    }
    // 포토라이브러리랑 저장앨범을 구분해서 알람창을 띄우기 위해! souce를 다르게 받아온다.
    func imgPikcer (_ source : UIImagePickerController.SourceType){
        let picker = UIImagePickerController()
        picker.sourceType = source
        picker.delegate = self
        picker.allowsEditing = true
        self.present(picker, animated: true)
    }
    
    // 이미지를 선택하면 이 메소드가 자동으로 출력된다.
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let img = info[UIImagePickerController.InfoKey.editedImage] as? UIImage {
            self.profileImgView.image = img
            WebApiManager.shared.createProfilePath(image: img) {
                (result) in
                self.profilePath = result["data"].stringValue
            } failure: { (error) in
                print(error)
            }
        }

        picker.dismiss(animated: true)
    }
}
