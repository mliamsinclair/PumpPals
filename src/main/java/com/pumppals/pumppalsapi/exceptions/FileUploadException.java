package com.pumppals.pumppalsapi.exceptions;

public class FileUploadException extends SpringBootFileUploadException{

   public FileUploadException(String message) {
       super(message);
   }
}
