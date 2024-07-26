package hellojpa.inheritance2;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

// @Entity
@DiscriminatorValue("Computer") // default = Computer
public class Computer extends Product{
    private String cpu;
}
