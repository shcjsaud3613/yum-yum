//
//  PeopleCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/13.
//

import UIKit

class PeopleCell: UICollectionViewCell {
    
    @IBOutlet var foodImgView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        foodImgView.contentMode = .scaleAspectFill
    }

    
}
