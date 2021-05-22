//
//  UserDefaultsExtension.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import SwiftyJSON
import GoogleSignIn

extension UserDefaults {
    static func setUserInfo(json:JSON) {
        UserDefaults.standard.setValue(json.rawString(), forKey: LOGINED_USERINFO_USERDEFAULT_KEY)
        UserDefaults.standard.synchronize()
    }
    
    static func getLoginedUserInfo() -> JSON? {
        if let jsonStr = UserDefaults.standard.string(forKey: LOGINED_USERINFO_USERDEFAULT_KEY), let data = jsonStr.data(using: .utf8) {
            do {
                let json = try JSON(data:data)
                return json
            }catch {
                return nil
            }
        }else {
            return nil
        }
    }
    
    static func removeUserData() {
        UserDefaults.standard.removeObject(forKey: LOGINED_USERINFO_USERDEFAULT_KEY)
        UserDefaults.standard.synchronize()
        GIDSignIn.sharedInstance().signOut()
    }
    
    
    // MARK: -TODO 아래 함수 쓴 부분 위에 함수로 수정하기
    static func getLoginedUserInfo2() -> NSDictionary {
        var userData = NSDictionary()

        if let jsonStr = UserDefaults.standard.dictionary(forKey: LOGINED_USERINFO_USERDEFAULT_KEY){
            userData = jsonStr as NSDictionary
        }
        return userData
    }

}
