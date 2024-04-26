package com.example.translator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private TextView textView;
    private ImageView imageView;
    private Toolbar toolbar;
    private boolean isTextVisible = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ImageView edit_view=view.findViewById(R.id.edit_view);
        ImageView refresh_button=view.findViewById(R.id.refresh_button);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshHistory();
                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        HistoryDbHelper ebHelper = HistoryDbHelper.getInstance(getActivity());
        ebHelper.deleteEmptySrcRecords();
        // 从数据库获取数据
        HistoryDbHelper dbHelper = HistoryDbHelper.getInstance(getActivity());
        List<HistoryInfo> historyList = dbHelper.queryHistoryListData();
        view.findViewById(R.id.edit_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "本功能暂未开发，请以后再来", Toast.LENGTH_SHORT).show();
            }
        });
        // 设置适配器
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
// 找到ImageView和TextView

        return view;
    }
    private void refreshHistory() {
        HistoryDbHelper dbHelper = HistoryDbHelper.getInstance(getActivity());
        List<HistoryInfo> updatedHistoryList = dbHelper.queryHistoryListData();
        adapter.updateHistoryList(updatedHistoryList);
        adapter.notifyDataSetChanged();
    }
}
