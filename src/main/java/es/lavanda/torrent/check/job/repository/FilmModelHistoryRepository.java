package es.lavanda.torrent.check.job.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.lavanda.torrent.check.job.model.FilmModelHistory;

public interface FilmModelHistoryRepository extends MongoRepository<FilmModelHistory, String> {

    boolean existsByTorrentUrl(String torrentUrl);

}
