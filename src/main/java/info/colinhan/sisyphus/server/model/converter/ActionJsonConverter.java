package info.colinhan.sisyphus.server.model.converter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONType;
import info.colinhan.sisyphus.tartarus.model.Action;

public class ActionJsonConverter {
    public static void register() {
        JSON.mixIn(Action.class, ActionMixin.class);
    }

    @JSONType(ignores = "definition")
    private static class ActionMixin {
    }
}
