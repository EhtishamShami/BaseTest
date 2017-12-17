package vophamtuananh.com.basetest;

import android.app.Activity;
import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vophamtuananh.base.imageloader.ImageLoader;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vophamtuananh.com.basetest.service.ApiService;

/**
 * Created by vophamtuananh on 12/17/17.
 */

public class NormalApplication extends Application {

    public static NormalApplication get(Activity activity) {
        return NormalApplication.class.cast(activity.getApplication());
    }

    private ImageLoader imageLoader;

    private ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        //imageLoader = new ImageLoader(this);

        // Gson
//        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
//        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
//        Gson gson = gsonBuilder.create();

        // logging interceptor
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        // okhttp
//        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                .addInterceptor(interceptor)
//                .build();

        // Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(okHttpClient)
//                .baseUrl("https://abc.xyz.com")
//                .build();

//        apiService = retrofit.create(ApiService.class);

    }

    public ImageLoader imageLoader() {
        return imageLoader;
    }

    public ApiService apiService() {
        return apiService;
    }
}
