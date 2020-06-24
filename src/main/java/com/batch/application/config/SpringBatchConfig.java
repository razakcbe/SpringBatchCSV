package com.batch.application.config;


import java.io.File;
import java.io.IOException;
import java.time.Duration;

import javax.annotation.PreDestroy;

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
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.batch.application.model.Address;
import com.batch.application.model.Member;
import com.batch.application.watch.MyFileChangeListener;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Autowired
	MyFileChangeListener changeListener;
				
	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemProcessor<Member, Member> itemProcessor, 
			ItemWriter<Member> itemWriter,
			ItemReader<Address> addressItemReader, 
			ItemProcessor<Address, Address> addressItemProcessor, 
			ItemWriter<Address> addressItemWriter) {

		Step step = stepBuilderFactory.get("Member-File-Upload")
				.<Member, Member>chunk(100)
				.reader(itemReader())
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
	public MultiResourceItemReader<Member> itemReader() {
		
		Resource[] inputResources = null;
        FileSystemXmlApplicationContext patternResolver = new FileSystemXmlApplicationContext();
        try {
            inputResources = patternResolver.getResources("file:C:/Users/akajakamaludeen/Documents/CsvFile/members*.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		MultiResourceItemReader<Member> resourceItemReader = new MultiResourceItemReader<>();
		FlatFileItemReader<Member> flatFileItemReader = new FlatFileItemReader<Member>();
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(flatFileItemReader);
		return resourceItemReader;
	}
	
	@Bean
	public MultiResourceItemReader<Address> addressItemReader() {
		
		Resource[] inputResources = null;
        FileSystemXmlApplicationContext patternResolver = new FileSystemXmlApplicationContext();
        try {
            inputResources = patternResolver.getResources("file:C:/Users/akajakamaludeen/Documents/CsvFile/address*.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		MultiResourceItemReader<Address> resourceItemReader = new MultiResourceItemReader<>();
		FlatFileItemReader<Address> flatFileItemReader = new FlatFileItemReader<Address>();
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(addressLineMapper());
		resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(flatFileItemReader);
		return resourceItemReader;
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
		lineTokenizer.setNames(new String[] { "id","aadharNumber","address1", "address2", "city", "taluk", "pincode"});
		BeanWrapperFieldSetMapper<Address> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Address.class);
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}
	
	@Bean
    public FileSystemWatcher fileSystemWatcher() {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(true, Duration.ofMillis(5000L), Duration.ofMillis(3000L));
        fileSystemWatcher.addSourceDirectory(new File("src/main/resources"));
        fileSystemWatcher.addListener(changeListener);
        fileSystemWatcher.start();
        System.out.println("started fileSystemWatcher");
        return fileSystemWatcher;
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        fileSystemWatcher().stop();
    }
}
