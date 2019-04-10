package net.lzzy.cinemanager.utils;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;

/**
 * @author lzzy_gxy on 2019/3/22.
 * Description:
 */
public class ViewUtils {
    public static abstract class AbstractQueryHandler implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return handleQuery(newText);
        }

        /**
         * handle query logic
         *
         * @param kw keyword
         * @return end query
         */
        public abstract boolean handleQuery(String kw);
    }

    public static abstract class AbstractTouchHandler implements View.OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return handleTouch(event);
        }

        /**
         * 处理触摸事件
         *
         * @param event 触摸事件对象
         * @return 消费触摸事件吗
         */
        public abstract boolean handleTouch(MotionEvent event);
    }
}
