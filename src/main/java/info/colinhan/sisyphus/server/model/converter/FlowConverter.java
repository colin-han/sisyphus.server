package info.colinhan.sisyphus.server.model.converter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import info.colinhan.sisyphus.tartarus.model.Flow;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FlowConverter implements AttributeConverter<Flow, String> {
    @Override
    public String convertToDatabaseColumn(Flow flow) {
        return JSON.toJSONString(flow, JSONWriter.Feature.WriteClassName);
    }

    @Override
    public Flow convertToEntityAttribute(String s) {
        return JSON.parseObject(s, Flow.class, JSONReader.Feature.SupportAutoType);
    }

}
