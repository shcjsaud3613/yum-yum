package com.yumyum.domain.feed.dao;

import com.yumyum.domain.feed.entity.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhoneFindDao {

    private final PhoneDao phoneDao;

    public boolean checkPhoneHeaderExist(final String phoneNum){
        fiilPhoneData();

        String[] arr = new String[3];
        if(phoneNum.contains("-")){
            arr = phoneNum.split("-");
        }else if(phoneNum.contains(".")){
            arr = phoneNum.split(".");
        }else{
            return false;
        }

        return phoneDao.existsByPhoneHeader(arr[0]);
    }

    public void fiilPhoneData(){
        String[] areaNumbers = {"02", "031", "032", "033", "041", "042", "043", "044",
                "051", "052", "053", "054", "055", "061", "062", "063", "064"};
        Long count = phoneDao.count();
        if(count == 0){ // DB에 데이터가 없으면 지역번호를 채워넣는다.
            for(String areaNumber : areaNumbers){
                boolean exist = phoneDao.existsByPhoneHeader(areaNumber);
                if(exist) continue;
                Phone phone = Phone.builder()
                        .phoneHeader(areaNumber)
                        .build();
                phoneDao.save(phone);
            }
        }
    }
}
