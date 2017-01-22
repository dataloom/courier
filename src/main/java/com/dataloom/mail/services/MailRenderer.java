package com.dataloom.mail.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dataloom.mail.RenderableEmailRequest;
import com.dataloom.mail.exceptions.InvalidTemplateException;
import com.dataloom.mail.templates.EmailTemplate;
import com.dataloom.mail.utils.TemplateUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.att.ByteArrayAttachment;

public class MailRenderer {
    private Logger                   logger                        = LoggerFactory
                                                                           .getLogger( MailRenderer.class );

    // TODO: Move to Dynamic Blacklist
    private static final Set<String> REGISTRATION_DOMAIN_BLACKLIST = Sets.newHashSet( "krypt.com" );

    @VisibleForTesting
    public static boolean isNotBlacklisted( String to ) {
        return !REGISTRATION_DOMAIN_BLACKLIST.stream().anyMatch( ( domain ) -> to.contains( domain ) );
    }

    protected Set<Email> renderEmail( RenderableEmailRequest emailRequest ) {
        Iterable<String> toAddresses = Iterables.filter( Arrays.asList( emailRequest.getTo() ),
                ( String address ) -> isNotBlacklisted( address ) );
        logger.info( "filtered e-mail addresses that are blacklisted." );

        if ( Iterables.size( toAddresses ) < 1 ) {
            logger.error( "Must include at least one valid e-mail address.");
            return ImmutableSet.of();
        }
        String template;
        try {
            template = TemplateUtils.loadTemplate( emailRequest.getTemplatePath() );
        } catch ( IOException e ) {
            throw new InvalidTemplateException(
                    "Invalid Email Template: " + emailRequest.getTemplatePath(),
                    e );
        }
        String templateHtml = TemplateUtils.DEFAULT_TEMPLATE_COMPILER
                .compile( template )
                .execute( emailRequest.getTemplateObjs().or( new Object() ) );

        /*
         * when someone invites multiple people, we want to spool an individual invite for each person. as such, we need
         * to create a multiple Email objects instead of one Email object with multiple "To" fields to avoid having
         * multiple emails appear in the "To" field in an email client like Gmail.
         */
        Set<Email> emailSet = Sets.newHashSet();
        for ( String toAddress : toAddresses ) {

            Email email = Email.create()
                    .from( emailRequest.getFrom().or( EmailTemplate.getCourierEmailAddress() ) )
                    .to( toAddress )
                    .subject( emailRequest.getSubject().or( "" ) )
                    .addHtml( templateHtml );
            if ( emailRequest.getByteArrayAttachment().isPresent() ) {
                ByteArrayAttachment[] attachments = emailRequest.getByteArrayAttachment().get();
                for ( int i = 0; i < attachments.length; i++ ) {
                    email.attach( attachments[ i ] );
                }
            }

            if ( emailRequest.getAttachmentPaths().isPresent() ) {
                String[] paths = emailRequest.getAttachmentPaths().get();
                for ( int i = 0; i < paths.length; i++ ) {
                    email.attach( EmailAttachment.attachment().file( paths[ i ] ) );
                }
            }

            emailSet.add( email );
        }
        return emailSet;
    }

}
