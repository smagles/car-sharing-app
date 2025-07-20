package com.mate.carsharing.config;

import com.mate.carsharing.service.payment.MockStripeService;
import com.mate.carsharing.service.payment.StripeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("develop")
public class StripeMockConfig {
    @Bean
    public StripeService stripeService() {
        return new MockStripeService();
    }
}
