//
//  WebApiManagerMedia.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/30.
//

import Foundation
import Alamofire
import SwiftyJSON


extension WebApiManager {
    func createMediaPath(mediaUrl: URL, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)video"
        let headers: HTTPHeaders = [
            "Content-Type": "multipart/form-data"
        ]
        AF.upload(multipartFormData: { (formData) in
            do {
                let fileName = UUID()
                let mediaStr = "\(mediaUrl)"
                
                // MARK: -Todo 사진 처리
                if mediaStr.contains(".jpeg") {
                    // 사진 처리
                } else if mediaStr.contains("mp4") {
                    let videoData = try Data(contentsOf: mediaUrl)
                    dump(videoData)
                    formData.append(mediaUrl, withName: "file", fileName: "\(fileName).mp4", mimeType: "video/mp4")
                }
            } catch {
                debugPrint("Couldn't get Data from URL: \(mediaUrl): \(error)")
            }
        }, to: url, method: .post, headers: headers)
        .response { (response) in
            print(response)
            switch response.result {
            case .success(_):
                dump(response.data!)
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
