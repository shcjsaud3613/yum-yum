//
//  AlertUtil.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/27.
//

import UIKit

extension UIViewController {
    
    // MARK: - 알람 에러 메서드
    func alertError(_ message: String, completion: (()-> Void)? = nil) {
        // 메인 스레드에서 실행되도록한다.
        DispatchQueue.main.async {
            let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
            
            let okAction = UIAlertAction(title: "확인", style: .cancel) {(_) in
                completion?() // completion 매개 변수 값이 nil이 아닐때에만 실행되도록한다.
            }
            alert.addAction(okAction)
            self.present(alert, animated: false)
        }
    }
    
    func alertConfirm(_ message: String, completion: (()-> Void)? = nil) {
        // 메인 스레드에서 실행되도록한다.
        DispatchQueue.main.async {
            let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
            
            let okAction = UIAlertAction(title: "확인", style: .default) {(_) in
                completion?() // completion 매개 변수 값이 nil이 아닐때에만 실행되도록한다.
            }
            alert.addAction(okAction)
            self.present(alert, animated: false)
        }
    }
    
    // MARK: - 스토리보드 이동
    func moveStoryBoard(_ name:String, _ identifier:String) {
        let storyboard: UIStoryboard? = UIStoryboard(name:name, bundle: Bundle.main)
        
        guard let movevc = storyboard?.instantiateViewController(withIdentifier: identifier) else {
            return
        }
        
        movevc.modalPresentationStyle = .fullScreen
        self.present(movevc, animated: true)
    }
    
    //MARK: - 루트뷰 이동 메서드
    func moveRootView(name:String, identifier:String) {
        let storyboard: UIStoryboard? = UIStoryboard(name:name, bundle: Bundle.main)
        
        guard let rootvc = storyboard?.instantiateViewController(identifier: identifier) else {
            return
        }
        self.view.window?.rootViewController = rootvc
    }
    
    
    //MARK: - 기본 알림창 구현
    func defaultalert(_ message:String, success: (() -> Void)? = nil, failure: (() -> Void)? = nil) {
        // 메인 스레드에서 실행되도록한다. 이유는 alert를 선택하기전에 뒤의 배경 UI/UX를 통제하기 위해서이다. ]
        DispatchQueue.main.async {
            let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
            
            let okAction = UIAlertAction(title: "확인", style: .default){(_) in
            success?() // completion 매개 변수 값이 nil이 아닐때에만 실행되도록한다.
            }
            let cancleAction = UIAlertAction(title: "취소", style: .cancel){(_) in
            failure?()
            }
            alert.addAction(okAction)
            alert.addAction(cancleAction)
            self.present(alert, animated: false)
        }
    }
    
    //MARK: - 이미지 뷰를 둥글게 만들기
    func imageMakeRouded(imageview:UIImageView) {
        let yumyumYellow: ColorSet = .yumyumYellow
        
        imageview.layer.borderWidth = 2
        imageview.layer.masksToBounds = false
        imageview.layer.borderColor = yumyumYellow.toColor().cgColor
        imageview.layer.cornerRadius = imageview.frame.height/2
        imageview.clipsToBounds = true
        imageview.contentMode = .scaleAspectFill
    }
    
    func showToast(message : String, font: UIFont = UIFont.systemFont(ofSize: 14.0)) {
        let toastLabel = UILabel(frame: CGRect(x: self.view.frame.size.width/2 - 75,
                    y: self.view.frame.size.height/9, width: 150, height: 35))
        toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        toastLabel.textColor = UIColor.white
        toastLabel.font = font
        toastLabel.textAlignment = .center
        toastLabel.text = message
        toastLabel.text = message
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10;
        toastLabel.clipsToBounds = true
        self.view.addSubview(toastLabel)
        UIView.animate(withDuration: 10.0, delay: 0.1, options: .curveEaseOut , animations: {
            toastLabel.alpha = 0.0
        }, completion: { (isCompleted) in
            toastLabel.removeFromSuperview()
        })
        
    }
}
