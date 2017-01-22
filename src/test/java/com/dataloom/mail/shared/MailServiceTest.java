package com.dataloom.mail.shared;

import com.dataloom.mail.RenderableEmailRequest;
import com.dataloom.mail.services.MailRenderer;
import com.dataloom.mail.services.MailService;
import com.dataloom.mail.templates.EmailTemplate;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.hazelcast.core.IQueue;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MailServiceTest extends GreenMailTest {
    private static final IQueue<RenderableEmailRequest> emailRequests = Mockito.mock( IQRER.class );
    private static MailService mailService;
    private static MailRenderer renderer       = new MailRenderer();
    private static String       EMAIL_REQUESTS = "EMAIL_REQUESTS";

    @BeforeClass
    public static void beforeClass() throws Exception {
        mailService = new MailService( testMailServiceConfig, renderer, emailRequests );
    }

    // use for local test only.
    //    @Test
    //    public void sendEmailLocalTest() {
    //        MailServiceConfig realConfig = new MailServiceConfig( "smtp.gmail.com", 465, "courier@kryptnostic.com", "cwqcqbqyjtocehvs" );
    //        mailService.configureSmtpServer( realConfig );
    //
    //        String[] toAddresses = new String[] { "Yao <yao@kryptnostic.com>" };
    //        // construct Gravatar
    //
    //        // construct RenderableEmailRequest
    //
    //        Mockito.when( emailRequests.poll() )
    //            .thenReturn( emailRequest )
    //            .thenReturn( null );
    //
    //        mailService.processEmailRequestsQueue();
    //
    //    }

    @Test
    public void sendEmailTest() throws MessagingException, InterruptedException {
        String[] toAddresses = new String[] { "GoJIRA <jira@kryptnostic.com>", "KryptoDoge <kryptodoge@gmail.com>" };
        RenderableEmailRequest emailRequest = new RenderableEmailRequest(
                Optional.of( EmailTemplate.getCourierEmailAddress() ),
                toAddresses,
                Optional.absent(),
                Optional.absent(),
                EmailTemplate.KODEX_INVITATION.getPath(),
                Optional.of( EmailTemplate.KODEX_INVITATION.getSubject() ),
                Optional.of( ImmutableMap
                        .of( "name", "Master Chief", "avatar-path", "the path", "registration-url", "test" ) ),
                Optional.absent(),
                Optional.absent() );

        CountDownLatch latch = new CountDownLatch( 1 );
        Lock lock = new ReentrantLock();
        Mockito.when( emailRequests.take() )
                .then( invocation -> {
                    if ( lock.tryLock() ) {
                        latch.countDown();
                        return emailRequest;
                    } else {
                        lock.lock();
                        return null;
                    }
                } );
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute( mailService::processEmailRequestsQueue ); // Will trigger rendering
        latch.await();

        Mockito.verify( emailRequests, Mockito.atLeastOnce() ).take();

        // waitForIncomingEmail() is useful if sending is done asynchronously in a separate thread
        Assert.assertTrue( greenMailServer.waitForIncomingEmail( DEFAULT_WAIT_TIME, 2 ) );

        Message[] emails = greenMailServer.getReceivedMessages();
        Assert.assertEquals( toAddresses.length, emails.length );

        for ( int i = 0; i < emails.length; ++i ) {
            Message email = emails[ i ];

            Assert.assertEquals( EmailTemplate.KODEX_INVITATION.getSubject(), email.getSubject() );

            Assert.assertEquals( 1, email.getAllRecipients().length );
            Assert.assertNull( email.getRecipients( Message.RecipientType.CC ) );
            Assert.assertNull( email.getRecipients( Message.RecipientType.BCC ) );

            Assert.assertEquals( 1, email.getHeader( "From" ).length );
            Assert.assertEquals( EmailTemplate.getCourierEmailAddress(), email.getHeader( "From" )[ 0 ] );

            Assert.assertEquals( 1, email.getHeader( "To" ).length );
            // For efficiency, I update the sendEmail method to spool out all emails through same session.
            // So we cannot guarantee the receiving order.
            //Assert.assertEquals( toAddresses[ i ], email.getHeader( "To" )[ 0 ] );
        }

    }

    @Test(
            expected = NullPointerException.class )
    public void testBadRequest_NullEmailRequest() throws IOException {
        mailService.sendEmailAfterRendering( null, null );
    }

    @Test
    public void testJustinIsBlacklisted() {
        Assert.assertFalse( MailRenderer.isNotBlacklisted( "justin@krypt.com" ) );
    }

    @Test
    public void testOthersAreNotBlacklisted() {
        Assert.assertTrue( MailRenderer.isNotBlacklisted( "ryan@kryptnostic.com" ) );
    }

    static interface IQRER extends IQueue<RenderableEmailRequest> {

    }
}
