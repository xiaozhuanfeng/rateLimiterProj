package com.example.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 自定义类，提高RandomAccessFile读写性能
 */
public class BufferedRandomAccessFile extends RandomAccessFile {
    /**
     * 代表BUF映射在当前文件的首偏移地址
     */
    private long bufstartpos;

    /**
     * 代表BUF映射在当前文件的尾偏移地址
     */
    private long bufendpos;

    /**
     * 指当前类文件指针的偏移地址
     */
    private long curpos;

    private boolean bufdirty;

    private byte[] buf;

    private long fileendpos;

    private int bufusedsize;

    private long bufbitlen;

    private long bufsize;

    public BufferedRandomAccessFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public BufferedRandomAccessFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
    }

    /**
     * 读取当前文件POS位置所在的字节
     *
     * @param pos
     * @return
     * @throws IOException
     */
    public byte read(long pos) throws IOException {
        if (pos < this.bufstartpos || pos > this.bufendpos) {
            this.flushbuf();
            this.seek(pos);
            if ((pos < this.bufstartpos) || (pos > this.bufendpos))
                throw new IOException();
        }
        this.curpos = pos;
        return this.buf[(int) (pos - this.bufstartpos)];
    }

    /**
     * 移动文件指针到pos位置，并把buf[]映射填充至POS所在的文件块
     *
     * @param pos
     * @throws IOException
     */
    public void seek(long pos) throws IOException {
        if ((pos < this.bufstartpos) || (pos > this.bufendpos)) { // seek pos not in buf
            this.flushbuf();
            if ((pos >= 0) && (pos <= this.fileendpos) && (this.fileendpos != 0)) {   // seek pos in file (file
                // length > 0)
                this.bufstartpos = pos * bufbitlen / bufbitlen;
                this.bufusedsize = this.fillbuf();
            } else if (((pos == 0) && (this.fileendpos == 0)) || (pos == this.fileendpos + 1)) {   // seek pos is
                // append pos
                this.bufstartpos = pos;
                this.bufusedsize = 0;
            }
            this.bufendpos = this.bufstartpos + this.bufsize - 1;
        }
        this.curpos = pos;
    }

    /**
     * bufdirty为真，把buf[]中尚未写入磁盘的数据，写入磁盘。
     *
     * @throws IOException
     */
    private void flushbuf() throws IOException {
        if (this.bufdirty == true) {
            if (super.getFilePointer() != this.bufstartpos) {
                super.seek(this.bufstartpos);
            }
            super.write(this.buf, 0, this.bufusedsize);
            this.bufdirty = false;
        }
    }

    /**
     * 根据bufstartpos，填充buf[]
     *
     * @return
     * @throws IOException
     */
    private int fillbuf() throws IOException {
        super.seek(this.bufstartpos);
        this.bufdirty = false;
        return super.read(this.buf);
    }

}
