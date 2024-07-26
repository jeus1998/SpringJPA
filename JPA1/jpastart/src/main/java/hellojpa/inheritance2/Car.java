package hellojpa.inheritance2;

import jakarta.persistence.Entity;

@Entity
public class Car extends Product{
    private String Engine;
}
