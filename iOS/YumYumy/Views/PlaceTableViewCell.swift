//
//  PlaceTableViewCell.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/12.
//

import UIKit

class PlaceTableViewCell: UITableViewCell {
    
    static let identifier = "PlaceTableViewCell"

    @IBOutlet var placeNameLabel: UILabel!
    @IBOutlet var placeAddressLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
}
