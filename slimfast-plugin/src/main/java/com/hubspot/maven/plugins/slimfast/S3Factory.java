package com.hubspot.maven.plugins.slimfast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Factory {

  private S3Factory() {
    throw new AssertionError();
  }

  public static AmazonS3 create(String accessKey, String secretKey, String endpoint, String region) {
    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    return AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withClientConfiguration(
            new ClientConfiguration()
                .withConnectionTimeout(2_000)
                .withRequestTimeout(5_000)
                .withMaxErrorRetry(5)
        )
        .build();
  }
}
