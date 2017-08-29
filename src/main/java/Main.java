import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiuwenbin on 2017/8/12.
 */
public class Main {

	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		String a = "aaaa", b = "bbb", c = "cc";
		LOGGER.debug("test debug {}", a);
		LOGGER.info("test info {}", b);
		LOGGER.error("test error {}", c);
	}
}
