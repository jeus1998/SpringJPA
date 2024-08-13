package spring.data_jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<Long> {
    @Id
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;
    @Override
    public Long getId() {
        return null;
    }
    @Override
    public boolean isNew() {
        if(createdDate == null) return true;
        return false;
    }
}
