package com.batch.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.batch.application.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	//@Query(value = "select m from Member m where m.aadharNumber = :aadharNumber")
	Member findByAadharNumber(@Param("aadharNumber") Long aadharNumber);
	
}
