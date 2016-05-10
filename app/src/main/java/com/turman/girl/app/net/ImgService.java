package com.turman.girl.app.net;

import com.turman.girl.app.bean.ImageListResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dqf on 2016/5/6.
 */
public interface ImgService {
    @GET(NetHelper.LIST)
    Observable<ImageListResult> getList(@Query("page")int page, @Query("rows") int rows,@Query("id") int id);
}
