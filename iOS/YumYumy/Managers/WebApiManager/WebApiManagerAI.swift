//
//  WebApiManagerAI.swift
//  YumYumy
//
//  Created by 염성훈 on 2021/05/19.
//

import Foundation
import Alamofire
import SwiftyJSON

extension WebApiManager {
    func postAiVdieo(mediaUrl: URL, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(AiDomainUrl)video/list"
        let headers: HTTPHeaders = [
            "Content-Type": "multipart/form-data"
        ]
        print("실행합니까???")
        AF.upload(multipartFormData: {(formData) in
            do {
                let fileName = UUID()
                let mediaStr = "\(mediaUrl)"
                print("미디어 STR!!")
                print(mediaStr)
                if mediaStr.contains(".jpeg"){
                    // 사진처리
                } else if mediaStr.contains("mp4") {
                    let videoData = try Data(contentsOf: mediaUrl)
                    print("비디오데이터들어와?")
                    dump(videoData)
                    formData.append(mediaUrl, withName: "video", fileName: "\(fileName).mp4", mimeType: "video/mp4")
                }
            } catch {
                debugPrint("Couldn't get Data from URL: \(mediaUrl): \(error)")
            }
        }, to: url, method: .post, headers: headers)
        .response { (response) in
            print("====af======")
               dump(response)
               print(response.data)
               print(response)
               print(response.result)
               print(response.value)
               print("====end=====")
            switch response.result {
            case .success(_):
                print(response.data!)
                let json = JSON(response.data! as Any)
                success(json)
                break
            case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
            }
        }
    }
    
}
