package com.github.richygreat.microuser.user.service;

import com.github.richygreat.microuser.stream.*;
import com.github.richygreat.microuser.stream.exception.EventPushFailedException;
import com.github.richygreat.microuser.user.entity.UserEntity;
import com.github.richygreat.microuser.user.exception.UserAlreadyExistsException;
import com.github.richygreat.microuser.user.model.UserDTO;
import com.github.richygreat.microuser.user.model.UserStatus;
import com.github.richygreat.microuser.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final Source source;
    private final UserRepository userRepository;
    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    private ReadOnlyKeyValueStore<String, UserDTO> userSnapshotStore;

    public void createUser(UserDTO userDTO) {
        validateIfExistingUser(userDTO);
        userDTO.setId(UUID.randomUUID().toString());
        boolean sent = source.userProducer().send(KafkaMessageUtility.createMessage(userDTO,
                KafkaEventConstants.USER_CREATION_REQUESTED, userDTO.getId(), userDTO.getEmail()));
        log.info("createUser: Exiting userDTO: {} sent: {}", userDTO.getId(), sent);
        if (!sent) {
            throw new EventPushFailedException();
        }
    }

    @Transactional
    @StreamListener(value = KafkaChannel.USER_SINK_CHANNEL, condition = KafkaEventConstants.USER_CREATION_REQUESTED_HEADER)
    public void handleUserCreationRequested(@Payload UserDTO userDTO,
                                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("handleUserCreationRequested: Entering: userDTO: {} partition: {}", userDTO, partition);
        validateIfExistingUser(userDTO);
        UserEntity user = new UserEntity();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setTaxId(userDTO.getTaxId());
        user.setStatus(UserStatus.CREATED);
        userRepository.save(user);
        boolean sent = source.userProducer().send(KafkaMessageUtility.createMessage(userDTO,
                KafkaEventConstants.USER_CREATED, userDTO.getId(), userDTO.getEmail()));
        log.info("handleUserCreationRequested: Exiting userDTO: {} sent: {}", userDTO.getId(), sent);
        if (!sent) {
            throw new EventPushFailedException();
        }
    }

    public UserDTO getUser(String id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            initOnRun();
            return userSnapshotStore.get(id);
        }
        UserEntity user = optionalUser.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setTaxId(user.getTaxId());
        userDTO.setStatus(user.getStatus());
        return userDTO;
    }

    public void updateUserStatus(String email, UserStatus status) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        optionalUser.ifPresent(user -> {
            user.setStatus(status);
            userRepository.save(user);
        });
    }

    private void initOnRun() {
        if (userSnapshotStore != null) {
            return;
        }
        userSnapshotStore = streamsBuilderFactoryBean.getKafkaStreams().store(KafkaConstants.USER_STORE,
                QueryableStoreTypes.keyValueStore());
    }

    private void validateIfExistingUser(UserDTO userDTO) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
    }
}
