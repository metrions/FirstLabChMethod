package org.example.config;

import org.example.GenerateData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class Config {
    @Bean
    public GenerateData generateData() {
        return new GenerateData(new ArrayList<>(Arrays.asList(
                0.5546312621941819,
                0.5707601032404624,
                0.6327994391545045,
                0.6949506549758557,
                0.7051433382314489,
                0.7064195191773128,
                0.7302175625667583,
                0.9302886304763456,
                0.9890664986010321,
                0.9970467772885679)
        ));
    }

}
