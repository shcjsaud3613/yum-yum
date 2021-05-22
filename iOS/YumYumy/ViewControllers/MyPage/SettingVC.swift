//
//  SettingVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/01.
//

import UIKit

class SettingVC: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    let authSectionLabels : [String] = ["계정 관리"]
    let infoSectionLabels : [String] = ["개인정보 보호정책"]
    let loginSecionsLabels : [String] = ["로그아웃"]
     
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
    }
}

extension SettingVC: UITableViewDelegate{
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // 이방법밖에없나? ㅠㅠ
        switch indexPath.section {
        case 0:
            switch indexPath.row {
            case 0:
                guard let editProfileVC = self.storyboard?.instantiateViewController(identifier: "EditProfileVC") else {
                    return
                }
                self.navigationController?.pushViewController(editProfileVC, animated: true)
            default:
                break
            }
        case 1:
            switch indexPath.row {
            case 0:
                print("개인정보 보호정책을 클릭!")
                guard let editProfileVC = self.storyboard?.instantiateViewController(identifier: "PrivateLawVC") else {
                    return
                }
                self.navigationController?.pushViewController(editProfileVC, animated: true)
            default:
                break
            }
        case 2:
            switch indexPath.row {
            case 0:
                print("로그아웃을 클릭했니다.")
                defaultalert("정말 로그아웃 하시겠어요?") {
                    UserDefaults.removeUserData()
                    self.moveRootView(name: "Accounts", identifier: "LoginVC")
                } failure: { () in
                    // 취소 눌렀을떄 여기가 실행됨
                }
            default:
                break
            }
        default:
            break
    }
    }
}

extension SettingVC: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    // 뷰가 아닌 단순히 헤더에 글자만 추가할경우 아래 메서드 실행
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if section == 0 {
            return "계정"
        } else if section == 1 {
            return "정보"
        } else if section == 2 {
            return "로그인"
        } else {
            return "잘못된값"
        }
    }
    // 해더의 높이 결정
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 40
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return authSectionLabels.count
        case 1:
            return infoSectionLabels.count
        case 2:
            return loginSecionsLabels.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if indexPath.section == 0 {
            let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
            cell.textLabel?.text = authSectionLabels[indexPath.row]
            return cell
        } else if indexPath.section == 1 {
            let cell : UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
            cell.textLabel?.text = infoSectionLabels[indexPath.row]
            return cell
        } else if indexPath.section == 2 {
            let cell : UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
            cell.textLabel?.text = loginSecionsLabels[indexPath.row]
            return cell
        } else {
            let cell : UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
            return cell
        }
    }
}
