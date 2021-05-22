//
//  MainTabBarVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/20.
//

import UIKit


class MainTabBarVC: UITabBarController {
    
    static func instance() -> MainTabBarVC {
        let vc = UIStoryboard.init(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainTabBarVC") as! MainTabBarVC
        return vc
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // delegate 설정
        self.delegate = self
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
}

extension MainTabBarVC: UITabBarControllerDelegate {
    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        if viewController.isKind(of: ReviewNavigationVC.self) {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "Review")
            vc?.modalPresentationStyle = .fullScreen
            self.present(vc!, animated: true, completion: nil)
            return false
        }
        return true
    }
    
}

