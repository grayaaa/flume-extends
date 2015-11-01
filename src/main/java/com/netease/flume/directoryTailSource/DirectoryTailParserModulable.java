package com.netease.flume.directoryTailSource;

import org.apache.flume.conf.Configurable;

public interface DirectoryTailParserModulable extends Configurable {
  public void flush();

  public void parse(String line, FileSet header);

  public boolean isFirstLine(String line);

  public boolean isLastLine(String line);
}
