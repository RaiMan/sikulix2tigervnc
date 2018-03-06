import com.sikulix.vnc.VNCClient;
import org.junit.*;

import java.awt.*;
import java.awt.image.BufferedImage;

@Ignore
public class TestVNC {

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  private static void p(String msg, Object... args) {
    System.out.println(String.format(msg, args));
  }

  private static void error(String msg, Object... args) {
    System.out.println(String.format("[ERROR] " + msg, args));
  }

  private static void pause(int time) {
    try {
      Thread.sleep(time * 1000);
    } catch (InterruptedException ex) {
    }
  }

  private static boolean shouldRun() {
    String travis = System.getenv("TRAVIS");
    return travis == null ? true : !"true".equals(travis);
  }

  private static String ip = "192.168.2.112";
  private static int port = 5900;
  private static String password = "vnc";
  private static int max = 5;
  private volatile boolean closed;

  @Test
  public void test_000_basic() {
    if (shouldRun()) {
      VNCClient.onDebugging();
      VNCClient client = VNCClient.connect(ip, port, null, true);
      Rectangle bounds = client.getBounds();
      int w = (int) bounds.getWidth();
      int h = (int) bounds.getWidth();
      p("bounds: %s", bounds);
      BufferedImage image = null;
      //M[16,109 406x143]
      int x = 1159;
      int y = 773;
      w = 406;
      h = 143;
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            client.processMessages();
          } catch (RuntimeException e) {
            if (!closed) {
              throw e;
            }
          }
        }
      }).start();
      client.refreshFramebuffer();
      for (int n = 0; n < max; n++) {
        image = client.getFrameBuffer(x, y, w, h);
        p("%d: %s", n, image);
        pause(1);
      }
      closed = true;
      client.close();
    }
  }
}
