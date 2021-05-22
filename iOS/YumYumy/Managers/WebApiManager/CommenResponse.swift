////
////  CommenResponse.swift
////  YumYum
////
////  Created by 염성훈 on 2021/04/29.
////
//
//import Foundation
//
//// 나중에 분리해주세요. 파일로
//struct BaseStruct<T: Decodable>: Decodable {
//    var status: String
//    var message: String
//    var data: T?
//    // json key가 아닌 내가 원하는 이름으로 지정해 줄 수 있게 해주는 프로토콜
//    enum CodingKeys: String, CodingKey {
//        case status = "status"
//        case message = "message"
//        case data = "data"
//    }
//
//    init(from decoder: Decoder) throws {
//        let values = try decoder.container(keyedBy: CodingKeys.self)
//
//        status = try values.decode(String.self, forKey: .status)
//        message = try values.decode(String.self, forKey: .message)
//        data = try values.decode(T.self, forKey: .data)
//    }
//}
//
//struct TestUserData: Codable {
//    let id: String
//    let email: String
//    let nickname: String
//    let introduction: String
//    let profilePath: String
//}

