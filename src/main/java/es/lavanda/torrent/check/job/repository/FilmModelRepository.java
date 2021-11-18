package es.lavanda.torrent.check.job.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import es.lavanda.torrent.check.job.model.FilmModel;



@Repository
public interface FilmModelRepository extends PagingAndSortingRepository<FilmModel, String> {

    Optional<FilmModel> findByTitle(String title);

    List<FilmModel> findAllByTitle(String title);

    Page<FilmModel> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<FilmModel> findTop6ByOrderByCreatedAtDesc();

    Page<FilmModel> findAll(Pageable pageable);

    boolean existsByTorrentsTorrentUrl(String torrentUrl);

    List<FilmModel> findByIdOriginal(String idOriginal);

    Optional<FilmModel> findByTorrentsTorrentId(String torrentId);

    Optional<FilmModel> findByTorrentsTorrentUrl(String torrentUrl);

    List<FilmModel> findByTitleContainsIgnoreCaseOrderByCreatedAtDesc(String title);

}