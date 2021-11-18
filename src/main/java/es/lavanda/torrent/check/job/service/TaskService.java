package es.lavanda.torrent.check.job.service;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import es.lavanda.torrent.check.job.exception.TorrentCheckJobException;
import es.lavanda.torrent.check.job.model.TorrentCheckedResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService implements CommandLineRunner {

    @Autowired
    private FilmsService filmsServiceImpl;

    private static AmazonSQS amazonSQS;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Value("https://sqs.eu-west-1.amazonaws.com/836783797541/torrent-checked-${spring.profiles.active}")
    private String queueUrl;

    @Override
    public void run(String... args) throws Exception {
        amazonSQS = AmazonSQSClientBuilder.standard().withRegion(awsRegion).build();
        final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withMaxNumberOfMessages(10);
        final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
        log.info("Num of messages received of the queue {}: {}", queueUrl, messages.size());
        for (Message messageObject : messages) {
            String message = messageObject.getBody();
            log.info("Received message: " + message);
            log.info("Reading message of the queue torrent-checked");
            ObjectMapper mapper = new ObjectMapper();
            TorrentCheckedResponse torrentChecked = new TorrentCheckedResponse();
            try {
                torrentChecked = mapper.readValue(message, TorrentCheckedResponse.class);
            } catch (JsonProcessingException e) {
                log.error("The message cannot convert to TorrentCheckedResponse", e);
                throw new TorrentCheckJobException("The message cannot convert to TorrentCheckedResponse", e);
            }
            filmsServiceImpl.checkedTorrent(torrentChecked);
            log.debug("Work message finished");
            log.info("Finish task, proceeding to delete message on queue");
            deleteMessage(messageObject);
        }
    }

    private void deleteMessage(Message messageObject) {
        log.info("Deleting message");
        final String messageReceiptHandle = messageObject.getReceiptHandle();
        amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
        log.info("Deleted");

    }
}