package com.netease.flume.directoryTailSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.apache.flume.Transaction;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSet {
  private static final Logger logger = LoggerFactory.getLogger(FileSet.class);
  private AbstractSource source;
  private FileObject fileObject;
  private BufferedReader bufferedReader;
  private RandomAccessFile rReader;
  private Transaction transaction;
  private List<String> bufferList;
  private Map<String, String> headers;
  private long lastAppendTime;
  private Long seq;

  public FileSet(AbstractSource source, FileObject fileObject, Map<String, Long> fileInitLengthMap) throws IOException {
    this.source = source;
    this.fileObject = fileObject;

    this.bufferList = new ArrayList<String>();

    File f = new File(fileObject.getName().getPath());

    rReader = new RandomAccessFile(f, "r");
    // rReader.seek(f.length());
    /*
     * 判断在初始化taildirSource时，这个文件是否存在，如果存在则游标定位当时记录下的文件长度开始 如果不存在，则说明这是一个新建的文件，游标从0开始
     */
    if (fileInitLengthMap.containsKey(fileObject.getName().getPath())) {
      rReader.seek(fileInitLengthMap.get(fileObject.getName().getPath()));
    } else {
      rReader.seek(0);
    }

    bufferList = new ArrayList<String>();
    headers = new HashMap<String, String>();
    logger.debug("FileSet has been created " + fileObject.getName().getPath());
    this.seq = 0L;
  }

  // public String readLine() throws IOException {
  // return rReader.readLine();
  // }
  /**
   *
   * @Title: readLine @Description: 读取文件中的一行
   */
  public String readLine() throws IOException {
    if (rReader.getFilePointer() < rReader.length()) {
      byte b = rReader.readByte();// 读取一个byte
      int i = 0;
      byte[] buf = new byte[10240];// 创建大小为1M的数据，如果你的单行超过1M，那么会出错
      // 如果读到换行符，或者读到文件最后就停止。表示已经读完一行
      while (b != '\n' && rReader.getFilePointer() < rReader.length()) {
        buf[i++] = b;
        b = rReader.readByte();

      }
      return new String(buf, 0, i);
    } else {
      return "";
    }
  }

  public long getLastAppendTime() {
    return lastAppendTime;
  }

  public void setLastAppendTime(long lastAppendTime) {
    this.lastAppendTime = lastAppendTime;
  }

  public boolean appendLine(String buffer) {
    boolean ret = bufferList.add(buffer);
    if (ret) {
      lastAppendTime = System.currentTimeMillis();
    }

    return ret;
  }

  public int getLineSize() {
    return bufferList.size();
  }

  public StringBuffer getAllLines() {

    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < bufferList.size(); i++) {
      sb.append(bufferList.get(i));
    }
    return sb;
  }

  public void setHeader(String key, String value) {
    headers.put(key, value);
  }

  public String getHeader(String key) {
    headers.get(key);
    return null;
  }

  public void clear() {
    bufferList.clear();
    headers.clear();
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public AbstractSource getSource() {
    return source;
  }

  public void setSource(AbstractSource source) {
    this.source = source;
  }

  public List<String> getBufferList() {
    return bufferList;
  }

  public void setBufferList(List<String> bufferList) {
    this.bufferList = bufferList;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public FileObject getFileObject() {
    return fileObject;
  }

  public void setFileObject(FileObject fileObject) {
    this.fileObject = fileObject;
  }

  public BufferedReader getBufferedReader() {
    return bufferedReader;
  }

  public void setBufferedReader(BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
  }

  public Long getSeq() {
    return ++seq;
  }

  /*
   * public void setSeq(Long seq) { this.seq = seq; }
   */

}
