//
//  ConfigUrls.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/27.
//

import Foundation

struct URLs {
    static let domain = "http://k4b206.p.ssafy.io:8081/yumyum/"
    //GET {email} 뒤에 추가해줘야함
    static let login = domain + "user/login/"
    //POST email,nickname,introduce, posterPath를 같이 담아서 보내줘야해
    static let signUp = domain + "user/signup"
    //POST formData형태의 이미지를 넣어주면됨
    static let profile = domain + "user/profile"
}
