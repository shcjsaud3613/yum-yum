//
//  PlaceSearchCell.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/07.
//

import UIKit

class PlaceSearchCell: UITableViewCell {

    @IBOutlet var nameLabel: UILabel!
    @IBOutlet var addressLabel: UILabel!
    @IBOutlet var reviewLabel: UILabel!
    @IBOutlet var distanceLabel : UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
