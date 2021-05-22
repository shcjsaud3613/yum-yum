//
//  FeedModel.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import Foundation
import SwiftyJSON

struct Feed {
    var userId: Int?
    var title: String?
    var content: String?
    var score: Int?
    var thumbnailPath: URL? = nil
    var videoPath: URL? = nil
    var isCompleted: Bool?
    
    //Decodable
    var id: Int?
    var likeCount: Int?
    var isLike: Bool?
    var placeId: Int?
    var user: User?

    
    // Encodable
    var place: Place?
    init() {
        
    }

    init(json: JSON) {
        self.videoPath = URL(string: json["videoPath"].stringValue)
        self.isLike = json["isLike"].boolValue
        self.id = json["id"].intValue
        self.thumbnailPath = URL(string: json["thumbnailPath"].stringValue)
        self.placeId = json["placeId"].intValue
        self.content = json["content"].stringValue
        self.likeCount = json["likeCount"].intValue
        self.isCompleted = json["isCompleted"].boolValue
    }
    
    init(feedJson: JSON) {
        self.videoPath = URL(string: feedJson["videoPath"].stringValue)
        self.isLike = feedJson["isLike"].boolValue
        self.id = feedJson["id"].intValue
        self.thumbnailPath = URL(string: feedJson["thumbnailPath"].stringValue)
        self.placeId = feedJson["placeId"].intValue
        self.content = feedJson["content"].stringValue
        self.likeCount = feedJson["likeCount"].intValue
        self.title = feedJson["title"].stringValue
        self.userId = feedJson["userId"].intValue
        self.score = feedJson["score"].intValue
        self.place = Place(feedJson: feedJson)
        self.user = User(fromjson: feedJson)
        self.isCompleted = feedJson["isCompleted"].boolValue
    }
    
    enum CodingKeys: String, CodingKey {
        case userId
        case title
        case content
        case score
        case thumbnailPath
        case videoPath
        case id
        case likeCount
        case isLike
        case placeId
        case isCompleted
        case place = "placeRequest"
    }
}

extension Feed: Decodable {
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        userId = try container.decode(Int.self, forKey: .userId)
        title = try container.decode(String.self, forKey: .title)
        content = try container.decode(String.self, forKey: .content)
        thumbnailPath = try container.decode(URL.self, forKey: .thumbnailPath)
        videoPath = try container.decode(URL.self, forKey: .videoPath)
        id = try container.decode(Int.self, forKey: .id)
        likeCount = try container.decode(Int.self, forKey: .likeCount)
        isLike = try container.decode(Bool.self, forKey: .isLike)
        placeId = try container.decode(Int.self, forKey: .placeId)
    }
}

extension Feed: Encodable {
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(userId, forKey: .userId)
        try container.encode(title, forKey: .title)
        try container.encode(content, forKey: .content)
        try container.encode(score, forKey: .score)
        try container.encode(thumbnailPath, forKey: .thumbnailPath)
        try container.encode(videoPath, forKey: .videoPath)
        try container.encode(place, forKey: .place)
        try container.encode(isCompleted, forKey: .isCompleted)
        try container.encode(id, forKey: .id)
    }
}
