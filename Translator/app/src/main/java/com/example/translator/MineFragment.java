package com.example.translator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
private TextView tv_username;
    private TextView tv_nickname;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_mine, container, false);
        // Inflate the layout for this fragment
        tv_username= rootView.findViewById(R.id.tv_username);
        tv_nickname= rootView.findViewById(R.id.tv_nickname);
        RelativeLayout ctheme = rootView.findViewById(R.id.ctheme);
        RelativeLayout policy = rootView.findViewById(R.id.policy);
        RelativeLayout notice = rootView.findViewById(R.id.notice);
        //退出登录
        rootView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("温馨提示").setMessage("确定要退出登录吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //退出登录的logic
                                getActivity().finish();
                                //打开登录的页面
                                Intent intent=new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });
        //修改密码
       rootView.findViewById(R.id.updatePwd).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getActivity(),UpdatePwdActivity.class);
               startActivityForResult(intent,1000);
           }
       });
       //关于APP
        rootView.findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),AboutActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.ctheme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "本功能暂未开发，请以后再来", Toast.LENGTH_SHORT).show();
            }
        });
        rootView.findViewById(R.id.policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "本功能暂未开发，请以后再来", Toast.LENGTH_SHORT).show();
            }
        });
        rootView.findViewById(R.id.notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "本功能暂未开发，请以后再来", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UserInfo userInfo=UserInfo.getUserInfo();
        if(null!=userInfo) {
            tv_username.setText(userInfo.getUsername());
            tv_nickname.setText(userInfo.getNickname());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1000){
            getActivity().finish();
            Intent intent=new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);

        }
    }
}