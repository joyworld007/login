package com.shyun.kr;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

public class csvWriter {

  //@Test
  public void write() throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    OutputStream outStream = new FileOutputStream("coupon.csv", true);
    for (int i = 0; i < 100000; i++) {
      outStream.write((i + "," + LocalDateTime.now().plusDays(1).format(formatter)).getBytes());
      outStream.write("\r\n".getBytes());
    }
    outStream.close();
  }
}
