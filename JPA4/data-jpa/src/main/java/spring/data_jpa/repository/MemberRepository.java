package spring.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.data_jpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
