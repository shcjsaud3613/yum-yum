//
//  VideoPlayBackVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import UIKit
import  AVFoundation

class VideoPlayBackVC: UIViewController {
    
    static func instance(videoUrl: URL) -> VideoPlayBackVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "VideoPlayBackVC") as! VideoPlayBackVC
        vc.videoUrl = videoUrl
        return vc
    }
    
    let avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!

    var videoUrl: URL!
    
    @IBOutlet var retryBtn: UIButton!
    @IBOutlet var nextBtn: UIButton!
    @IBOutlet weak var videoView: UIView!

    
    @IBAction func didTapPrevButton(_ sender: Any) {
        print("돌아가~")
//        self.navigationController?.popViewController(animated: true)
//        self.dismiss(animated: true, completion: nil)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setLayout()
        playBackVideo()
        print("PlayVC URL 확인: ", videoUrl)
        let retryImage = UIImage(named: "ic_retry")
        let nextImage = UIImage(named: "ic_next")
        let stencil = retryImage!.withRenderingMode(.alwaysTemplate)
        let next = nextImage!.withRenderingMode(.alwaysTemplate)
        retryBtn.setImage(stencil, for: .normal)
        retryBtn.tintColor = .systemYellow
        nextBtn.setImage(next, for: .normal)
        nextBtn.tintColor = .systemYellow
    }
    func setLayout() {
        self.navigationController?.navigationBar.isHidden = true
    }
    
    func playBackVideo() {
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        videoView.layer.insertSublayer(avPlayerLayer, at: 0)
        view.layoutIfNeeded()
        let playerItem = AVPlayerItem(url: videoUrl as! URL)
        avPlayer.replaceCurrentItem(with: playerItem)
        avPlayer.play()
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        guard let nextVC = segue.destination as? ReviewVC
        else {
            return
        }
        print("확인!!", videoUrl)
        nextVC.videoUrl = videoUrl
    }
    

}
