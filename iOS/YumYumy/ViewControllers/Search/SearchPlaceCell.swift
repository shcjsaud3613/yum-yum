//
//  SearchPlaceCell.swift
//  YumYumy
//
//  Created by 염성훈 on 2021/05/20.
//

import UIKit

class SearchPlaceCell: UICollectionViewCell {
    
    static let identifier = "SearchPlaceCell"
    @IBOutlet var thumbnailView: UIImageView!
    @IBOutlet var userProfileView: UIImageView!
    @IBOutlet var userNameLabel: UILabel!
    @IBOutlet var reviewLabel: UILabel!
    @IBOutlet var titleLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        thumbnailView.contentMode = .scaleToFill
        imageMakeRouded(imageview: userProfileView)
    }

}
