//
//  LoginViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/15.
//


import UIKit
import AuthenticationServices
import GoogleSignIn
import Firebase
import Alamofire

class LoginVC : UIViewController, GIDSignInDelegate {
    
    let plist = UserDefaults.standard
    
    @IBOutlet var appleLoginBtn: UIView!
    @IBOutlet var googleLoginBtn: GIDSignInButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance()?.presentingViewController = self
         // 자동로그인을 설정헌다.
        GIDSignIn.sharedInstance()?.restorePreviousSignIn()
         // 버튼 스타일지정
        googleLoginBtn.style = .wide
        addButton()
//        checkLogin()
        // 구글 로그인되어있는지 안되어 있는지 확인
        print(GIDSignIn.sharedInstance()?.currentUser != nil)

        GIDSignIn.sharedInstance()?.signOut()
    }
    
    func checkLogin(){
        print("들어오나요?")
        if plist.string(forKey: "USEREMAIL") != nil {
            let storyboard: UIStoryboard? = UIStoryboard(name: "Main", bundle: Bundle.main)

            if let tabbarvc = storyboard?.instantiateViewController(identifier: "MainTabBarVC") as? UITabBarController {
                
                tabbarvc.modalPresentationStyle = .fullScreen
                self.present(tabbarvc, animated: true, completion: nil)
            } else {
                print("로그인이 안되어있어요!")
            }
        }
    }
    
    // 애플 로그인 버튼을 구현할 메서드 추가
    func addButton() {
        let button = ASAuthorizationAppleIDButton(authorizationButtonType: .signIn, authorizationButtonStyle: .black)

        button.addTarget(self, action: #selector(handleAuthorizationAppleIDButtonPress), for: .touchUpInside)

        button.translatesAutoresizingMaskIntoConstraints = false

        appleLoginBtn.addSubview(button)
        button.centerXAnchor.constraint(equalTo:appleLoginBtn.centerXAnchor)
                .isActive = true
        button.centerYAnchor.constraint(equalTo:appleLoginBtn.centerYAnchor)
                .isActive = true
        button.heightAnchor.constraint(equalTo: appleLoginBtn.heightAnchor)
                    .isActive = true
        button.widthAnchor.constraint(equalTo: appleLoginBtn.widthAnchor)
                    .isActive = true

    }
    
    // 애플 로그인 버튼을 눌면 실행되는 메서드
    @objc
    func handleAuthorizationAppleIDButtonPress() {
        let request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = [.fullName, .email]
        let controller = ASAuthorizationController(authorizationRequests: [request])
        controller.delegate = self as? ASAuthorizationControllerDelegate
        controller.presentationContextProvider = self as? ASAuthorizationControllerPresentationContextProviding
        controller.performRequests()
    }

    // 애플로그인 버튼 클릭시 모달 띄우기
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return self.view.window!
    }

    // Apple ID 연동 성공 시 실행되는 메서드
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        switch authorization.credential {
        // Apple ID
        case let appleIDCredential as ASAuthorizationAppleIDCredential:

            // 계정 정보 가져오기
            let userIdentifier = appleIDCredential.user
            let fullName = appleIDCredential.fullName
            let email = appleIDCredential.email

            print("User ID : \(userIdentifier)")
            print("User Email : \(email ?? "")")
            print("User Name : \((fullName?.givenName ?? "") + (fullName?.familyName ?? ""))")
            
        default:
            break
        }
    }
    // 인증 실패시 아래 함수 호출
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        // Handle error.
    }
    
    // 구글 로그인을 완료했을때 불려져 오는 메서드이다.
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!,
              withError error: Error!) {
                  
          if let error = error {
            if (error as NSError).code == GIDSignInErrorCode.hasNoAuthInKeychain.rawValue {
              print("The user has not signed in before or they have since signed out.")
            } else {
              print("\(error.localizedDescription)")
            }
            return
            // 구글 로그인이 완료되었을떄 아래 실행
            } else {
            guard let userEmail = user.profile.email else {
                return
            }
//            print("나는 이메일이다", userEmail)
            // 이메일 가입 체크
                WebApiManager.shared.checkUser(userEmail: userEmail) { (result) in
//                    print("emailcheck: \(result)")
                    if result["status"] == "200" {
                        // 회원가입된 유저라면
                        if result["data"]["existence"] == true {
                            // 로그인
                            WebApiManager.shared.login(userEmail: userEmail) {(result) in
                                print("login: \(result)")
                                if result["status"] == "200" {
                                    UserDefaults.setUserInfo(json: result["data"])
                                    let storyboard: UIStoryboard? = UIStoryboard(name: "Main", bundle: Bundle.main)
                                    Messaging.messaging().subscribe(toTopic: result["data"]["id"].stringValue) { error in
                                      print("Subscribed to Message \(result["data"]["id"].stringValue)")
                                    }
                                    if let tabbarvc = storyboard?.instantiateViewController(identifier: "MainTabBarVC") as? UITabBarController {
                                        self.view.window?.rootViewController = tabbarvc
                                    } else {
                                        print("탭바가 없습니다.")
                                    }
                                }
                            } failure: { (error) in
                                print("login error: \(error)")
                            }
                        // 아닐경우 회원가입
                        } else {
                            let vc = SignupVC.instance(userEmail: userEmail)
//                            self.navigationController?.pushViewController(vc, animated: true)
                            vc.modalPresentationStyle = .overFullScreen
                            self.present(vc, animated: true, completion: nil)
                        }
                    }
                } failure: { (error) in
                    print("이메일체크 error다")
                }


        }
        
        
        // 로그인 완료 했을때 firebase에 저장하기
        guard let authentication = user.authentication else {return}
        
        let credential = GoogleAuthProvider.credential(withIDToken: authentication.idToken, accessToken: authentication.accessToken)
        
        Auth.auth().signIn(with: credential, completion: {(user, error) in
            if let error = error {
                    print(error)
                }
            }
        )
    }
        
    // 로그아웃되면 실행되는 함수
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!,
              withError error: Error!) {
      // Perform any operations when the user disconnects from app here.
      // ...
        print("로그아웃되었어요!")
    }
}
