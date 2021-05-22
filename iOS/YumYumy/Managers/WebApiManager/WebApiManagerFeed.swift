//
//  WebApiManagerFeed.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import Alamofire
import SwiftyJSON

extension WebApiManager {
    //MARK: - 피드 생성
    func createFeed(feed: Feed, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)"
        AF.request(url, method: .post, parameters: feed, encoder: JSONParameterEncoder.default)
          .responseJSON {(response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
                
            case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
            }
        }
    }
    //MARK: -피드 업데이트
    func updateFeed(feed: Feed, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)"
        AF.request(url, method: .put, parameters: feed, encoder: JSONParameterEncoder.default)
            .responseJSON{(response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    success(json)
                    break
                case .failure(_):
                    let error: Error = response.error!
                    failure(error)
                    break
                }
            }
    }
    
    func getFeedList(userId: Int, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)/list/\(userId)"
        AF.request(url, method: .get)
          .responseJSON {(response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
                
            case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
            }
        }
    }
    
    func getMyFeedList(userId: Int, authorId: Int, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        
        let url = "\(domainUrl)\(feedUrl)/list/\(authorId)/\(userId)"
        
        AF.request(url, method: .get)
            .responseJSON{ (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    success(json)
                    break
                case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
                }
            }
    }
    //MARK: - 피드 좋아요
    func postLikeFeed(likeInfo : userLike, suceess: @escaping (JSON) -> Void,
                      failure : @escaping(Error) -> Void) {

        let url = "\(domainUrl)\(feedUrl)like"

        AF.request(url, method: .post, parameters: likeInfo, encoder: JSONParameterEncoder.default)
            .responseJSON{ (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    suceess(json)
                    break
                case .failure(_):
                    let error: Error = response.error!
                    failure(error)
                    break
                }
            }
    }
    
    //MARK: - 피드 좋아요 취소
    func cancleLikeFeed(feedId : Int, userId : Int, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)like/\(feedId)/\(userId)"
        
        AF.request(url, method: .delete).responseJSON{ (response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
            case .failure(_):
                let error = response.error!
                failure(error)
                print("좋아요 취소 요청 실패!")
                break
            }
        }
        
    }
    //MARK: - 좋아요한 피드들 가져오기
    func getMyLikeFeed(userId : Int, success: @escaping (JSON) -> Void, faliure: @escaping (Error) -> Void) {
        
        let url = "\(domainUrl)\(feedUrl)list/like/\(userId)"
        
        AF.request(url, method: .get).responseJSON{ (response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
            case .failure(_):
                let error: Error = response.error!
                faliure(error)
                break
            }
        }
    }
    
    //MARK: - 피드 삭제하기
    func deleteMyFeed(feedId: Int, success: @escaping (JSON) -> Void, faliure: @escaping (Error) -> Void) {
        
        let url = "\(domainUrl)\(feedUrl)\(feedId)"
        
        AF.request(url, method: .delete).responseJSON{(response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
            case .failure(_):
                let error: Error = response.error!
                faliure(error)
                break
            }
        }
    }
    
    func getPlaceFeedList(placeId: Int, userId: Int, success: @escaping (JSON) -> Void, faliure: @escaping (Error) -> Void) {
        
        let url = "\(domainUrl)\(feedUrl)list/place/\(placeId)/\(userId)"
        
        AF.request(url, method: .get).responseJSON{(response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
            case .failure(_):
                let error : Error = response.error!
                faliure(error)
                break
            }
        }
        
    }
    
    
}

