package com.dataloom.mail.exceptions;

import java.io.IOException;

public class InvalidTemplateException extends RuntimeException {
    private static final long serialVersionUID = -8728023702368839574L;

    public InvalidTemplateException( String message, IOException exception ) {
        super( message, exception );
    }

}
