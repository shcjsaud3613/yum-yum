//
//  WebApiManagerUser.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import Alamofire
import SwiftyJSON

extension WebApiManager {
    func checkUser(userEmail: String, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(userUrl)email/\(userEmail)"
        AF.request(url, method: .get)
            .responseJSON { (response) in
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

    func login(userEmail: String, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(userUrl)login/\(userEmail)"
        AF.request(url, method: .get)
            .responseJSON { (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    success(json)
                    break
                case .failure(_):
                    let error = response.error!
                    failure(error)
                    break
                }
            }
    }
    
    func signup(user: User, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {

        let url = "\(domainUrl)\(signUpUrl)"
        
        AF.request(url, method: .post, parameters: user, encoder: JSONParameterEncoder.default)
          .responseJSON { (response) in
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
    
    func updateProfile(user: User, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        
        let url = "\(domainUrl)user"
        AF.request(url, method: .put, parameters: user, encoder: JSONParameterEncoder.default)
          .responseJSON { (response) in
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
    
    func getUserInfo(userId: Int, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(userUrl)/\(userId)"
        
        AF.request(url, method: .get).responseJSON{ (response) in
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
    
    
    //MARK: - 프로필사진 URL을 반환하는 메서드
    func createProfilePath(image: UIImage, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(userUrl)profile"
        let imgData = image.jpegData(compressionQuality: 0.2)!
        let headers: HTTPHeaders
        headers = [
            "Content-type": "multipart/form-data",
            "Content-Disposition" : "form-data",
        ]
        
        let parameters = ["name": "name"]

        let call = AF.upload(multipartFormData:{ multipartFormData in
            for (key, value) in parameters {
                multipartFormData.append(value.data(using: .utf8)!, withName: key)
                } //Optional for extra parameters
            multipartFormData.append(imgData, withName: "file", fileName: "file.jpeg",mimeType: "image/jpeg")
        }, to: url, method: .post, headers: headers)
        
        call.response{ res in
            switch res.result {
            case .success(_):
                let json = JSON(res.data! as Any)
                success(json)
                break
            case .failure(_):
                let error: Error = res.error!
                failure(error)
                break
            }
        }
        
    }
        
        
}
