package net.lzzy.cinemanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.BaseFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.FragmentAddCinemas;
import net.lzzy.cinemanager.fragments.FragmentAddOrder;
import net.lzzy.cinemanager.fragments.OnFragmentInteractionListener;
import net.lzzy.cinemanager.fragments.OrderFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.utils.ViewUtils;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        OnFragmentInteractionListener, FragmentAddCinemas.onCinemaCreatedListener,
        FragmentAddOrder.onOrderCreatedListener,
        CinemasFragment.OnCinemaSelectedListener {
    public static final String CINEMA_ID = "cinemaId";
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private LinearLayout layoutAddOrder;
    private SparseArray<String> titlieArray = new SparseArray<>();
    private FragmentManager manager = getSupportFragmentManager();
    private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment = manager.findFragmentById(R.id.fragment_container);
                if (fragment != null) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).search(kw);
                    }
                }
                return true;

            }
        });
        //首个加载的Fragment界面
        manager.beginTransaction().add(R.id.fragment_container, new OrderFragment())
                .commit();


    }


    /**
     * 标题栏
     **/
    private void setTitleMenu() {
        titlieArray.put(R.id.bar_title_tv_add_cinema, "添加影院");
        titlieArray.put(R.id.bar_title_tv_view_cinema, "影院列表");
        titlieArray.put(R.id.bar_title_tv_add_order, "添加订单");
        titlieArray.put(R.id.bar_title_tv_view_order, "我的订单");
        layoutMenu = findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(this);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(v -> {
            int visible = layoutMenu.getVisibility() == View.VISIBLE ? View.GONE :
                    View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_orders);
        search = findViewById(R.id.main_sv_search);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(v -> {
            System.exit(0);
        });
    }

    @Override
    public void onClick(View v) {
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titlieArray.get(v.getId()));
        Fragment fragment = fragmentSparseArray.get(v.getId());
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment == null) {
            fragment = createFragment(v.getId());
            fragmentSparseArray.put(v.getId(), fragment);
            transaction.add(R.id.fragment_container, fragment);
        }
        for (Fragment f : manager.getFragments()) {
            transaction.hide(f);
        }
        transaction.show(fragment).commit();
    }

    private Fragment createFragment(int id) {
        switch (id) {
            case R.id.bar_title_tv_add_cinema:
                return new FragmentAddCinemas();

            case R.id.bar_title_tv_view_cinema:
                return new CinemasFragment();

            case R.id.bar_title_tv_add_order:
                return new FragmentAddOrder();

            case R.id.bar_title_tv_view_order:
                return new OrderFragment();

            default:
                break;
        }
        return null;
    }

    /**
     * 调用接口方法
     */
    @Override
    public void hideSearch() {
        search.setVisibility(View.GONE);
    }

    /**
     * 调用接口方法(保存取消)
     **/
    @Override
    public void cancelAddCinema() {
        Fragment addCinemaFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_add_cinema);
        if (addCinemaFragment == null) {
            return;
        }
        Fragment cinemaFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (cinemaFragment == null) {
            cinemaFragment = new CinemasFragment();
            fragmentSparseArray.put(R.id.bar_title_tv_view_cinema, cinemaFragment);
            transaction.add(R.id.fragment_container, cinemaFragment);
        }
        transaction.hide(addCinemaFragment).show(cinemaFragment).commit();
        tvTitle.setText(titlieArray.get(R.id.bar_title_tv_view_cinema));
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemaFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_add_cinema);
        if (addCinemaFragment == null) {
            return;
        }
        Fragment cinemasFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (cinemasFragment == null) {
            cinemasFragment = CinemasFragment.newInstance(cinema);
            fragmentSparseArray.put(R.id.bar_title_tv_view_cinema, cinemasFragment);
            transaction.add(R.id.fragment_container, cinemasFragment);
        } else {
            ((CinemasFragment) cinemasFragment).save(cinema);
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment).commit();
        tvTitle.setText(titlieArray.get(R.id.bar_title_tv_view_cinema));
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void cancelAddOrder() {
        Fragment addOrderFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_add_order);
        if (addOrderFragment == null) {
            return;
        }
        Fragment orderFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_view_order);
        FragmentTransaction transaction = manager.beginTransaction();
        if (orderFragment == null) {
            orderFragment = new CinemasFragment();
            fragmentSparseArray.put(R.id.bar_title_tv_view_order, orderFragment);
            transaction.add(R.id.fragment_container, orderFragment);
        }
        transaction.hide(addOrderFragment).show(orderFragment).commit();
        tvTitle.setText(titlieArray.get(R.id.bar_title_tv_view_order));
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveOrder(Order order) {
        Fragment addOrderFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_add_order);
        if (addOrderFragment == null) {
            return;
        }
        Fragment ordersFragment = fragmentSparseArray.
                get(R.id.bar_title_tv_view_order);
        FragmentTransaction transaction = manager.beginTransaction();
        if (ordersFragment == null) {
            ordersFragment = OrderFragment.newInstance(order);
            fragmentSparseArray.put(R.id.bar_title_tv_view_order, ordersFragment);
            transaction.add(R.id.fragment_container, ordersFragment);
        } else {
            ((OrderFragment) ordersFragment).save(order);
        }
        transaction.hide(addOrderFragment).show(ordersFragment).commit();
        tvTitle.setText(titlieArray.get(R.id.bar_title_tv_view_order));
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCinemaSelected(String cinemaId) {
        Intent intent = new Intent(this, CinemaOrdersActivity.class);
        intent.putExtra(CINEMA_ID, cinemaId);
        startActivity(intent);
    }
}
