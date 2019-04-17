package com.pzdf.essayjoke;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pzdf.essayjoke.mode.DiscoverListResult;
import com.pzdf.essayjoke.process.JobWakeUpService;
import com.pzdf.essayjoke.process.MessageService;
import com.pzdf.essayjokebaselibrary.dialog.AlertDialog;
import com.pzdf.essayjokebaselibrary.fixBug.FixDexManager;
import com.pzdf.essayjokebaselibrary.ioc.ExceptionCrashHandler;
import com.pzdf.essayjokebaselibrary.ioc.OnClick;
import com.pzdf.framelibrary.BaseSkinActivity;
import com.pzdf.framelibrary.DefaultNavigationBar;
import com.pzdf.framelibrary.banner.BannerAdapter;
import com.pzdf.framelibrary.banner.BannerViewPager;
import com.pzdf.framelibrary.skin.SkinResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author jihl
 */
public class MainActivity extends BaseSkinActivity implements View.OnClickListener {
    private BannerViewPager mBannerViewPager;
    @Override
    protected void initDate() {
        //获取上次崩溃的异常信息，上传到服务器。
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if (crashFile.exists()) {
            //上传服务器
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer = new char[1024];
                int len = 0;
                while ((len = inputStreamReader.read(buffer)) != -1) {
                    String str = new String(buffer, 0, len);
                    System.out.println("str==" + str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initAliFotFix();


//        initFixDexBug();


//        IDaoSupport<Person> daoSoupport = DaoSupportFactory.getFactory().getDao(Person.class);
//        daoSoupport.insert(new Person("helloJ",18));


        //路径  apk  参数都需要放到jni 防止反编译
       /* HttpUtils.with(this).url("http://is.snssdk.com/2/essay/discovery/v3/")// 路径 apk  参数都需要放到jni
                .addParam("iid", "6152551759")
                .addParam("aid", "7")
                .cache(true)
                .exchangeEngine(new OkHttpEngine())  // 切换引擎
                .execute(new HttpCallBack<DiscoverListResult>() {
            @Override
            public void onPreExecute(Context context, Map<String, Object> params) {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(DiscoverListResult result) {
                System.out.println("result====" + result);
                //String --->对象



                //获取到接口返回数据，初始化广告位
                initBanner(result.getData().getRotate_banner().getBanners());
            }

        });*/


        //1、请求参数很多 但是有些是公用的
        //2、回调每次都用 Json转换  但是不能直接用泛型
        //3、数据库的问题，缓存 新闻类特有效果，第三方的数据库都是有缓存在data/data/xxx/databases


        // 1. 为什么用Factory  目前的数据是在  内存卡中  有时候我们需要放到 data/data/xxx/database
        // 获取的Factory不一样那么写入的位置   是可以不一样的

        // 2. 面向接口编程，获取IDaoSoupport 那么不需要关心实现 ，目前的实现 是我们自己写的，方便以后使用第三方的

        // 3. 就是为了高扩展

        // 强调一下  科学教学 ， 反射  泛型  设计模式  单个单个讲   后面不断结合（设计模式 + 泛型 + 反射）
        // 特别注意一定要敲


//        List<Person> persons=new ArrayList<>();
//        for (int i=0;i<1000;i++){
//            persons.add(new Person("J",i));
//        }
//        //litePal使用
//        DataSupport.saveAll(persons);
//        //自定义使用
//        daoSoupport.insert(persons);
//
//
//        List<Person> personList=daoSoupport.querySupport()
//                .selection("age = ?")
//                .selectionArgs("2")
//                .query();


    }

    /**
     *
      * @param banners
     */
    private void initBanner(final List<DiscoverListResult.DataBean.RotateBannerBean.BannersBean> banners) {
        mBannerViewPager.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView bannerIv=null;
                if (convertView==null) {
                    bannerIv=new ImageView(MainActivity.this);
                    bannerIv.setScaleType(ImageView.ScaleType.FIT_XY);
                }else{
                    bannerIv=(ImageView)convertView;
                }
                String imagePath=banners.get(position).getBanner_url().getUrl_list().get(position).getUrl();
                Glide.with(MainActivity.this)
                    .load(imagePath)
                        //站位图  默认图片
                    .placeholder(R.drawable.banner_default)
                    .into(bannerIv);
                return bannerIv;
            }

            @Override
            public int getCount() {
                return banners.size();
            }

            @Override
            public String getBannerDesc(int position) {
                return banners.get(position).getBanner_url().getTitle();
            }
        });
    }

    /**
     * 自定义修改
     */
    private void initFixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        if (fixFile.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(this, "success J", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed J", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * ali热修复
     */
    private void initAliFotFix() {
        //每次启动去后台下载差分包fix.apatch 然后修复本地bug
        System.out.println("path===" + Environment.getExternalStorageDirectory());

        //获取本地内存卡里面的fix.apatch
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (fixFile.exists()) {
            //修复bug
            try {
                //立刻生效不需重启
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "faild", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void initView() {
        viewById(R.id.acb_text).setOnClickListener(this);
        viewById(R.id.acb_text_t).setOnClickListener(this);
        viewById(R.id.acb_reple).setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this,
                (ViewGroup) findViewById(R.id.view_group))
                .setTitle("投稿")
                .setRightText("发布")
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "hello J", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRightIcon(R.drawable.account_icon_weibo)
                .builder();

    }

    @Override
    protected void setContentView() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        //xUtils
        //x.view().inject(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acb_text:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setContentView(R.layout.detail_comment_dialog)
                        .setText(R.id.submit_btn, "J")
                        .addDefaultAnimation()
                        .fromBottom(true)
                        .fullWidth()
                        .show();

                final EditText commit = dialog.getView(R.id.comment_editor);

                dialog.setOnClickListener(R.id.account_icon_weibo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "J" + commit.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.acb_text_t:

                break;
            case R.id.acb_reple:

                //读取本地*.skin里面的一个资源
                Resources superRes = getResources();
                try {
                    //创建AssetManager
                    AssetManager asset = AssetManager.class.newInstance();
                    //添加本地下载好的的资源皮肤
                    Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
                    method.setAccessible(true);
                    //反射执行方法
                    method.invoke(asset, Environment.getExternalStorageDirectory()
                            .getAbsolutePath()
                            + File.separator
                            + "皮肤文件");
                    Resources resources = new Resources(asset, superRes.getDisplayMetrics(), superRes.getConfiguration());

                    int drawableId = resources.getIdentifier("", "drawable", "");
                    Drawable drawable = resources.getDrawable(drawableId);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            default:
                break;
        }
    }

    @OnClick(R.id.acb_process)
    public void startProcess(View view) {
        startService(new Intent(this, MessageService.class));
        startService(new Intent(this, MessageService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startService(new Intent(this, JobWakeUpService.class));
        }
    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        //做一些第三方的改变
        Toast.makeText(this, "换肤了。", Toast.LENGTH_LONG).show();
    }

}
