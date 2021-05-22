//
//  ViewController.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/13.
//

import UIKit
import AVFoundation
import Lottie


class ViewController: UIViewController {

    
    @IBOutlet var oneView: UIView!
    
    @IBOutlet var twoView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let animationview = AnimationView(name: "ic_vomited")
        animationview.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview.center = oneView.center
        animationview.contentMode = .scaleAspectFit
        oneView.addSubview(animationview)
        animationview.play()
        animationview.loopMode = .loop

        let animationview2 = AnimationView(name: "ic_lol")
        animationview2.frame = CGRect(x: 0, y: 0, width: 100, height: 100)
        animationview2.center = twoView.center
        animationview2.contentMode = .scaleAspectFit
        twoView.addSubview(animationview2)
        animationview2.play()
        animationview2.loopMode = .loop
    }
    

}



