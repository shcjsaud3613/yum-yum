//
//  MyCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/30.
//

import UIKit

class MyCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet var foodImageView : UIImageView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        foodImageView.contentMode = .scaleAspectFill
    }
}
