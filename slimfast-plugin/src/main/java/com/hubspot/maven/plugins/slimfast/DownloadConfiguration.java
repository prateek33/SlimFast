package com.hubspot.maven.plugins.slimfast;

import java.nio.file.Path;

import com.amazonaws.services.s3.AmazonS3;

public class DownloadConfiguration {
  private final Path prefix;
  private final Path cacheDirectory;
  private final Path outputDirectory;
  private final String s3Endpoint;
  private final String s3Region;
  private final String s3AccessKey;
  private final String s3SecretKey;

  public DownloadConfiguration(Path prefix,
                               Path cacheDirectory,
                               Path outputDirectory,
                               String s3Endpoint,
                               String s3Region,
                               String s3AccessKey,
                               String s3SecretKey) {
    this.prefix = prefix;
    this.cacheDirectory = cacheDirectory;
    this.outputDirectory = outputDirectory;
    this.s3Endpoint = s3Endpoint;
    this.s3Region = s3Region;
    this.s3AccessKey = s3AccessKey;
    this.s3SecretKey = s3SecretKey;
  }

  public Path getPrefix() {
    return prefix;
  }

  public Path getCacheDirectory() {
    return cacheDirectory;
  }

  public Path getOutputDirectory() {
    return outputDirectory;
  }

  public String getS3Endpoint() {
    return s3Endpoint;
  }

  public String getS3Region() {
    return s3Region;
  }

  public String getS3AccessKey() {
    return s3AccessKey;
  }

  public String getS3SecretKey() {
    return s3SecretKey;
  }
}
