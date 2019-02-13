package com.example.zjz.swipecardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zjz.swipecardview.swipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener {
    private SwipeFlingAdapterView swipeView;
    private SwipeAdapter adapter;
    private LinearLayout linear_left;
    private LinearLayout linear_right;
    private LinearLayout linear_bottom;
    boolean isFinishCard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSwipeView();
        initCardData();
    }

    private void initCardData() {
        List<MyData> cardList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MyData data = new MyData();
            data.setTitle(String.valueOf(i + 1));
            cardList.add(data);
        }
        adapter.addAll(cardList);
        linear_bottom.setVisibility(View.VISIBLE);
    }

    private void initSwipeView() {

        swipeView = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);
        if (swipeView != null) {
            swipeView.setIsNeedSwipe(true);
            swipeView.setFlingListener(this);
            swipeView.setOnItemClickListener(this);
            swipeView.setLeftResource(R.id.img_left);
            swipeView.setRightResource(R.id.img_right);
            adapter = new SwipeAdapter();
            swipeView.setAdapter(adapter);
        }

        linear_left = (LinearLayout) findViewById(R.id.linear_left);
        linear_right = (LinearLayout) findViewById(R.id.linear_right);
        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);
        linear_left.setOnClickListener(this);
        linear_right.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_left:
                swipeView.swipeLeft(350);
                break;
            case R.id.linear_right:
                swipeView.swipeRight(350);
                break;
        }
    }

    /**
     * 卡片点击事件
     *
     * @param event
     * @param v
     * @param dataObject
     */
    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {
        Toast.makeText(MainActivity.this, "卡片点击", Toast.LENGTH_SHORT).show();
    }

    /**
     * 第一个卡片移除监听事件
     */
    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    /**
     * 卡片左滑退出事件
     *
     * @param dataObject
     */
    @Override
    public void onLeftCardExit(Object dataObject) {
        Log.i("zjz", "left==adapter_size=" + adapter.getCount());
        if (adapter.getCount() == 0) {
            isFinishCard = true;
        }
    }

    /**
     * 卡片右滑退出事件
     *
     * @param dataObject
     */
    @Override
    public void onRightCardExit(Object dataObject) {
        MyData talent = (MyData) dataObject;
        List<MyData> list = new ArrayList<>();
        list.add(talent);
        adapter.addAll(list);
        Log.i("zjz", "right==adapter_size=" + adapter.getCount());
    }

    /**
     * 卡片列表空监听事件
     *
     * @param itemsInAdapter
     */
    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 0 && isFinishCard) {
            linear_left.setClickable(false);
            linear_right.setClickable(false);
            linear_bottom.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "卡片空了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 滑动监听事件
     *
     * @param view
     * @param progress
     * @param scrollXProgress
     */
    @Override
    public void onScroll(View view, float progress, float scrollXProgress) {

    }


    private class SwipeAdapter extends BaseAdapter {

        ArrayList<MyData> objs;

        public SwipeAdapter() {
            objs = new ArrayList<>();
        }

        public void addAll(Collection<MyData> collection) {
            if (isEmpty()) {
                objs.addAll(collection);
                notifyDataSetChanged();
            } else {
                objs.addAll(collection);
            }
        }

        public void clear() {
            objs.clear();
            notifyDataSetChanged();
        }


        public boolean isEmpty() {
            return objs.isEmpty();
        }

        public void remove(int index) {
            if (index > -1 && index < objs.size()) {
                objs.remove(index);
                notifyDataSetChanged();
            }
        }


        @Override
        public int getCount() {
            return objs.size();
        }

        @Override
        public MyData getItem(int position) {
            if (objs == null || objs.size() == 0) return null;
            return objs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // TODO: getView
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final MyData talent = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cards, parent, false);
                holder = new ViewHolder();
                holder.text = convertView.findViewById(R.id.text);
                holder.img_left = convertView.findViewById(R.id.img_left);
                holder.img_right = convertView.findViewById(R.id.img_right);
                holder.relative_top = convertView.findViewById(R.id.relative_top);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Log.i("zjz", "position=" + position + ",text=" + talent.getTitle());
            holder.text.setText(talent.getTitle());


            if (position != 0) {
                holder.img_left.setVisibility(View.INVISIBLE);
                holder.img_right.setVisibility(View.INVISIBLE);
            } else {
                holder.img_left.setVisibility(View.VISIBLE);
                holder.img_right.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

    }

    private static class ViewHolder {
        TextView text;
        ImageView img_left;
        ImageView img_right;
        RelativeLayout relative_top;
    }

    public class MyData {
        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
