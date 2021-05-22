//
//  UITabBarExtension.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/12.
//

import Foundation

extension UIBarButtonItem {

    convenience init(image: UIImage?, size: CGSize, tintColor: UIColor, target: Any?, action: Selector?) {
        let holderView = UIButton()

        let button: UIButton = UIButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        holderView.addSubview(button)

        button.centerYAnchor.constraint(equalTo: holderView.centerYAnchor).isActive = true
        button.centerXAnchor.constraint(equalTo: holderView.centerXAnchor).isActive = true
        button.heightAnchor.constraint(equalToConstant: size.height).isActive = true
        button.heightAnchor.constraint(equalToConstant: size.width).isActive = true

        button.setImage(image?.withRenderingMode(.alwaysTemplate), for: .normal)
        button.imageView?.contentMode = .scaleAspectFit
        button.tintColor = tintColor

        if let target = target, let action = action {
            button.addTarget(target, action: action, for: .touchUpInside)
        }

        self.init(customView: holderView)
    }

}
