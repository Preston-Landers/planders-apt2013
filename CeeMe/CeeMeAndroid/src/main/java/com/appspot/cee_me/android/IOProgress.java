package com.appspot.cee_me.android;

/**
 * Used to update progress bars in input/output (network uploads/downloads).
 */
public interface IOProgress {
    /**
     * Indicate current file upload/download progress.
     *
     * @param progress         indicate progress between 1-100
     * @param bytesTransferred bytes transferred so far
     * @param totalSize        total bytes to transfer
     */
    abstract void setProgress(int progress, long bytesTransferred, long totalSize);

    /**
     * Called when the IO begins
     */
    abstract void started();

    /**
     * Called when the IO initialization phase completes and the main transfer begins
     */
    abstract void initiationCompleted();

    /**
     * Called when the transfer is complete.
     */
    abstract void completed();

    abstract void setCurrentRate(double bytesPerSec);
}
