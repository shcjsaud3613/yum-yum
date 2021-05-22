//
//  UserModel.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/28.
//
  
import Foundation
import SwiftyJSON

struct User: Codable {
    var id: Int?
    var profilePath: String?
    var nickname: String?
    var introduction: String?
    var email: String?
    
    init () {
        
    }
    
    init(profilePath: String, nickname: String, introduction: String, email: String) {
        self.profilePath = profilePath
        self.nickname = nickname
        self.introduction = introduction
        self.email = email
    }
    
    init(json: JSON) {
        id = json["id"].intValue
//        profilePath = URL(string: json["profilePath"].stringValue)
        profilePath = json["profilePath"].stringValue
        nickname = json["nickname"].stringValue
        introduction = json["introduction"].stringValue
        email = json["email"].stringValue
    }
    
    init(profilePath: String, nickname: String, introduction: String, id: Int) {
        self.profilePath = profilePath
        self.nickname = nickname
        self.introduction = introduction
        self.id = id
    }
    
    init(fromjson: JSON) {
        self.id = fromjson["user"]["id"].intValue
        self.email = fromjson["user"]["email"].stringValue
        self.nickname = fromjson["user"]["nickname"].stringValue
        self.introduction = fromjson["user"]["introduction"].stringValue
        self.profilePath = fromjson["user"]["profilePath"].stringValue
    }
    
    enum CodingKeys: String, CodingKey {
        case id
        case profilePath
        case nickname
        case introduction
        case email
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        try container.encode(nickname, forKey: .nickname)
        try container.encode(introduction, forKey: .introduction)
        try container.encode(profilePath, forKey: .profilePath)
    }
    
}

struct userLike:Codable {
    var userId : Int
    var feedId : Int

    init () {
        self.userId = 0
        self.feedId = 0
    }
}
