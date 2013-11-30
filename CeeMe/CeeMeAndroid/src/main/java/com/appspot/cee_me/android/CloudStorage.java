package com.appspot.cee_me.android;

import android.util.Log;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


/**
 * Simple wrapper around the Google Cloud Storage API
 * original version:
 * https://github.com/pliablematter/simple-cloud-storage
 */
public class CloudStorage {

    private Storage storage;

    private static final String TAG = Config.APPNAME + ".CloudStorage";
    private static final String PROJECT_ID = Config.GCM_SENDER_KEY;
    private static final String APPLICATION_NAME = Config.APPNAME + "/1.0";

    // private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    // private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static HttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private PrivateKey privateKey;

    public CloudStorage(InputStream keyStream) throws GeneralSecurityException, IOException {
        // HTTP_TRANSPORT  = com.appspot.cee_me.android.HttpTransport.getInstance().getHttpTransport();
        privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
                SecurityUtils.getPkcs12KeyStore(), keyStream, "notasecret", "privatekey", "notasecret");
        storage = getStorage();
    }

    /**
     * Uploads a file to a bucket. Filename and content type will be based on
     * the original file.
     *
     * @param bucketName  Bucket where file will be uploaded
     * @param filePath    Absolute path of the local file to upload
     * @param gcsFilename Filename to create within GCS
     * @param ioProgress  an object to report upload progress for the GUI
     * @throws IOException
     */
    public void uploadFile(String bucketName, String filePath, String mimeType, String gcsFilename, IOProgress ioProgress)
            throws IOException {

        StorageObject object = new StorageObject();
        object.setBucket(bucketName);

        File file = new File(filePath);
        Long fileSize = file.length();
        DateTime startTime = new DateTime();
        Log.d(TAG, "uploadFile START: " + bucketName + ":" + gcsFilename + " -> " + filePath + " time: " + startTime);

        try (InputStream stream = new FileInputStream(file)) {
            InputStreamContent content = new InputStreamContent(mimeType,
                    stream);

            Storage.Objects.Insert insert = storage.objects().insert(
                    bucketName, null, content);
            insert.setName(gcsFilename);
            insert.getMediaHttpUploader().setDisableGZipContent(true); // this seems to help to disable... at least when debugging
            // insert.getMediaHttpUploader().setDirectUploadEnabled(true);
            insert.getMediaHttpUploader().setChunkSize(MediaHttpUploader.DEFAULT_CHUNK_SIZE);
            if (ioProgress != null) {
                insert.getMediaHttpUploader().setProgressListener(new CloudUploadProgressListener(ioProgress, fileSize));
            }

            insert.execute();
            DateTime endTime = new DateTime();
            Period period = new Period(startTime, endTime);
            String periodStr = PeriodFormat.getDefault().print(period);
            Double bytesPerSecond = fileSize / (double) period.getSeconds();
            String logMsg = "uploadFile FINISH: " + bucketName + ":" + gcsFilename + " -> " + filePath + " total time: " + periodStr + " rate: " + bytesPerSecond;
            Log.d(TAG, logMsg);
        }
    }


    public void downloadFile(String bucketName, String gcsFilename, String destinationDirectory, IOProgress ioProgress) throws IOException {

        File directory = new File(destinationDirectory);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Provided destinationDirectory path is not a directory");
        }
        File file = new File(directory.getAbsolutePath() + "/" + gcsFilename);
        Log.d(TAG, "downloadFile START: " + bucketName + ":" + gcsFilename);

        Storage.Objects.Get get = storage.objects().get(bucketName, gcsFilename);
        StorageObject SO = get.execute();
        long fileSize = -1;
        if (SO != null) {
            fileSize = SO.getSize().longValue();
            Log.i(TAG, "found file size " + fileSize + "  for download: " + gcsFilename);
        }
        get.getMediaHttpDownloader().setProgressListener(new CloudDownloadProgressListener(ioProgress, fileSize));
        try (FileOutputStream stream = new FileOutputStream(file)) {
            // get.executeAndDownloadTo(stream);   /// what's the difference?
            get.executeMediaAndDownloadTo(stream); /// this is the one in docs
        }

    }

    /**
     * Deletes a file within a bucket
     *
     * @param bucketName Name of bucket that contains the file
     * @param fileName   The file to delete
     * @throws Exception
     */
    public void deleteFile(String bucketName, String fileName)
            throws Exception {

        storage.objects().delete(bucketName, fileName).execute();
    }

    /**
     * Creates a bucket
     *
     * @param bucketName Name of bucket to create
     * @throws Exception
     */
    public void createBucket(String bucketName) throws Exception {

        Bucket bucket = new Bucket();
        bucket.setName(bucketName);

        storage.buckets().insert(
                PROJECT_ID, bucket).execute();
    }

    /**
     * Deletes a bucket
     *
     * @param bucketName Name of bucket to delete
     * @throws Exception
     */
    public void deleteBucket(String bucketName) throws Exception {
        storage.buckets().delete(bucketName).execute();
    }

    /**
     * Lists the objects in a bucket
     *
     * @param bucketName bucket name to list
     * @return Array of object names
     * @throws Exception
     */
    public List<String> listBucket(String bucketName) throws IOException {

        List<String> list = new ArrayList<String>();

        List<StorageObject> objects = storage.objects().list(bucketName).execute().getItems();
        if (objects != null) {
            for (StorageObject o : objects) {
                list.add(o.getName());
            }
        }

        return list;
    }

    /**
     * List the buckets with the project
     * (Project is configured in properties)
     *
     * @return a list of all buckets
     * @throws Exception
     */
    public List<String> listBuckets() throws Exception {

        List<String> list = new ArrayList<String>();

        List<Bucket> buckets = storage.buckets().list(PROJECT_ID).execute().getItems();
        if (buckets != null) {
            for (Bucket b : buckets) {
                list.add(b.getName());
            }
        }

        return list;
    }

    private Storage getStorage() {

        if (storage == null) {
            HTTP_TRANSPORT = new ApacheHttpTransport();
            List<String> scopes = getStorageScopes();
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId(Config.SERVICE_ACCOUNT_EMAIL)
                    .setServiceAccountScopes(scopes)
                    .setServiceAccountPrivateKey(privateKey)
                    // .setServiceAccountPrivateKeyFromP12File(keyStream)
                    // .setServiceAccountUser("user@example.com")
                    .build();

            storage = new Storage.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }

        return storage;
    }

    /**
     * Return a list of the Google API scopes used by our storage operations.
     * Needed to procure the GoogleAccountCredential.
     * @return list of storage scopes
     */
    public static List<String> getStorageScopes() {
        List<String> scopes = new ArrayList<>();
        scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
        return scopes;
    }

    /**
     * Listen for and report upload progress
     */
    public static class CloudUploadProgressListener implements MediaHttpUploaderProgressListener {
        private IOProgress ioProgress;
        private long fileSize;

        public CloudUploadProgressListener(IOProgress ioProgress, long fileSize) {
            this.ioProgress = ioProgress;
            this.fileSize = fileSize;
        }

        public void progressChanged(MediaHttpUploader uploader) throws IOException {
            switch (uploader.getUploadState()) {
                case INITIATION_STARTED:
                    Log.i(TAG, "Initiation Started");
                    if (ioProgress != null) {
                        ioProgress.started();
                    }
                    break;
                case INITIATION_COMPLETE:
                    Log.i(TAG, "Initiation Completed");
                    if (ioProgress != null) {
                        ioProgress.initiationCompleted();
                    }
                    break;
                case MEDIA_IN_PROGRESS:
                    // API Javadoc comment says:
                    // Do not use if the specified AbstractInputStreamContent has no content length specified. Instead, consider using getNumBytesUploaded() to denote progress.
                    // are we handling that?
                    int currentProgress;
/*
                    try {
                        currentProgress = new Double(uploader.getProgress() * 100.0).intValue();
                    } catch (IllegalArgumentException e) {
                        currentProgress = -1;
                    }
*/
                    long bytesXfered = uploader.getNumBytesUploaded();
                    currentProgress = (int) ((double) bytesXfered  * 100.0 / (double) fileSize);
                    Log.i(TAG, "Upload percentage: " + currentProgress);
                    if (ioProgress != null) {
                        ioProgress.setProgress(currentProgress, bytesXfered, fileSize);
                    }
                    break;
                case MEDIA_COMPLETE:
                    Log.i(TAG, "Upload Completed!");
                    if (ioProgress != null) {
                        ioProgress.completed();
                    }
                    break;
            }
        }
    }

    /**
     * Listen for and report download progress
     */
    public static class CloudDownloadProgressListener implements MediaHttpDownloaderProgressListener {
        private IOProgress ioProgress;
        private long fileSize;

        public CloudDownloadProgressListener(IOProgress ioProgress, long fileSize) {
            this.ioProgress = ioProgress;
            this.fileSize = fileSize;
        }

        public void progressChanged(MediaHttpDownloader downloader) throws IOException {
            switch (downloader.getDownloadState()) {
                case MEDIA_IN_PROGRESS:
                    int currentProgress;
                    try {
                        currentProgress = new Double(downloader.getProgress() * 100.0).intValue();
                    } catch (IllegalArgumentException e) {
                        currentProgress = -1;
                    }
                    long bytesReceived = downloader.getNumBytesDownloaded();
                    if (currentProgress == -1) {
                        currentProgress = (int) ((double) fileSize * (double) 100 / (double) bytesReceived);
                    }
                    Log.i(TAG, "Download percentage: " + currentProgress);
                    if (ioProgress != null) {
                        ioProgress.setProgress(currentProgress, bytesReceived, fileSize);
                    }
                    break;
                case MEDIA_COMPLETE:
                    Log.i(TAG, "Download Completed!");
                    if (ioProgress != null) {
                        ioProgress.completed();
                    }
                    break;
            }
        }
    }

}
