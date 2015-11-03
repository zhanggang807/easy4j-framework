package org.easy4j.framework.web.startup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.easy4j.framework.core.config.GlobalConfig;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-3
 */
public class FastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    private SerializerFeature[] features = {SerializerFeature.WriteMapNullValue};

    public FastJsonHttpMessageConverter(){
        super(new MediaType("application", "json", GlobalConfig.CHARSET), new MediaType("application", "*+json", GlobalConfig.CHARSET));
    }

    public SerializerFeature[] getFeatures() {
        return features;
    }

    public void setFeatures(SerializerFeature... features) {
        this.features = features;
    }

    /**
     * Indicates whether the given class is supported by this converter.
     *
     * @param clazz the class to test for support
     * @return {@code true} if supported; {@code false} otherwise
     */
    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * Abstract template method that reads the actual object. Invoked from {@link #read}.
     *
     * @param clazz        the type of object to return
     * @param inputMessage the HTTP input message to read from
     * @return the converted object
     * @throws java.io.IOException in case of I/O errors
     * @throws org.springframework.http.converter.HttpMessageNotReadableException
     *                             in case of conversion errors
     */
    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        InputStream in = inputMessage.getBody();

        byte[] buf = new byte[1024];
        for (;;) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }

            if (len > 0) {
                baos.write(buf, 0, len);
            }
        }

        byte[] bytes = baos.toByteArray();

        return JSON.parseObject(bytes, 0, bytes.length, GlobalConfig.CHARSET.newDecoder(), clazz);
    }

    /**
     * Abstract template method that writes the actual body. Invoked from {@link #write}.
     *
     * @param obj             the object to write to the output message
     * @param outputMessage the HTTP output message to write to
     * @throws java.io.IOException in case of I/O errors
     * @throws org.springframework.http.converter.HttpMessageNotWritableException
     *                             in case of conversion errors
     */
    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        String text = JSON.toJSONString(obj, features);
        byte[] bytes = text.getBytes(GlobalConfig.CHARSET);
        out.write(bytes);
    }
}
