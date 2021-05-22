//
//  UIcollectionViewCellExtension.swift
//  YumYumy
//
//  Created by 염성훈 on 2021/05/20.
//

import Foundation
extension UICollectionViewCell {
    func imageMakeRouded(imageview:UIImageView) {
        imageview.layer.borderWidth = 2
        imageview.layer.masksToBounds = false
        imageview.layer.borderColor = UIColor.systemYellow.cgColor
        imageview.layer.cornerRadius = imageview.frame.height/2
        imageview.clipsToBounds = true
        imageview.contentMode = .scaleAspectFill
    }
}
