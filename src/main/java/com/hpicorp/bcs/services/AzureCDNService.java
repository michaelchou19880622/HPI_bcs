package com.hpicorp.bcs.services;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.CloudBlockBlob;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;

@Service
public class AzureCDNService {

	@Value("${upload.file.directory}")
	private String path;
	
	@Value("${azure.key}")
	private String accountKey;

	public String uploadFile(MultipartFile file, String fileName) throws Exception {

		CloudStorageAccount account = CloudStorageAccount.parse(accountKey);

		CloudBlobClient blobClient = account.createCloudBlobClient();
		
		CloudBlobContainer container = blobClient.getContainerReference("booster5");

		CloudBlockBlob blob = container.getBlockBlobReference(fileName);

		blob.upload(file.getInputStream(), file.getBytes().length);
		
		return fileName;
		
	}
	
	public void downFile(String fileName) throws Exception {
		
		CloudStorageAccount account = CloudStorageAccount.parse(accountKey);

		CloudBlobClient blobClient = account.createCloudBlobClient();

		CloudBlobContainer container = blobClient.getContainerReference("booster5");

		File file = new File(path + "/" + fileName);

		CloudBlockBlob blob = container.getBlockBlobReference(fileName);

		blob.download(new FileOutputStream(file));
		
	}


}
