//
//  EditProfileVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/03.
//

import UIKit

class EditProfileVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var profileImgView: UIImageView!
    
    @IBOutlet weak var nickNameTF: UITextField!
    
    @IBOutlet weak var introduceTF: UITextField!
    
    var user = User(profilePath: "", nickname: "", introduction: "", id: 0)
    
    var isProfile: Bool = true
    
    @IBAction func tapView(_ sender: UITapGestureRecognizer) {
        self.view.endEditing(true)
    }
    
    @IBAction func completeBtn(_ sender: Any) {
        let userData = UserDefaults.getLoginedUserInfo()
        let tempprofilePath: String = userData!["profilePath"].stringValue
        
        user.id = userData!["id"].intValue
        user.nickname = self.nickNameTF.text
        user.introduction = self.introduceTF.text
        print(tempprofilePath.isEmpty)
    
        if isProfile == false {
            user.profilePath = ""
        } else {
            if tempprofilePath.isEmpty , user.profilePath == ""  {
                user.profilePath = ""
            } else if tempprofilePath.isEmpty == false, user.profilePath == "" {
                user.profilePath = tempprofilePath
            }
        }
        
        WebApiManager.shared.updateProfile(user: user) { (result) in
            if result["status"] == "200" {
                UserDefaults.setUserInfo(json: result["data"])
                print("저장되었습니다.")
            }
        } failure: { (error) in
            print("제발뜨지마라")
        }
        showToast(message: "변경이 완료되었습니다.")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        let user = UserDefaults.getLoginedUserInfo()
        // 인전값을 그대로 가져와서 회원변경 탭에 넣어준다.
        nickNameTF.text = user!["nickname"].stringValue
        introduceTF.text = user!["introduction"].stringValue
        if let url = URL(string: user!["profilePath"].stringValue) {
            var image: UIImage?
            print("url들어오나요?")
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: url)
                DispatchQueue.main.async {
                    image = UIImage(data: data!)
                    self.profileImgView.image = image
                }
            }
        } else {
            var image: UIImage?
            image = UIImage(named: "ic_profile")
            self.profileImgView.image = image
        }
        
        imageMakeRouded(imageview: profileImgView)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(touchToPickPhoto))
        
        profileImgView.addGestureRecognizer(tapGesture)
        profileImgView.isUserInteractionEnabled = true
        self.nickNameTF.autocorrectionType = .no
        self.nickNameTF.autocapitalizationType = .none
        self.introduceTF.autocorrectionType = .no
        self.introduceTF.autocapitalizationType = .none
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
        let defaultImgAction = UIAlertAction(title: "기본 이미지로 변경", style: .default) { (_) in
            var image: UIImage?
            image = UIImage(named: "ic_profile")
            self.profileImgView.image = image
            self.isProfile = false
        }
        alert.addAction(defaultImgAction)
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
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let img = info[UIImagePickerController.InfoKey.editedImage] as? UIImage {
            self.profileImgView.image = img
            WebApiManager.shared.createProfilePath(image: img) {
                (result) in
                self.user.profilePath = result["data"].stringValue
            } failure: { (error) in
                print(error)
            }
        }

        picker.dismiss(animated: true)
    }
}
