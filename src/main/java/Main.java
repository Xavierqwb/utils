import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by qiuwenbin on 2017/8/12.
 */
public class Main {

	private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		Optional<String> optional = Optional.of("aaa");
		optional = optional.flatMap((s) -> Optional.of(s + "!"));
		optional.ifPresent(System.out::println);
	}
}
