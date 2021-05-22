//
//  PrivateLawVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/09.
//

import UIKit
import WebKit

class PrivateLawVC: UIViewController {
    
    @IBOutlet var webView: WKWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let weburl = URL(string: "https://week-year.tistory.com/190?category=891710")
        let req = URLRequest(url: weburl!)
        self.webView.load(req)
    }
}
