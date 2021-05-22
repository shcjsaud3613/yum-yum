//
//  ChatModel.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/16.
//

import Foundation


struct Chat: Codable {
    var userId: Int?
    var message: String?
    var geohash: String?
    var lat: Double?
    var lng: Double?
    var profilePath: String?
    var nickname: String?
}
