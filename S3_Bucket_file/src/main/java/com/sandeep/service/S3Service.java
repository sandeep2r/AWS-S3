package com.sandeep.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class S3Service {

	private final S3Client s3Client;
	private final String bucketName;

	public S3Service(@Value("${aws.accessKey}") String accessKey, @Value("${aws.secretKey}") String secretKey,
			@Value("${aws.s3.bucketName}") String bucketName) {
		this.s3Client = S3Client.builder().credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();
		this.bucketName = bucketName;
	}

	public List<String> searchFiles(String userName, String term) {
		ListObjectsRequest request = ListObjectsRequest.builder().bucket(bucketName).prefix(userName + "/").build();

		List<String> fileNames = s3Client.listObjects(request).contents().stream().map(S3Object::key)
				.filter(key -> key.endsWith(term)).collect(Collectors.toList());

		return fileNames;
	}

	public byte[] downloadFile(String userName, String fileName) throws IOException {
		GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(userName + "/" + fileName).build();

		ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(request);
		byte[] fileBytes = responseBytes.asByteArray();

		return fileBytes;
	}

//    public void uploadFile(String userName, MultipartFile file) throws IOException {
//        String fileName = file.getOriginalFilename();
//        byte[] fileBytes = file.getBytes();
//
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(userName + "/" + fileName)
//                .build();
//
//        s3Client.putObject(request, RequestBody.fromBytes(fileBytes));
//    }
	public void uploadFile(String userName, byte[] fileData, String fileName) {
	    PutObjectRequest request = PutObjectRequest.builder()
	            .bucket(bucketName)
	            .key(userName + "/" + fileName)
	            .build();

	    s3Client.putObject(request, RequestBody.fromBytes(fileData));
	}

}
