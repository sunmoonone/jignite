package f.jhandy.ignite.cache;

import javax.cache.configuration.Factory;
import javax.cache.expiry.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * @author sunmoonone
 * @version 2018/12/28
 */
public class ExpiryPolicy {

    @NotBlank
    private String name;
    @Min(1)
    private int timeout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Factory<? extends javax.cache.expiry.ExpiryPolicy> buildFactory() {
        switch (name.toLowerCase()) {
            case "created":
                return CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, timeout));
            case "accessed":
                return AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, timeout));
            case "modified":
                return ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, timeout));
            case "touched":
                return TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, timeout));

        }
        throw new IllegalStateException("unsupported expiry policy " + name);
    }
}
