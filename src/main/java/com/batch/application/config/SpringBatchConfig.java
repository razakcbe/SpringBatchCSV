package com.batch.application.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.batch.application.model.Address;
import com.batch.application.model.Member;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemReader<Member> itemReader, 
			ItemProcessor<Member, Member> itemProcessor, 
			ItemWriter<Member> itemWriter,
			ItemReader<Address> addressItemReader, 
			ItemProcessor<Address, Address> addressItemProcessor, 
			ItemWriter<Address> addressItemWriter) {

		Step step = stepBuilderFactory.get("Member-File-Upload")
				.<Member, Member>chunk(100)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.build();
		
		Step stepTwo = stepBuilderFactory.get("Address-File-Upload")
				.<Address, Address>chunk(100)
				.reader(addressItemReader)
				.processor(addressItemProcessor)
				.writer(addressItemWriter)
				.build();

		return jobBuilderFactory
				.get("Upload-CSV")
				.incrementer(new RunIdIncrementer())
				.start(step)
				.next(stepTwo)
				.build();
	}

	@Bean
	public FlatFileItemReader<Member> itemReader() {

		FlatFileItemReader<Member> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource("src/main/resources/members.csv"));
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}
	
	@Bean
	public FlatFileItemReader<Address> addressItemReader() {

		FlatFileItemReader<Address> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource("src/main/resources/address.csv"));
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(addressLineMapper());
		return flatFileItemReader;
	}

	@Bean
	public LineMapper<Member> lineMapper() {

		DefaultLineMapper<Member> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] {"aadharNumber", "firstName", "lastName"});
		BeanWrapperFieldSetMapper<Member> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Member.class);
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}
	
	@Bean
	public LineMapper<Address> addressLineMapper() {

		DefaultLineMapper<Address> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "aadharNumber","address1", "address2", "city", "taluk", "pincode"});
		BeanWrapperFieldSetMapper<Address> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Address.class);
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}
}
