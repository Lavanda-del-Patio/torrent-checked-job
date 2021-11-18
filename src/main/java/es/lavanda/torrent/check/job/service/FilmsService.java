package es.lavanda.torrent.check.job.service;

import es.lavanda.torrent.check.job.model.TorrentCheckedResponse;

public interface FilmsService {

    void deleteFilmById(String id);

    void checkedTorrent(TorrentCheckedResponse torrentChecked);

}
