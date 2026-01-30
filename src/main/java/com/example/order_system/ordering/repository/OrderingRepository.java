package com.example.order_system.ordering.repository;

import com.example.order_system.member.domain.Member;
import com.example.order_system.ordering.domain.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering, Long> {

    @Query("""
        select distinct o from Ordering o
        join fetch o.member
        join fetch o.orderingDetails d
        join fetch d.product
        order by o.id
    """)
    List<Ordering> findAllFetch();
    List<Ordering> findByMember(Member member);
}
