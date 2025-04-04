package com.ticketing.storage.performance.mongo.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;


@Configuration
public class ConvertConfig {

	@Bean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(Arrays.asList(
			new LocalDateTimeToDateConverter(),
			new DateToLocalDateTimeConverter()
		));
	}

	@WritingConverter
	static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
		@Override
		public Date convert(LocalDateTime source) {
			return Date.from(source.atZone(ZoneId.of("Asia/Seoul")).toInstant());
		}
	}

	@ReadingConverter
	static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
		@Override
		public LocalDateTime convert(Date source) {
			return LocalDateTime.ofInstant(source.toInstant(), ZoneId.of("Asia/Seoul"));
		}
	}
}
