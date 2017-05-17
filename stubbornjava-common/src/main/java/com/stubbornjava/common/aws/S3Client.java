package com.stubbornjava.common.aws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;


public class S3Client {
    private static final Logger log = LoggerFactory.getLogger(S3Client.class);

    private final AmazonS3 client;

    private S3Client(AmazonS3 client) {
        this.client = client;
    }

    public void putBytes(String bucket, String key, byte[] bytes) {
        ObjectMetadata md = new ObjectMetadata();
        md.setContentLength(bytes.length);
        PutObjectRequest request = new PutObjectRequest(bucket, key, new ByteArrayInputStream(bytes), md);
        client.putObject(request);
    }

    public void putString(String bucket, String key, String content) {
        putBytes(bucket, key, content.getBytes(Charset.forName(Charsets.UTF_8.name())));
    }

    public void putInputStream(String bucket, String key, InputStream inputStream) {
        // This can blow up on large files but that's ok for now.
        byte[] bytes = Unchecked.supplier(() -> IOUtils.toByteArray(inputStream)).get();
        putBytes(bucket, key, bytes);
    }

    public InputStream get(String bucket, String key) {
        GetObjectRequest request = new GetObjectRequest(bucket, key);
        S3Object response = client.getObject(request);
        return response.getObjectContent();
    }

    public String getString(String bucket, String key) {
        GetObjectRequest request = new GetObjectRequest(bucket, key);
        S3Object response = client.getObject(request);
        try (S3ObjectInputStream is = response.getObjectContent()) {
            return CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static S3Client fromAccessAndScret(String accessKey, String secretKey, Regions region) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return new S3Client(AmazonS3ClientBuilder.standard()
                                        .withRegion(region)
                                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                                        .build());
    }

    public static void main(String[] args) {
        S3Client client = S3Client.fromAccessAndScret("key", "secret", Regions.US_EAST_1);
        client.putString("quickdraw-logos", "test.txt", "test");
        client.putBytes("quickdraw-logos", "test2.txt", "test".getBytes(Charset.forName("utf8")));
        client.putInputStream("quickdraw-logos", "test3.txt", new ByteArrayInputStream("test".getBytes(Charset.forName("utf8"))));

        log.debug(client.getString("quickdraw-logos", "test.txt"));
    }
}
