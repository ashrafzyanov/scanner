package com.ashrafzyanov.scanner.catalog.file;

import java.io.File;

public class FileObject {
    private File file; // Ссылка на файл
    private String fileName = null; // Имя файла (без расширения)
    private String typeFile = null;; // Тип файла по расширению, если каталог то тип <DIR>
    private long fileSize = 0; // Размер файла в байтах, если это каталог то 0
    private long fileModif = 0; // Дата изменения в миллисекундах c 1970
    private String parrent = null; // Родитель
    private String absolutePath = null; // Абсолютный путь
    private boolean isDirectory = false; // Папка это или нет
    private boolean isFile = false; // Файл это или нет
    private boolean isArchiv = false; // Архив это или нет
    private boolean isHidden = false; // Скрытый файл или нет

    private void init(File file) {
        this.file = file;
        try {
            this.isDirectory = file.isDirectory();
        } catch (Exception e) {
            this.isDirectory = false;
        }

        try {
            if (!this.isDirectory) {
                this.isFile = file.isFile();
                try {
                    if (this.isFile)
                        this.fileSize = file.length();
                } catch (Exception e) {
                    this.fileSize = -1;
                }
            }
        } catch (Exception e) {
            this.isFile = true;
        }

        this.fileName = (this.isDirectory) ? file.getName() : ParseNameFile.getNameFile(file.getName());
        if (this.fileName.length() <= 0) {
            this.fileName = file.getPath().replace("\\", "/");
        }

        this.typeFile = (this.isDirectory) ? "<DIR>" : ParseNameFile.TypeFile(file.getName());
        this.isArchiv = (this.typeFile == null || this.isDirectory) ? false : ParseNameFile.TypeArchiv(this.typeFile);

        try {
            this.fileModif = (file.lastModified() == 0 ? -1 : file.lastModified());
        } catch (Exception e) {
            this.fileModif = -1;
        }

        this.parrent = file.getParent();

        try {
            this.absolutePath = file.getAbsolutePath();
        } catch (Exception e) {
            this.absolutePath = null;
        }

        try {
            this.isHidden = file.isHidden();
        } catch (Exception e) {
            this.isHidden = false;
        }
    }

    public FileObject(File file) {
        init(file);
    }

    public FileObject(String path) {
        File file = new File(path);
        init(file);
    }

    public FileObject[] getChildren() throws NullPointerException {
        File[] file = this.file.listFiles();
        FileObject[] fileObjChildren = new FileObject[file.length];
        FileObject fileOb;
        for (int i = 0; i < file.length; i++) {
            fileOb = new FileObject(file[i]);
            fileObjChildren[i] = fileOb;
        }
        return fileObjChildren;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.fileName;
    }

    public String getTypeFile() {
        return this.typeFile;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public long getDateModif() {
        return this.fileModif;
    }

    public String getParrent() {
        return this.parrent;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public boolean isFile() {
        return this.isFile;
    }

    public boolean isArchiv() {
        return this.isArchiv;
    }

    public boolean isHidden() {
        return this.isHidden;
    }
    
}
