package es.lavanda.torrent.check.job.model;

import lombok.Data;

@Data
public class TorrentCheckedResponse {

    private String torrent;

    private boolean validate;

    private String magnet;
}
