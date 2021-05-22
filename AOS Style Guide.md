# 1. Android Project Guideline

## 1.1 Basic Rules

- kotlin coding convention은 다음의 글을 따른다.

  https://kotlinlang.org/docs/coding-conventions.html

## 1.2 File naming

### 1.2.1 Resources files

#### 1.2.1.1 Layout files

{component name}_{name}

| Component        | Class Name             		| Layout Name                   	|
| ---------------- | ---------------------------------- | ------------------------------------- |
| Activity         | `UserProfileActivity`  		| `activity_user_profile.xml`   	|
| Fragment         | `SignUpFragment`       		| `fragment_sign_up.xml`        	|
| FragmentDialog   | `ChangePasswordFragmentDialog` 	| `fragment_dialog_change_password.xml` |
| Dialog           | `ChangePasswordDialog` 		| `dialog_change_password.xml` 		|
| Viewholder       | `HomeMainViewHolder`            | `viewholder_home_main.xml`         |
| CustomView       | `TitleBar`                    	| `view_title_bar.xml`	     	  	|

#### 1.2.1.2 Layout resources

| Component        | Resource ID             		
| ---------------- | ---------------------------|
| CustomView       | `ct_`  			|
| View		   | `v_`       		|
| ImageView	   | `iv_`       		|
| TextView	   | `tv_`       		|
| RecyclerView	   | `rc_`       		|
| NestedScrollView	   | `nsv_`       		|

#### 1.2.1.3 String resources

String resource는 사용되는 View의 이름과 동일하게 한다. 

| View ID        | String Resource ID             		
| ---------------- | ---------------------------|
| `ct_title`       | `ct_title`  			|
| `tv_title`		   | `tv_title`       		|


## 1.3 Architecture

아키텍처는 MVVM구조를 따르며, 자세한 내용은 다음과 같다.

https://developer.android.com/jetpack/guide?hl=ko

<img width="705" alt="스크린샷 2021-04-09 오후 10 18 11" src="https://user-images.githubusercontent.com/47018460/114186421-ccc18e80-9981-11eb-999e-d9a8134bc4da.png">

