package com.github.richygreat.microuser;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.github.richygreat.microuser.stream.KafkaConstants;
import com.github.richygreat.microuser.stream.Processor;
import com.github.richygreat.microuser.user.model.UserDTO;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableKafkaStreams
public class MicroUserApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroUserApplication.class, args);
	}

	@Bean
	public GlobalKTable<String, UserDTO> userSnapshotTable(StreamsBuilder streamsBuilder) {
		return streamsBuilder.globalTable("li5jiphz-user",
				Consumed.with(Serdes.String(), new JsonSerde<>(UserDTO.class)),
				Materialized.as(KafkaConstants.USER_STORE));
	}
}
