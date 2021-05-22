//
//  SearchVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/11.
//

import UIKit

class SearchVC: UIViewController , CustomMenuBarDelegate{
    static func instance() -> SearchVC {
        let vc = UIStoryboard.init(name: "Home", bundle: nil).instantiateViewController(withIdentifier: "SearchVC") as! SearchVC
        return vc
    }

    var feedResult: [Feed]?
    var placeResult: [Place]?
    var itemId : Int?

    
    var pageCollectionView: UICollectionView = {
        let collectionViewLayout = UICollectionViewFlowLayout()
        collectionViewLayout.scrollDirection = .horizontal
        let collectionView = UICollectionView(frame: CGRect(x: 0, y: 0, width: 100, height: 100), collectionViewLayout: collectionViewLayout)
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    

    var customMenuBar: CustomMenuBar = CustomMenuBar()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setLayout()
    }
    //MARK: Setup view
    func setLayout() {
        let searchController = UISearchController(searchResultsController: nil)
        searchController.delegate = self
        searchController.searchBar.delegate = self
        searchController.searchBar.placeholder = "검색"
        searchController.obscuresBackgroundDuringPresentation = false
    
        self.navigationItem.title = "Search"
        self.navigationItem.searchController = searchController
        self.navigationItem.hidesSearchBarWhenScrolling = false
        self.navigationController?.navigationBar.transparentNavigationBar()
        
        self.view.backgroundColor = .white
        
        navigationController?.hidesBarsOnSwipe = true
        setupCustomTabBar()
        setupPageCollectionView()
    }
    
    func setupCustomTabBar(){
        self.view.addSubview(customMenuBar)
        customMenuBar.delegate = self
        customMenuBar.translatesAutoresizingMaskIntoConstraints = false
        customMenuBar.indicatorViewWidthConstraint?.constant = self.view.frame.width / 2
        customMenuBar.indicatorViewWidthConstraint?.isActive = true
        customMenuBar.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        customMenuBar.trailingAnchor.constraint(equalTo: self.view.trailingAnchor).isActive = true
        customMenuBar.topAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.topAnchor).isActive = true
        customMenuBar.heightAnchor.constraint(equalToConstant: 60).isActive = true
    }
    
    func customMenuBar(scrollTo index: Int) {
        let indexPath = IndexPath(row: index, section: 0)
        self.pageCollectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: true)
    }
    
    func setupPageCollectionView(){
        pageCollectionView.backgroundColor = .black
        pageCollectionView.delegate = self
        pageCollectionView.dataSource = self
        pageCollectionView.showsHorizontalScrollIndicator = false
        pageCollectionView.isPagingEnabled = true
        pageCollectionView.register(UINib(nibName: PageCell.reusableIdentifier, bundle: nil), forCellWithReuseIdentifier: PageCell.reusableIdentifier)
        self.view.addSubview(pageCollectionView)
        pageCollectionView.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        pageCollectionView.trailingAnchor.constraint(equalTo: self.view.trailingAnchor).isActive = true
        pageCollectionView.bottomAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.bottomAnchor).isActive = true
        pageCollectionView.topAnchor.constraint(equalTo: self.customMenuBar.bottomAnchor).isActive = true
    }
    


}


extension SearchVC: UISearchControllerDelegate {
    
}

extension SearchVC: UISearchBarDelegate {
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        guard let searchKey = searchBar.text else { return }
        WebApiManager.shared.searchStore(searchKey: searchKey) { (result) in
            print(result["data"])
            self.placeResult = result["data"].arrayValue.compactMap{Place(searchJson: $0)}
            print("나와뇽?")
            print(self.placeResult)
            self.pageCollectionView.reloadData()
        } failure: { (error) in
            print(#function, error)
        }
        
        WebApiManager.shared.searchFeed(userId: 24, searchKey: searchKey) { (result) in
            print("feed", result["data"])
            self.feedResult = result["data"].arrayValue.compactMap{ Feed(feedJson: $0) }
            self.pageCollectionView.reloadData()
        } failure: { (error) in
            print(#function, error)
        }
    }
}


//MARK:- UICollectionViewDelegate, UICollectionViewDataSource
extension SearchVC: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: PageCell.reusableIdentifier, for: indexPath) as! PageCell
        
        cell.placeResult = self.placeResult
        cell.feedResult = self.feedResult
        
        cell.collectionView.reloadData()
        cell.delegate = self
        cell.placedelegate = self
        
        switch indexPath.row {
        case 0:
            cell.tableView.isHidden = true
            cell.collectionView.isHidden = false
            cell.collectionView.reloadData()
        case 1:
            cell.tableView.isHidden = false
            cell.collectionView.isHidden = true
            cell.tableView.reloadData()
        default: break
            
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 4
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        customMenuBar.indicatorViewLeadingConstraint.constant = scrollView.contentOffset.x / 2
    }
    
    func scrollViewWillEndDragging(_ scrollView: UIScrollView, withVelocity velocity: CGPoint, targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        let itemAt = Int(targetContentOffset.pointee.x / self.view.frame.width)
        let indexPath = IndexPath(item: itemAt, section: 0)
        customMenuBar.customTabBarCollectionView.selectItem(at: indexPath, animated: true, scrollPosition: [])
    }
}
//MARK:- UICollectionViewDelegateFlowLayout
extension SearchVC: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: pageCollectionView.frame.width, height: pageCollectionView.frame.height)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
}

extension SearchVC: pageCellDelegate {
    func sendToSearchFeed(itemId: Int, feedList:[Feed]) {
        let searchFeedVC = UIStoryboard(name: "Search", bundle: nil).instantiateViewController(identifier: "SearchFeedVC") as! SearchFeedVC
        searchFeedVC.itemId = itemId
        searchFeedVC.feedList = feedList
        searchFeedVC.modalPresentationStyle = .fullScreen
        self.present(searchFeedVC, animated: true)
    }
}

extension SearchVC : pageCellPlaceDelegate {
    func sendToSearchPlaceFeed(itemId: Int, place: Place) {
        let searchPlaceVC = UIStoryboard(name: "Search", bundle: nil).instantiateViewController(identifier: "SearchPlaceVC") as! SearchPlaceVC
        
        searchPlaceVC.place = place
        let userId = UserDefaults.getLoginedUserInfo()!["id"].intValue
        WebApiManager.shared.getPlaceFeedList(placeId: place.id!, userId: userId){
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                searchPlaceVC.placeFeedList = results.arrayValue.compactMap({Feed(feedJson: $0)})
                self.navigationController?.pushViewController(searchPlaceVC, animated: true)
            } else {
                print("장소 피드 리스트 오류!")
            }
        }faliure: { (error) in
            print(error)
        }
        
    }
}
