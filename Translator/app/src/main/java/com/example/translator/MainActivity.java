package com.example.translator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;


import com.example.translator.TranslatorFragment;
import com.example.translator.MineFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private BottomNavigationView navigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.content);

        TranslatorFragment TranslatorFragment= new TranslatorFragment();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TranslatorFragment()); //新建一个homeFragment对象将这个对象加入到数组fragments中
        fragments.add(new HistoryFragment());
        fragments.add(new MineFragment());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        //创建对象并通过构造函数初始化，该适配器可以知道要显示哪些片段。
        viewPager.setAdapter(viewPagerAdapter);
        //将前面创建的 viewPagerAdapter 适配器设置给 viewPager 视图组件，以便在 ViewPager 中显示相应的页面。
        //底部导航栏监听事件
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //根据菜单ID显示页面
                if(item.getItemId()==R.id.page1) {//监听事件中，点击菜单立马执行item.getItemId()方法
                    //item.getItemId() 方法用于获取选中的 MenuItem 的唯一标识符（ID）
                    //R.id.xxx是整数类型。
                    viewPager.setCurrentItem(0);
                    // 将 ViewPager 的当前页面显示成索引为 0 的页面
                    return true;
                }
                if(item.getItemId()==R.id.page2) {
                    viewPager.setCurrentItem(1);
                    return true;
                }
                if(item.getItemId()==R.id.page3) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }

        });

        // 添加页面切换的监听器，根据页面切换实现菜单切换
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){  // 根据页面位置更新导航栏的选中状态
                    case 0:
                        navigationView.setSelectedItemId(R.id.page1);
                        //将导航栏中的选中项设置为 R.id.page1
                        break;
                    case 1:
                        navigationView.setSelectedItemId(R.id.page2);
                        break;
                    case 2:
                        navigationView.setSelectedItemId(R.id.page3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });//通过使用页面切换监听器，
        // 我们可以根据页面切换的情况来更改导航栏的选中状态，
        // 从而实现页面切换时导航栏菜单的同步切换效果。

    }


}
