package com.imwiz.flightdata.etl.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
public class AppConfig {

	public static final String IN_DIR = "~/tmp/flightdata/raw";
	public static final String OUT_DIR = "~/tmp/flightdata/analytic";
	public static final String FILE_TYPE_FILTER = "*.txt";

	@Bean
	@InboundChannelAdapter(channel = "pubSubFileChannel", poller = @Poller(fixedDelay = "1000"))
	public MessageSource<File> fileReadingMessageSource() {
		FileReadingMessageSource sourceReader = new FileReadingMessageSource();
		sourceReader.setDirectory(new File(IN_DIR));
		sourceReader.setFilter(new SimplePatternFileListFilter(FILE_TYPE_FILTER));
		return sourceReader;
	}

	@Bean
	@ServiceActivator(inputChannel = "pubSubFileChannel")
	public MessageHandler fileWritingMessageHandler() {
		FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUT_DIR));
		handler.setFileExistsMode(FileExistsMode.REPLACE);
		handler.setExpectReply(false);
		return handler;
	}

}
