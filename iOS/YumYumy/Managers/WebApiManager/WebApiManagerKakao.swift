//
//  WebApiManagerKakao.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/06.
//

import Foundation
import Alamofire
import SwiftyJSON

extension WebApiManager {
    
    private var kakaoApiKey: String {
      get {
        guard let filePath = Bundle.main.path(forResource: "KAKAO-Info", ofType: "plist") else {
          fatalError("Couldn't find file 'KAKAO-Info.plist'.")
        }
        
        let plist = NSDictionary(contentsOfFile: filePath)
        guard let value = plist?.object(forKey: "API_KEY") as? String else {
          fatalError("Couldn't find key 'API_KEY' in 'KAKAO-Info.plist'.")
        }
        
        return value
      }
    }
    
    func searchPlace(searchKey: String, logitudeX: Double, latitudeY:Double, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let kakaoUrl = "https://dapi.kakao.com/v2/local/search/keyword.json"
        
        let headers: HTTPHeaders = [
            "Authorization": "KakaoAK \(kakaoApiKey)"
        ]
        
        let parameters: [String: Any] = [
            "query": searchKey,
            "page": 1,
            "size": 15,
            "x": logitudeX,
            "y": latitudeY
        ]
        
        AF.request(kakaoUrl, method: .get, parameters: parameters, headers: headers)
            .responseJSON { (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.data! as Any)
                    success(json)
                case .failure(_):
                    let error = response.error!
                    failure(error)
                    break
                }
            }
        
    }
}
