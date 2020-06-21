package com.batch.application.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.application.dao.AddressRepository;
import com.batch.application.model.Address;

@Component
public class AddressDBWriter implements ItemWriter<Address> {

	@Autowired
    private AddressRepository addressRepository;

    @Override
    public void write(List<? extends Address> addresses) throws Exception {
        addressRepository.saveAll(addresses);
    }
}
