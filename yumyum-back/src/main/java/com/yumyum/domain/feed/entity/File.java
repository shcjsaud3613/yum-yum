package com.yumyum.domain.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "orig_file_name", nullable = false)
    private String origFileName;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "extension_name", nullable = false)
    private String extensionName;
}
