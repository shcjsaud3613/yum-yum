//
//  NSObjectExtension.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/12.
//

import Foundation

extension NSObject {
    static var reusableIdentifier: String {
        return String(describing: self)
    }
}
