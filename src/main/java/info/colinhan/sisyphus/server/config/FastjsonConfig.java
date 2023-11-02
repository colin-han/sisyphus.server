package info.colinhan.sisyphus.server.config;

import info.colinhan.sisyphus.server.model.converter.ActionJsonConverter;
import info.colinhan.sisyphus.server.model.converter.VariableTypeJsonConverter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastjsonConfig {
    static {
        VariableTypeJsonConverter.register();
        ActionJsonConverter.register();
    }
}
