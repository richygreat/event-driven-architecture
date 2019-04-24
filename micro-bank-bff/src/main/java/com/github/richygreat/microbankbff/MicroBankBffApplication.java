package com.github.richygreat.microbankbff;

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

import com.github.richygreat.microbankbff.stream.KafkaConstants;
import com.github.richygreat.microbankbff.stream.Processor;
import com.github.richygreat.microbankbff.transaction.model.TransactionDTO;
import com.github.richygreat.microbankbff.user.model.UserDTO;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableKafkaStreams
public class MicroBankBffApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroBankBffApplication.class, args);
	}

	@Bean
	public GlobalKTable<String, UserDTO> userSnapshotTable(StreamsBuilder streamsBuilder) {
		return streamsBuilder.globalTable("li5jiphz-user",
				Consumed.with(Serdes.String(), new JsonSerde<>(UserDTO.class)),
				Materialized.as(KafkaConstants.USER_STORE));
	}
	
	@Bean
	public GlobalKTable<String, TransactionDTO> transactionSnapshotTable(StreamsBuilder streamsBuilder) {
		return streamsBuilder.globalTable("li5jiphz-transaction",
				Consumed.with(Serdes.String(), new JsonSerde<>(TransactionDTO.class)),
				Materialized.as(KafkaConstants.TRANSACTION_STORE));
	}
}
