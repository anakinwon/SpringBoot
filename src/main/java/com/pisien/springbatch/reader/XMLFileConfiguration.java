package com.pisien.springbatch.reader;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.jdbc.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class XMLFileConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlatFixedLenFilesConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     *   <XML을 객체로 변환 API>
     *
     * */
    @Bean
    public Job job() {
        logger.info("1. xmlFileConfigurationJob Start ");
        return this.jobBuilderFactory.get("xmlFileConfigurationJob")
                .incrementer(new RunIdIncrementer())
                .start(xmlFileConfigurationStep())
                .build();
    }

    @Bean
    public Step xmlFileConfigurationStep() {
        logger.info("\t2. xmlFileConfigurationStep Start ");
        return stepBuilderFactory.get("xmlFileConfigurationStep")
                .<Member, Member>chunk(2)// chunk-size 설정 = Commit Interval
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Member> customItemReader() {
        logger.info("\t\t3. customItemReader Start ");

        return new StaxEventItemReaderBuilder<Member>()
                .name("statXml")
                .resource(new ClassPathResource("member.xml"))
                .addFragmentRootElements("member")
                .unmarshaller(itemUnMarshaller())                // XML 문서를 객체로 변환하는 API
                .build();
    }

    @Bean
    public Unmarshaller itemUnMarshaller() {
        logger.info("\t\t\t4. itemUnMarshaller Start ");

        Map<String, Class<?>> aliases = new HashMap<>();

        aliases.put("member", Member.class);
        aliases.put("username", Integer.class);
        aliases.put("password", String.class);
//        aliases.put("createdDt", LocalDate.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAliases(aliases);

        return xStreamMarshaller;
    }

    @Bean
    public ItemWriter<Member> customItemWriter() {
        logger.info("\t\t\t\t5. customItemWriter Start ");
        return items -> {
            for (Member item : items) {
                logger.info("\t\t\t\t\t customItemWriter.item = " + item.toString());
            }
        };
    }

}
