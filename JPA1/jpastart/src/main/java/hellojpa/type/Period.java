package hellojpa.type;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

// @Embeddable
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
