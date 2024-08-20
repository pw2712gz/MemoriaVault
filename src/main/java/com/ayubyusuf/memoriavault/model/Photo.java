package com.ayubyusuf.memoriavault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    public Photo(String image, String contentType, Date date) {
        this.fileName = image;
        this.fileType = contentType;
        this.uploadDate = date;
    }
}
