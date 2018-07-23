package com.n26.challenge.web.configuration;

import com.n26.challenge.api.request.SaveTransactionRequest;
import com.n26.challenge.model.Transaction;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.addMappings(new PropertyMap<SaveTransactionRequest, Transaction>() {
            @Override
            protected void configure() {
                using(new AbstractConverter<Long, LocalDateTime>() {
                      @Override
                      protected LocalDateTime convert(Long epochMillis) {
                          Instant instant = Instant.ofEpochMilli(epochMillis);
                          return LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId());
                      }

               }).map(source.getTimestamp()).setTime(null);
            }
        });

        return modelMapper;
    }


}
