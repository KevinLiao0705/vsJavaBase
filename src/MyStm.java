/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class MyStm {

    int inx;
    int spcChar_f;
    byte[] rdata = new byte[4096];
    byte[] rbuf = new byte[4096];
    byte[] tdata = new byte[4096];
    byte[] tbuf = new byte[4096];
    int rxin_f;
    int rbuf_byte;
    int tbuf_byte;
    int rxlen;
    int rxInx;
    int txlen;
    int rdataPt=0;
    int rdataNextPt=0;
    int tmpBasePt=0;
    int tmpAddPt=0;
    int tmpLen=0;

    public MyStm() {

    }
    
    void readStart(){
        rdataPt=0;
    }
    void setRdataPt(int pt){
        rdataPt=pt;
    }
    void setRdataNextPt(int pt){
        rdataNextPt=pt;
    }
    
    public int readTmpWord(){
        int ibuf=rdata[tmpBasePt+tmpAddPt]&255;
        tmpAddPt++;
        ibuf+=(rdata[tmpBasePt+tmpAddPt]&255)*256;
        tmpAddPt++;
        return ibuf;
    }
    public int readTmpByte(){
        int ibuf=rdata[tmpBasePt+tmpAddPt]&255;
        tmpAddPt++;
        return ibuf;
    }
    
    public int readWord(){
        int ibuf=rdata[rdataPt++]&255;
        ibuf+=(rdata[rdataPt++]&255)*256;
        return ibuf;
    }
    public int readInt(){
        int ibuf=rdata[rdataPt++]&255;
        ibuf+=(rdata[rdataPt++]&255)<<8;
        ibuf+=(rdata[rdataPt++]&255)<<16;
        ibuf+=(rdata[rdataPt++]&255)<<24;
        return ibuf;
    }
    
    
    
    
    public int readByte(){
        int ibuf=rdata[rdataPt++]&255;
        return ibuf;
    }

    void dec_mystm(byte indata) {
        rbuf_byte = 1;
        rbuf[0] = indata;
        dec_mystm();
    }

    void dec_mystm() {
        int i, j;
        int len;
        int chksum0, chksum1;
        for (i = 0; i < rbuf_byte; i++) {
            if (rbuf[i] == (byte) 0xEA) {
                inx = 0;
                spcChar_f = 0;
                continue;
            }
            if (rbuf[i] == (byte) 0xEC) {
                spcChar_f = 1;
                continue;
            }
            if (rbuf[i] != (byte) 0xEB) {
                if (inx < rdata.length) {
                    if (spcChar_f == 1) {
                        rdata[inx] = (byte) (rbuf[i] ^ 0xAB);
                    } else {
                        rdata[inx] = rbuf[i];
                    }
                    spcChar_f = 0;
                    inx++;
                    if (inx >= 500) {
                        spcChar_f = 0;
                    }
                }
                continue;
            }
            spcChar_f = 0;
            
            len=inx-2;
            chksum0 = 0xab;
            chksum1 = 0;
            for (j = 0; j < len; j++) {
                chksum0 ^= rdata[j];
                chksum1 += rdata[j];
            }
            if (((chksum0 ^ rdata[j + 0]) & 0xff) != 0) {
                continue;
            }
            if (((chksum1 ^ rdata[j + 1]) & 0xff) != 0) {
                continue;
            }
            rxlen = len;
            rxin_f = 1;
            //mstp->fptr();
        }
    }

    void encmst(byte uch, int enc) {
        if (enc != 0) {
            if (uch == (byte) 0xEA || uch == (byte) 0xEB || uch == (byte) 0xEC) {
                tdata[txlen++] = (byte) 0xEC;
                tdata[txlen++] = (byte) (uch ^ (byte) 0xAB);
                return;
            }
            tdata[txlen++] = uch;
            return;
        }
        tdata[txlen++] = uch;
    }

    void enc_mystm() {
        int i;
        int chksum0, chksum1;
        txlen = 0;
        encmst((byte) 0xEA, 0);
        chksum0 = 0xAB;
        chksum1 = 0;
        for (i = 0; i < tbuf_byte; i++) {
            encmst(tbuf[i], 1);
            chksum0 ^= tbuf[i];
            chksum1 += tbuf[i];
        }
        encmst((byte) (chksum0 & 255), 1);
        encmst((byte) (chksum1 & 255), 1);
        encmst((byte) 0xEB, 0);
    }
    
    void enc_myPack() {
        int i;
        int chksum0, chksum1;
        txlen = 0;
        encmst((byte) 0xEA, 0);
        chksum0 = 0xAB;
        chksum1 = 0;
        for (i = 0; i < tbuf_byte; i++) {
            encmst(tbuf[i], 1);
            chksum0 ^= tbuf[i];
            chksum1 += tbuf[i];
        }
        encmst((byte) (chksum0 & 255), 1);
        encmst((byte) (chksum1 & 255), 1);
        encmst((byte) 0xEB, 0);
    }
    
    

}
