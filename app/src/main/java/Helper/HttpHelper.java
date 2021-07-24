package Helper;


import android.text.Editable;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpHelper {


    private static final String kwCookie="_ga=GA1.2.1455935171.1621933479; _gid=GA1.2.1011836789.1621933479; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1621933480,1621933533,1621933760,1621934528; reqid=d822ef7fX53a0X4ba8XaeaaXe5e728aac8e9; gtoken=GUhWWxeRcPZC; gid=e0f5026c-21b8-4ffc-9b71-c54286ad772f; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1621935249; _gat=1; kw_token=IE4QDSW3Y6";
    private static final String kgCookie="kg_mid=2a8d3831bb3f6df045c357e0405ec7b5; kg_dfid=2A4iVc1tS1v50mcyW54KTthw; kg_dfid_collect=d41d8cd98f00b204e9800998ecf8427e; Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1590572979; ACK_SERVER_10015=%7B%22list%22%3A%5B%5B%22bjlogin-user.kugou.com%22%5D%5D%7D; ACK_SERVER_10016=%7B%22list%22%3A%5B%5B%22bjreg-user.kugou.com%22%5D%5D%7D; ACK_SERVER_10017=%7B%22list%22%3A%5B%5B%22bjverifycode.service.kugou.com%22%5D%5D%7D; kg_mid_temp=2a8d3831bb3f6df045c357e0405ec7b5; KuGooRandom=66811590573014726; Hm_lpvt_aedee6983d4cfc62f509129360d6bb3d=1590573430";
    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public void doGetBySearch(String keyWords, String sourceType, Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        Request request;
        String reqUrl;
        String tempCookie=null;
        if(sourceType.equals("KW")){
            reqUrl=String.format("http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key=%s&pn=%s&rn=20&httpsStatus=1&reqId=d7446a30-bd3a-11eb-b1bb-1104bf986836", keyWords, 1);
            tempCookie=kwCookie;
            request = new Request.Builder().addHeader("cookie",tempCookie)
                    .addHeader("csrf","IE4QDSW3Y6")
                    .addHeader("Host","www.kuwo.cn")
                    .addHeader("Referer","http://www.kuwo.cn/search/list?key="+keyWords)
                    .url(reqUrl).build();
        }else if(sourceType.equals("KG")){
            reqUrl=String.format("https://songsearch.kugou.com/song_search_v2?keyword=%s&page=%d&pagesize=100&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0",keyWords,1);
            tempCookie=kgCookie;
            request = new Request.Builder().addHeader("cookie",tempCookie)
                    .url(reqUrl).build();
        }else
        {
            return;
        }
        client.newCall(request).enqueue(callback);
    }

    public void doGetByKW(String url,Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        Request request;
        String tempCookie=null;
        tempCookie=kwCookie;
        request = new Request.Builder().addHeader("cookie",tempCookie)
                    .addHeader("csrf","IE4QDSW3Y6")
                    .addHeader("Host","www.kuwo.cn")
                    .addHeader("Referer","http://www.kuwo.cn/search/list?key=")
                    .url(url).build();
        client.newCall(request).enqueue(callback);
    }

    public void doGet(String url,Callback callback)
    {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }



    public void doSongGet(String url,Callback callback)
    {

        OkHttpClient client=new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        cookieStore.put(httpUrl.host(),list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();
        Request request = new Request.Builder()
                .url(url).build();
        client.newCall(request).enqueue(callback);
    }

    public void download(final String url,final String fileUrl,Callback callback) throws IOException {
        System.out.println("当前请求URL"+url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

}
