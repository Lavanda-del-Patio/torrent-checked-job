package es.lavanda.torrent.check.job.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.lavanda.lib.common.model.FilmModelTorrent;
import es.lavanda.torrent.check.job.exception.TorrentCheckJobException;
import es.lavanda.torrent.check.job.model.FilmModel;
import es.lavanda.torrent.check.job.model.FilmModelHistory;
import es.lavanda.torrent.check.job.model.TorrentCheckedResponse;
import es.lavanda.torrent.check.job.repository.FilmModelHistoryRepository;
import es.lavanda.torrent.check.job.repository.FilmModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmsServiceImpl implements FilmsService {

    private final FilmModelRepository filmModelRepository;

    private final FilmModelHistoryRepository filmModelHistoryRepository;

    @Override
    public void checkedTorrent(TorrentCheckedResponse torrentChecked) {
        log.info("checkedTorrent {}", torrentChecked.getTorrent());
        Optional<FilmModel> optFilmModel = filmModelRepository.findByTorrentsTorrentUrl(torrentChecked.getTorrent());
        if (optFilmModel.isPresent()) {
            if (Boolean.FALSE.equals(optFilmModel.get().getTorrents().isEmpty())) {
                FilmModel filmModel = optFilmModel.get();
                FilmModelTorrent filmModelTorrent = filmModel.getTorrents().stream()
                        .filter(torrent -> torrentChecked.getTorrent().equals(torrent.getTorrentUrl())).findFirst()
                        .orElseThrow(() -> new TorrentCheckJobException(
                                "Not found torrent on database " + torrentChecked.getTorrent()));
                if (torrentChecked.isValidate()) {
                    log.info("Torrent checked and validate");
                    filmModelTorrent.setTorrentMagnet(torrentChecked.getMagnet());
                    filmModelTorrent.setTorrentValidate(true);
                    save(filmModel);
                } else {
                    log.info("Torrent to remove and validate");
                    filmModel.getTorrents().remove(filmModelTorrent);
                    if (filmModel.getTorrents().size() == 0) {
                        deleteFilmById(filmModel.getId());
                    } else {
                        save(filmModel);
                    }
                    filmModelHistoryRepository.save(new FilmModelHistory(torrentChecked.getTorrent()));
                }
            } else {
                filmModelRepository.delete(optFilmModel.get());
            }
        }
    }

    @Override
    public void deleteFilmById(String id) {
        filmModelRepository.deleteById(id);
    }

    private FilmModel save(FilmModel filmModel) {
        return filmModelRepository.save(filmModel);
    }

}