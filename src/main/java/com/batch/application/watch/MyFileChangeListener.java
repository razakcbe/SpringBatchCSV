package com.batch.application.watch;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFile.Type;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

@Component
public class MyFileChangeListener implements FileChangeListener {
	
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;
    
	@Override
	public void onChange(Set<ChangedFiles> changeSet) {
		for (ChangedFiles cfiles : changeSet) {
			for (ChangedFile cfile : cfiles.getFiles()) {
				if ((cfile.getType().equals(Type.MODIFY) || cfile.getType().equals(Type.ADD))
						&& !isLocked(cfile.getFile().toPath())) {
					//System.out.println("Done writing: " + cfile.getFile().getName());
					try {
						jobLauncher.run(job, new JobParametersBuilder()
						        .addDate("date", new Date())
						        .toJobParameters());
					} catch (JobExecutionAlreadyRunningException e) {
						e.printStackTrace();
					} catch (JobRestartException e) {
						e.printStackTrace();
					} catch (JobInstanceAlreadyCompleteException e) {
						e.printStackTrace();
					} catch (JobParametersInvalidException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private boolean isLocked(Path path) {
		try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
			return lock == null;
		} catch (IOException e) {
			return true;
		}
	}
}
