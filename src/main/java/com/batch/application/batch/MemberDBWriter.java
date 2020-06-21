package com.batch.application.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.application.dao.MemberRepository;
import com.batch.application.model.Member;

@Component
public class MemberDBWriter implements ItemWriter<Member> {

	@Autowired
    private MemberRepository userRepository;

    @Override
    public void write(List<? extends Member> members) throws Exception {
        userRepository.saveAll(members);
    }
}
