package clear4j.web.img;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 10/08/12
 * Time: 20:00
 */
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface Path {
	String value();
}
