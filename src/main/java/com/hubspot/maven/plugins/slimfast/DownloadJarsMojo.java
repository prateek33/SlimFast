package com.hubspot.maven.plugins.slimfast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Mojo(name = "download", threadSafe = true)
public class DownloadJarsMojo extends AbstractMojo {

  @Parameter(alias = "fileDownloader", defaultValue = "com.hubspot.maven.plugins.slimfast.DefaultFileDownloader")
  private String fileDownloaderType;

  @Parameter(defaultValue = "${s3.access.key}", required = true)
  private String s3AccessKey;

  @Parameter(defaultValue = "${s3.secret.key}", required = true)
  private String s3SecretKey;

  @Parameter(defaultValue = "10")
  private int s3DownloadThreads;

  @Parameter(defaultValue = "${settings.localRepository}")
  private String cacheDirectory;

  @Parameter(defaultValue = "${project.build.directory}/s3.artifacts.json")
  private String inputFile;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    final DownloadConfiguration configuration = buildConfiguration();
    Collection<S3Artifact> artifacts = readArtifacts();

    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("slimfast-download").setDaemon(true).build();
    ExecutorService executor = Executors.newFixedThreadPool(s3DownloadThreads, threadFactory);
    final FileDownloader downloader = instantiateFileDownloader();
    downloader.init(configuration, getLog());

    List<Future<?>> futures = new ArrayList<>();
    for (final S3Artifact artifact : artifacts) {
      futures.add(executor.submit(new Callable<Object>() {

        @Override
        public Object call() throws Exception {
          downloader.download(configuration, artifact);
          return null;
        }
      }));
    }

    executor.shutdown();
    waitForDownloadsToFinish(executor, futures);
    downloader.destroy();
  }

  private Collection<S3Artifact> readArtifacts() throws MojoFailureException {
    try {
      return JsonHelper.readArtifactsFromJson(new File(inputFile));
    } catch (IOException e) {
      throw new MojoFailureException("Error writing dependencies json to file", e);
    }
  }

  private void waitForDownloadsToFinish(ExecutorService executor, List<Future<?>> futures) throws MojoExecutionException, MojoFailureException {
    try {
      if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
        getLog().error("Took more than 5 minutes to download files, quitting");
        throw new MojoExecutionException("Took more than 5 minutes to download files");
      }

      for (Future<?> future : futures) {
        future.get();

      }
    } catch (InterruptedException e) {
      throw new MojoExecutionException("Interrupted", e);
    } catch (ExecutionException e) {
      Throwables.propagateIfInstanceOf(e.getCause(), MojoExecutionException.class);
      Throwables.propagateIfInstanceOf(e.getCause(), MojoFailureException.class);
      throw new MojoExecutionException("Unexpected exception", e.getCause());
    }
  }

  private DownloadConfiguration buildConfiguration() {
    return new DownloadConfiguration(cacheDirectory, s3AccessKey, s3SecretKey);
  }

  private FileDownloader instantiateFileDownloader() throws MojoExecutionException {
    try {
      return (FileDownloader) Class.forName(fileDownloaderType).newInstance();
    } catch (ClassNotFoundException e) {
      throw new MojoExecutionException("Unable to find file downloader implementation", e);
    } catch (InstantiationException | IllegalAccessException e) {
      throw new MojoExecutionException("Unable to instantiate file downloader", e);
    } catch (ClassCastException e) {
      throw new MojoExecutionException("Must implement FileDownloader interface", e);
    }
  }
}