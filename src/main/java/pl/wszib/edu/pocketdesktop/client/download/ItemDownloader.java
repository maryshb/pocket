package pl.wszib.edu.pocketdesktop.client.download;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ItemDownloader {

  @Autowired
  private RestTemplate restTemplate;

  public void downloadItem(File fileToDownload, String fileNameToDownload, String urlToDownload)
      throws IOException {

    if (fileToDownload == null || fileNameToDownload == null || urlToDownload == null) {
      throw new NullPointerException("One or more input parameters is null");
    }

    String content = getHtmlContent(urlToDownload);

    String fileName = fileNameToDownload + ".html";
    String filePath = fileToDownload.getAbsolutePath();

    File file = new File(filePath, fileName);
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

    FileUtils.copyToFile(inputStream, file);
  }

  private String getHtmlContent(String url) {

    return restTemplate.getForObject(url, String.class);
  }
}
