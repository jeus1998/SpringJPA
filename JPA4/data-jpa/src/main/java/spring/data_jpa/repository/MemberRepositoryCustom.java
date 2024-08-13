package spring.data_jpa.repository;
import spring.data_jpa.entity.Member;
import java.util.*;
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
