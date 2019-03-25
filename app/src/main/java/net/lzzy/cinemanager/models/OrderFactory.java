package net.lzzy.cinemanager.models;

import net.lzzy.cinemanager.constants.DbConstants;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.sqllib.SqlRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzzy_gxy on 2019/3/12.
 * Description:
 */
public class OrderFactory {
    private static OrderFactory instance;
    private SqlRepository<Order> repository;
    private OrderFactory(){
        repository = new SqlRepository<>(AppUtils.getContext(),Order.class, DbConstants.packager);
    }

    public static OrderFactory getInstance(){
        if(instance == null){
            synchronized (OrderFactory.class){
                if(instance == null){
                    instance = new OrderFactory();
                }
            }
        }
        return instance;
    }

    public List<Order> get(){
        return repository.get();
    }

    public List<Order> searchOrders(String kw){
        try {
            List<Order> orders =repository.getByKeyword(kw,new String[]{Order.COL_MOVIE,
                    Order.COL_PRICE,Order.COL_MOVIE_TIME},false);
            List<Cinema> cinemas = CinemaFactory.getInstance().searchCinemas(kw);
            if(cinemas.size()>0){
                for(Cinema cinema:cinemas){
                    List<Order> cOrders = repository.getByKeyword(cinema.getId().toString(),
                            new String[]{Order.COL_CINEMA_ID},true);
                    orders.addAll(cOrders);
                }
            }
            return orders;
        } catch (IllegalAccessException|InstantiationException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Order> getOrdersByCinema(String cinemaId){
        try {
            return repository.getByKeyword(cinemaId,new String[]{Order.COL_CINEMA_ID},true);
        }  catch (IllegalAccessException|InstantiationException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean addOrder(Order order){
        repository.insert(order);
        return true;
    }

    public boolean delete(Order order){
        repository.delete(order);
        return true;
    }
}
