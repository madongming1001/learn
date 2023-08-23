package com.mdm.function;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args) {

        Type[] genericInterfaces = strHandler().getClass().getGenericInterfaces();
    }

    public static Function<Integer, Integer> strHandler() {
        return x -> (int) Math.pow(x, 10);
    }
}
