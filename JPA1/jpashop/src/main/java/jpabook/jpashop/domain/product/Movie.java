package jpabook.jpashop.domain.product;

import jakarta.persistence.Entity;
import jpabook.jpashop.domain.Item;

@Entity
public class Movie extends Item {
    private String actor;
    private String director;

    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
}
