package com.batch.application.batch;

import java.util.Date;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.application.model.Member;

@Component
public class MemberProcessor implements ItemProcessor<Member, Member> {

	@Override
    public Member process(Member member) throws Exception {
		member.setCreatedTime(new Date());
        return member;
    }
}
