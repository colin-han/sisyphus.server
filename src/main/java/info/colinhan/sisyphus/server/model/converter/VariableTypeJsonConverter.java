package info.colinhan.sisyphus.server.model.converter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;
import info.colinhan.sisyphus.exception.ParseError;
import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.model.VariableTypes;
import info.colinhan.sisyphus.util.ResultOrErrors;

import java.lang.reflect.Type;

public class VariableTypeJsonConverter {
    public static void register() {
        JSON.register(new VariableTypeReaderModule());
        JSON.register(new VariableTypeWriterModule());
    }

    private static class VariableTypeReaderModule implements ObjectReaderModule {
        @Override
        public ObjectReader<?> getObjectReader(Type type) {
            if (type instanceof Class<?> clazz) {
                if (VariableType.class.isAssignableFrom(clazz)) {
                    return new VariableTypeReader();
                }
            }
            return null;
        }
    }

    private static class VariableTypeReader implements ObjectReader<VariableType> {
        @Override
        public VariableType readObject(JSONReader jsonReader, Type type, Object o, long l) {
            ResultOrErrors<VariableType, ParseError> resultOrErrors = VariableTypes.parseType(jsonReader.readString());
            if (resultOrErrors.isSuccess()) {
                return resultOrErrors.result();
            }
            throw new RuntimeException(resultOrErrors.errors().get(0).message());
        }
    }

    private static class VariableTypeWriterModule implements ObjectWriterModule {
        @Override
        public ObjectWriter<?> getObjectWriter(Type type, Class clazz) {
            if (VariableType.class.isAssignableFrom(clazz)) {
                return new VariableTypeWriter();
            }
            return null;
        }
    }

    private static class VariableTypeWriter implements ObjectWriter<VariableType> {
        @Override
        public void write(JSONWriter jsonWriter, Object o, Object o1, Type type, long l) {
            jsonWriter.writeString(((VariableType) o).getName());
        }
    }
}
