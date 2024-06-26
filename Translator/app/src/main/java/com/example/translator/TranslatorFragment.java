package com.example.translator;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.translator.HistoryDbHelper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.translator.R;
import com.example.translator.TranslateResult;
import com.google.gson.Gson;
import com.google.mlkit.nl.translate.Translator;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TranslatorFragment extends Fragment implements View.OnClickListener{

    private LinearLayout beforeLay;//翻译之前的布局
    private NiceSpinner spLanguage;//语言选择下拉框
    private LinearLayout afterLay;//翻译之后的布局
    private TextView tvFrom;//翻译源语言
    private TextView tvTo;//翻译目标语言

    private EditText edContent;//输入框（要翻译的内容）
    private ImageView ivClearTx;//清空输入框按钮
    private TextView tvTranslation;//翻译
    private View rootView;
    private LinearLayout resultLay;//翻译结果布局
    private TextView tvResult;//翻译的结果

    private String fromLanguage = "auto";//目标语言
    private String toLanguage = "auto";//翻译语言

    private ClipboardManager myClipboard;//复制文本

    private final String appId = "20240417002027472";//APP ID 来源于百度翻译平台 请使用自己的

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final List<String> data = new
            LinkedList<>(Arrays.asList(
            "自动检测语言", "中文 → 英文", "英文 → 中文",
            "中文 → 繁体中文", "中文 → 粤语", "中文 → 日语",
            "中文 → 韩语", "中文 → 法语", "中文 → 俄语",
            "中文 → 阿拉伯语", "中文 → 西班牙语 ", "中文 → 意大利语"));

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TranslatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Translator.
     */
    // TODO: Rename and change types and number of parameters
    public static TranslatorFragment newInstance(String param1, String param2) {
        TranslatorFragment fragment = new TranslatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    private void initView() {
        if (getActivity() != null && rootView != null) {
            Window window = getActivity().getWindow();
            if (window != null) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        //设置亮色状态栏模式 systemUiVisibility在Android11中弃用了，可以尝试一下。
        //控件初始化

        // 控件初始化
        if (rootView != null) {
            beforeLay = rootView.findViewById(R.id.before_lay);
        }
        if (rootView != null) {
            spLanguage = rootView.findViewById(R.id.sp_language);
        }
        if (rootView != null) {
            afterLay = rootView.findViewById(R.id.after_lay);
        }
        if (rootView != null) {
            tvFrom = rootView.findViewById(R.id.tv_from);
        }
        if (rootView != null) {
            tvTo = rootView.findViewById(R.id.tv_to);
        }
        if (rootView != null) {
            edContent = rootView.findViewById(R.id.ed_content);
        }
        if (rootView != null) {
            ivClearTx = rootView.findViewById(R.id.iv_clear_tx);
        }
        if (rootView != null) {
            tvTranslation = rootView.findViewById(R.id.tv_translation);
        }
        if (rootView != null) {
            resultLay = rootView.findViewById(R.id.result_lay);
        }
        if (rootView != null) {
            tvResult = rootView.findViewById(R.id.tv_result);
        }

        // 复制翻译的结果
        ImageView ivCopyTx = null;
        if (rootView != null) {
            ivCopyTx = rootView.findViewById(R.id.iv_copy_tx);
        }

        // 点击事件
        ivClearTx.setOnClickListener(this);
        if (ivCopyTx != null) {
            ivCopyTx.setOnClickListener(this);
        }
        tvTranslation.setOnClickListener(this);

        // 设置下拉数据
        spLanguage.attachDataSource(data);
        editTextListener(); // 输入框监听
        spinnerListener(); // 下拉框选择监听

        // 获取系统粘贴板服务
        myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_translator, container, false); // 替换为您的布局文件
        initView();
        return rootView;
    }
    private void editTextListener() {
        edContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ivClearTx.setVisibility(View.VISIBLE);//显示清除按钮
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivClearTx.setVisibility(View.VISIBLE);//显示清除按钮
            }

            @Override
            public void afterTextChanged(Editable s) {
                ivClearTx.setVisibility(View.VISIBLE);//显示清除按钮

                String content = edContent.getText().toString().trim();
                if (content.isEmpty()) {//为空
                    resultLay.setVisibility(View.GONE);
                    tvTranslation.setVisibility(View.VISIBLE);
                    beforeLay.setVisibility(View.VISIBLE);
                    afterLay.setVisibility(View.GONE);
                    ivClearTx.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 语言类型选择
     */
    private void spinnerListener() {
        spLanguage.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {

            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                switch (position) {
                    case 0://自动检测
                        fromLanguage = "auto";
                        toLanguage = fromLanguage;
                        break;
                    case 1://中文 → 英文
                        fromLanguage = "zh";
                        toLanguage = "en";
                        break;
                    case 2://英文 → 中文
                        fromLanguage = "en";
                        toLanguage = "zh";
                        break;
                    case 3://中文 → 繁体中文
                        fromLanguage = "zh";
                        toLanguage = "cht";
                        break;
                    case 4://中文 → 粤语
                        fromLanguage = "zh";
                        toLanguage = "yue";
                        break;
                    case 5://中文 → 日语
                        fromLanguage = "zh";
                        toLanguage = "jp";
                        break;
                    case 6://中文 → 韩语
                        fromLanguage = "zh";
                        toLanguage = "kor";
                        break;
                    case 7://中文 → 法语
                        fromLanguage = "zh";
                        toLanguage = "fra";
                        break;
                    case 8://中文 → 俄语
                        fromLanguage = "zh";
                        toLanguage = "ru";
                        break;
                    case 9://中文 → 阿拉伯语
                        fromLanguage = "zh";
                        toLanguage = "ara";
                        break;
                    case 10://中文 → 西班牙语
                        fromLanguage = "zh";
                        toLanguage = "spa";
                        break;
                    case 11://中文 → 意大利语
                        fromLanguage = "zh";
                        toLanguage = "it";
                        break;
                    default:
                        break;
                }
            }

        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_clear_tx) {
            //清空输入框
            edContent.setText("");//清除文本
            ivClearTx.setVisibility(View.GONE);//清除数据之后隐藏按钮
        }
        if (v.getId() == R.id.iv_copy_tx) {//复制翻译后的结果
            String inviteCode = tvResult.getText().toString();
            //剪辑数据
            ClipData myClip = ClipData.newPlainText("text", inviteCode);
            myClipboard.setPrimaryClip(myClip);
            showMsg("已复制");
        }
        if (v.getId() == R.id.tv_translation) {//翻译
            translation();//翻译
        }
    }
    /**
     * 翻译
     */
    private void translation() {
        //获取输入的内容
        String inputTx = edContent.getText().toString().trim();

        //判断输入内容是否为空
        if (!inputTx.isEmpty() || !"".equals(inputTx)) {//不为空
            tvTranslation.setText("翻译中...");
            tvTranslation.setEnabled(false);//不可更改，同样就无法点击
            String salt = num(1);//随机数
            //拼接一个字符串然后加密
            String key = "BssMuuCp6uDZC9IjdBRe";
            String spliceStr = appId + inputTx + salt + key;//根据百度要求 拼接
            String sign = stringToMD5(spliceStr);//将拼接好的字符串进行MD5加密   作为一个标识
            //异步Get请求访问网络
            asyncGet(inputTx, fromLanguage, toLanguage, salt, sign);
        } else {//为空
            showMsg("请输入要翻译的内容！");
        }
    }

    /**
     * 随机数 (根据百度的要求需要一个随机数)
     */
    public static String num(int a) {
        Random r = new Random(a);
        int ran1 = 0;
        for (int i = 0; i < 5; i++) {
            ran1 = r.nextInt(100);
            System.out.println(ran1);
        }
        return String.valueOf(ran1);
    }

    /**
     * 将字符串转成MD5值
     *
     * @param string 需要加密的内容
     * @return 加密后的字符串
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * 异步Get请求
     *
     * @param content  要翻译的内容
     * @param fromType 翻译源语言
     * @param toType   翻译后语言
     * @param salt     随机数
     * @param sign     标识
     */
    private void asyncGet(String content, String fromType, String toType, String salt, String sign) {
        //通用翻译API HTTP地址：
        //http://api.fanyi.baidu.com/api/trans/vip/translate
        //通用翻译API HTTPS地址：
        //https://fanyi-api.baidu.com/api/trans/vip/translate

        String httpStr = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        String httpsStr = "https://fanyi-api.baidu.com/api/trans/vip/translate";
        //拼接请求的地址
        String url = httpsStr +
                "?appid=" + appId + "&q=" + content + "&from=" + fromType + "&to=" +
                toType + "&salt=" + salt + "&sign=" + sign;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //异常返回
                goToUIThread(e.toString(), 0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //正常返回
                if (response.body() != null) {
                    goToUIThread(response.body().string(), 1);
                }
            }
        });
    }

    /**
     * 接收到返回值后，回到UI线程操作页面变化
     *
     * @param object 接收一个返回对象
     * @param key    表示正常还是异常
     */
    private void goToUIThread(final Object object, final int key) {
        // 确保Fragment已经与Activity关联
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (tvTranslation != null) {
                    tvTranslation.setText("翻译");
                    tvTranslation.setEnabled(true);
                }

                if (key == 0) { // 异常返回
                    if (getContext() != null) {
                        showMsg("异常信息：" + object.toString());
                    }
                    Log.e("Translator", object.toString());
                } else { // 正常返回
                    // 通过Gson 将 JSON字符串转为实体Bean
                    final TranslateResult result = new Gson().fromJson(object.toString(), TranslateResult.class);
                    if (tvTranslation != null) {
                        tvTranslation.setVisibility(View.GONE);
                    }
                    if (tvResult != null) {
                        // 显示翻译的结果
                        tvResult.setText(result.getTrans_result().get(0).getDst());
                    }
                    if (resultLay != null) {
                        resultLay.setVisibility(View.VISIBLE);
                    }
                    if (beforeLay != null) {
                        beforeLay.setVisibility(View.GONE);
                    }
                    if (afterLay != null) {
                        afterLay.setVisibility(View.VISIBLE);
                    }
                    // 翻译成功后的语言判断显示
                    initAfter(result.getFrom(), result.getTo());
                    // 获取翻译的原文和译文
                    String srcText = result.getTrans_result().get(0).getSrc();
                    String dstText = result.getTrans_result().get(0).getDst();

// 获取 HistoryDbHelper 实例
                    HistoryDbHelper dbHelper = HistoryDbHelper.getInstance(getActivity());

// 将原文和译文插入数据库
                    dbHelper.addHistory(srcText, dstText);
                }
            });
        }
    }


    /**
     * 翻译成功后的语言判断显示
     */
    private void initAfter(String from, String to) {
        if (("zh").equals(from)) {
            tvFrom.setText("中文");
        } else if (("en").equals(from)) {
            tvFrom.setText("英文");
        } else if (("yue").equals(from)) {
            tvFrom.setText("粤语");
        } else if (("cht").equals(from)) {
            tvFrom.setText("繁体中文");
        } else if (("jp").equals(from)) {
            tvFrom.setText("日语");
        } else if (("kor").equals(from)) {
            tvFrom.setText("韩语");
        } else if (("fra").equals(from)) {
            tvFrom.setText("法语");
        } else if (("ru").equals(from)) {
            tvFrom.setText("俄语");
        } else if (("ara").equals(from)) {
            tvFrom.setText("阿拉伯语");
        } else if (("spa").equals(from)) {
            tvFrom.setText("西班牙语");
        } else if (("it").equals(from)) {
            tvFrom.setText("意大利语");
        }
        if (("zh").equals(to)) {
            tvTo.setText("中文");
        } else if (("en").equals(to)) {
            tvTo.setText("英文");
        } else if (("yue").equals(to)) {
            tvTo.setText("粤语");
        } else if (("cht").equals(to)) {
            tvTo.setText("繁体中文");
        } else if (("jp").equals(to)) {
            tvTo.setText("日语");
        } else if (("kor").equals(to)) {
            tvTo.setText("韩语");
        } else if (("fra").equals(to)) {
            tvTo.setText("法语");
        } else if (("ru").equals(to)) {
            tvTo.setText("俄语");
        } else if (("ara").equals(to)) {
            tvTo.setText("阿拉伯语");
        } else if (("spa").equals(to)) {
            tvTo.setText("西班牙语");
        } else if (("it").equals(to)) {
            tvTo.setText("意大利语");
        }
    }

    /**
     * Toast提示
     *
     * @param msg
     */
    private void showMsg(String msg) {
        // 确保Fragment已经与Activity关联
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }


}


