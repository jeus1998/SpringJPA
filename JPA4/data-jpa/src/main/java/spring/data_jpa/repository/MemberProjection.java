package spring.data_jpa.repository;
import java.time.LocalDateTime;

public interface MemberProjection {
    String getUsername();
    String getTeamName();
    int getAge();
    LocalDateTime getCreatedDate();
}
