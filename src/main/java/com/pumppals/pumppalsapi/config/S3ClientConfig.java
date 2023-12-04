package com.pumppals.pumppalsapi.config;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientConfig {

   @Value("${aws.accessKey}")
   private String accessKey;

   @Value("${aws.secretKey}")
   private String secretKey;

   @Bean
   public AmazonS3 initS3Client(){
       AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
       return AmazonS3ClientBuilder.standard()
               .withRegion(Regions.US_EAST_2)
               .withCredentials(new AWSStaticCredentialsProvider(credentials))
               .build();
   }

}
