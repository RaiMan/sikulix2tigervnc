import com.sikulix.api.Element;
import com.sikulix.api.Picture;
import com.sikulix.core.SX;
import com.sikulix.devices.vnc.VNCDevice;
import com.sikulix.vnc.VNCClient;
import com.sikulix.devices.IDevice;

import org.junit.*;

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

  private static String ip = "192.168.2.63";
  private static int port = 5900;
  private static String password = "vnc";

  @Test
  public void test_000_basic() {
    if (shouldRun()) {
      VNCClient.onDebugging();
      IDevice vnc = new VNCDevice();
      vnc.start("192.168.2.63", 5900, "vnc");
      Element area = new Element(100, 100, 300, 300);
      Picture picture;
      for (int n = 0; n < 1; n++) {
        picture = vnc.capture();
        picture.show(2);
      }
      SX.pause(1);
      vnc.stop();
    }
  }
}
