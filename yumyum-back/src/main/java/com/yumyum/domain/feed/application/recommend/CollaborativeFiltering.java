package com.yumyum.domain.feed.application.recommend;

import com.yumyum.domain.feed.application.FeedResponseService;
import com.yumyum.domain.feed.dao.FeedDao;
import com.yumyum.domain.feed.dao.FeedFindDao;
import com.yumyum.domain.feed.dao.LikeDao;
import com.yumyum.domain.feed.dto.FeedResponse;
import com.yumyum.domain.feed.dto.SimilarityDto;
import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.dao.UserDao;
import com.yumyum.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CollaborativeFiltering {

    private final UserDao userDao;
    private final FeedDao feedDao;
    private final FeedFindDao feedFindDao;
    private final LikeDao likeDao;
    private final FeedResponseService feedResponseService;

    public List<FeedResponse> getRecommendList(final Long userId){
        final List<User> users = userDao.findAll();
        final List<Feed> feeds = feedDao.findAll();
        final List<Like> likes = likeDao.findAll();
        int N = users.get(users.size()-1).getId().intValue();
        int M = feeds.get(feeds.size()-1).getId().intValue();

        double[][] map = new double[N+1][M+1];
        for(Like like : likes){
            int i = like.getUser().getId().intValue();
            int j = like.getFeed().getId().intValue();
            map[i][j] = 1;
        }

        double[][] smap = new double[N+1][N+1];
        for(int i = 1; i <= N; i++){
            for(int j = 1; j <= N; j++){
                smap[i][j] = getSimilarity(map, M, i, j);
            }
        }

        for(int i = 1; i <= N; i++){
            for(int j = 1; j <= M; j++){
                if(map[i][j] == 1) continue;
                map[i][j] = getPrediction(map, smap, N, i, j);
            }
        }

        final List<SimilarityDto> sList = new ArrayList<>();
        int u = userId.intValue();
        for(int m = 1; m <= M; m++){
            if(map[u][m] == 1) continue; // 이미 좋아요 한 피드는 제외
            if(map[u][m] == 0) continue; // 비추천 피드는 제외
            Long feedId = new Long(m);
            Feed feed = feedFindDao.findById(feedId);
            if(feed.getUser().getId() == userId) continue; // 본인이 작성한 피드는 제외
            sList.add(new SimilarityDto(feedId, map[u][m]));
        }

        Collections.sort(sList, new CollaborativeFiltering.SimilarityComparator());

        final List<Feed> fList = new ArrayList<>();
        for(SimilarityDto dto : sList){
            Long feedId = dto.getFeedId();
            fList.add(feedFindDao.findById(feedId));
        }
        final List<FeedResponse> list = feedResponseService.entityToDto(fList, userId);
        return list;
    }

    public double getSimilarity(double[][] map, int M, int a, int b) {
        double sumTop = 0;
        for(int m = 0; m < M; m++) {
            if(map[a][m] == -1 || map[b][m] == -1) continue;
            sumTop += map[a][m] * map[b][m];
        }

        double sumBottom_A = 0, sumBottom_B = 0;
        for(int m = 0; m < M; m++) {
            if(map[a][m] == -1 || map[b][m] == -1) continue;
            sumBottom_A += Math.pow(map[a][m], 2);
            sumBottom_B += Math.pow(map[b][m], 2);
        }

        double sumButtom = Math.sqrt(sumBottom_A) * Math.sqrt(sumBottom_B);

        if(sumButtom == 0) return 0;

        double similarity = sumTop / sumButtom;

        return Math.round(similarity*100)/100.0;
    }

    public double getPrediction(double[][] map, double[][] smap, int N, int a, int m) {
        double sumTop = 0, sumBottom = 0;
        for(int n = 0; n < N; n++) {
            if(n == a) continue;
            sumTop += smap[n][a] * map[n][m];
            sumBottom += smap[n][a];
        }

        if(sumBottom == 0) return 0;

        double prediction = sumTop / sumBottom;

        return Math.round(prediction*100)/100.0;
    }

    public class SimilarityComparator implements Comparator<SimilarityDto> {
        @Override
        public int compare(SimilarityDto a, SimilarityDto b){
            if (a.getSimilarity() > b.getSimilarity()) return 1;
            else if (a.getSimilarity() < b.getSimilarity()) return -1;
            else return 0;
        }
    }
}
