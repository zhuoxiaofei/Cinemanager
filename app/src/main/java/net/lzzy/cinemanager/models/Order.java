package net.lzzy.cinemanager.models;

import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;
import net.lzzy.sqllib.Table;

import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/3/11.
 * Description:
 */
@Table(name = "Orders")
public class Order extends BaseEntity implements Sqlitable {
    @Ignored
    static final String COL_MOVIE = "movie";
    @Ignored
    static final String COL_MOVIE_TIME = "movieTime";
    @Ignored
    static final String COL_PRICE = "price";
    @Ignored
    static final String COL_CINEMA_ID = "cinemaId";
    private String movie;
    private String movieTime;
    private float price;
    private UUID cinemaId;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(String movieTime) {
        this.movieTime = movieTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public UUID getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(UUID cinemaId) {
        this.cinemaId = cinemaId;
    }

    @Override
    public boolean needUpdate() {
        return false;
    }
}
