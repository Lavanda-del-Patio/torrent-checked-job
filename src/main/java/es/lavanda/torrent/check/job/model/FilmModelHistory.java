package es.lavanda.torrent.check.job.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("feed_films_history")
public class FilmModelHistory {

    @Id
    private String id;

    private String torrentUrl;

    public FilmModelHistory(String torrentUrl) {
        this.torrentUrl = torrentUrl;
    }

}
