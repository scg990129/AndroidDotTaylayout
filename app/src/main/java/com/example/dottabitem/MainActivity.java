package com.example.dottabitem;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;

//  https://itnext.io/4-steps-to-android-dot-tabitem-dadeeef44f51
public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static long loopDelayMillis = 750 * 3;

    private final Handler loopHandler = new Handler();
    private Runnable runnable = this::onTabAutoLoop;
    @ColorInt
    @Deprecated
    protected int[] colors = {Color.WHITE, Color.YELLOW, Color.MAGENTA};

    protected androidx.viewpager2.widget.ViewPager2 viewerPager;
    protected RecyclerViewAdapter recyclerViewAdapter;
    protected com.google.android.material.tabs.TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewerPager = this.findViewById(R.id.viewpager);
        this.viewerPager.setAdapter(recyclerViewAdapter = new RecyclerViewAdapter());

        this.tabLayout = this.findViewById(R.id.tablayout);
        new TabLayoutMediator(this.tabLayout,
                this.viewerPager,
                (tab, position) -> {
                    // tab.setText(String.format("position: %d", position));
                }).attach();
    }

    @Override
    public void onResume() {
        super.onResume();
//        loopHandler.removeCallbacks();
        loopHandler.postDelayed(runnable, loopDelayMillis);
    }

    @Override
    public synchronized void onPause() {
        loopHandler.removeCallbacks(runnable);
        super.onPause();
    }

    protected synchronized void onTabAutoLoop() {
        loopHandler.removeCallbacks(this.runnable);
        if (colors.length < 2) {
            return;
        }

        tabLayout.selectTab(tabLayout.getTabAt(
                tabLayout.getSelectedTabPosition() + 1 == tabLayout.getTabCount() ?
                        0 : tabLayout.getSelectedTabPosition() + 1
        ));

        loopHandler.postDelayed(this.runnable, loopDelayMillis);
    }

    // https://developer.android.com/guide/navigation/navigation-swipe-view-2
    protected class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolderImp>  {

        @NonNull
        @Override
        public ViewHolderImp onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imageview, parent, false);
            return new ViewHolderImp(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderImp holder, int position) {
            holder.setColor(colors[position]);
        }

        @Override
        public int getItemCount() {
            return colors.length;
        }



    }

    protected class ViewHolderImp extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnTouchListener {

        protected ImageView imageview;

        public ViewHolderImp(@NonNull View itemView) {
            super(itemView);
            this.imageview = itemView.findViewById(R.id.imageview);
            this.imageview.setOnClickListener(this);
            this.imageview.setOnTouchListener(this);
        }

        public void setColor(@ColorInt int colorId) {
            this.imageview.setBackgroundColor(colorId);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Hi", Toast.LENGTH_SHORT).show();
        }
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    loopHandler.removeCallbacks(runnable);
                    Log.i(TAG, "MotionEvent: ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    loopHandler.removeCallbacks(runnable);
                    loopHandler.postDelayed(runnable, loopDelayMillis);
                    Log.i(TAG, String.format("MotionEvent: ACTION_UP(1) / ACTION_CANCEL(3) = %d", event.getAction()));
                    // if (MotionEvent.ACTION_UP == event.getAction()) return view.performClick();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "MotionEvent: ACTION_MOVE");
                    break;
                default:
                    Log.i(TAG, "MotionEvent: " + event.getAction());
            }
            return false;
        }
    }

}
/*
    public synchronized void setCarousels(@NonNull Collection<Carousel> carousels) {
        loopHandler.removeCallbacks(this.runnable);
        this.carousels.clear();
        this.carousels.addAll(carousels);
        this.recyclerViewCarouselAdapter.notifyDataSetChanged();
        switch (this.carousels.size()) {
            case 0:
                this.getView().setVisibility(View.GONE);
                break;
            case 1:
                this.getView().setVisibility(View.VISIBLE);
                this.tabLayoutCarouselSlider.setVisibility(View.GONE);
                this.tabLayoutCarouselSlider.getTabAt(0).select();
                break;
            default:
                this.getView().setVisibility(View.VISIBLE);
                this.tabLayoutCarouselSlider.getTabAt(0).select();
                this.tabLayoutCarouselSlider.setVisibility(View.VISIBLE);
                loopHandler.postDelayed(this.runnable, loopDelayMillis);
        }
    }
 */