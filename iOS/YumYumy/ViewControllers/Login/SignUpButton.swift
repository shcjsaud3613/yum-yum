//
//  SignUpButton.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/17.
//

import UIKit

class SignUpButton: UIButton {

    required init(coder aDecoder: NSCoder){
        super.init(coder: aDecoder)!
        self.layer.cornerRadius = 5.0;
        self.backgroundColor = .yellow
        self.tintColor = .black
    }
    
}
