package com.nick.file.repositories;

import com.nick.file.po.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @version V1.0
 * @ClassName：FileRepositories
 * @author: hbj
 * @CreateDate：2018/8/27 19:34
 */
public interface FileRepositories extends JpaRepository<Attachment,String> {
}
