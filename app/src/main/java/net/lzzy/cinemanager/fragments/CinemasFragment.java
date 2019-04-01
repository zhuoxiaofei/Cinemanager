package net.lzzy.cinemanager.fragments;

import android.view.View;
import android.widget.ListView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;


public class CinemasFragment extends BaseFragment {

    private List<Cinema> cinemas;
    private ListView lv;
    private CinemaFactory factory= CinemaFactory.getInstance();
    private GenericAdapter<Cinema> adapter;
    private Cinema cinema;

    public CinemasFragment(){}
    public CinemasFragment(Cinema cinema){
        this.cinema = cinema;
    }
    @Override
    protected void populate() {
        lv = find(R.id.activity_cinema_lv);
        View empty=find(R.id.activity_cinemas_tv_none);
        lv.setEmptyView(empty);
        cinemas=factory.get();
        adapter = new GenericAdapter<Cinema>(getActivity(),
                R.layout.cinemas_item,cinemas) {
            @Override
            public void populate(ViewHolder viewHolder, Cinema cinema) {
                viewHolder.setTextView(R.id.cinemas_items_name,cinema.getName())
                        .setTextView(R.id.cinemas_items_location,cinema.getLocation());
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
    }

    public void save(Cinema cinema){
        adapter.add(cinema);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }
}
