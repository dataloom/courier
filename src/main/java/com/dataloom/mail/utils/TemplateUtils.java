package com.dataloom.mail.utils;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.samskivert.mustache.Mustache;

public final class TemplateUtils {

    public static final Mustache.Compiler DEFAULT_TEMPLATE_COMPILER = Mustache.compiler()
            .withLoader( templateResourcePath -> {
                return new StringReader( loadTemplate( templateResourcePath ) );
            } );

    private TemplateUtils() {}

    public static String loadTemplate( String templatePath ) throws IOException {
        URL templateResource = Resources.getResource( templatePath );
        return Resources.toString( templateResource, Charsets.UTF_8 );
    }

}
