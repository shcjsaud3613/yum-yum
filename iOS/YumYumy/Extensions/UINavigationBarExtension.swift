//
//  UINavigationBarExtension.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import UIKit

// Mark: Todo - 키이름 대문자로 수정해주세요 forKey: "USER" 이런식으로
extension UINavigationBar {
    func transparentNavigationBar() {
        self.setBackgroundImage(UIImage(), for: .default)
        self.shadowImage = UIImage()
        self.isTranslucent = true
    }
}
