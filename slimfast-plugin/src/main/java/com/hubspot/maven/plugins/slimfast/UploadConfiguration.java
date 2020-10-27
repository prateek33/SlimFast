package com.hubspot.maven.plugins.slimfast;

import java.nio.file.Path;

public class UploadConfiguration {
  private final Path prefix;
  private final String s3Endpoint;
  private final String s3Region;
  private final String s3Bucket;
  private final String s3ArtifactRoot;
  private final String s3AccessKey;
  private final String s3SecretKey;
  private final Path outputFile;
  private final boolean allowUnresolvedSnapshots;

  public UploadConfiguration(Path prefix,
                             String s3Endpoint,
                             String s3Region,
                             String s3Bucket,
                             String s3ArtifactRoot,
                             String s3AccessKey,
                             String s3SecretKey,
                             Path outputFile,
                             boolean allowUnresolvedSnapshots) {
    this.prefix = prefix;
    this.s3Endpoint = s3Endpoint;
    this.s3Region = s3Region;
    this.s3Bucket = s3Bucket;
    this.s3ArtifactRoot = s3ArtifactRoot;
    this.s3AccessKey = s3AccessKey;
    this.s3SecretKey = s3SecretKey;
    this.outputFile = outputFile;
    this.allowUnresolvedSnapshots = allowUnresolvedSnapshots;
  }

  public Path getPrefix() {
    return prefix;
  }

  public String getS3Endpoint() {
    return s3Endpoint;
  }

  public String getS3Region() {
    return s3Region;
  }

  public String getS3Bucket() {
    return s3Bucket;
  }

  public String getS3ArtifactRoot() {
    return s3ArtifactRoot;
  }

  public String getS3AccessKey() {
    return s3AccessKey;
  }

  public String getS3SecretKey() {
    return s3SecretKey;
  }

  public Path getOutputFile() {
    return outputFile;
  }

  public boolean isAllowUnresolvedSnapshots() {
    return allowUnresolvedSnapshots;
  }
}
