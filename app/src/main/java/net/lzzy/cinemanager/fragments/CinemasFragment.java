package net.lzzy.cinemanager.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * @author lzzy_gxy
 * @date 2019/3/26
 * Description:
 */
public class CinemasFragment extends BaseFragment {

    private static final String CINEMA = "cinema";
    private ListView lv;
    private List<Cinema> cinemas;
    private CinemaFactory factory = CinemaFactory.getInstance();
    private GenericAdapter<Cinema> adapter;
    private Cinema cinema;
    private OnCinemaSelectedListener listener;

    public static CinemasFragment newInstance(Cinema cinema) {
        CinemasFragment fragment = new CinemasFragment();
        Bundle args = new Bundle();
        args.putParcelable(CINEMA, cinema);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Cinema cinema = getArguments().getParcelable(CINEMA);
            this.cinema = cinema;
        }
    }

    @Override
    protected void populate() {

        lv = find(R.id.activity_cinema_lv);
        View empty = find(R.id.activity_cinemas_tv_none);
        lv.setEmptyView(empty);
        cinemas = factory.get();

        //适配器
        adapter = new GenericAdapter<Cinema>(getActivity(),
                R.layout.cinemas_item, cinemas) {
            @Override
            public void populate(ViewHolder viewHolder, Cinema cinema) {
                viewHolder.setTextView(R.id.cinemas_items_name, cinema.getName())
                        .setTextView(R.id.cinemas_items_location,
                                cinema.getLocation());
            }

            @Override
            public boolean persistInsert(Cinema cinema) {
                return factory.addCinema(cinema);
            }

            @Override
            public boolean persistDelete(Cinema cinema) {
                return factory.deleteCinema(cinema);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) ->
                listener.onCinemaSelected(adapter.getItem(position).
                        getId().toString()));

        if (cinema != null) {
            save(cinema);
        }
    }

    public void save(Cinema cinema) {
        adapter.add(cinema);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }

    @Override
    public void search(String kw) {
        cinemas.clear();
        if (TextUtils.isEmpty(kw)) {
            cinemas.addAll(factory.get());
        } else {
            cinemas.addAll(factory.searchCinemas(kw));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnCinemaSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "必须实现OnCinemaSelectedListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener = null;
    }

    public interface OnCinemaSelectedListener {
        void onCinemaSelected(String cinemaId);
    }
}
