package com.batch.application.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.application.dao.MemberRepository;
import com.batch.application.model.Address;
import com.batch.application.model.Member;

@Component
public class AddressProcessor implements ItemProcessor<Address, Address> {
	
	@Autowired
	MemberRepository memberRepository;

	@Override
    public Address process(Address address) throws Exception {
		Member member = memberRepository.findByAadharNumber(address.getAadharNumber());
		member.setAddress(address);
        return address;
    }
}
