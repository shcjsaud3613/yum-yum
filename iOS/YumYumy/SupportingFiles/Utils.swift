//
//  Utils.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/01.
//

import Foundation


let LOGINED_USERINFO_USERDEFAULT_KEY = "LOGINED_USERINFO_USERDEFAULT_KEY"


public enum ColorSet: String {
    case yumyumYellow = "#F6D705"
    
    func toColor() -> UIColor {
        return UIColor(hex: self.rawValue) ?? UIColor.clear
    }
}
