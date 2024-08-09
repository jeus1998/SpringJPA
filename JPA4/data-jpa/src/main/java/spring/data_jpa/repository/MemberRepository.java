package spring.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.data_jpa.entity.Member;
import java.util.*;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

}
