package com.dataloom.mail.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kryptnostic.rhizome.configuration.Configuration;
import com.kryptnostic.rhizome.configuration.ConfigurationKey;
import com.kryptnostic.rhizome.configuration.SimpleConfigurationKey;

public final class MailServiceConfig implements Configuration {
    private static final long         serialVersionUID    = -6047689414585379842L;

    protected static ConfigurationKey key                 = new SimpleConfigurationKey( "mail-service-config.yaml" );

    protected static final String     SMPTP_HOST_PROPERTY = "smtp-host";
    protected static final String     SMTP_PORT_PROPERTY  = "smtp-port";
    protected static final String     USERNAME_PROPERTY   = "username";
    protected static final String     PASSWORD_PROPERTY   = "password";

    protected final String            smtpHost;
    protected final int               smtpPort;
    protected final String            username;
    protected final String            password;

    @JsonCreator
    public MailServiceConfig(
            @JsonProperty( SMPTP_HOST_PROPERTY ) String smtpHost,
            @JsonProperty( SMTP_PORT_PROPERTY ) int smtpPort,
            @JsonProperty( USERNAME_PROPERTY ) String username,
            @JsonProperty( PASSWORD_PROPERTY ) String password) {

        /*
         * TODO: copy of MailServiceConfiguration.java from Rhizome; need to think about how configuration will work
         */

        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
    }

    @JsonProperty( SMPTP_HOST_PROPERTY )
    public String getSmtpHost() {
        return smtpHost;
    }

    @JsonProperty( SMTP_PORT_PROPERTY )
    public int getSmtpPort() {
        return smtpPort;
    }

    @JsonProperty( USERNAME_PROPERTY )
    public String getUsername() {
        return username;
    }

    @JsonProperty( PASSWORD_PROPERTY )
    public String getPassword() {
        return password;
    }

    public static ConfigurationKey key() {
        return key;
    }

    @Override
    @JsonIgnore
    public ConfigurationKey getKey() {
        return key;
    }

}
