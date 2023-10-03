package info.colinhan.sisyphus.jacal.converter;

import com.alibaba.fastjson2.JSON;
import info.colinhan.sisyphus.jacal.model.Form;
import info.colinhan.sisyphus.tartarus.model.Flow;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FormConverter implements AttributeConverter<Form, String> {
    @Override
    public String convertToDatabaseColumn(Form form) {
        return JSON.toJSONString(form);
    }

    @Override
    public Form convertToEntityAttribute(String s) {
        return JSON.parseObject(s, Form.class);
    }
}
