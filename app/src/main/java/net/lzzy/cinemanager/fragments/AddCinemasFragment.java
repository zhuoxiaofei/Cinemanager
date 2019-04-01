package net.lzzy.cinemanager.fragments;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;

/**
 *
 * @author lzzy_gxy
 * @date
 * Description:
 */
public class AddCinemasFragment extends BaseFragment {
    private String province="广西壮族自治区";
    private String city="柳州市";
    private String area="鱼峰区";
    private TextView tvArea;
    private EditText edtName;
    private CinemaFactory factory;
    private OnFragmentInteractionListener listener;
    private OnCinemaCreatedListener cinemaListener;

    @Override
    protected void populate() {
        listener.hideSearch();
        tvArea = find(R.id.dialog_add_tv_area);
        edtName = find(R.id.dialog_add_cinema_edt_name);
//        showDialog();
        find(R.id.dialog_add_cinema_layout_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JDCityPicker cityPicker = new JDCityPicker();
                cityPicker.init(AddCinemasFragment.this.getActivity());
                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        super.onSelected(province, city, district);
                        AddCinemasFragment.this.province = province.getName();
                        AddCinemasFragment.this.city = city.getName();
                        AddCinemasFragment.this.area = district.getName();
                        String loc = province.getName() + city.getName() + district.getName();
                        tvArea.setText(loc);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                cityPicker.showCityPicker();
            }
        });
        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(AddCinemasFragment.this.getActivity(), "要有名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cinema cinema = new Cinema();
                cinema.setArea(area);
                cinema.setCity(city);
                cinema.setName(name);
                cinema.setProvince(province);
                cinema.setLocation(tvArea.getText().toString());
                edtName.setText("");
                cinemaListener.saveCinema(cinema);
            }
        });
        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cinemaListener.cancelAddCinema();
            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_cinemas;
    }

//    private void showDialog() {
//        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(v -> {
//
//        });
//        find(R.id.dialog_add_cinema_layout_area).setOnClickListener(v -> {
//            showLocation();
//        });
//        find(R.id.dialog_add_cinema_btn_save).setOnClickListener(v -> {
//            String name=edtName.getText().toString();
//            String location=tvArea.getText().toString();
//            if (name.isEmpty()){
//                Toast.makeText(getActivity(),"影院名称不能为空",Toast.LENGTH_SHORT).show();
//            }else {
//                Cinema cinema=new Cinema(name,location,province,city,area);
//                factory = CinemaFactory.getInstance();
//                factory.addCinema(cinema);
//            }
//
//        });
//    }
//
//    private void showLocation() {
//        JDCityPicker cityPicker = new JDCityPicker();
//        cityPicker.init(getActivity());
//        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
//            @Override
//            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
//                AddCinemasFragment.this.province=province.getName();
//                AddCinemasFragment.this.city=city.getName();
//                AddCinemasFragment.this.area=district.getName();
//                String loc=province.getName()+city.getName()+district.getName();
//                tvArea.setText(loc);
//            }
//
//            @Override
//            public void onCancel() {
//            }
//        });
//        cityPicker.showCityPicker();
//    }


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
        listener = (OnFragmentInteractionListener) context;
        cinemaListener = (OnCinemaCreatedListener) context;

       }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnFragmentInteractionListener");
        }
     }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        cinemaListener = null;
    }

    public interface OnCinemaCreatedListener {

        void cancelAddCinema();

        void saveCinema(Cinema cinema);
    }

    public interface OnFragmentInteractionListener {
        void hideSearch();
    }
}
