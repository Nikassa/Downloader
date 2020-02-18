package com.home;

import java.util.Date;

public class Links {
    private Integer id;
    private String name;
    private String fileLink;
    private String fileExtension;
    private Date lastDownload;
    private Integer iActive;


    public Links() {
    }

    public Links(Integer id, String name, String fileLink, String fileExtension, Date lastDownload, Integer iActive) {
        this.id = id;
        this.name = name;
        this.fileLink = fileLink;
        this.fileExtension = fileExtension;
        this.lastDownload = lastDownload;
        this.iActive = iActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Date getLastDownload() {
        return lastDownload;
    }

    public void setLastDownload(Date lastDownload) {
        this.lastDownload = lastDownload;
    }

    public Integer getiActive() {
        return iActive;
    }

    public void setiActive(Integer iActive) {
        this.iActive = iActive;
    }

    @Override
    public String toString() {
        return  id + ";" +
                name + ";" +
                fileLink + ";" +
                fileExtension + ";" +
                lastDownload + ";" +
                iActive
                ;
    }

    public String toWrite() {
        return
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileLink='" + fileLink + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", lastDownload=" + lastDownload +
                ", iActive=" + iActive
                ;
    }


}
