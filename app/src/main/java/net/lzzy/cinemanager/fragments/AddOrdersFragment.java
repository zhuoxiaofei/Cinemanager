package net.lzzy.cinemanager.fragments;


import android.content.Context;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;

/**
 *
 * @author lzzy_gxy
 * @date
 * Description:
 */
public class AddOrdersFragment extends BaseFragment {
    private AddCinemasFragment.OnFragmentInteractionListener listener;
    @Override
    protected void populate() {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_orders;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            listener.hideSearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddCinemasFragment.OnFragmentInteractionListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    interface OnCinemaCreatedListener{
        /**
         * 取消保存
         */
        void cancelAddCinema();
        void saveCinema(Cinema cinema);
    }
}
