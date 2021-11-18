package es.lavanda.torrent.check.job.exception;

public class TorrentCheckJobException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TorrentCheckJobException(String message) {
        super(message);
    }

    public TorrentCheckJobException(String message, Exception e) {
        super(message, e);
    }
}