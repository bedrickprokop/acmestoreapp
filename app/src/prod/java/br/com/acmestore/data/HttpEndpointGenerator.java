package br.com.acmestore.data;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import br.com.acmestore.App;
import br.com.acmestore.BuildConfig;
import br.com.acmestore.Constants;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.common.net.HttpHeaders.CACHE_CONTROL;
import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

public class HttpEndpointGenerator<T> {

    private static final String TAG = "ACMESTOREAPP";

    public T gen(Class<T> clazz) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(provideOkHttpClient())
                .build();

        return retrofit.create(clazz);
    }

    private OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideOffLineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build();
    }

    private Interceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {

                    @Override
                    public void log(String message) {
                        Log.d(TAG, message);
                    }
                });
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);
        return httpLoggingInterceptor;
    }

    private Interceptor provideOffLineCacheInterceptor() {

        return new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                //Get the request
                Request request = chain.request();

                if (!App.hasNetwork()) {
                    //If no had network, add a cache to request to simulate the request

                    //stale = velhice
                    //maxStale = tempo maximo de reproveito de cache(ja armazenado localmente) - se outra
                    //requisicao for feita(com internet) durante este periodo, a contagem inicia novamente
                    CacheControl cacheControl = new CacheControl.Builder().maxStale(7, TimeUnit.DAYS)
                            .build();

                    //recriate a request with the cache
                    request = request.newBuilder().cacheControl(cacheControl).build();
                }

                //proceed with request to the server or not
                return chain.proceed(request);
            }
        };
    }

    private Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //proceed with request to the server
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                // cache de dois minutos - apos isso ele e deletado
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder().header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(App.getInstance().getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!", e);
        }
        return cache;
    }


}
