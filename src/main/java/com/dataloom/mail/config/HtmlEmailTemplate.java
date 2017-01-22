package com.dataloom.mail.config;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@kryptnostic.com&gt;
 */
public class HtmlEmailTemplate {
    private final String templateId;
    private final String template;

    public HtmlEmailTemplate( String templateId, String template ) {
        checkArgument( StringUtils.isNotBlank( templateId ), "TemplateId cannot be blank." );
        checkArgument( StringUtils.isNotBlank( template ), "Template cannot be blank." );

        this.templateId = templateId;
        this.template = template;
    }

    public static class Builder {
        private String templateId = "";
        private String template   = "";

        public Builder() {
        }

        Builder withId( String templateId ) {
            this.templateId = templateId;
            return this;
        }

        Builder withTemplate( String template ) {
            this.template = template;
            return this;
        }

    }
}
