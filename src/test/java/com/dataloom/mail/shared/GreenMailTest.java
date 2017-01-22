package com.dataloom.mail.shared;

import java.security.Security;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.dataloom.mail.config.MailServiceConfig;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

public class GreenMailTest {

    protected static final int         DEFAULT_WAIT_TIME = 2000;

    protected static final String      HOST              = "localhost";
    protected static final String      USERNAME          = "username";
    protected static final String      PASSWORD          = "password";

    protected static final int         PORT              = ServerSetupTest.SMTPS.getPort();

    protected static GreenMail         greenMailServer;
    protected static MailServiceConfig testMailServiceConfig;

    static  {

        // needed to avoid "javax.net.ssl.SSLHandshakeException: Received fatal alert: certificate_unknown"
        Security.setProperty( "ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName() );

        greenMailServer = new GreenMail( ServerSetupTest.SMTPS );

        testMailServiceConfig = new MailServiceConfig(
                HOST,
                PORT,
                USERNAME,
                PASSWORD );

        greenMailServer.start();
    }

    @Before
    public void startGreenMail() {


    }

}