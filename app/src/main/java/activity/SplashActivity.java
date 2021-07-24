package activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.freeplayer.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import Dao.VersionControlResponse;
import Helper.HttpHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SplashActivity extends AppCompatActivity {

    Animation animation;
    HttpHelper httpHelper=new HttpHelper();
    final  String TAG=this.getClass().getName();
    Dialog dialog;
    int progressValue;
    VersionControlResponse vp;
    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showDialog();

                    break;
                case 2:
                    progressDialog.setProgress(progressValue);
                    if(progressValue>=100){
                        progressDialog.dismiss();//关闭进度条对话框
                    }
                    break;
                case 3:
                    try {
                        showSmsCopyDialog();
                        downloadUpdateMethod("http://183.6.117.112:7889/"+vp.getFileName(),vp.getFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG, "onResponse: "+"开始检查");
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        requestPermission();
        checkUpdate();


    }


    private void requestPermission(){

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }
    }


    private void checkUpdate(){
        String url="http://183.6.117.112:7889/update.json.txt";
        httpHelper.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                skip();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                vp=new Gson().fromJson(response.body().string(),new TypeToken<VersionControlResponse>(){}.getType());
                try {
                    if(getPackageManager().getPackageInfo(getPackageName(),0).versionCode<Integer.parseInt(vp.getVersion())){
                        Log.d(TAG, "onResponse: "+"结果：需要更新");
                        //progressBar.setVisibility(View.VISIBLE);
                        handler.sendEmptyMessage(1);
                    }else
                    {
                        skip();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void downloadUpdateMethod(String downUrl,final String fileName) throws IOException {
        final String path= getSDPath();
        System.out.println("path:"+path);
        httpHelper.download(downUrl,path, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                skip();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                File f1=new File(path,fileName);
                if(f1.exists()){
                    f1.delete();
                }
                RandomAccessFile accessFile = new RandomAccessFile(f1, "rw");
                Log.d(TAG, "文件大小：" + body.contentLength());
                // 移动文件指针到断点续传的位置
                accessFile.seek(0);
                // 开始断点续传
                InputStream inputStream = body.byteStream();
                byte[] bytes = new byte[1024];
                int len = inputStream.read(bytes);
                while (len != -1) {
                    accessFile.write(bytes, 0, len);
                    Log.d(TAG, "已下载字节：" + f1.length());
                    progressValue=(int)((double) f1.length()*100/body.contentLength());
                    handler.sendEmptyMessage(2);
                    len = inputStream.read(bytes);
                }
                Log.d(TAG, "文件下载完毕：" + accessFile.getFilePointer());
                //startInstallPermissionSettingActivity();
                installApk(f1);
            }
        });
    }


    private void skip(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void installApk(File file ){
        if(!getPackageManager().canRequestPackageInstalls()){
            startInstallPermissionSettingActivity();
        }
        if (Build.VERSION.SDK_INT >= 24) {// 判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(this, "com.example.freeplayer.provider", file);// 在AndroidManifest中的android:authorities值
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "demo.apk")),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }


    /**
     * 开启设置安装未知来源应用权限界面
     * @param
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {

        Intent intent = new Intent();
        //获取当前apk包URI，并设置到intent中（这一步设置，可让“未知应用权限设置界面”只显示当前应用的设置项）
        Uri packageURI = Uri.parse("package:" + this.getPackageName());
        intent.setData(packageURI);
        //设置不同版本跳转未知应用的动作
        if (Build.VERSION.SDK_INT >= 26) {
            //intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
            intent.setAction(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        } else {
            intent.setAction(android.provider.Settings.ACTION_SECURITY_SETTINGS);
        }
        startActivity(intent);
        Toast.makeText(this, "请打开未知应用安装权限", Toast.LENGTH_SHORT).show();
    }


    public String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().toString();// 获取根目录
        } else {
            return getCacheDir().getAbsolutePath(); // 获取内置内存卡目录
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("自动升级");
        //    设置Content来显示一个信息
        builder.setMessage("发现新版本，是否更新？");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               handler.sendEmptyMessage(3);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeDialog();
                skip();
            }
        });
        dialog=builder.show();
    }

    private void closeDialog(){
        dialog.dismiss();
    }

    private void showSmsCopyDialog() {
        progressDialog = new ProgressDialog(this); //注意：这里的上下文必须是this，而不能用getApplicationContext()
        progressDialog.setIcon(R.drawable.qqmusic); //设置对话框的图标
        progressDialog.setTitle("正在下载更新，请稍后");    //设置对话框标题
        progressDialog.setMax(100);        //设置进度条的最大值
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //指定进度条的样式为水平
        progressDialog.show();
    }
}
