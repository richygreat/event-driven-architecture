package com.github.richygreat.microtransaction;

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

import com.github.richygreat.microtransaction.stream.KafkaConstants;
import com.github.richygreat.microtransaction.stream.Processor;
import com.github.richygreat.microtransaction.transaction.model.TransactionDTO;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableKafkaStreams
public class MicroTransactionApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroTransactionApplication.class, args);
	}

	@Bean
	public GlobalKTable<String, TransactionDTO> transactionSnapshotTable(StreamsBuilder streamsBuilder) {
		return streamsBuilder.globalTable("li5jiphz-transaction",
				Consumed.with(Serdes.String(), new JsonSerde<>(TransactionDTO.class)),
				Materialized.as(KafkaConstants.TRANSACTION_STORE));
	}
}
