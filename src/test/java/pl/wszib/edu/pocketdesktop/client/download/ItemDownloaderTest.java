package pl.wszib.edu.pocketdesktop.client.download;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class ItemDownloaderTest {

  @Mock
  private RestTemplate restTemplate;
  @InjectMocks
  private ItemDownloader itemDownloader;

  @Test
  void downloadItem_WhenProvidedValidArgumentsDownloadContent()
      throws IOException, HttpClientErrorException {

    // given
    String url = "http://example.com";
    String content = "<html><head><title>Example</title></head><body>Example content</body></html>";
    File fileToDownload = new File(System.getProperty("java.io.tmpdir"),
        UUID.randomUUID().toString());
    String fileNameToDownload = "example";

    when(restTemplate.getForObject(url, String.class)).thenReturn(content);

    // when
    itemDownloader.downloadItem(fileToDownload, fileNameToDownload, url);

    // then
    File downloadedFile = new File(fileToDownload, fileNameToDownload + ".html");
    assertTrue(downloadedFile.exists());
    assertEquals(FileUtils.readFileToString(downloadedFile, StandardCharsets.UTF_8), content);
    FileUtils.deleteQuietly(downloadedFile);
    FileUtils.deleteQuietly(fileToDownload);
  }

  @Test
  void downloadItem_WhenProvidedArgumentsNullThrowsNullPointerException() {

    // given
    String url = "http://example.com";
    String fileNameToDownload = "example";

    // then
    assertThrows(NullPointerException.class,
        () -> itemDownloader.downloadItem(null, fileNameToDownload, url));
  }
}
