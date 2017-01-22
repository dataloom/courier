package com.dataloom.mail.pods;

import java.io.IOException;

import javax.inject.Inject;

import com.dataloom.mail.config.MailServiceRequirements;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dataloom.mail.config.MailServiceConfig;
import com.dataloom.mail.services.MailRenderer;
import com.dataloom.mail.services.MailService;
import com.hazelcast.core.HazelcastInstance;
import com.kryptnostic.rhizome.configuration.service.ConfigurationService;

/**
 * This is the plugin pod that will activate a mail service lambda
 * @author Matthew Tamayo-Rios &lt;matthew@kryptnostic.com&gt;
 */
@Configuration
public class MailServicePod {

    @Inject
    private MailServiceRequirements requirements;

    @Inject
    private ConfigurationService config;

    @Bean
    public MailService mailService() throws IOException {
        return new MailService( mailServiceConfig(), mailRenderer(), requirements.getEmailQueue() );
    }

    @Bean
    public MailRenderer mailRenderer() {
        return new MailRenderer();
    }

    @Bean
    public MailServiceConfig mailServiceConfig() throws IOException {
        return config.getConfiguration( MailServiceConfig.class );
    }

}