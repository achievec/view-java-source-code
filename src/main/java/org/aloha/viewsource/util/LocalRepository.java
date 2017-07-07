package org.aloha.viewsource.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocalRepository {

	@Value("${local.mvn.repository.dir}")
	private String localRepository;

	public static void listLocalProject(String dir) throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(new File(dir).toPath(), "*.{jar}")) {
			for (Path entry : stream) {
				System.out.println(entry.getFileName());
			}
		} catch (DirectoryIteratorException ex) {
			throw ex.getCause();
		}
	}

	public static void main(String[] args) throws IOException {
		listLocalProject("/Users/aloha/.m2/repository");
	}

}
