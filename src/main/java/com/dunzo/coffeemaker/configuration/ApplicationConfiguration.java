package com.dunzo.coffeemaker.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MachineConfigurationProperties.class)
public class ApplicationConfiguration {
}
