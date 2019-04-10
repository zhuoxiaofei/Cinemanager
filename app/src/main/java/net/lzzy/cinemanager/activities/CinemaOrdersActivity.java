package net.lzzy.cinemanager.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.CinemaOrderFragment;

import static net.lzzy.cinemanager.activities.MainActivity.CINEMA_ID;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/3
 * Description:
 */
public class CinemaOrdersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cinema_orders);
        String cinemaId = getIntent().getStringExtra(CINEMA_ID);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container_orders);
        if (fragment == null) {
            fragment = CinemaOrderFragment.newInstance(cinemaId);
            manager.beginTransaction().add(R.id.fragment_container_orders,
                    fragment).commit();
        }
    }
}
