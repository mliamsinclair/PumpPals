package com.pumppals.pumppalsapi.exceptions;

public class FileEmptyException extends SpringBootFileUploadException {
   public FileEmptyException(String message) {
       super(message);
   }
}
