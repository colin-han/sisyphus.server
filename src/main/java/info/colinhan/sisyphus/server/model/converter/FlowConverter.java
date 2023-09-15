package info.colinhan.sisyphus.server.model.converter;

import com.alibaba.fastjson2.JSON;
import info.colinhan.sisyphus.tartarus.model.Flow;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FlowConverter implements AttributeConverter<Flow, String> {
    @Override
    public String convertToDatabaseColumn(Flow flow) {
        return JSON.toJSONString(flow);
    }

    @Override
    public Flow convertToEntityAttribute(String s) {
        return JSON.parseObject(s, Flow.class);
    }
}
