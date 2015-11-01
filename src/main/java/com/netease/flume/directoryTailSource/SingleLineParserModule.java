package com.netease.flume.directoryTailSource;

import org.apache.flume.Context;
import org.apache.flume.conf.Configurable;

public class SingleLineParserModule implements Configurable, DirectoryTailParserModulable {

  public void flush() {}

  public void parse(String line, FileSet header) {}

  public boolean isFirstLine(String line) {
    return false;
  }

  public boolean isLastLine(String line) {
    return true;
  }

  public void configure(Context context) {
    // TODO Auto-generated method stub
  }
}
