package spring.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.querydsl.entity.Member;
import java.util.*;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsername(String username);
}
