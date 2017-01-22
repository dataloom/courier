package com.dataloom.mail.config;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.StringUtils;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( template == null ) ? 0 : template.hashCode() );
        result = prime * result + ( ( templateId == null ) ? 0 : templateId.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( !( obj instanceof HtmlEmailTemplate ) ) {
            return false;
        }
        HtmlEmailTemplate other = (HtmlEmailTemplate) obj;
        if ( template == null ) {
            if ( other.template != null ) {
                return false;
            }
        } else if ( !template.equals( other.template ) ) {
            return false;
        }
        if ( templateId == null ) {
            if ( other.templateId != null ) {
                return false;
            }
        } else if ( !templateId.equals( other.templateId ) ) {
            return false;
        }
        return true;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplate() {
        return template;
    }

    public static class Builder {
        private String templateId = "";
        private String template   = "";

        public Builder() {}

        Builder withId( String templateId ) {
            this.templateId = templateId;
            return this;
        }

        Builder withTemplate( String template ) {
            this.template = template;
            return this;
        }

        public HtmlEmailTemplate build() {
            return new HtmlEmailTemplate( templateId, template );
        }
    }
}
