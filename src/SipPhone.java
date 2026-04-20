/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 *
 * twinkle codec switch(codec) { case CODEC_G711_ALAW: case CODEC_G711_ULAW:
 * case CODEC_GSM: case CODEC_SPEEX_NB: case CODEC_ILBC: case CODEC_G729A: case
 * CODEC_G726_16: case CODEC_G726_24: case CODEC_G726_32: case CODEC_G726_40:
 * case CODEC_TELEPHONE_EVENT: return 8000; case CODEC_G722: case
 * CODEC_SPEEX_WB: return 16000; case CODEC_SPEEX_UWB: return 32000;
 *
 */
public class SipPhone {

    static SipPhone scla;
    Object owner;
    String lcd1;
    String lcd2;
    String status;

    int nowVr;
    int preVrVol = 0xffff;
    int nowVrVol = 0;
    int nowVrVol_f = 1;
    int nowVrVolTime = 0;
    int printEerorFirst_f = 0;
    int softPhoneType = 0;

    String selfSipDispName = "";
    String selfSipNumber = "";
    String sipActName = "";
    int sipActTime;
    Ssh sshSound = null;
    int pttOnTime = 0;
    int[] lineCallIds = new int[]{0, 0};

    int status_f;
    int cmd_cnt = 0;
    int cmd_para0 = 0;
    int cmd_para1 = 0;
    int shutDown_cnt = 0;
    int byeDelayTime = 0;
    String referNo = "";
    String referName = "";
    int referTime = 0;
    int hangonWaitTime = 0;
    int f4WaitTime = 0;
    int soundCardInit = 0;
    int soundCardInitStep = 0;

    int line2ring_f = 0;
    String laterCall = "";
    int laterCall_tim = 0;

    int auto_register_tim = 0;
    int linphone_connect_tim = 0;
    int holdRelease_tim;
    int ict_connect_tim = 0;
    int ict_connected_f = 0;
    String ictCommandStr = "";
    int ictCommandTim = 0;
    int ictCheckPhno_f = 0;
    String ictCheckPhno_str = "";
    int dtmf_enable_f = 0;
    int wait_dtmf_f = 0;
    String dtmfStr = "";
    String ictPreData = "";

    long connected_tim = 0;

    int sipCommandTim = 0;
    String sipCommandStr = "";

    int set_local_ip_cnt = 255;
    int set_switch_ip_cnt = 255;

    byte[] ioBuf = new byte[16];
    //===============================
    //String connectId_str = "";
    //String connectNo_str = "";
    //String status_str = "";
    //String action_str = "";
    String keypad_str = "";
    String callToStr = "";
    int keypad_tim = 0;
    int keypad_on_f = 0;
    int auto_answer_tim = 0;

    int txSipInf_step = 0;
    int txSipInf_step_wait_f = 0;

    int txSipInf_step_a = 0;
    int txSipInf_step_b = 0;

    int menu_on_tim = 0;
    int menu_on_f = 0;

    String setId = "";
    String setting_str = "";
    int setting_tim = 0;
    int setting_on_f = 0;
    int mute_f = 0;
    public int pttEn_f = 0;
    public int amaMute_f = 0;

    byte[] sipflag = new byte[4];
    //int sipStatus = 0;         //0 no raspberryPi,1:raspberry pi ready,2:linphonec load,3:pbx registed,4:on call,5:ring,
    //int connected_cnt = 0;      //0:no connect 1:call to 2:call from 3:connected
    //int handStatusTime = 0;
    //int handStatus = 0;      //0:handon,1:earPhone on,2:spaeker on
    int handStatus_pre = 0;      //0:handon,1:earPhone on,2:spaeker on
    int shellCommandStatus = 0;      //0:ready,1:play dial tone
    String callto = "";
    String callfrom = "";
    //String callConnectNo = "";
    //String callConnectName = "";
    //String callfromId = "";
    //String callfromName = "";
    //==============================
    //int speakerVolume = 4;      //mix=0,max=45
    //int earphoneVolume = 4;     //mix=0,max=45
    //int micPhoneVolume = 4;     //mix=0,max=30
    int[] outVolumeTbl = {0, 2, 5, 7, 10, 12, 15, 18, 21, 25};
    int[] inVolumeTbl = {0, 2, 4, 6, 7, 8, 9, 10, 11, 12};   //ear mic
    int[] inVolumeTblMax = {0, 2, 4, 6, 7, 8, 9, 10, 11, 12};//speaker mic
    int shlFirstIn_f = 0;
    int ngrepFirstIn_f = 0;
    int ictFirstIn_f = 0;
    int twinkleCfgFirst_f = 0;
    int broadcast_tim = 0;
    int broadcast_f = 0;
    String[] prenoStrA = new String[10];
    int preno_inx = 0;
    int preno_cnt = 0;

    String dtmf;
    int status_tim;
    int sipphone_load_f = 0;
    //============================
    Ssh sshSip = null;
    SiprxTd siprxTd = null;
    SipconTd sipconTd = null;
    int siprxTd_run_f = 0;
    int siprxTd_destroy_f = 0;
    int sipconTd_run_f = 0;
    int sipconTd_destroy_f = 0;
    SipPhoneRx sipPhoneRx;
    //============================
    Ssh sshShl = null;
    ShlrxTd shlrxTd = null;
    ShlconTd shlconTd = null;
    int shlrxTd_run_f = 0;
    int shlrxTd_destroy_f = 0;
    int shlconTd_run_f = 0;
    int shlconTd_destroy_f = 0;
    ShellRx shellRx;
    //============================
    Ssh sshNgrep = null;
    NgreprxTd ngreprxTd = null;
    NgrepconTd ngrepconTd = null;
    int ngreprxTd_run_f = 0;
    int ngreprxTd_destroy_f = 0;
    int ngrepconTd_run_f = 0;
    int ngrepconTd_destroy_f = 0;
    NgrepRx ngrepRx;
    //============================
    Ssh sshIct = null;
    IctrxTd ictrxTd = null;
    IctconTd ictconTd = null;
    int ictrxTd_run_f = 0;
    int ictrxTd_destroy_f = 0;
    int ictconTd_run_f = 0;
    int ictconTd_destroy_f = 0;
    IctRx ictRx;
    //============================

    Timer tm1 = null;//for display
    Ssocket sskio;    //from nkv6in1_io
    Ssocket sskweb; //from web
    Ssocket sskui; //from web
    int sskui_tx_cnt;

    Vt100 vtsip;
    Vt100 vtshl;
    Vt100 vtngrep;
    Vt100 vtict;
    SipData sipData = new SipData();

    int piIoStatus0;
    int piIoStatus1;
    int piIoInFlag0;
    int piIoInFlag1;
    int piMcuStatus;
    int piMcuVrAdi;
    int roipCor_f = 0;
    int roipCorCnt = 0;
    int roipCorTim = 0;

    int sipPhoneDeviceId = 0x1947;
    int piIoDeviceId = 0x1946;
    int piMcuDeviceId = 0x1945;

    int piIoOut = 0;  //b0:ptt, 1=>on 0=>off
    int piIoSet0 = 0;
    int piIoSet1 = 0;
    int piIoSet2 = 0;

    TrxPack tpk0;

    SipPhone() {
        SipPhone.scla = this;
        int i;
        for (i = 0; i < 10; i++) {
            prenoStrA[i] = "";
        }
        tpk0 = new TrxPack(3, 0x10);
        preno_inx = 0;

    }

    public void transSipPhoneData(JSONObject outJson) {

        //public int phoneSta = 0;  //0 no raspberryPi,1:raspberry pi ready,2:linphonec load,3:pbx registed
        //public int[] lineFlagA = new int[]{0, 0};   //hold,mute,dtmf
        //public int[] lineStaA = new int[]{0, 0};     //0:ready, 1: ring out, 2:ring in, 3:connect, 4:hold 
        //public int[] handStaA = new int[]{0, 0};     //0:ready, 1: earphone, 2:epeaker 
        String ntpServerIp = GB.paraSetMap.get("ntpServerAddress").toString();
        String setVersion = GB.paraSetMap.get("version").toString();
        try {
            KvJson kj = new KvJson();
            kj.jStart();
            kj.jadd("realSipphoneIp", GB.realIpAddress);
            kj.jadd("realSipphoneMac", GB.macStr);
            kj.jadd("sipName", sipData.sipName);
            kj.jadd("sipNo", sipData.sipNo);
            kj.jadd("sipServerIp", sipData.sipServerIp);
            kj.jadd("ntpServerIp", ntpServerIp);
            kj.jadd("phoneSta", sipData.phoneSta);
            kj.jadd("version", GB.version + "-" + setVersion);
            //================
            int ibuf = 0;
            ibuf += sipData.reDirection_f;
            kj.jadd("phoneFlag", ibuf);
            //=================
            kj.jadd("nowLine", sipData.nowLine);
            kj.jadd("status", sipData.status);
            String actionStr = sipData.action;
            if (this.keypad_on_f == 1) {
                actionStr = this.keypad_str;
            }
            if (this.setting_on_f == 1) {
                actionStr = this.setting_str;
            }
            kj.jadd("action", actionStr);

            kj.jadd("lineFlagA", sipData.lineFlagA);
            kj.jadd("lineStaA", sipData.lineStaA);
            kj.jadd("handStaA", sipData.handStaA);

            kj.jEnd();
            JSONObject syncJson = new JSONObject(kj.jstr);
            outJson.put("sipphoneData", syncJson);

        } catch (Exception ex) {
            if (this.printEerorFirst_f == 0) {
                ex.printStackTrace();
            }
            this.printEerorFirst_f = 1;

        }
    }

    static public JSONObject wsCallBack(String userName, JSONObject mesJson, String actStr, JSONObject outJson) {
        try {
            String act = (String) mesJson.get("act");
            if (act.equals("tick")) {
                scla.transSipPhoneData(outJson);
                return outJson;
            }

            Object obj = null;
            try {
                obj = mesJson.get("paras");
            } catch (Exception ex) {

            }
            JSONArray paras = null;
            if (obj != null) {
                paras = (JSONArray) obj;
            }
            outJson.put("status", "ok");
            if (act.equals("phoneCommand")) {
                String phoneCommand = paras.get(0).toString();
                SipPhone.scla.phoneCommandIn(phoneCommand);
                return outJson;
            }
            if (act.equals("sipCommandDirect")) {
                String sipCommand = paras.get(0).toString();
                SipPhone.scla.sshWriteSip(sipCommand);
                return outJson;
            }

        } catch (Exception ex) {

        }
        return outJson;

    }

    public void create() {


        final SipPhone cla = this;
        softPhoneType = (int) GB.paraSetMap.get("softPhoneType");

        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        //ft.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeStr = ft.format(dNow);
        System.out.println("UTC" + timeStr);

        cla.sipData.status = "JSSIP電話 , 版本: 3.0";
        cla.sipData.action = "啟動中 ....";
        //=================================================
        vtsip = new Vt100(cla);
        vtsip.clr_telscr();
        vtsip.vtcmp = new Vtcmp() {
            @Override
            public void cmp() {
                if (cla.softPhoneType == 0) {
                    cla.vtcmpTwinkle();
                } else {
                    cla.vtcmpLinPhone();
                }
            }
        };
        //linphone ssh rx thread
        if (cla.siprxTd == null) {
            cla.siprxTd = new SiprxTd(cla);
            cla.siprxTd.start();
            cla.siprxTd_run_f = 1;
            cla.siprxTd_destroy_f = 0;
        }
        //connect linphone thread
        if (cla.sipconTd == null) {
            cla.sipconTd = new SipconTd(cla);
            cla.sipconTd.start();
            cla.sipconTd_run_f = 1;
            cla.sipconTd_destroy_f = 0;
        }
        //===================================================
        vtshl = new Vt100(cla);
        vtshl.clr_telscr();
        vtshl.vtcmp = new Vtcmp() {
            @Override
            public void cmp() {
                cla.vtcmpShl();
            }
        };
        //linphone ssh rx thread
        if (cla.shlrxTd == null) {
            cla.shlrxTd = new ShlrxTd(cla);
            cla.shlrxTd.start();
            cla.shlrxTd_run_f = 1;
            cla.shlrxTd_destroy_f = 0;
        }
        //connect linphone thread
        if (cla.shlconTd == null) {
            cla.shlconTd = new ShlconTd(cla);
            cla.shlconTd.start();
            cla.shlconTd_run_f = 1;
            cla.shlconTd_destroy_f = 0;
        }
        //===================================================
        if (GB.ngrepOn_f != 0) {
            vtngrep = new Vt100(cla);
            vtngrep.clr_telscr();
            vtngrep.vtcmp = new Vtcmp() {
                @Override
                public void cmp() {
                    cla.vtcmpNgrep();
                }
            };
            if (cla.ngreprxTd == null) {
                cla.ngreprxTd = new NgreprxTd(cla);
                cla.ngreprxTd.start();
                cla.ngreprxTd_run_f = 1;
                cla.ngreprxTd_destroy_f = 0;
            }
            if (cla.ngrepconTd == null) {
                cla.ngrepconTd = new NgrepconTd(cla);
                cla.ngrepconTd.start();
                cla.ngrepconTd_run_f = 1;
                cla.ngrepconTd_destroy_f = 0;
            }
        }
        //===================================================
        if (GB.pbxConOn_f != 0) {
            vtict = new Vt100(cla);
            vtict.clr_telscr();
            vtict.vtcmp = new Vtcmp() {
                @Override
                public void cmp() {
                    cla.vtcmpIct();
                }
            };
            if (cla.ictrxTd == null) {
                cla.ictrxTd = new IctrxTd(cla);
                cla.ictrxTd.start();
                cla.ictrxTd_run_f = 1;
                cla.ictrxTd_destroy_f = 0;
            }
            if (cla.ictconTd == null) {
                cla.ictconTd = new IctconTd(cla);
                cla.ictconTd.start();
                cla.ictconTd_run_f = 1;
                cla.ictconTd_destroy_f = 0;
            }
        }
        //===================================================

        //general timer
        if (cla.tm1 == null) {

            cla.tm1 = new Timer();
            //設定計時器
            //第一個參數為"欲執行的工作",會呼叫對應的run() method
            //第二個參數為程式啟動後,"延遲"指定的毫秒數後"第一次"執行該工作
            //第三個參數為每間隔多少毫秒執行該工作
            tm1.schedule(new SipPhoneTm1(cla), 1000, 20);
            //cla.tm1 = new Timer(20, new SipPhoneTm1(cla));
            //cla.tm1.start();
        }
        //for io(io,uart,i2c,spi...)
        //======================================
        sskio = new Ssocket();
        sskio.format = 1;
        sskio.rxcon_ltim = 100;//unit 10ms
        sskio.create(1234);
        sskio.sskRx = new SskRx() {
            @Override
            public void sskRx(int format) {
                cla.sskioRx(format);
            }
        };
        sskio.start();
        //for ui
        //======================================
        sskui = new Ssocket();
        sskui.format = 1;
        sskui.rxcon_ltim = 100;//unit 10ms
        sskui.create(1236);
        sskui.sskRx = new SskRx() {
            @Override
            public void sskRx(int format) {
                sskui.datain_f = 0;
                sskui.connect_f = 1;
                int okf = chkSipRx(sskui.inbuf, sskui.inbuf_len, 0);
                if (okf == 1) {
                    if (sskui.txMode == 0) {
                        sskui.stm.tbuf_byte = loadSipInfData(sskui.stm.tbuf, 0);
                        sskui.stm.enc_mystm();
                        sskui.tx_port = GB.sipToUiSocketPort;
                        sskui.tx_bytes = sskui.stm.tdata;
                        sskui.tx_len = sskui.stm.txlen;
                        sskui.txMode = 5; //return txip with len and port
                    }
                }
            }
        };
        sskui.start();
        int setPhoneVolume = (int) GB.paraSetMap.get("setPhoneVolume");

        nowVrVol = setPhoneVolume;

    }

    void vtcmpNgrep() {
        SipPhone cla = this;
        String str;

        //============================================
        if (ngrepFirstIn_f == 0) {
            if (cla.vtngrep.ncmp("@raspberrypi:~$")) {
                ngrepFirstIn_f = 1;
                String broadcastIp = GB.paraSetMap.get("broadcastIp").toString();
                String broadcastPort = GB.paraSetMap.get("broadcastPort").toString();
                str = "sudo ngrep -q \"" + broadcastIp + "\" port " + broadcastPort + "\n";
                cla.sshWriteNgrep(str);
                return;
            }
            return;
        } else {
            broadcast_tim = 0;
            broadcast_f = 1;
        }
    }

    void vtcmpIct() {
        SipPhone cla = this;

        //============================================
        if (ict_connected_f == 0) {
            //ictFirstIn_f = 1;
            if (cla.vtict.ncmp("@raspberrypi:~$")) {
                cla.ictCommandStr = "sudo telnet " + GB.alueIcsPassword + " 23" + "\n";
                cla.ictCommandTim = 0;
                return;
            }
            if (cla.vtict.ncmp("\nlogin:")) {
                cla.ictCommandStr = GB.alueIcsUserName + "\n";
                cla.ictCommandTim = 0;
                return;
            }
            if (cla.vtict.ncmp("\nPassword:")) {
                cla.ictCommandStr = GB.alueIcsPassword + "\n";
                cla.ictCommandTim = 0;
                return;
            }
            if (cla.vtict.ncmp("\nACD VERSION")) {
                cla.ictCommandStr = "ippstat" + "\n";
                cla.ictCommandTim = -100;
                return;
            }
            if (cla.vtict.ncmp("Enter your choice :")) {
                cla.ictCommandStr = "3" + "\n";
                cla.ictCommandTim = 0;
                cla.ictCheckPhno_f = 1;
                return;
            }
            if (cla.vtict.ncmp("return to menu : press ENTER")) {
                if (cla.ictCheckPhno_f == 1) {
                    String bstr = cla.ictPreData + cla.vtict.incha;
                    String[] strA = bstr.split("\n");
                    String strb;
                    GB.icsPhnos_amt = 0;
                    //System.out.println("\n******************** Ict Phone no *************************");
                    for (int k = 0; k < strA.length; k++) {
                        strb = strA[k].trim();
                        if (strA[k].contains("IPv4")) {
                            String[] strB = strb.split("\\s+");
                            for (int m = 0; m < strB.length; m++) {
                                if (strB[m].equals("IPv4")) {
                                    if (!strB[m + 1].equals("|Unused")) {
                                        String phNo = "";
                                        for (int n = 1; n < strB[0].length(); n++) {
                                            phNo += strB[0].charAt(n);
                                        }
                                        GB.icsPhnos[GB.icsPhnos_amt++] = phNo;
                                    }
                                    break;
                                }
                            }
                            k += 0;
                        }
                    }
                    //System.out.println("*********************************************");
                }
                cla.ictCheckPhno_f = 0;
                cla.ictCommandStr = "\n";
                cla.ictCommandTim = -3000;
                return;
            }

        } else {
        }
    }

    void vtcmpShl() {
        SipPhone cla = this;
        String str;
        //============================================

        int earPhoneVolume = (int) GB.paraSetMap.get("earPhoneVolume");
        int setPhoneVolume = (int) GB.paraSetMap.get("setPhoneVolume");
        if (cla.vtshl.cmp("@raspberrypi:~$")) {
            if (shlFirstIn_f == 0) {
                shlFirstIn_f = 1;
                int phoneType = (int) GB.paraSetMap.get("phoneType");
                if (phoneType == 1) {//roip
                    str = "sudo amixer cset numid=6 " + outVolumeTbl[setPhoneVolume] + "," + outVolumeTbl[setPhoneVolume] + "\n";
                    cla.sshWriteShl(str);
                    str = "sudo amixer cset numid=8 " + inVolumeTblMax[GB.phsetMicSens] + "," + inVolumeTblMax[GB.phsetMicSens] + "\n";
                    cla.sshWriteShl(str);
                } else {
                    str = "sudo amixer cset numid=4 " + outVolumeTbl[setPhoneVolume] + "," + outVolumeTbl[earPhoneVolume] + "\n";
                    cla.sshWriteShl(str);
                    str = "sudo amixer cset numid=6 " + inVolumeTblMax[GB.phsetMicSens] + "," + inVolumeTbl[GB.earMicSens] + "\n";
                    cla.sshWriteShl(str);
                }
                ioBuf[0] &= 0xfc;

            }
            /*
            if (shellCommandStatus == 1) {
                shellCommandStatus = 0;
                if (cla.sipData.phoneSta <= 3) {
                    handStatus = 0;
                }
            }
             */
            return;
        }
        if (cla.vtshl.cmp("Playing WAVE")) {
            shellCommandStatus = 1;
            return;
        }
    }

    int setTwincleCfg() {

        String fname;
        String bstr;
        fname = GB.twinkleCfgPath;

        String sipName = GB.paraSetMap.get("sipName").toString();
        String sipNo = GB.paraSetMap.get("sipNumber").toString();
        String sipServerIp = GB.paraSetMap.get("sipServerAddress").toString();
        String sipServerPin = GB.paraSetMap.get("sipServerPassword").toString();

        this.sipData.sipName = sipName;
        this.sipData.sipNo = sipNo;
        this.sipData.sipServerIp = sipServerIp;

        System.out.println("setTwincleCfg");
        System.out.println("===============================");
        System.out.println(sipNo);
        System.out.println(sipServerIp);
        System.out.println(sipName);
        System.out.println("===============================");

        try {
            FileWriter fw = new FileWriter(fname);
            //=====================
            bstr = "# USER";
            fw.write(bstr + "\n");
            //=====================
            bstr = "user_name=" + sipNo;
            fw.write(bstr + "\n");
            //=====================
            bstr = "user_domain=" + sipServerIp;
            fw.write(bstr + "\n");
            //=====================
            bstr = "user_display=" + sipName;
            fw.write(bstr + "\n");
            //=====================
            bstr = "user_organization=" + "";
            fw.write(bstr + "\n");
            //=====================
            bstr = "auth_realm=" + "";
            fw.write(bstr + "\n");
            //=====================
            bstr = "auth_name=" + sipNo;
            fw.write(bstr + "\n");
            //=====================
            bstr = "auth_pass=" + sipServerPin;
            fw.write(bstr + "\n");
            //=====================
            bstr = "# SIP SERVER";
            fw.write(bstr + "\n");
            //=====================
            bstr = "outbound_proxy=" + sipServerIp;
            fw.write(bstr + "\n");
            //=====================
            bstr = "all_requests_to_proxy=" + "no";
            fw.write(bstr + "\n");
            //=====================
            bstr = "registrar=" + sipServerIp;
            fw.write(bstr + "\n");
            //=====================
            bstr = "register_at_startup=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "registration_time=" + "3600";
            fw.write(bstr + "\n");
            //=====================
            bstr = "# RTP AUDIO";
            fw.write(bstr + "\n");
            //=====================
            /*
            "g711a"
            "g711u"
            "gsm"
            "speex-nb"
            "speex-wb"
            "speex-uwb"
            "ilbc"
            "g722"
            "g726-16"
            "g726-24"
            "g726-32"
            "g726-40"
            "g729a"
             */
            //bstr = "codecs=" + "g711a,g711u,gsm";
            bstr = "codecs=" + "speex-uwb";
            fw.write(bstr + "\n");
            //=====================
            fw.write("out_far_end_codec_pref=yes" + "\n");
            fw.write("speex_nb_payload_type=97" + "\n");
            fw.write("speex_wb_payload_type=98" + "\n");
            fw.write("speex_uwb_payload_type=99" + "\n");
            fw.write("speex_bit_rate_type=cbr" + "\n");
            fw.write("speex_dtx=no" + "\n");
            fw.write("speex_penh=yes" + "\n");
            fw.write("speex_quality=6" + "\n");
            fw.write("speex_complexity=3" + "\n");
            fw.write("speex_dsp_vad=no" + "\n");
            fw.write("speex_dsp_agc=no" + "\n");
            fw.write("speex_dsp_aec=no" + "\n");
            fw.write("speex_dsp_nrd=no" + "\n");
            fw.write("speex_dsp_agc_level=20" + "\n");
            fw.write("ilbc_payload_type=96" + "\n");
            //==========================
            bstr = "ptime=" + "20";
            fw.write(bstr + "\n");
            //=====================
            bstr = "dtmf_payload_type=" + "101";
            fw.write(bstr + "\n");
            //=====================
            bstr = "dtmf_duration=" + "100";
            fw.write(bstr + "\n");
            //=====================
            bstr = "dtmf_pause=" + "40";
            fw.write(bstr + "\n");
            //=====================
            bstr = "dtmf_volume=" + "10";
            fw.write(bstr + "\n");
            //=====================
            bstr = "# SIP PROTOCOL";
            fw.write(bstr + "\n");
            //=====================
            bstr = "hold_variant=" + "rfc3264";
            fw.write(bstr + "\n");
            //=====================
            bstr = "check_max_forwards=" + "no";
            fw.write(bstr + "\n");
            //=====================
            bstr = "allow_missing_contact_reg=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "registration_time_in_contact=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "compact_headers=" + "no";
            fw.write(bstr + "\n");
            //=====================
            bstr = "use_domain_in_contact=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "allow_redirection=" + "no";
            fw.write(bstr + "\n");
            //=====================
            bstr = "ask_user_to_redirect=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "max_redirections=" + "5";
            fw.write(bstr + "\n");
            //=====================
            bstr = "ext_100rel=" + "supported";
            fw.write(bstr + "\n");
            //=====================
            bstr = "referee_hold=" + "no";
            fw.write(bstr + "\n");
            //=====================
            bstr = "referrer_hold=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "allow_refer=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "ask_user_to_refer=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "auto_refresh_refer_sub=" + "no";
            fw.write(bstr + "\n");
            //=====================
            bstr = "# NAT";
            fw.write(bstr + "\n");
            //=====================
            bstr = "nat_public_ip=" + "";
            fw.write(bstr + "\n");
            //=====================
            bstr = "#stun_server=**sip.foo.bar**:10000";
            fw.write(bstr + "\n");
            //=====================
            bstr = "# TIMERS";
            fw.write(bstr + "\n");
            //=====================
            bstr = "timer_noanswer=" + "30";
            fw.write(bstr + "\n");
            //=====================
            bstr = "timer_nat_keepalive=" + "30";
            fw.write(bstr + "\n");
            //=====================
            bstr = "# ADDRESS FORMAT";
            fw.write(bstr + "\n");
            //=====================
            bstr = "display_useronly_phone=" + "yes";
            fw.write(bstr + "\n");
            //=====================
            bstr = "numerical_user_is_phone=" + "no";
            fw.write(bstr + "\n");
            //=====================
            bstr = "# RING TONES";
            fw.write(bstr + "\n");
            //=====================
            bstr = "ringtone_file=" + "";
            fw.write(bstr + "\n");
            //=====================
            bstr = "ringback_file=" + "";
            fw.write(bstr + "\n");
            //=====================
            bstr = "# SCRIPTS";
            fw.write(bstr + "\n");
            //=====================
            bstr = "script_incoming_call=" + "";
            fw.write(bstr + "\n");
            //=====================
            //bstr="="+"";
            //fw.write(bstr+"\n");
            //=====================

            fw.flush();
            fw.close();
            return 1;
        } catch (FileNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return 0;

    }
//%1

    void vtcmpLinPhone() {
        SipPhone cla = this;
        String str;
        String[] strA;
        String tmpStr;
        String[] tmpStrA;
        int test = 1;

        int inLine;

        if (cla.vtsip.ncmp("@raspberrypi:~$")) {
            if (GB.syssec_f == 1) {
                cla.sipAct("loadSip", null);//<<debug
            }
            cla.sipData.ready();
            cla.sipData.phoneSta = 1;
            return;
        }
        if (cla.sipData.phoneSta == 1) {
            if (cla.vtsip.ncmp("\nlinphonec>")) {
                if (GB.lang == 0) {
                    cla.sipData.status = "In Registing PBX ....";
                    cla.sipData.action = "Registing PBX";
                }
                if (GB.lang == 1) {
                    cla.sipData.status = "電話註冊中請稍後 ....";
                    cla.sipData.action = "註冊電話";
                }
                cla.sipData.ready();
                cla.sipData.phoneSta = 2;
                cla.registSip();
                return;
            }

            /*
            linphonec> register sip:131@
            tmpStr = cla.vtsip.ncmpStar("%", linphonec> register sip:131@, 80);
            if (tmpStr != null) {
                strA = tmpStr.split("%");
                if (strA.length == 3) {
                    cla.selfSipDispName = strA[0];
                    cla.selfSipNumber = strA[1];
                    return;
                }
            }
             */
        }
        if (cla.sipData.phoneSta == 2) {
            tmpStr = cla.vtsip.ncmpStar("%", "linphonec> register sip:%@", 80);
            if (tmpStr != null) {
                strA = tmpStr.split("%");
                if (strA.length == 1) {
                    cla.selfSipDispName = strA[0];
                    cla.selfSipNumber = strA[0];

                    if (GB.lang == 0) {
                        cla.sipData.status = "Registration Succeeded";
                        cla.sipData.action = "";
                    }
                    if (GB.lang == 1) {
                        cla.sipData.status = "電話註冊成功";
                        cla.sipData.action = "";
                    }
                    cla.sipData.ready();//phoneSta=3;
                    cla.sipData.phoneSta = 3;
                    cla.status_tim = 50;
                    return;
                }
            }
            return;
        }

        cla.sipData.lineMessageA[0] = "";
        cla.sipData.lineMessageA[1] = "";
        for (;;) {
            if (cla.sipData.phoneSta >= 3) {

                if (cla.vtsip.ncmpA("Redirection enabled")) {
                    cla.sipData.reDirection_f = 1;
                    break;
                }

                if (cla.vtsip.ncmpA("All redirections disabled")) {
                    cla.sipData.reDirection_f = 0;
                    break;
                }

                if (cla.vtsip.ncmpA("Line 1 is now active.")) {
                    cla.sipData.nowLine = 0;
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "changeLine";
                    break;
                }
                if (cla.vtsip.ncmpA("Line 2 is now active.")) {
                    cla.sipData.nowLine = 1;
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "changeLine";
                    break;
                }
                if (cla.vtsip.ncmpA("Line 1 is already active.")) {
                    cla.sipData.nowLine = 0;
                    break;
                }
                if (cla.vtsip.ncmpA("Line 2 is already active.")) {
                    cla.sipData.nowLine = 1;
                    break;
                }
                if (cla.sipActName.equals("mute")) {
                    if (cla.vtsip.ncmpA("\nmute\n")) {
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "mute";
                        cla.vtsip.clrCmpbuf();
                        break;
                    }
                    if (cla.vtsip.ncmpA("unmute")) {
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "unmute";
                        cla.vtsip.clrCmpbuf();
                        break;
                    }
                }
                //=================================================
                if (cla.sipActName.equals("hold")) {
                    tmpStr = cla.vtsip.ncmpStar("%", "Pausing call % with sip:%@%.", 80);
                    if (tmpStr != null) {
                        tmpStrA = tmpStr.split("%");
                        cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                        inLine = cla.sipData.nowLine;
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "hold";
                        cla.vtsip.clrCmpbuf();
                        break;
                    }

                    tmpStr = cla.vtsip.ncmpStar("%", "Resuming call % with sip:%@%.", 80);
                    if (tmpStr != null) {
                        tmpStrA = tmpStr.split("%");
                        cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                        inLine = cla.sipData.nowLine;
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "unhold";
                        cla.vtsip.clrCmpbuf();
                        break;
                    }

                }

                /*
                if (cla.vtsip.ncmpA("hold\n" + "Twinkle> \n" + "Line *: re-INVITE successful.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "hold";
                    break;
                }
                if (cla.vtsip.ncmpA("hold\n" + "Twinkle> ")) {
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "hold";
                    break;
                }
                if (cla.vtsip.ncmpA("retrieve\n" + "Twinkle> \n" + "Line *: re-INVITE successful.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "unhold";
                    break;
                }
                if (cla.vtsip.ncmpA("retrieve\n" + "Twinkle> ")) {
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "unhold";
                    break;
                }
                 */
                //============= call out process ================
                tmpStr = cla.vtsip.ncmpStar("%", "linphonec> Call % to sip:%@% ringing.", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "callOut~" + tmpStrA[1];
                    cla.vtsip.clrCmpbuf();
                    break;
                }

                tmpStr = cla.vtsip.ncmpStar("%", "Call % with sip:%@% error.", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "callFailFarEndBusy";
                    cla.vtsip.clrCmpbuf();
                    break;
                }
                tmpStr = cla.vtsip.ncmpStar("%", "Call % with sip:%@% ended (Call declined).", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "callFailFarEndNoAnswer";
                    cla.vtsip.clrCmpbuf();
                    break;
                }

                tmpStr = cla.vtsip.ncmpStar("%", "Call % with sip:%@% connected.", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "farEndAnswerCall~" + tmpStrA[1];
                    cla.vtsip.clrCmpbuf();
                    break;
                }
                tmpStr = cla.vtsip.ncmpStar("%", "Call % with % sip:%@% connected.", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "farEndAnswerCall~" + tmpStrA[2];
                    cla.vtsip.clrCmpbuf();
                    break;
                }

                tmpStr = cla.vtsip.ncmpStar("%", "Call % with sip:%@% ended (Unknown error).", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "farEndEndCall";
                    cla.vtsip.clrCmpbuf();
                    break;
                }

                tmpStr = cla.vtsip.ncmpStar("%", "Call % with \"%\" <sip:%@%> ended (Unknown error).", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "farEndEndCall";
                    cla.vtsip.clrCmpbuf();
                    break;
                }

                tmpStr = cla.vtsip.ncmpStar("%", "incoming call from \"%\" <sip:%@%>, assigned id %\n", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[3], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "incomeCall~" + tmpStrA[0] + "~" + tmpStrA[1];
                    cla.vtsip.clrCmpbuf();
                    break;
                }
                tmpStr = cla.vtsip.ncmpStar("%", "Call % with sip:%@% ended (No error).", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    cla.lineCallIds[cla.sipData.nowLine] = Lib.str2int(tmpStrA[0], 0);
                    inLine = cla.sipData.nowLine;
                    cla.sipData.lineMessageA[inLine] = "selfEndCall";
                    cla.vtsip.clrCmpbuf();
                    break;
                }

                if (cla.vtsip.ncmpA("Receiving tone 1 from")) {
                    int phoneType = (int) GB.paraSetMap.get("phoneType");
                    if (phoneType == 1) {//roip
                        cla.pttOnPrg();
                    }
                    break;
                }
                if (cla.vtsip.ncmpA("Receiving tone 2 from")) {
                    int phoneType = (int) GB.paraSetMap.get("phoneType");
                    if (phoneType == 1) {//roip
                        cla.pttOffPrg();
                    }
                    break;
                }

                //===============================================================================================
                if (test == 1) {
                    break;
                }

                if (cla.vtsip.ncmpA("Line *: call failed.\n" + "486 Busy Here")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "callFailFarEndBusy";
                    break;
                }

                if (cla.vtsip.ncmpA("Line *: call failed.\n" + "404 Not Found")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "callFail";
                    break;
                }
                //if (cla.vtsip.ncmpA("Line *: call ended.\n" + "Twinkle>")) {
                if (cla.vtsip.ncmpA("Line *: call ended.\n")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "selfEndCall";
                    break;
                }

                tmpStr = cla.vtsip.ncmpStar("%", "Line %: far end answered call.\n" + "200 OK\n" + "To: sip:%@", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    inLine = Lib.str2int(tmpStrA[0], 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "farEndAnswerCall~" + tmpStrA[1];
                    break;
                }
                //============= callin process ================
                tmpStr = cla.vtsip.ncmpStar("%", "Line %: incoming call\nFrom:% <sip:%@%>\nTo:", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    inLine = Lib.str2int(tmpStrA[0], 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "incomeCall~" + tmpStrA[1] + "~" + tmpStrA[2];
                }
                if (cla.vtsip.ncmpA("Line *: far end cancelled call.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "farEndCancelCall";
                    break;
                }
                if (cla.vtsip.ncmpA("Line *: call rejected.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "rejectRingIn";
                    break;
                }

                if (cla.vtsip.ncmpA("DTMF detected: 1")) {
                    int phoneType = (int) GB.paraSetMap.get("phoneType");
                    if (phoneType == 1) {//roip
                        cla.pttOnPrg();
                    }
                    break;
                }
                if (cla.vtsip.ncmpA("DTMF detected: 2")) {
                    int phoneType = (int) GB.paraSetMap.get("phoneType");
                    if (phoneType == 1) {//roip
                        cla.pttOffPrg();
                    }
                    break;
                }

                //same as above
                /*
                if (cla.vtsip.ncmpA("bye\n" +"Twinkle>")) {
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "selfByeCall";
                    break;
                }
                 */
                if (cla.vtsip.ncmpA("Line *: call established.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "answerConnectCall";
                    break;
                }
                //============= connect process ================
                if (cla.vtsip.ncmpA("Line *: far end ended call.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "farEndEndCall";
                    break;
                }
                //same in ringout
                /*
                if (cla.vtsip.ncmpA("Line *: call ended.\n" +"Twinkle>")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "selfEndCall";
                    break;
                }
                 */
            }
            break;
        }
        if (cla.sipData.lineMessageA[0].length() == 0 && cla.sipData.lineMessageA[1].length() == 0) {
            return;
        }
        int actLine;
        String actStr;
        if (cla.sipData.lineMessageA[1].length() != 0) {
            actLine = 1;
            actStr = cla.sipData.lineMessageA[1];
        } else {
            actLine = 0;
            actStr = cla.sipData.lineMessageA[0];
        }
        str = "\n====== Act Line ";
        str += (actLine + 1) + " : " + actStr;
        str += " ====== \n";
        System.out.println(str);
        strA = actStr.split("~");
        switch (strA[0]) {
            case "changeLine":
                cla.txShellEsc();
                cla.sipData.lineFlagA[0] &= 0x02;
                cla.sipData.lineFlagA[1] &= 0x02;
                break;
            case "hold":
                cla.sipData.lineFlagA[actLine] |= 1;
                break;
            case "unhold":
                cla.sipData.lineFlagA[actLine] &= 0xfe;
                cla.sipActName = "";
                break;
            case "mute":
                cla.sipData.lineFlagA[actLine] |= 2;
                break;
            case "unmute":
                cla.sipData.lineFlagA[actLine] &= 0xfd;
                cla.sipActName = "";
                break;
            //=== call out 
            case "callOut":
                cla.sipData.action = "撥打 " + strA[1];
                cla.sipData.lineStaA[actLine] = 1;
                cla.sipData.lineNameA[actLine] = strA[1];
                cla.sipData.lineNoA[actLine] = strA[1];
                cla.status_tim = 99999;
                break;
            case "ringOut":
                cla.sipData.status = "對方響鈴中....";
                cla.status_tim = 99999;
                break;
            case "callFailNoThisCall":
                cla.sipData.status = "電話號碼錯誤";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "callFailFarEndNoAnswer":
                cla.sipData.status = "對方逾時無接聽";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "callFailFarEndBusy":
                cla.sipData.status = "對方忙線中";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "callFail":
                cla.sipData.status = "呼叫失敗";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "selfEndCall":
                cla.sipData.status = "通話已中斷";
                cla.sipData.action = "取消通話";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "farEndAnswerCall":
                cla.sipData.status = "對方已接聽";
                cla.sipData.lineStaA[actLine] = 3;
                cla.status_tim = 50;
                Date dNow = new Date();
                cla.sipData.lineConnectTimeA[actLine] = dNow.getTime();
                break;
            //===income call 
            case "incomeCall":
                cla.sipData.status = "電話撥入 " + strA[1] + " <" + strA[2] + ">";
                cla.sipData.action = "請接電話 !";
                cla.sipData.lineStaA[actLine] = 2;
                cla.sipData.lineNameA[actLine] = strA[1];
                cla.sipData.lineNoA[actLine] = strA[2];
                cla.status_tim = 9999;
                if (strA[2].contains("*0*")) {
                    cla.phoneCommandIn("speakerAct");
                }
                cla.txShellEsc();
                break;
            case "farEndCancelCall":
                cla.sipData.status = "對方已掛斷";
                cla.sipData.action = cla.sipData.lineNoA[actLine] + " 取消通話";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "rejectRingIn":
                cla.sipData.action = "拒絕通話";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "selfByeCall":
                cla.sipData.action = "取消通話";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "answerConnectCall":
                cla.sipData.action = "通話已建立";
                cla.sipData.lineStaA[actLine] = 3;
                cla.status_tim = 50;
                dNow = new Date();
                cla.sipData.lineConnectTimeA[actLine] = dNow.getTime();

                break;
            //===connected 
            case "farEndEndCall":
                cla.sipData.status = "對方已掛斷";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 100;
                break;
            /*        
            case "selfEndCall":
                    break;
             */

        }

    }

    void vtcmpTwinkle() {
        SipPhone cla = this;
        String str;
        String[] strA;
        String tmpStr;
        String[] tmpStrA;

        int inLine;
        //============================================
        if (cla.vtsip.ncmp("@raspberrypi:~$")) {
            if (GB.syssec_f == 1) {
                if (twinkleCfgFirst_f == 0) {
                    cla.setTwincleCfg();
                    twinkleCfgFirst_f = 1;
                }
                cla.sipAct("loadSip", null);//<<debug
            }
            cla.sipData.ready();
            cla.sipData.phoneSta = 1;
            return;
        }
        if (cla.sipData.phoneSta == 1) {
            tmpStr = cla.vtsip.ncmpStar("%", " % <sip:%@%>", 80);
            if (tmpStr != null) {
                strA = tmpStr.split("%");
                if (strA.length == 3) {
                    cla.selfSipDispName = strA[0];
                    cla.selfSipNumber = strA[1];
                    return;
                }
            }

            if (cla.vtsip.ncmp("Twinkle>")) {
                if (GB.lang == 0) {
                    cla.sipData.status = "In Registing PBX ....";
                    cla.sipData.action = "Registing PBX";
                }
                if (GB.lang == 1) {
                    cla.sipData.status = "電話註冊中請稍後 ....";
                    cla.sipData.action = "註冊電話";
                }
                cla.sipData.ready();
                cla.sipData.phoneSta = 2;
            }
        }

        if (cla.sipData.phoneSta <= 2) {
            if (cla.vtsip.ncmpA(" registration succeeded")) {
                if (GB.lang == 0) {
                    cla.sipData.status = "Registration Succeeded";
                    cla.sipData.action = "";
                }
                if (GB.lang == 1) {
                    cla.sipData.status = "電話註冊成功";
                    cla.sipData.action = "";
                }
                cla.sipData.ready();//phoneSta=3;
                cla.sipData.phoneSta = 3;
                cla.status_tim = 50;
            }
            return;
        }

        cla.sipData.lineMessageA[0] = "";
        cla.sipData.lineMessageA[1] = "";
        for (;;) {
            if (cla.sipData.phoneSta >= 3) {

                if (cla.vtsip.ncmpA("Redirection enabled")) {
                    cla.sipData.reDirection_f = 1;
                    break;
                }

                if (cla.vtsip.ncmpA("All redirections disabled")) {
                    cla.sipData.reDirection_f = 0;
                    break;
                }

                if (cla.vtsip.ncmpA("Line 1 is now active.")) {
                    cla.sipData.nowLine = 0;
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "changeLine";
                    break;
                }
                if (cla.vtsip.ncmpA("Line 2 is now active.")) {
                    cla.sipData.nowLine = 1;
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "changeLine";
                    break;
                }
                if (cla.vtsip.ncmpA("Line 1 is already active.")) {
                    cla.sipData.nowLine = 0;
                    break;
                }
                if (cla.vtsip.ncmpA("Line 2 is already active.")) {
                    cla.sipData.nowLine = 1;
                    break;
                }
                if (cla.sipActName.equals("mute")) {
                    if (cla.vtsip.ncmpA("Line muted.")) {
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "mute";
                        break;
                    }
                    if (cla.vtsip.ncmpA("Line unmuted.")) {
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "unmute";
                        break;
                    }
                }
                if (cla.vtsip.ncmpA("bye\n" + "Twinkle>")) {
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "selfByeCall";
                    break;
                }
                //=================================================
                if (cla.sipActName.equals("hold")) {
                    if (cla.vtsip.ncmpA("hold")) {
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "hold";
                        break;
                    }
                    if (cla.vtsip.ncmpA("retrieve")) {
                        cla.sipData.lineMessageA[cla.sipData.nowLine] = "unhold";
                        break;
                    }

                }

                /*
                if (cla.vtsip.ncmpA("hold\n" + "Twinkle> \n" + "Line *: re-INVITE successful.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "hold";
                    break;
                }
                if (cla.vtsip.ncmpA("hold\n" + "Twinkle> ")) {
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "hold";
                    break;
                }
                if (cla.vtsip.ncmpA("retrieve\n" + "Twinkle> \n" + "Line *: re-INVITE successful.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "unhold";
                    break;
                }
                if (cla.vtsip.ncmpA("retrieve\n" + "Twinkle> ")) {
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "unhold";
                    break;
                }
                 */
                //============= call out process ================
                tmpStr = cla.vtsip.ncmpStar("%", "call %\n" + "Twinkle> \n" + "Line %: received 100 Trying", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    inLine = Lib.str2int(tmpStrA[1], 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "callOut~" + tmpStrA[0];
                }

                if (cla.vtsip.ncmpA("Line *: received 180 Ringing")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "ringOut";
                    break;
                }

                /*
                if (cla.vtsip.ncmpA("Twinkle> \n" + "Line *: call failed.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "callFailNoThisCall";
                    break;
                }
                 */
                if (cla.vtsip.ncmpA("Line *: call failed.\n" + "603 Decline")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "callFailFarEndNoAnswer";
                    break;
                }
                if (cla.vtsip.ncmpA("Line *: call failed.\n" + "486 Busy Here")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "callFailFarEndBusy";
                    break;
                }

                if (cla.vtsip.ncmpA("Line *: call failed.\n" + "404 Not Found")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "callFail";
                    break;
                }
                //if (cla.vtsip.ncmpA("Line *: call ended.\n" + "Twinkle>")) {
                if (cla.vtsip.ncmpA("Line *: call ended.\n")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "selfEndCall";
                    break;
                }

                tmpStr = cla.vtsip.ncmpStar("%", "Line %: far end answered call.\n" + "200 OK\n" + "To: sip:%@", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    inLine = Lib.str2int(tmpStrA[0], 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "farEndAnswerCall~" + tmpStrA[1];
                }
                //============= callin process ================
                tmpStr = cla.vtsip.ncmpStar("%", "Line %: incoming call\nFrom:% <sip:%@%>\nTo:", 80);
                if (tmpStr != null) {
                    tmpStrA = tmpStr.split("%");
                    inLine = Lib.str2int(tmpStrA[0], 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "incomeCall~" + tmpStrA[1] + "~" + tmpStrA[2];
                }
                if (cla.vtsip.ncmpA("Line *: far end cancelled call.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "farEndCancelCall";
                    break;
                }
                if (cla.vtsip.ncmpA("Line *: call rejected.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "rejectRingIn";
                    break;
                }

                if (cla.vtsip.ncmpA("DTMF detected: 1")) {
                    int phoneType = (int) GB.paraSetMap.get("phoneType");
                    if (phoneType == 1) {//roip
                        cla.pttOnPrg();
                    }
                    break;
                }
                if (cla.vtsip.ncmpA("DTMF detected: 2")) {
                    int phoneType = (int) GB.paraSetMap.get("phoneType");
                    if (phoneType == 1) {//roip
                        cla.pttOffPrg();
                    }
                    break;
                }

                //same as above
                /*
                if (cla.vtsip.ncmpA("bye\n" +"Twinkle>")) {
                    cla.sipData.lineMessageA[cla.sipData.nowLine] = "selfByeCall";
                    break;
                }
                 */
                if (cla.vtsip.ncmpA("Line *: call established.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "answerConnectCall";
                    break;
                }
                //============= connect process ================
                if (cla.vtsip.ncmpA("Line *: far end ended call.")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "farEndEndCall";
                    break;
                }
                //same in ringout
                /*
                if (cla.vtsip.ncmpA("Line *: call ended.\n" +"Twinkle>")) {
                    inLine = Lib.str2int(cla.vtsip.cmpAstr, 1) - 1;
                    cla.sipData.lineMessageA[inLine] = "selfEndCall";
                    break;
                }
                 */
            }
            break;
        }
        if (cla.sipData.lineMessageA[0].length() == 0 && cla.sipData.lineMessageA[1].length() == 0) {
            return;
        }
        int actLine;
        String actStr;
        if (cla.sipData.lineMessageA[1].length() != 0) {
            actLine = 1;
            actStr = cla.sipData.lineMessageA[1];
        } else {
            actLine = 0;
            actStr = cla.sipData.lineMessageA[0];
        }
        str = "\n====== Act Line ";
        str += (actLine + 1) + " : " + actStr;
        str += " ====== \n";
        System.out.println(str);
        strA = actStr.split("~");
        switch (strA[0]) {
            case "changeLine":
                cla.txShellEsc();
                cla.sipData.lineFlagA[0] &= 0x02;
                cla.sipData.lineFlagA[1] &= 0x02;
                break;
            case "hold":
                cla.sipData.lineFlagA[actLine] |= 1;
                break;
            case "unhold":
                cla.sipData.lineFlagA[actLine] &= 0xfe;
                cla.sipActName = "";
                break;
            case "mute":
                cla.sipData.lineFlagA[actLine] |= 2;
                break;
            case "unmute":
                cla.sipData.lineFlagA[actLine] &= 0xfd;
                cla.sipActName = "";
                break;
            //=== call out 
            case "callOut":
                cla.sipData.action = "撥打 " + strA[1];
                cla.sipData.lineStaA[actLine] = 1;
                cla.sipData.lineNameA[actLine] = strA[1];
                cla.sipData.lineNoA[actLine] = strA[1];
                cla.status_tim = 99999;
                break;
            case "ringOut":
                cla.sipData.status = "對方響鈴中....";
                cla.status_tim = 99999;
                break;
            case "callFailNoThisCall":
                cla.sipData.status = "電話號碼錯誤";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "callFailFarEndNoAnswer":
                cla.sipData.status = "對方逾時無接聽";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "callFailFarEndBusy":
                cla.sipData.status = "對方忙線中";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "callFail":
                cla.sipData.status = "呼叫失敗";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "selfEndCall":
                cla.sipData.status = "通話已中斷";
                cla.sipData.action = "取消通話";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "farEndAnswerCall":
                cla.sipData.status = "對方已接聽";
                cla.sipData.lineStaA[actLine] = 3;
                cla.status_tim = 50;
                Date dNow = new Date();
                cla.sipData.lineConnectTimeA[actLine] = dNow.getTime();
                break;
            //===income call 
            case "incomeCall":
                cla.sipData.status = "電話撥入 " + strA[1] + " <" + strA[2] + ">";
                cla.sipData.action = "請接電話 !";
                cla.sipData.lineStaA[actLine] = 2;
                cla.sipData.lineNameA[actLine] = strA[1];
                cla.sipData.lineNoA[actLine] = strA[2];
                cla.status_tim = 9999;
                if (strA[2].contains("*0*")) {
                    cla.phoneCommandIn("speakerAct");
                }
                cla.txShellEsc();
                break;
            case "farEndCancelCall":
                cla.sipData.status = "對方已掛斷";
                cla.sipData.action = cla.sipData.lineNoA[actLine] + " 來電無接聽";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "rejectRingIn":
                cla.sipData.action = "拒絕通話";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "selfByeCall":
                cla.sipData.action = "取消通話";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 50;
                break;
            case "answerConnectCall":
                cla.sipData.action = "通話已建立";
                cla.sipData.lineStaA[actLine] = 3;
                cla.status_tim = 50;
                dNow = new Date();
                cla.sipData.lineConnectTimeA[actLine] = dNow.getTime();

                break;
            //===connected 
            case "farEndEndCall":
                cla.sipData.status = "對方已掛斷";
                cla.sipData.lineStaA[actLine] = 0;
                cla.status_tim = 100;
                break;
            /*        
            case "selfEndCall":
                    break;
             */

        }

        //===============================================================================================
    }

    void vtcmpSip() {
    }
//%2

    void clearKeypad() {
        keypad_str = "";
        keypad_on_f = 0;

    }

    void sipLinPhoneAction(String act, String[] paras) {
        SipPhone cla = this;
        String str;
        int nowLine = sipData.nowLine;

        if (act.equals("loadSip")) {
            cla.sshWriteSip("linphonec\n");
            sipData.status = "Load SIP Phone";
            sipData.action = "載入SIP ....";
            return;
        }

        if (act.equals("call")) {
            str = "call " + paras[0] + "\n";
            sshWriteSip(str);
            sipData.lineNameA[nowLine] = "";
            sipData.lineNoA[nowLine] = paras[0];
            sipData.lineStaA[nowLine] = 1;
            if (GB.lang == 0) {
                sipData.status = "Call Out ....";
                sipData.action = "Call <" + paras[0] + ">";
            }
            if (GB.lang == 1) {
                sipData.status = "撥號中 ....";
                if (sipData.nowLine == 0) {
                    sipData.action = "撥打 <" + paras[0] + ">";
                } else {
                    sipData.action = "線路2: 連線到 <" + paras[0] + ">";
                }
            }
            status_tim = 100;
            clearKeypad();
            return;
        }

        if (act.equals("hold")) {
            if (sipData.lineStaA[sipData.nowLine] != 3) {
                return;
            }
            sipActName = "hold";
            sipActTime = 50;
            if ((sipData.lineFlagA[cla.sipData.nowLine] & 1) == 0) {
                sshWriteSip("pause\n");
            } else {
                sshWriteSip("resume\n");
            }
            return;
        }

        if (act.equals("mute")) {
            if (sipData.lineStaA[sipData.nowLine] != 3) {
                return;
            }
            int lineFlag = cla.sipData.lineFlagA[cla.sipData.nowLine];
            if (((lineFlag >> 1) & 1) == 1) {//mute
                sipActName = "mute";
                sipActTime = 50;
                sshWriteSip("unmute\n");

            } else {
                sipActName = "mute";
                sipActTime = 50;
                sshWriteSip("mute\n");
            }
            return;
        }

        if (act.equals("line2")) {
            sshWriteSip("line 2\n");
            return;
        }
        if (act.equals("bye")) {
            byeDelayTime = 50;
            sshWriteSip("terminate\n");
            return;
        }
        if (act.equals("line1")) {
            sshWriteSip("line 1\n");
            return;
        }
        if (act.equals("reject")) {
            sshWriteSip("terminate\n");
            return;
        }
        if (act.equals("answer")) {
            sshWriteSip("answer\n");
        }
        if (act.equals("transfer")) {
            transferPrg(paras[1]);
        }

    }

    void sipTwinckleAction(String act, String[] paras) {
        SipPhone cla = this;
        String str;
        int nowLine = sipData.nowLine;

        if (act.equals("loadSip")) {
            sshWriteSip("twinkle -c\n");
            sipData.status = "Load SIP Phone";
            sipData.action = "載入SIP ....";
            return;
        }

        if (act.equals("call")) {
            str = "call " + paras[0] + "\n";
            sshWriteSip(str);
            sipData.lineNameA[nowLine] = "";
            sipData.lineNoA[nowLine] = paras[0];
            sipData.lineStaA[nowLine] = 1;
            if (GB.lang == 0) {
                sipData.status = "Call Out ....";
                sipData.action = "Call <" + paras[0] + ">";
            }
            if (GB.lang == 1) {
                sipData.status = "撥號中 ....";
                if (sipData.nowLine == 0) {
                    sipData.action = "撥打 <" + paras[0] + ">";
                } else {
                    sipData.action = "線路2: 連線到 <" + paras[0] + ">";
                }
            }
            status_tim = 100;
            clearKeypad();
            return;
        }

        if (act.equals("hold")) {
            if (sipData.lineStaA[sipData.nowLine] != 3) {
                return;
            }
            sipActName = "hold";
            sipActTime = 50;
            if ((sipData.lineFlagA[cla.sipData.nowLine] & 1) == 0) {
                sshWriteSip("hold\n");
            } else {
                sshWriteSip("retrieve\n");
            }
            return;
        }

        if (act.equals("mute")) {
            if (sipData.lineStaA[sipData.nowLine] != 3) {
                return;
            }
            sipActName = "mute";
            sipActTime = 50;
            sshWriteSip("mute\n");
            return;
        }

        if (act.equals("line2")) {
            sshWriteSip("line 2\n");
            return;
        }
        if (act.equals("bye")) {
            byeDelayTime = 50;
            sshWriteSip("bye\n");
            return;
        }
        if (act.equals("line1")) {
            sshWriteSip("line 1\n");
            return;
        }
        if (act.equals("reject")) {
            sshWriteSip("reject\n");
            return;
        }
        if (act.equals("answer")) {
            sshWriteSip("answer\n");
            return;
        }
        if (act.equals("reDirect")) {
            if (paras[1].equals("reset")) {
                sshWriteSip("Redirect -a off\n");
            } else {
                sshWriteSip("Redirect -t always " + paras[1] + "\n");
                cla.sipData.reDirectNumber = paras[1];
            }
            return;
        }
        if (act.equals("transferNumber")) {
            transferPrg(paras[1]);
            return;
        }

    }

    void sipAct(String act, String[] paras) {
        if (this.softPhoneType == 0) {
            sipTwinckleAction(act, paras);
        } else {
            sipLinPhoneAction(act, paras);
        }
    }

    public void sshWriteSip(String shellCommand) {
        System.out.print("TX: " + shellCommand);

        SipPhone cla = this;
        if (cla.sshSip == null || cla.sshSip.connect_f == 0) {
            return;
        }
        try {
            cla.sshSip.outStrm.write(shellCommand.getBytes());
        } catch (IOException ex) {
        }
        try {
            cla.sshSip.outStrm.flush();
        } catch (IOException ex) {
        }
    }

    public void sshWriteNgrep(String shellCommand) {
        SipPhone cla = this;
        if (cla.sshNgrep == null || cla.sshNgrep.connect_f == 0) {
            return;
        }
        try {
            cla.sshNgrep.outStrm.write(shellCommand.getBytes());
        } catch (IOException ex) {
        }
        try {
            cla.sshNgrep.outStrm.flush();
        } catch (IOException ex) {
        }
    }

    public void sshWriteIct(String shellCommand) {
        SipPhone cla = this;
        if (cla.sshIct == null || cla.sshIct.connect_f == 0) {
            return;
        }
        try {
            cla.sshIct.outStrm.write(shellCommand.getBytes());
        } catch (IOException ex) {
        }
        try {
            cla.sshIct.outStrm.flush();
        } catch (IOException ex) {
        }
    }

    public void sshWriteShl(String shellCommand) {
        SipPhone cla = this;
        if (cla.sshShl == null || cla.sshShl.connect_f == 0) {
            return;
        }
        try {
            System.out.print("Cmd: " + shellCommand);
            cla.sshShl.outStrm.write(shellCommand.getBytes());
        } catch (IOException ex) {
        }
        try {
            cla.sshShl.outStrm.flush();
        } catch (IOException ex) {
        }
    }

    public void loadSockTx(TrxPack tpk, Ssocket ssk) {
        SipPhone cla = this;
        int blen = 0;
        for (int i = 0; i < tpk.amt; i++) {
            blen += tpk.txLen[i];
        }
        blen += tpk.amt * 4;
        int inx = 0;
        byte[] bts = ssk.stm.tbuf;
        bts[inx++] = (byte) (cla.piIoDeviceId & 255);
        bts[inx++] = (byte) ((cla.piIoDeviceId >> 8) & 255);
        bts[inx++] = (byte) (0xff);
        bts[inx++] = (byte) (0xff);
        bts[inx++] = (byte) (0xff);
        bts[inx++] = (byte) (0xa9);
        bts[inx++] = (byte) (blen & 255);
        bts[inx++] = (byte) ((blen >> 8) & 255);
        for (int i = 0; i < tpk.amt; i++) {
            int dlen = tpk.txLen[i];
            bts[inx++] = (byte) (tpk.idBase + i);
            bts[inx++] = (byte) (0xa9);
            bts[inx++] = (byte) (dlen & 255);
            bts[inx++] = (byte) ((dlen >> 8) & 255);
            for (int j = 0; j < dlen; j++) {
                bts[inx++] = tpk.txData[i][j];
            }
            tpk.txLen[i] = 0;
        }
        ssk.stm.tbuf_byte = inx;
        ssk.stm.enc_mystm();
    }

    //pi io 
    public void loadTxPiIo(TrxPack tpk, int packInx) {
        SipPhone cla = this;
        tpk.nowPack = packInx;
        tpk.loadStart();
        tpk.loadWord(cla.piIoDeviceId);
        tpk.loadWord(0xffff);
        tpk.loadWord(0xab00);
        tpk.loadWord(10);

        tpk.loadWord(0x1000);//cmd
        tpk.loadWord(piIoOut);//par0
        tpk.loadWord(piIoSet0);//par1
        tpk.loadWord(piIoSet1);//par2
        tpk.loadWord(piIoSet2);//par3
        tpk.txLen[packInx] = tpk.txDataPt;
    }

    //outside mcu controller 
    public void loadTxPiUart1(TrxPack tpk, int packInx) {
        SipPhone cla = this;
        tpk.nowPack = packInx;
        tpk.loadStart();
        tpk.loadWord(cla.piMcuDeviceId);
        tpk.loadWord(0xffff);
        tpk.loadWord(0xab00);
        tpk.loadWord(10);
        tpk.loadWord(0x1000);//par0
        int handSta = (cla.sipData.handStaA[1] << 4) + cla.sipData.handStaA[0];
        int ibuf = handSta;
        ibuf += cla.pttEn_f << 8;
        ibuf += cla.amaMute_f << 9;
        tpk.loadWord(ibuf);
        tpk.loadWord(0);//par1
        tpk.loadWord(0);//par2
        tpk.loadWord(0);//par3
        tpk.txLen[packInx] = tpk.txDataPt;
    }

    public int loadSipInfData(byte[] txBytes, int stInx) {
        int i;
        SipPhone cla = this;
        byte[] tmpbyte;
        int txlen;
        int ibuf;
        //ssk.stm.tbuf[stx_index++] = (byte) GB.sipmd_device_id;
        //===================================================
        int pos = stInx;

        txBytes[pos++] = (byte) (cla.sipPhoneDeviceId & 255);
        txBytes[pos++] = (byte) ((cla.sipPhoneDeviceId >> 8) & 255);

        String[] ipStrA = GB.realIpAddress.split("\\.");
        txBytes[pos++] = (byte) Lib.str2int(ipStrA[3], -1, 255, 0);
        txBytes[pos++] = (byte) Lib.str2int(ipStrA[2], -1, 255, 0);

        //txBytes[pos++] = (byte) 255;
        //txBytes[pos++] = (byte) 255;
        txBytes[pos++] = (byte) 0x00;
        txBytes[pos++] = (byte) 0xab;
        int tempPt = pos;
        txBytes[pos++] = (byte) 0x00;
        txBytes[pos++] = (byte) 0x00;
        txBytes[pos++] = (byte) 0x00;
        txBytes[pos++] = (byte) 0x10;
        //============================
        txBytes[pos++] = 0x00;       //ioBuf
        txBytes[pos++] = (byte) 4;
        txBytes[pos++] = (byte) ioBuf[0];
        txBytes[pos++] = (byte) ioBuf[1];
        txBytes[pos++] = (byte) ioBuf[2];
        txBytes[pos++] = (byte) ioBuf[3];
        //===================================================
        txBytes[pos++] = 0x10;       //sipphone status
        txBytes[pos++] = (byte) 10;
        txBytes[pos++] = (byte) cla.sipData.phoneSta;
        ////0:ready, 1: ring out, 2:ring in, 3:connect, 4:hold 
        ibuf = cla.sipData.lineStaA[0] & 15;
        ibuf += cla.sipData.lineStaA[1] << 4;

        //txBytes[pos++] = (byte) cla.sipData.connectSta;
        int earPhoneVolume = (int) GB.paraSetMap.get("earPhoneVolume");
        int setPhoneVolume = (int) GB.paraSetMap.get("setPhoneVolume");
        txBytes[pos++] = (byte) ibuf;;
        int handSta = (cla.sipData.handStaA[1] << 4) + cla.sipData.handStaA[0];
        txBytes[pos++] = (byte) handSta;
        txBytes[pos++] = (byte) earPhoneVolume;
        txBytes[pos++] = (byte) setPhoneVolume;
        txBytes[pos++] = (byte) GB.earMicSens;
        txBytes[pos++] = (byte) GB.phsetMicSens;
        //0:hold, 1:mute, 2:dtmf
        int lineFlag = cla.sipData.lineFlagA[cla.sipData.nowLine];
        cla.sipflag[0] = 0;
        if (((lineFlag >> 1) & 1) == 1) {//mute
            cla.sipflag[0] += 0x01;
        }
        if (GB.syssec_f == 1) {
            cla.sipflag[0] += 0x02;
        }
        if (cla.sipData.nowLine == 1) {
            cla.sipflag[0] += 0x04;
        }
        if (((lineFlag >> 2) & 1) == 1) {//dtmf
            cla.sipflag[0] += 0x08;
        }
        if (((lineFlag >> 0) & 1) == 1) {//hold
            cla.sipflag[0] += 0x10;
        }
        if (cla.line2ring_f == 1) {
            cla.sipflag[0] += 0x40;
        }

        if (cla.sipData.reDirection_f == 1) {
            cla.sipflag[0] += 0x80;
        }

        txBytes[pos++] = (byte) cla.sipflag[0];
        txBytes[pos++] = (byte) cla.sipflag[1];
        txBytes[pos++] = (byte) cla.sipflag[2];
        //===================================================
        //tmpbyte = cla.status_str.getBytes();
        tmpbyte = sipData.status.getBytes();
        txlen = tmpbyte.length;
        if (txlen > 40) {
            txlen = 40;
        }
        //==================
        txBytes[pos++] = 0x11;       //
        txBytes[pos++] = (byte) txlen;
        for (i = 0; i < txlen; i++) {
            txBytes[pos++] = tmpbyte[i];
        }
        //===================================================
        //tmpbyte = cla.action_str.getBytes();
        tmpbyte = sipData.action.getBytes();
        if (cla.keypad_on_f == 1) {
            tmpbyte = cla.keypad_str.getBytes();
        }
        if (cla.setting_on_f == 1) {
            tmpbyte = cla.setting_str.getBytes();
        }
        txlen = tmpbyte.length;
        if (txlen > 40) {
            txlen = 40;
        }
        //==================
        txBytes[pos++] = 0x12;       //
        txBytes[pos++] = (byte) txlen;
        for (i = 0; i < txlen; i++) {
            txBytes[pos++] = tmpbyte[i];
        }
        //===================================================
        tmpbyte = cla.callto.getBytes();
        txlen = tmpbyte.length;
        if (txlen > 10) {
            txlen = 10;
        }
        //==================
        txBytes[pos++] = 0x13;       //
        txBytes[pos++] = (byte) txlen;
        for (i = 0; i < txlen; i++) {
            txBytes[pos++] = tmpbyte[i];
        }
        //===================================================
        tmpbyte = cla.callfrom.getBytes();
        txlen = tmpbyte.length;
        if (txlen > 10) {
            txlen = 10;
        }
        //==================
        txBytes[pos++] = 0x14;       //
        txBytes[pos++] = (byte) txlen;
        for (i = 0; i < txlen; i++) {
            txBytes[pos++] = tmpbyte[i];
        }
        //===================================================
        /*
        if (txSipInf_step == 5) {
            if (txSipInf_step1 >= GB.carTypeName_len) {
                txSipInf_step++;
            }
        } else {
            if (++txSipInf_step >= 7) {
                txSipInf_step = 0;
            }
            txSipInf_step1 = 0;
        }
         */

        if (txSipInf_step_wait_f == 0) {
            if (++txSipInf_step >= 7) {
                txSipInf_step = 0;
            }
            txSipInf_step_a = 0;
            txSipInf_step_b = 0;
        }
        char[] chA;
        byte[] bytes;
        switch (txSipInf_step) {
            case 0:
                txSipInf_step_wait_f = 0;
                bytes = new byte[20];
                String[] slst;
                slst = GB.realIpAddress.split("\\.");
                bytes[0] = (byte) Lib.str2int(slst[0], -1, 255, 0);
                bytes[1] = (byte) Lib.str2int(slst[1], -1, 255, 0);
                bytes[2] = (byte) Lib.str2int(slst[2], -1, 255, 0);
                bytes[3] = (byte) Lib.str2int(slst[3], -1, 255, 0);
                slst = GB.realNetMask.split("\\.");
                bytes[4] = (byte) Lib.str2int(slst[0], -1, 255, 0);
                bytes[5] = (byte) Lib.str2int(slst[1], -1, 255, 0);
                bytes[6] = (byte) Lib.str2int(slst[2], -1, 255, 0);
                bytes[7] = (byte) Lib.str2int(slst[3], -1, 255, 0);
                slst = GB.realGateWay.split("\\.");
                bytes[8] = (byte) Lib.str2int(slst[0], -1, 255, 0);
                bytes[9] = (byte) Lib.str2int(slst[1], -1, 255, 0);
                bytes[10] = (byte) Lib.str2int(slst[2], -1, 255, 0);
                bytes[11] = (byte) Lib.str2int(slst[3], -1, 255, 0);

                //slst = GB.sipui_ip_str.split("\\.");
                slst = "0.0.0.0".split("\\.");
                bytes[12] = (byte) Lib.str2int(slst[0], -1, 255, 0);
                bytes[13] = (byte) Lib.str2int(slst[1], -1, 255, 0);
                bytes[14] = (byte) Lib.str2int(slst[2], -1, 255, 0);
                bytes[15] = (byte) Lib.str2int(slst[3], -1, 255, 0);

                //slst = GB.switch_ip_str.split("\\.");
                slst = "0.0.0.0".split("\\.");
                bytes[16] = (byte) Lib.str2int(slst[0], -1, 255, 0);
                bytes[17] = (byte) Lib.str2int(slst[1], -1, 255, 0);
                bytes[18] = (byte) Lib.str2int(slst[2], -1, 255, 0);
                bytes[19] = (byte) Lib.str2int(slst[3], -1, 255, 0);

                txBytes[pos++] = 0x15;       //
                txBytes[pos++] = (byte) 20;
                for (i = 0; i < 20; i++) {
                    txBytes[pos++] = bytes[i];
                }
                //=============================
                String sipName = GB.paraSetMap.get("sipName").toString();
                tmpbyte = sipName.getBytes();
                txBytes[pos++] = (byte) (0x16);       //
                txBytes[pos++] = (byte) tmpbyte.length;
                for (i = 0; i < tmpbyte.length; i++) {
                    txBytes[pos++] = tmpbyte[i];
                }

                String sipNumber = GB.paraSetMap.get("sipNumber").toString();
                chA = sipNumber.toCharArray();
                txBytes[pos++] = 0x17;       //
                txBytes[pos++] = (byte) chA.length;
                for (i = 0; i < chA.length; i++) {
                    txBytes[pos++] = (byte) chA[i];
                }
                String sipServerAddress = GB.paraSetMap.get("sipServerAddress").toString();
                chA = sipServerAddress.toCharArray();
                txBytes[pos++] = 0x18;       //
                txBytes[pos++] = (byte) chA.length;
                for (i = 0; i < chA.length; i++) {
                    txBytes[pos++] = (byte) chA[i];
                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                txSipInf_step_wait_f = 0;
                String version = GB.version + "-" + GB.paraSetMap.get("version").toString();
                chA = version.toCharArray();
                txBytes[pos++] = 0x40;       //
                txBytes[pos++] = (byte) chA.length;
                for (i = 0; i < chA.length; i++) {
                    txBytes[pos++] = (byte) chA[i];
                }
                //============================
                txBytes[pos++] = 0x42;                          //
                txBytes[pos++] = 6;                             //
                txBytes[pos++] = (byte) 0x00;
                txBytes[pos++] = (byte) 0x00;
                txBytes[pos++] = (byte) 0x00;
                txBytes[pos++] = (byte) 0x00;
                txBytes[pos++] = (byte) 0x00;
                txBytes[pos++] = (byte) 0x00;

                break;

            case 4:
                break;
            case 5:
                break;

            case 6:
                //cla.cmd_cnt=1;
                //cla.cmd_para0=0;
                txSipInf_step_wait_f = 0;

                switch (cla.cmd_cnt) {
                    case 0:
                        break;
                    case 1:
                        txBytes[pos++] = (byte) (0xA0);       //
                        txBytes[pos++] = (byte) 3;
                        txBytes[pos++] = (byte) cla.cmd_cnt;
                        txBytes[pos++] = (byte) cla.cmd_para0;
                        txBytes[pos++] = (byte) cla.cmd_para1;

                        break;

                    case 2:
                        txBytes[pos++] = (byte) (0xA0);       //
                        txBytes[pos++] = (byte) 3;
                        txBytes[pos++] = (byte) cla.cmd_cnt;
                        txBytes[pos++] = (byte) 0;
                        txBytes[pos++] = (byte) 0;

                        sipName = GB.paraSetMap.get("sipName").toString();
                        tmpbyte = sipName.getBytes();
                        txBytes[pos++] = (byte) (0xA1);       //
                        txBytes[pos++] = (byte) tmpbyte.length;
                        for (i = 0; i < tmpbyte.length; i++) {
                            txBytes[pos++] = tmpbyte[i];
                        }
                        sipNumber = GB.paraSetMap.get("sipNumber").toString();
                        tmpbyte = sipNumber.getBytes();
                        txBytes[pos++] = (byte) (0xA2);       //
                        txBytes[pos++] = (byte) tmpbyte.length;
                        for (i = 0; i < tmpbyte.length; i++) {
                            txBytes[pos++] = tmpbyte[i];
                        }
                        String sysIp = GB.paraSetMap.get("systemIpAddress").toString();
                        tmpbyte = sysIp.getBytes();
                        txBytes[pos++] = (byte) (0xA3);       //
                        txBytes[pos++] = (byte) tmpbyte.length;
                        for (i = 0; i < tmpbyte.length; i++) {
                            txBytes[pos++] = tmpbyte[i];
                        }

                        sipServerAddress = GB.paraSetMap.get("sipServerAddress").toString();
                        tmpbyte = sipServerAddress.getBytes();
                        txBytes[pos++] = (byte) (0xA4);       //
                        txBytes[pos++] = (byte) tmpbyte.length;
                        for (i = 0; i < tmpbyte.length; i++) {
                            txBytes[pos++] = tmpbyte[i];
                        }

                        break;

                }

                break;

        }
        txBytes[tempPt++] = (byte) ((pos - 8) & 255);
        txBytes[tempPt++] = (byte) (((pos - 8) >> 8) & 255);
        return pos;

    }

    void resetNetwork() {
        String cmdStr;
        String sysIp = GB.paraSetMap.get("systemIpAddress").toString();
        String sysMask = GB.paraSetMap.get("systemNetMask").toString();
        String sysGateWay = GB.paraSetMap.get("systemGateWay").toString();

        cmdStr = "sudo ifconfig eth0 ";
        cmdStr += sysIp;
        cmdStr += " netmask ";
        cmdStr += sysMask;
        cmdStr += " broadcast ";
        cmdStr += sysGateWay;
        Lib.exe(cmdStr);

        GB.realIpAddress = sysIp;
        GB.realNetMask = sysMask;
        GB.realGateWay = sysGateWay;

        //============================    
    }

    void hangOnPrg() {
        System.out.println("\n********** Hang on *********");
        setting_on_f = 0;
        keypad_on_f = 0;
        keypad_str = "";
        dtmfStr = "";
        sipData.lineFlagA[sipData.nowLine] = 0;
        sipData.handStaA[sipData.nowLine] = 0;
        sipData.lineStaA[sipData.nowLine] = 0;

        txShellEsc();
        if (sipData.phoneSta < 3) {//no register
            return;
        }
        if (GB.lang == 0) {
            sipData.action = "Hang On";
        }
        if (GB.lang == 1) {
            sipData.action = "掛上電話";
        }
        status_tim = 50;
        piIoOut &= 0xfffe;
        //==============================
        SipPhone cla = this;
        int nowSta = cla.sipData.lineStaA[cla.sipData.nowLine];
        int otherSta = cla.sipData.lineStaA[cla.sipData.nowLine ^ 1];
        int otherLine = cla.sipData.nowLine ^ 1;
        if (nowSta == 1 || nowSta == 3 || nowSta == 0) {//call out or connect
            sipAct("bye", null);
        }
        if (nowSta == 2) {//call in
            sipAct("reject", null);
        }
        if (otherSta != 0) {
            if (otherLine == 0) {
                sipAct("line1", null);
            } else {
                sipAct("line2", null);
            }
        } else {
            if (otherLine == 0) {
                sipAct("line1", null);
            }
        }
    }

    int chkSipRx(byte[] bts, int btsLen, int stInx) {
        SipPhone cla = this;
        String str;
        int i;
        int inx = stInx;
        int cmdinx;
        int cmdlen;
        int cmd;
        int btsEnd = btsLen + stInx;
        byte[] bytes;
        int deviceId = (bts[inx + 0] & 255) + (bts[inx + 1] & 255) * 256;
        @SuppressWarnings("unused")
        int serialId = (bts[inx + 2] & 255) + (bts[inx + 3] & 255) * 256;
        int groupId = (bts[inx + 4] & 255) + (bts[inx + 5] & 255) * 256;
        @SuppressWarnings("unused")
        int packLen = (bts[inx + 6] & 255) + (bts[inx + 7] & 255) * 256;
        int packCmd = (bts[inx + 8] & 255) + (bts[inx + 9] & 255) * 256;
        if (deviceId != sipPhoneDeviceId) {
            return 0;
        }
        if (groupId != 0xab00) {
            return 0;
        }
        if (packCmd != 0x1000) {
            return 0;
        }
        inx += 10;
        while (inx < btsEnd) {
            cmd = bts[inx];
            cmdlen = bts[inx + 1];
            cmdinx = inx + 2;
            switch (cmd) {
                case 0x1f://"tick by ics"
                    @SuppressWarnings("unused") int uiPort = 0;
                    @SuppressWarnings("unused") String uiIpStr = "";
                    uiIpStr = (bts[cmdinx++] & 255) + ".";
                    uiIpStr += (bts[cmdinx++] & 255) + ".";
                    uiIpStr += (bts[cmdinx++] & 255) + ".";
                    uiIpStr += (bts[cmdinx++] & 255) + "";
                    uiPort = (bts[cmdinx++] & 255);
                    uiPort += (bts[cmdinx++] & 255) * 256;
                    break;

                case 0x11://direct linphone command
                    if (shellCommandStatus == 1) {
                        //txShellEsc();
                    }
                    bytes = new byte[cmdlen];
                    for (i = 0; i < cmdlen; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    cla.sshWriteSip(new String(bytes));
                    break;
                case 0x12://direct shell command
                    if (shellCommandStatus == 1) {
                        //txShellEsc();
                    }
                    bytes = new byte[cmdlen];
                    for (i = 0; i < cmdlen; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    cla.sshWriteShl(new String(bytes));
                    //System.out.println(new String(bytes));
                    break;
                case 0x13://save net address
                    //ibuf = bts[cmdinx + 0] & 255;
                    str = (bts[cmdinx + 1] & 255) + ".";
                    str += (bts[cmdinx + 2] & 255) + ".";
                    str += (bts[cmdinx + 3] & 255) + ".";
                    str += (bts[cmdinx + 4] & 255) + "";

                    String sysIp = GB.paraSetMap.get("systemIpAddress").toString();
                    String sysMask = GB.paraSetMap.get("systemNetMask").toString();
                    String sysGateWay = GB.paraSetMap.get("systemGateWay").toString();

                    switch (bts[cmdinx + 0] & 255) {
                        case 0:
                            sysIp = str;
                            GB.paraSaveMap.put("systemIpAddress", sysIp);
                            App.saveParaSet();
                            break;
                        case 1:
                            sysMask = str;
                            GB.paraSaveMap.put("systemNetMask", sysMask);
                            App.saveParaSet();
                            break;
                        case 2:
                            sysGateWay = str;
                            GB.paraSaveMap.put("systemGateWay", sysGateWay);
                            App.saveParaSet();
                            break;
                        case 255:
                            sysIp = str;
                            str = (bts[cmdinx + 5] & 255) + ".";
                            str += (bts[cmdinx + 6] & 255) + ".";
                            str += (bts[cmdinx + 7] & 255) + ".";
                            str += (bts[cmdinx + 8] & 255) + "";
                            sysMask = str;
                            str = (bts[cmdinx + 9] & 255) + ".";
                            str += (bts[cmdinx + 10] & 255) + ".";
                            str += (bts[cmdinx + 11] & 255) + ".";
                            str += (bts[cmdinx + 12] & 255) + "";
                            sysGateWay = str;
                            GB.paraSaveMap.put("systemIpAddress", sysIp);
                            GB.paraSaveMap.put("systemNetMask", sysMask);
                            GB.paraSaveMap.put("systemGateWay", sysGateWay);
                            App.saveParaSet();
                            break;

                    }
                    break;
                case 0x14://sip phone command
                    bytes = new byte[cmdlen];
                    for (i = 0; i < cmdlen; i++) {
                        bytes[i] = bts[cmdinx++];
                    }

                    String valueStr = new String(bytes, StandardCharsets.UTF_8);
                    phoneCommandIn(valueStr);
                    break;
                case 0x15:
                    bytes = new byte[cmdlen];
                    for (i = 0; i < cmdlen; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    String sipNumber = new String(bytes);
                    GB.paraSaveMap.put("sipNumber", sipNumber);
                    App.saveParaSet();
                    break;
                case 0x16:
                    bytes = new byte[cmdlen];
                    for (i = 0; i < cmdlen; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    String sipServerPassword = new String(bytes);
                    GB.paraSaveMap.put("sipServerPassword", sipServerPassword);
                    App.saveParaSet();
                    break;
                case 0x17:
                    bytes = new byte[cmdlen];
                    for (i = 0; i < cmdlen; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    String sipServerAddress = new String(bytes);
                    GB.paraSaveMap.put("sipServerAddress", sipServerAddress);
                    App.saveParaSet();
                    break;
                case 0x18:
                    cla.resetTwinkle();
                    break;
                case 0x19:
                    str = (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + "";
                    sysIp = str;
                    str = (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + "";
                    sysMask = str;
                    str = (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + "";
                    sysGateWay = str;
                    str = (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + "";
                    String serverIp = str;

                    str = (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + ".";
                    str += (bts[cmdinx++] & 255) + "";
                    String ntpServerIp = str;

                    int cmdId;
                    cmdId = bts[cmdinx++] & 255;
                    if (cmdId != 0xab) //sipServerPin
                    {
                        break;
                    }
                    int len = bts[cmdinx++] & 255;
                    bytes = new byte[len];
                    for (i = 0; i < len; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    String sipServerPin = new String(bytes, Charset.forName("UTF-8"));

                    cmdId = bts[cmdinx++] & 255;
                    if (cmdId != 0xac) //sipName
                    {
                        break;
                    }
                    len = bts[cmdinx++] & 255;
                    bytes = new byte[len];
                    for (i = 0; i < len; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    String sipName = new String(bytes, Charset.forName("UTF-8"));

                    cmdId = bts[cmdinx++] & 255;
                    if (cmdId != 0xad) //sipNumber
                    {
                        break;
                    }
                    len = bts[cmdinx++] & 255;
                    bytes = new byte[len];
                    for (i = 0; i < len; i++) {
                        bytes[i] = bts[cmdinx++];
                    }
                    String sipNo = new String(bytes, Charset.forName("UTF-8"));
                    GB.paraSaveMap.put("systemIpAddress", sysIp);
                    GB.paraSaveMap.put("systemNetMask", sysMask);
                    GB.paraSaveMap.put("systemGateWay", sysGateWay);
                    GB.paraSaveMap.put("sipServerAddress", serverIp);
                    GB.paraSaveMap.put("sipServerPassword", sipServerPin);
                    GB.paraSaveMap.put("sipName", sipName);
                    GB.paraSaveMap.put("sipNumber", sipNo);
                    GB.paraSaveMap.put("ntpServerAddress", ntpServerIp);
                    App.saveParaSet();
                    System.out.println("saveParaSet " + sysIp);

                    break;

                case 0x1A:
                    cla.cmd_cnt = bts[cmdinx + 0] & 255;
                    cla.cmd_para0 = bts[cmdinx + 1] & 255;
                    cla.cmd_para1 = bts[cmdinx + 2] & 255;
                    break;
                case 0x1B:
                    break;
                case 0x1c:
                    break;
                case 0x1d:
                    break;

            }
            inx = inx + cmdlen + 2;
        }

        return 1;
    }

    void speakerAct() {
        @SuppressWarnings("unused")
        int line1Sta = sipData.lineStaA[0];
        @SuppressWarnings("unused")
        int line2Sta = sipData.lineStaA[1];
        int lineSta = sipData.lineStaA[sipData.nowLine];
        int lineElse = sipData.nowLine ^ 1;
        int lineElseSta = sipData.lineStaA[lineElse];
        int handStatus = sipData.handStaA[sipData.nowLine];
        if (handStatus == 1) {
            speakerOn();
            return;
        }
        if (handStatus == 2) {
            if (lineSta == 2) {
                sipAct("answer", null);
                return;
            }
            sipData.lineFlagA[sipData.nowLine] = 0;
            speakerOff();
            if (lineSta == 0) {
                stopDialTone();
            }
            if (lineSta == 1 || lineSta == 3) { //call out or connect
                sipAct("bye", null);
            }
            if (lineElseSta >= 1) {
                if (lineElse == 0) {
                    sipAct("line1", null);
                } else {
                    sipAct("line2", null);
                }
                return;
            } else {
                if (sipData.nowLine == 1) {
                    sipAct("line1", null);
                }
            }
            return;
        }
        if (handStatus == 0) {
            if (lineSta == 0) {
                stopDialTone();
                speakerOn();
                if (!keypad_str.equals("")) {
                    callStr(keypad_str);
                    keypad_str = "";
                    return;
                }
                playDialTone();
                return;
            }
            if (lineSta == 1) { //call out
                speakerOn();
                return;
            }
            if (lineSta == 2) { //call in
                sipAct("answer", null);
                speakerOn();
                return;
            }
            if (lineSta == 3) { //call in
                speakerOn();
            }
        }
    }

    void earPhoneAct() {
        @SuppressWarnings("unused")
        int line1Sta = sipData.lineStaA[0];
        @SuppressWarnings("unused")
        int line2Sta = sipData.lineStaA[1];
        int lineSta = sipData.lineStaA[sipData.nowLine];
        int lineElse = sipData.nowLine ^ 1;
        int lineElseSta = sipData.lineStaA[lineElse];
        int handStatus = sipData.handStaA[sipData.nowLine];
        if (handStatus == 2) {
            earPhoneOn();
            return;
        }
        if (handStatus == 1) {
            if (lineSta == 2) {
                sipAct("answer", null);
                return;
            }
            sipData.lineFlagA[sipData.nowLine] = 0;
            earPhoneOff();
            if (lineSta == 0) {
                stopDialTone();
            }
            if (lineSta == 1 || lineSta == 3) { //call out or connect
                sipAct("bye", null);
            }
            if (lineElseSta >= 1) {
                if (lineElse == 0) {
                    sipAct("line1", null);
                } else {
                    sipAct("line2", null);
                }
                return;
            } else {
                if (sipData.nowLine == 1) {
                    sipAct("line1", null);
                }
            }
            return;
        }
        if (handStatus == 0) {
            if (lineSta == 0) {
                stopDialTone();
                earPhoneOn();
                if (!keypad_str.equals("")) {
                    callStr(keypad_str);
                    keypad_str = "";
                    return;
                }
                playDialTone();
                return;
            }
            if (lineSta == 1) { //call out
                earPhoneOn();
                return;
            }
            if (lineSta == 2) { //call in
                sipAct("answer", null);
                earPhoneOn();
                return;
            }
            if (lineSta == 3) { //call in
                earPhoneOn();
            }
        }
    }

    void speakerSwap() {
        int handStatus = sipData.handStaA[sipData.nowLine];
        if (handStatus != 2) {
            speakerOn();
        } else {
            speakerOff();
        }
    }

    void earPhoneSwap() {
        int handStatus = sipData.handStaA[sipData.nowLine];
        if (handStatus != 1) {
            earPhoneOn();
        } else {
            earPhoneOff();
        }
    }

    void playDialTone() {
        sshWriteSound("aplay /home/pi/kevin/sipphone/dial_tone.wav & PID=$! \n");
    }

    void stopDialTone() {
        byte[] bytes;
        bytes = new byte[2];
        bytes[0] = 0x03;
        bytes[1] = 13;
        sshWriteSound("kill $PID\n");
        shellCommandStatus = 0;
    }

    public void sshWriteSound(String shellCommand) {
        SipPhone cla = this;
        if (cla.sshSound == null || cla.sshSound.connect_f == 0) {
            return;
        }
        try {
            cla.sshSound.outStrm.write(shellCommand.getBytes());
        } catch (IOException ex) {
        }
        try {
            cla.sshSound.outStrm.flush();
        } catch (IOException ex) {
        }
    }

    void speakerOn() {
        System.out.println("\n********** speaker on *********");
        sipData.handStaA[sipData.nowLine] = 2;
        sipData.handTimeA[sipData.nowLine] = 0;
        setSpeakerVolume();
        ioBuf[0] &= 0xfc;
        ioBuf[0] |= 0x03;

    }

    void speakerOff() {
        System.out.println("\n********** speaker off *********");
        sipData.handStaA[sipData.nowLine] = 0;
        sipData.handTimeA[sipData.nowLine] = 0;
        ioBuf[0] &= 0xfc;
    }

    void earPhoneOn() {
        System.out.println("\n********** earPhone on *********");
        sipData.handStaA[sipData.nowLine] = 1;
        sipData.handTimeA[sipData.nowLine] = 0;
        setEarphoneVolume();
        ioBuf[0] &= 0xfc;
        ioBuf[0] |= 0x01;
    }

    void earPhoneOff() {
        System.out.println("\n********** earPhone off *********");
        sipData.handStaA[sipData.nowLine] = 0;
        sipData.handTimeA[sipData.nowLine] = 0;
        ioBuf[0] &= 0xfc;
    }

    void pttPrg() {
        piIoOut ^= 1;
        pttOnTime = 0;
        System.out.println("\npttOnOff");

    }

    void pttOnPrg() {
        piIoOut |= 1;
        pttOnTime = 0;
        System.out.println("\npttOn");
    }

    void pttOffPrg() {
        piIoOut &= 0xfffe;
        pttOnTime = 0;
        System.out.println("\npttOff");
    }

    void phoneCommandIn(String cmdStr) {
        String[] strA = cmdStr.split(" ");
        switch (strA[0]) {
            case "ptt":
                pttPrg();
                break;

            case "hangon": //hangon
                hangOnPrg();
                break;
            case "hangoff": //hangoff
                earPhoneAct();
                break;
            case "speaker": //speaker on
                speakerAct();
                break;
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8"
            + "":
            case "9":
            case "*":
            case "#":
            case "ok":
                sipData.handTimeA[sipData.nowLine] = 0;
                phoneKeyin(cmdStr);
                break;
            case "+":
                sipData.handTimeA[sipData.nowLine] = 0;
                volumePlus();
                break;
            case "-":
                sipData.handTimeA[sipData.nowLine] = 0;
                volumeMinus();
                break;
            case "prev":
                show_preno(0);
                break;
            case "up":
                if (keypad_on_f == 1) {
                    break;
                }
                if (setting_on_f == 1) {
                    if (!setId.equals("prevCall")) {
                        break;
                    }
                }
                int lineSta = sipData.lineStaA[sipData.nowLine];
                if (lineSta != 0) {
                    break;
                }
                show_preno(0);
                break;
            case "down":
                if (keypad_on_f == 1) {
                    break;
                }
                if (setting_on_f == 1) {
                    if (!setId.equals("prevCall")) {
                        break;
                    }
                }
                lineSta = sipData.lineStaA[sipData.nowLine];
                if (lineSta != 0) {
                    break;
                }
                show_preno(1);
                break;
            case "right":
                break;
            case "left":
                if (keypad_on_f == 1) {
                    if (keypad_str.length() != 0) {
                        keypad_str = keypad_str.substring(0, keypad_str.length() - 1);
                    }
                    keypad_tim = 0;
                }

                break;
            case "menu":
                break;
            case "esc":
                break;
            case "cancle":
                keypad_on_f = 0;
                keypad_str = "";
                keypad_tim = 0;
                setting_on_f = 0;
                setting_str = "";
                dtmfStr = "";
                status_tim = 0;
                break;
            case "hold":
                sipAct("hold", null);
                break;
            case "line1":
                sipAct("line1", null);
                break;
            case "line2":
                sipAct("line2", null);
                break;
            case "mute":
                sipAct("mute", null);
                break;
            case "transfer":
                transferCall();
                break;
            case "transferNumber":
                sipAct("transferNumber", strA);
                break;
            case "reDirect":
                sipAct("reDirect", strA);
                break;
            case "f1":
                break;
            case "f2":
                break;
            case "f3":
                break;
            case "f4":
                break;
            case "book":
                break;
            case "dtmf":
                keypad_tim = 9999;
                status_tim = 0;
                lineSta = sipData.lineStaA[sipData.nowLine];
                if (lineSta != 3) {
                    return;
                }
                if ((sipData.lineFlagA[sipData.nowLine] & 0x04) != 0) {
                    sipData.lineFlagA[sipData.nowLine] &= 0xfb;
                } else {
                    sipData.lineFlagA[sipData.nowLine] |= 0x04;
                }
                break;
            case "hotline":
                String callNo = GB.paraSetMap.get("hotLineNumber#" + strA[1]).toString();
                callStr(callNo);
                break;

            case "call":
                callStr(strA[1]);
                break;

        }

    }

    void menuKeyUp() {
    }

    void menuKeyDown() {
    }

    void settingOk() {
        if (setId.equals("prevCall")) {
            phoneKeyin("#");
            return;
        }
    }

    void show_preno(int incdec_f) {
        if (setting_on_f == 1) {
            if (setId.equals("prevCall")) {
                if (incdec_f == 0) {
                    preno_cnt++;
                } else {
                    preno_cnt--;
                }
            }
        }
        if (preno_cnt > 9) {
            preno_cnt = 0;
        }
        if (preno_cnt < 0) {
            preno_cnt = 9;
        }
        setId = "prevCall";
        setting_str = "Last " + (preno_cnt + 1) + " call: " + get_preno(preno_cnt);
        setting_on_f = 1;
        setting_tim = 0;
    }

    void save_preno(String noStr) {
        preno_inx++;
        if (preno_inx >= 10) {
            preno_inx = 0;
        }
        prenoStrA[preno_inx] = noStr;
        preno_cnt = 0;
    }

    String get_preno(int prenoCnt) {
        int i;
        i = preno_inx - prenoCnt;
        if (i < 0) {
            i += 10;
        }
        @SuppressWarnings("unused")
        String Str = prenoStrA[i];
        return prenoStrA[i];
    }

    void txShellEsc() {
        byte[] bytes;
        bytes = new byte[2];
        bytes[0] = 0x03;
        bytes[1] = 13;
        //sshWriteShl(new String(bytes));
        sshWriteShl("kill $PID\n");
        shellCommandStatus = 0;
        //dndOff();

    }

    void dndOn() {
        String str;
        str = "dnd -a on\n";
        sshWriteSip(str);
    }

    void dndOff() {
        String str;
        str = "dnd -a off\n";
        sshWriteSip(str);

    }

    void callStr(String noStr) {
        txShellEsc();
        if (setting_on_f == 1) {
            return;
        }
        if (sipData.phoneSta != 3) {
            return;
        }

        int lineSta = sipData.lineStaA[sipData.nowLine];
        int handStatus = sipData.handStaA[sipData.nowLine];
        if (lineSta != 0) {
            return;
        }

        if (handStatus == 0) {
            speakerOn();
        }
        save_preno(noStr);
        sipAct("call", new String[]{noStr});
    }

    void transferPrg(String number) {
        String str;
        str = "transfer " + number + "\n";
        sshWriteSip(str);
        String keypadStr = keypad_str;
        hangOnPrg();
        setId = "transferDone";
        setting_str = keypadStr + " 轉接中";
        setting_on_f = 1;
        setting_tim = 150;
        keypad_str = number;

    }

    void transferCall() {
        int nowSta = sipData.lineStaA[sipData.nowLine];
        if (nowSta != 3) {
            return;
        }
        setting_on_f = 1;
        setting_tim = 0;
        keypad_str = "";
        setting_str = "轉接到 ";
        setId = "transfer";
    }

    void phoneKeyin(String cmd) {
        String str;
        @SuppressWarnings("unused")
        byte[] bytes;
        setting_tim = 0;
        if (shellCommandStatus == 1) {
            //txShellEsc();
        }
        if (setting_on_f == 1) {
            while (true) {
                if (setId.equals("transfer")) {
                    if (cmd.equals("ok") || cmd.equals("#")) {
                        if (keypad_str.equals("")) {
                            return;
                        }
                        transferPrg(keypad_str);
                        return;
                    }
                    keypad_str += cmd;
                    keypad_tim = 0;
                    setting_tim = 0;
                    setting_str = "轉接到 " + keypad_str;
                    return;
                }

                if (setId.equals("prevCall")) {
                    if (!cmd.equals("ok")) {
                        return;
                    }
                    setting_on_f = 0;
                    keypad_str = get_preno(preno_cnt);
                    break;
                }
                return;
            }
        }

        if (sipData.phoneSta == 3) {
            int lineSta = sipData.lineStaA[sipData.nowLine];
            if (lineSta == 0) {
                if (cmd.equals("ok") || cmd.equals("#")) {
                    if (keypad_str.equals("")) {
                        return;
                    }
                    callStr(keypad_str);
                    keypad_str = "";
                    return;
                } else {
                    keypad_tim = 0;
                    keypad_str += cmd;
                    @SuppressWarnings("unused")
                    String qq = keypad_str;
                    keypad_on_f = 1;
                }
                return;
            }
            if (lineSta == 3) {
                //if ((sipData.lineFlagA[sipData.nowLine] & 0x04) != 0) {
                if (setting_on_f == 1) {
                    return;
                }
                int dtmf_f = 0;
                if (cmd.equals("0")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("1")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("2")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("3")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("4")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("5")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("6")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("7")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("8")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("9")) {
                    dtmf_f = 1;
                }
                if (cmd.equals("*")) {
                    dtmf_f = 1;
                }
                if (dtmf_f == 0) {
                    return;
                }

                str = "dtmf " + cmd + "\n";
                sshWriteSip(str);
                dtmfStr += cmd;
                keypad_str = "DTMF " + cmd;
                keypad_tim = 0;
                keypad_on_f = 1;
                //status_tim = 50 * 30;
                return;
                //}
            }

        }

    }

    void vrVolume() {
        if (nowVrVol_f == 0) {
            return;
        }
        int setPhoneVolume = (int) GB.paraSetMap.get("setPhoneVolume");
        if (setPhoneVolume == nowVrVol) {
            nowVrVol_f = 0;
            return;
        }
        nowVrVolTime++;
        if (nowVrVolTime < 8) {
            return;
        }
        nowVrVolTime = 0;
        if (setPhoneVolume < nowVrVol) {
            setPhoneVolume++;
            if (setPhoneVolume > 9) {
                setPhoneVolume = 9;
            }
        } else {
            setPhoneVolume--;
            if (setPhoneVolume < 0) {
                setPhoneVolume = 0;
            }
        }

        setting_str = "Speaker Volume= " + setPhoneVolume;
        GB.paraSetMap.put("setPhoneVolume", setPhoneVolume);
        setSpeakerVolume();
        //Base3.scla.editNewDb("phset_speaker_vol", "" + setPhoneVolume);
        setting_on_f = 1;
        setId = "setVolume";
        setting_tim = 0;
        System.out.println("vrVolume set: " + setPhoneVolume);

    }

    void volumePlus() {
        setting_tim = 0;
        int handStatus = sipData.handStaA[sipData.nowLine];
        int earPhoneVolume = (int) GB.paraSetMap.get("earPhoneVolume");
        int setPhoneVolume = (int) GB.paraSetMap.get("setPhoneVolume");

        if (handStatus == 1) {
            if (setting_on_f == 1 && earPhoneVolume < 9) {
                earPhoneVolume++;
            }
            setting_str = "Ear Phone Volume= " + earPhoneVolume;
            setEarphoneVolume();
            //Base3.scla.editNewDb("ear_speaker_vol", "" + earPhoneVolume);
        } else {
            if (setting_on_f == 1 && setPhoneVolume < 9) {
                setPhoneVolume++;
            }
            setting_str = "Speaker Volume= " + setPhoneVolume;
            setSpeakerVolume();
            //Base3.scla.editNewDb("phset_speaker_vol", "" + setPhoneVolume);
        }
        GB.paraSetMap.put("earPhoneVolume", earPhoneVolume);
        GB.paraSetMap.put("setPhoneVolume", setPhoneVolume);
        setting_on_f = 1;
        setId = "setVolume";
    }

    void volumeMinus() {
        setting_tim = 0;
        int handStatus = sipData.handStaA[sipData.nowLine];
        int earPhoneVolume = (int) GB.paraSetMap.get("earPhoneVolume");
        int setPhoneVolume = (int) GB.paraSetMap.get("setPhoneVolume");
        if (handStatus == 1) {
            if (setting_on_f == 1 && earPhoneVolume > 0) {
                earPhoneVolume--;
            }
            setting_str = "Ear Phone Volume= " + earPhoneVolume;
            setEarphoneVolume();
            //Base3.scla.editNewDb("ear_speaker_vol", "" + earPhoneVolume);
        } else {
            if (setting_on_f == 1 && setPhoneVolume > 0) {
                setPhoneVolume--;
            }
            setting_str = "Speaker Volume= " + setPhoneVolume;
            setSpeakerVolume();
            //Base3.scla.editNewDb("phset_speaker_vol", "" + setPhoneVolume);
        }
        GB.paraSetMap.put("earPhoneVolume", earPhoneVolume);
        GB.paraSetMap.put("setPhoneVolume", setPhoneVolume);
        setting_on_f = 1;
        setId = "setVolume";

    }

    void setSpeakerVolume() {
        SipPhone cla = this;
        String str;
        int setPhoneVolume = (int) GB.paraSetMap.get("setPhoneVolume");
        //txShellEsc();
        //str = "sudo amixer cset numid=4 " + outVolumeTbl[GB.phset_speaker_vol] + "," + outVolumeTbl[GB.phset_speaker_vol] + "\n";
        //str = "sudo amixer cset numid=4 " + 0 + "," + outVolumeTbl[GB.phset_speaker_vol] + "\n";

        int phoneType = (int) GB.paraSetMap.get("phoneType");
        if (phoneType == 1) //roip
        {
            str = "sudo amixer cset numid=6 " + outVolumeTbl[setPhoneVolume] + "," + outVolumeTbl[setPhoneVolume] + "\n";
        } else {
            str = "sudo amixer cset numid=4 " + outVolumeTbl[setPhoneVolume] + "," + 0 + "\n";
        }

        cla.sshWriteShl(str);

    }

    void setEarphoneVolume() {
        SipPhone cla = this;
        @SuppressWarnings("unused")
        byte[] bytes;
        String str;
        //txShellEsc();
        //str = "sudo amixer cset numid=4 " + outVolumeTbl[GB.ear_speaker_vol] + "," + outVolumeTbl[GB.ear_speaker_vol] + "\n";
        //str = "sudo amixer cset numid=4 " + outVolumeTbl[GB.ear_speaker_vol] + "," + 0 + "\n";
        int earPhoneVolume = (int) GB.paraSetMap.get("earPhoneVolume");
        str = "sudo amixer cset numid=4 " + outVolumeTbl[earPhoneVolume] + "," + 0 + "\n";
        cla.sshWriteShl(str);
    }

    void sskioRx(int format) {
        SipPhone cla = this;
        @SuppressWarnings("unused")
        String str;
        cla.sskio.datain_f = 0;
        cla.sskio.connect_f = 1;
        MyStm stm = cla.sskio.stm;
        int rxLen = stm.rxlen;
        stm.setRdataPt(0);
        int deviceId = stm.readWord();
        @SuppressWarnings("unused")
        int serialId = stm.readWord();
        if (deviceId != cla.piIoDeviceId) {
            return;
        }
        int packId = stm.readWord();
        if (packId != 0xa9ff) {
            return;
        }
        int packLen = stm.readWord();
        if (packLen > 4000) {
            return;
        }
        stm.setRdataNextPt(stm.rdataPt);
        for (;;) {
            stm.rdataPt = stm.rdataNextPt;
            if ((stm.rdataPt) + 4 > rxLen) {
                break;
            }
            if (stm.rdataPt > 4000) {
                break;
            }
            packId = stm.readWord();
            packLen = stm.readWord();
            if (packId == 0xa910) {//piIo
                stm.setRdataNextPt(stm.rdataPt + packLen);
                deviceId = stm.readWord();
                serialId = stm.readWord();
                int groupId = stm.readWord();
                if (deviceId != 0x1946 || groupId != 0xab00) {
                    continue;
                }
                @SuppressWarnings("unused")
                int groupLen = stm.readWord();
                int packCmd = stm.readWord();
                if (packCmd == 0x1000) {
                    piIoStatus0 = stm.readWord();
                    piIoStatus1 = stm.readWord();
                    piIoInFlag0 = stm.readWord();
                    piIoInFlag1 = stm.readWord();
                }
                loadTxPiIo(cla.tpk0, 0);
                continue;
            }
            if (packId == 0xa911) {//uart0 sip
                stm.setRdataNextPt(stm.rdataPt + packLen);
                @SuppressWarnings("unused")
                int okf = chkSipRx(stm.rdata, packLen, stm.rdataPt);
                cla.tpk0.txLen[1] = loadSipInfData(cla.tpk0.txData[1], 0);
                continue;
            }
            if (packId == 0xa912) {//mcuIo
                stm.setRdataNextPt(stm.rdataPt + packLen);
                deviceId = stm.readWord();
                serialId = stm.readWord();
                int groupId = stm.readWord();
                if (deviceId != 0x1945 || groupId != 0xac00) {
                    continue;
                }
                @SuppressWarnings("unused")
                int groupLen = stm.readWord();
                int packCmd = stm.readWord();
                if (packCmd == 0x1000) {
                    piMcuStatus = stm.readWord();
                    piMcuVrAdi = stm.readWord();
                    if (piMcuVrAdi > 0x3ff) {
                        piMcuVrAdi = 0x3ff;
                    }
                    nowVr = 0x3ff - piMcuVrAdi;
                    int vol = 0;
                    for (int i = 1; i <= 9; i++) {
                        if (nowVr < (i * 102 - 10)) {
                            break;
                        }
                        vol++;
                    }
                    int aVol = vol;
                    vol = 0;
                    for (int i = 1; i <= 9; i++) {
                        if (nowVr < (i * 102 + 10)) {
                            break;
                        }
                        vol++;
                    }
                    int bVol = vol;
                    if (nowVrVol != aVol && nowVrVol != bVol) {
                        nowVrVol = aVol;
                    }
                    if (aVol == bVol) {
                        nowVrVol = aVol;
                    }
                    if (preVrVol == 0xffff) {
                        preVrVol = nowVrVol;
                        continue;
                    }
                    if (nowVrVol == preVrVol) {
                        continue;
                    }
                    System.out.println("nowVrVol " + nowVrVol);
                    preVrVol = nowVrVol;
                    nowVrVolTime = 0;
                    nowVrVol_f = 1;

                }
                //loadTxPiUart1(cla.tpk0, 2);
                continue;
            }
            if (packId == 0xa913) { //uart2
                stm.setRdataNextPt(stm.rdataPt + packLen);
                continue;
            }
            break;
        }
        loadTxPiUart1(cla.tpk0, 2);
        loadSockTx(cla.tpk0, cla.sskio);
        cla.sskio.txret();
    }

    void txSock(Ssocket ssk) {
        try {
            for (int i = 0; i < ssk.stm.txlen; i++) {
                ssk.outstr.write(ssk.stm.tdata[i]);
            }
        } catch (IOException ex) {
        }
        ssk.stm.txlen = 0;
    }

    void resetTwinkle() {
        SipPhone cla = this;
        cla.sipData.phoneSta = 2;
        cla.setTwincleCfg();
        cla.sshWriteSip("quit\n");
        cla.auto_register_tim = 0;

    }

    void reRegister() {
        SipPhone cla = this;
        @SuppressWarnings("unused")
        String str;
        int lineSta = cla.sipData.lineStaA[cla.sipData.nowLine];////0:ready, 1: ring out, 2:ring in, 3:connect, 4:hold 
        if (lineSta != 0) {
            cla.auto_register_tim = 0;
            return;
        }
        if (cla.sipData.phoneSta >= 2) {
            System.out.println("reRegist pbx");
            registSip();
        }
        cla.auto_register_tim = 0;
    }

    void registSip() {
        SipPhone cla = this;
        String str;
        if (cla.softPhoneType == 0) {
            cla.sshWriteSip("register -a\n");
        } else {
            @SuppressWarnings("unused")
            String sipName = GB.paraSetMap.get("sipName").toString();
            String sipNo = GB.paraSetMap.get("sipNumber").toString();
            String sipServerIp = GB.paraSetMap.get("sipServerAddress").toString();
            String sipServerPin = GB.paraSetMap.get("sipServerPassword").toString();

            str = "register sip:" + sipNo + "@";
            str += sipServerIp;
            str += " sip:" + sipServerIp + ' ' + sipServerPin + '\n';
            cla.sshWriteSip(str);

        }

    }
}

class SiprxTd extends Thread {

    SipPhone cla;

    SiprxTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.siprxTd_run_f == 1) {
                if (cla.sshSip != null && cla.sshSip.connect_f == 1) {
                    try {
                        if (cla.sshSip.inStrm.available() > 0) {
                            byte[] data = new byte[cla.sshSip.inStrm.available()];
                            int nLen = cla.sshSip.inStrm.read(data);
                            if (nLen < 0) {
                            } else if (nLen != 0) {
                                cla.vtsip.dataAvailable(data);
                                cla.sipPhoneRx.sshRx(cla.vtsip.incha);
                            } else {
                            }
                        }

                    } catch (IOException ex) {
                    }
                }
                Lib.thSleep(10);
                if (cla.siprxTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}

class SipconTd extends Thread {

    SipPhone cla;
    int dis_connect_tim = 0;

    SipconTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.sipconTd_run_f == 1) {
                //==========================
                int ibuf;

                ibuf = Lib.ping(GB.twinkleDeviceIp);
                if (ibuf == 0) {
                    dis_connect_tim = 0;
                    if (cla.sshSip == null) {
                        cla.sshSip = new Ssh(GB.twinkleDeviceIp, GB.twinkleDeviceName, GB.twinkleDevicePassword);
                        cla.sshSip.connect();
                        if (cla.sshSip.connect_f == 0) {
                            cla.sshSip = null;
                        }

                    }
                } else {
                    dis_connect_tim++;
                    if (dis_connect_tim >= 5) {
                        if (cla.sshSip != null) {
                            cla.sshSip.connect_f = 0;
                            cla.sshSip = null;
                        }
                    }
                }
                //==========================
                Lib.thSleep(100);
                if (cla.sipconTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}

class ShlconTd extends Thread {

    SipPhone cla;
    int dis_connect_tim = 0;

    ShlconTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.shlconTd_run_f == 1) {
                //==========================
                int ibuf;

                ibuf = Lib.ping(GB.twinkleDeviceIp);
                if (ibuf == 0) {
                    dis_connect_tim = 0;
                    if (cla.sshShl == null) {
                        cla.sshShl = new Ssh(GB.twinkleDeviceIp, GB.twinkleDeviceName, GB.twinkleDevicePassword);
                        cla.sshShl.connect();
                        if (cla.sshShl.connect_f == 0) {
                            cla.sshShl = null;
                        }

                    }
                } else {
                    dis_connect_tim++;
                    if (dis_connect_tim >= 5) {
                        if (cla.sshShl != null) {
                            cla.sshShl.connect_f = 0;
                            cla.sshShl = null;
                        }
                    }
                }
                //==========================
                Lib.thSleep(100);
                if (cla.shlconTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}

class NgrepconTd extends Thread {

    SipPhone cla;
    int dis_connect_tim = 0;

    NgrepconTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.ngrepconTd_run_f == 1) {
                //==========================
                int ibuf;
                ibuf = Lib.ping(GB.twinkleDeviceIp);
                if (ibuf == 0) {
                    dis_connect_tim = 0;
                    if (cla.sshNgrep == null) {
                        cla.sshNgrep = new Ssh(GB.twinkleDeviceIp, GB.twinkleDeviceName, GB.twinkleDevicePassword);
                        cla.sshNgrep.connect();
                        if (cla.sshNgrep.connect_f == 0) {
                            cla.sshNgrep = null;
                        }

                    }
                } else {
                    dis_connect_tim++;
                    if (dis_connect_tim >= 5) {
                        if (cla.sshNgrep != null) {
                            cla.sshNgrep.connect_f = 0;
                            cla.sshNgrep = null;
                        }
                    }
                }
                //==========================
                Lib.thSleep(100);
                if (cla.ngrepconTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}

class IctconTd extends Thread {

    SipPhone cla;
    int dis_connect_tim = 0;

    IctconTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.ictconTd_run_f == 1) {
                //==========================
                int ibuf;
                ibuf = Lib.ping(GB.twinkleDeviceIp);
                if (ibuf == 0) {
                    dis_connect_tim = 0;
                    if (cla.sshIct == null) {
                        cla.sshIct = new Ssh(GB.twinkleDeviceIp, GB.twinkleDeviceName, GB.twinkleDevicePassword);
                        cla.sshIct.connect();
                        if (cla.sshIct.connect_f == 0) {
                            cla.sshIct = null;
                        }

                    }
                } else {
                    dis_connect_tim++;
                    if (dis_connect_tim >= 5) {
                        if (cla.sshIct != null) {
                            cla.sshIct.connect_f = 0;
                            cla.sshIct = null;
                        }
                    }
                }
                //==========================
                Lib.thSleep(100);
                if (cla.ictconTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}

class ShlrxTd extends Thread {

    SipPhone cla;

    ShlrxTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.shlrxTd_run_f == 1) {
                if (cla.sshShl != null && cla.sshShl.connect_f == 1) {
                    try {
                        if (cla.sshShl.inStrm.available() > 0) {
                            byte[] data = new byte[cla.sshShl.inStrm.available()];
                            int nLen = cla.sshShl.inStrm.read(data);
                            if (nLen < 0) {
                            } else if (nLen != 0) {
                                cla.vtshl.dataAvailable(data);            //<<debug
                                cla.shellRx.sshRx(cla.vtshl.incha);       //<<debug
                            } else {
                            }
                        }

                    } catch (IOException ex) {
                    }
                }
                Lib.thSleep(10);
                if (cla.shlrxTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}

class NgreprxTd extends Thread {

    SipPhone cla;

    NgreprxTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.ngreprxTd_run_f == 1) {
                if (cla.sshNgrep != null && cla.sshNgrep.connect_f == 1) {
                    try {
                        if (cla.sshNgrep.inStrm.available() > 0) {
                            byte[] data = new byte[cla.sshNgrep.inStrm.available()];
                            int nLen = cla.sshNgrep.inStrm.read(data);
                            if (nLen < 0) {
                            } else if (nLen != 0) {
                                cla.vtngrep.dataAvailable(data);          //debug
                                cla.ngrepRx.sshRx(cla.vtngrep.incha);     //debug
                            } else {
                            }
                        }

                    } catch (IOException ex) {
                    }
                }
                Lib.thSleep(10);
                if (cla.ngreprxTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}

class IctrxTd extends Thread {

    SipPhone cla;

    IctrxTd(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void run() { // override Thread's run()
        //Test cla=Test.thisCla;
        for (;;) {
            if (cla.ictrxTd_run_f == 1) {
                if (cla.sshIct != null && cla.sshIct.connect_f == 1) {
                    try {
                        if (cla.sshIct.inStrm.available() > 0) {
                            byte[] data = new byte[cla.sshIct.inStrm.available()];
                            int nLen = cla.sshIct.inStrm.read(data);
                            if (nLen < 0) {
                            } else if (nLen != 0) {
                                cla.vtict.dataAvailable(data);          //debug
                                cla.ictRx.sshRx(cla.vtict.incha);     //debug
                                cla.ictPreData = cla.vtict.incha;
                            } else {
                            }
                        }

                    } catch (IOException ex) {
                    }
                }
                Lib.thSleep(10);
                if (cla.ictrxTd_destroy_f == 1) {
                    break;
                }
            }
        }
    }
}


/*
class SipPhoneTm1 implements ActionListener {

    String str;
    SipPhone cla;

    SipPhoneTm1(SipPhone owner) {
        cla = owner;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        if (cla.sipStatus == 3) {
            if (--cla.status_tim < 0) {
                cla.status_tim = 0;
                Date dNow = new Date();
                //SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss");
                cla.status_str = ft.format(dNow);
            }

        }

    }

}
 */
// unit =20ms
class SipPhoneTm1 extends TimerTask {

    String str;
    SipPhone cla;
    int chkParaSetTime = 0;
    String preParaSetTime = "";

    SipPhoneTm1(SipPhone owner) {
        cla = owner;
    }

    @Override
    //int sipStatus = 0;         //0 no raspberryPi,1:raspberry pi ready,2:linphonec load,3:pbx registed,4:ring,5:connect 
    //int connected_cnt = 0;      //0:no connect 1:call to 2:call from 3:connected
    public void run() {
        try {
            if (GB.webSocketAddr == null) {
                GB.webSocketAddr = GB.realIpAddress;
                KvWebSocketServer.serverStart();
            }
        if (cla.sipData.lineStaA[0] == 3 || cla.sipData.lineStaA[1] == 3) {
        }
        else{
            cla.piIoOut &= 0xfffe;
        }
            
            if ((cla.piIoOut & 1) == 0) {
                cla.pttOnTime = 0;
            } else {
                cla.pttOnTime++;
                if (cla.pttOnTime > 3000) {
                    cla.piIoOut &= 0xfffe;
                }
            }
                

            int phoneType = (int) GB.paraSetMap.get("phoneType");
            if (phoneType == 1) {//roip
                if ((cla.piIoStatus0 & 0x0100) == 0) {
                    if (cla.roipCor_f == 1) {//high to low
                        cla.roipCorTim = 0;
                    }
                    cla.roipCor_f = 0;
                } else {
                    if (cla.roipCor_f == 0) {//low to high
                        cla.roipCorCnt++;
                        cla.roipCorTim = 0;
                    }
                    cla.roipCor_f = 1;
                }
                cla.roipCorTim++;
                if (cla.roipCorTim == 50) {
                    int roipDialPushCount = (int) GB.paraSetMap.get("roipDialPushCount");
                    int roipCutPushCount = (int) GB.paraSetMap.get("roipCutPushCount");
                    int roipCorCnt = cla.roipCorCnt;
                    cla.roipCorCnt = 0;
                    if (roipDialPushCount == roipCorCnt) {
                        String roipDialNumber = GB.paraSetMap.get("roipDialNumber").toString();
                        cla.sipAct("call", new String[]{roipDialNumber});
                    }
                    if (roipCutPushCount == roipCorCnt) {
                        cla.hangOnPrg();
                    }
                }
            }

            chkParaSetTime++;
            if (chkParaSetTime >= 50) {
                chkParaSetTime = 0;
                Path file = Paths.get(GB.paraSetPath);
                BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                String nowParaSetTime = attr.lastModifiedTime().toString();
                if (!GB.preParaSetTime.equals(nowParaSetTime)) {
                    System.out.println("paraSet change");
                    ArrayList<String> chgA = App.checkParaSet();
                    App.loadParaSet();
                    GB.preParaSetTime = nowParaSetTime;
                    int setNet = 0;
                    int setTwinkle = 0;
                    int setNtp = 0;

                    for (int i = 0; i < chgA.size(); i++) {
                        String chgKey = chgA.get(i);
                        switch (chgKey) {
                            case "systemIpAddress":
                                setNet = 1;
                                setTwinkle = 1;
                                break;
                            case "systemNetMask":
                                setNet = 1;
                                setTwinkle = 1;
                                break;
                            case "systemGateWay":
                                setNet = 1;
                                break;
                            case "sipName":
                                setTwinkle = 1;
                                break;
                            case "sipNumber":
                                setTwinkle = 1;
                                break;
                            case "sipServerAddress":
                                setTwinkle = 1;
                                break;
                            case "sipServerPassword":
                                setTwinkle = 1;
                                break;
                            case "ntpServerAddress":
                                setNtp = 1;
                                break;
                            case "setAllCnt":
                                setNtp = 1;
                                setNet = 1;
                                break;

                        }
                    }

                    String systemIp = GB.paraSetMap.get("systemIpAddress").toString();
                    System.out.println("write sysIp=" + systemIp);
                    if (!systemIp.equals(GB.realIpAddress)) {
                        setNet = 1;
                    }
                    if (setNtp == 1) {
                        String ntpIp = GB.paraSetMap.get("ntpServerAddress").toString();
                        Lib.wrNtp(ntpIp);

                    }
                    if (setNet == 1) {
                        System.out.println("setNet=1");
                        String sysIp = GB.paraSetMap.get("systemIpAddress").toString();
                        String sysMask = GB.paraSetMap.get("systemNetMask").toString();
                        String sysGateWay = GB.paraSetMap.get("systemGateWay").toString();
                        Lib.wrInterfaces(sysIp, sysMask, sysGateWay);
                        System.out.println("write sysIp=" + sysIp);
                        cla.sshWriteShl("sudo reboot \n");//<<debug

                    }
                    if (setTwinkle == 1) {
                        cla.resetTwinkle();
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (cla.byeDelayTime > 0) {
            cla.byeDelayTime--;
        }
        if (cla.referTime > 0) {
            cla.referTime--;
        }

        if (cla.hangonWaitTime > 0) {
            cla.hangonWaitTime--;
        }

        if (cla.f4WaitTime > 0) {
            cla.f4WaitTime--;
        }
        cla.vrVolume();

        if (cla.sipData.lineStaA[0] == 0) {
            if (cla.sipData.handStaA[0] > 0) {
                cla.sipData.handTimeA[0] += 1;
                if (cla.sipData.handTimeA[0] == (50 * 10)) {
                    cla.sipData.handStaA[0] = 0;
                    if (cla.sipData.nowLine == 0 && cla.sipData.lineStaA[1] > 0) {
                        cla.sipAct("line2", null);
                    }
                    cla.txShellEsc();
                }
            }
        }

        if (cla.sipData.lineStaA[1] == 0) {
            if (cla.sipData.handStaA[1] > 0) {
                cla.sipData.handTimeA[1] += 1;
                if (cla.sipData.handTimeA[1] == (50 * 10)) {
                    cla.sipData.handStaA[1] = 0;
                    if (cla.sipData.nowLine == 1) {
                        cla.sipAct("line1", null);
                    }
                    cla.txShellEsc();
                }
            }
        }

        if (cla.shutDown_cnt > 0) {
            cla.shutDown_cnt--;
            if (cla.set_local_ip_cnt > 9 && cla.set_switch_ip_cnt > 9) {
                cla.shutDown_cnt = 0;
            }
            if (cla.shutDown_cnt == 0) {
                Lib.exe("sudo shutdown -r +0");
                return;
            }
        }

        //public int[] lineStaA = new int[]{0, 0};     //0:ready, 1: ring out, 2:ring in, 3:connect, 4:hold 
        if ((cla.sipData.lineStaA[0] | cla.sipData.lineStaA[1]) != 0) {
            cla.auto_register_tim = 0;
        }

        int autoRegistTime = (int) GB.paraSetMap.get("autoRegistTime");
        if (cla.sipData.phoneSta <= 2) {
            autoRegistTime = 10 * 50;
        }
        if (++cla.auto_register_tim >= (autoRegistTime * 50)) {
            cla.reRegister();
        }

        if (!cla.laterCall.equals("")) {
            if (cla.laterCall_tim != 0) {
                cla.laterCall_tim--;
                if (cla.laterCall_tim == 50) {
                    //cla.hangOnPrg();
                }
                if (cla.laterCall_tim == 0) {
                    if (cla.handStatus_pre == 1) {
                        cla.earPhoneAct();
                    }
                    if (cla.handStatus_pre == 2) {
                        cla.speakerOn();
                    }
                    cla.callStr(cla.laterCall);
                    cla.laterCall = "";
                    /*
                    cla.callto = cla.laterCall;
                    cla.callfrom = "";
                    //cla.sipData.status = "撥打 < " + cla.callto + " >";
                    cla.sipData.status = "響鈴....";
                    cla.sipData.action = "撥打 " + cla.callto;
                    cla.sipData.connectName = cla.laterCall;
                    cla.sipData.connectNo = cla.laterCall;
                    cla.status_tim = 100;
                    cla.sipData.connectSta = 1;
                    cla.holdRelease_tim = 0;
                    cla.sipData.phoneSta = 5;
                     */

                }
            }

        }

        /*    
        if (cla.sipStatus == 5) {
            if (++cla.holdRelease_tim > (50 * 300)) {
                cla.holdRelease_tim = 0;
                cla.sshWriteSip("hold\n");  //<<debug
                cla.sshWriteSip("retrieve\n");  //<<debug
            }
        }
         */
        if (++cla.ictCommandTim > 50) {
            cla.ictCommandTim = 0;
            if (!cla.ictCommandStr.equals("")) {
                cla.sshWriteIct(cla.ictCommandStr);
                cla.ictCommandStr = "";
            }
        }
        if (--cla.sipCommandTim == 0) {
            if (!cla.sipCommandStr.equals("")) {
                cla.sshWriteSip(cla.sipCommandStr);
                cla.sipCommandStr = "";
            }
        }

        if (++cla.broadcast_tim > 100) {
            cla.broadcast_f = 0;
        }
        int lineSta = cla.sipData.lineStaA[cla.sipData.nowLine];////0:ready, 1: ring out, 2:ring in, 3:connect, 4:hold 
        if (lineSta != 2) {
            cla.auto_answer_tim = 0;
        }
        if (lineSta == 2) {
            int autoAnswer = (int) GB.paraSetMap.get("autoAnswer");
            int autoAnswerWaitTime = (int) GB.paraSetMap.get("autoAnswerWaitTime");
            if (autoAnswer == 1) {
                if (cla.auto_answer_tim == autoAnswerWaitTime) {
                    cla.speakerAct();
                    cla.broadcast_f = 0;
                }
                cla.auto_answer_tim++;
            }
            if (cla.broadcast_f == 1) {
                cla.broadcast_f = 0;
                for (int i = 0; i < GB.icsPhnos_amt; i++) {
                    if (GB.icsPhnos[i].equals(cla.callfrom)) {
                        cla.speakerOn();
                    }
                }
                cla.auto_answer_tim = autoAnswerWaitTime;

            }
        }
        if (++cla.keypad_tim >= 200) {
            cla.keypad_on_f = 0;
            cla.keypad_str = "";
        }
        if (++cla.setting_tim >= 200) {
            cla.setting_on_f = 0;
            cla.preno_cnt = 0;
            cla.setting_str = "";

        }

        if (cla.sipData.phoneSta >= 3) {
            if (--cla.status_tim < 0) {
                cla.status_tim = 0;
                if (lineSta < 3) {
                    Date dNow = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
                    //ft.setTimeZone(TimeZone.getTimeZone("UTC"));
                    cla.sipData.status = ft.format(dNow);
                    if (cla.sipData.reDirection_f == 1) {
                        cla.sipData.status = "電話已轉接至 " + cla.sipData.reDirectNumber;
                    }
                    cla.sipData.action = cla.sipData.sipName + "<" + cla.sipData.sipNo + "> Ready";
                } else {
                    Date dNow = new Date();
                    Date passT = new Date(dNow.getTime() - cla.sipData.lineConnectTimeA[cla.sipData.nowLine] - 3600000 * 8);
                    String nameStr = cla.sipData.lineNameA[cla.sipData.nowLine];
                    String noStr = cla.sipData.lineNoA[cla.sipData.nowLine];
                    if (--cla.status_tim < 0) {
                        cla.status_tim = 0;
                        cla.sipData.status = " 連線到 " + nameStr;
                        cla.sipData.status += " <" + noStr + ">";
                        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
                        cla.sipData.action = ft.format(passT);
                    }

                }
            }

        }

        /*
        if (cla.sipData.phoneSta == 3) {
            if (--cla.status_tim < 0) {
                cla.status_tim = 0;
                Date dNow = new Date();
                //SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
                SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
                cla.sipData.status = ft.format(dNow);
                cla.sipData.action = "Ready <" + GB.phone_no + ">";
            }

        }
         */
 /*
        if (cla.sipData.phoneSta >= 4 && cla.sipData.connectSta == 3) {
            Date dNow = new Date();
            Date passT = new Date(dNow.getTime() - cla.connected_tim - 3600000 * 8);

            String tmpStr = cla.sipData.connectName;

            if (cla.sipData.nowLine == 0) {
                if (--cla.status_tim < 0) {
                    cla.status_tim = 0;
                    cla.sipData.status = " 連線到 " + tmpStr;
                    cla.sipData.status += " <" + cla.sipData.connectNo + ">";
                    SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
                    cla.sipData.action = ft.format(passT);
                }
            }

        }
         */
    }

}

//at PhoneCs.java
abstract class NgrepRx {

    public abstract void sshRx(String str);
}

abstract class SipPhoneRx {

    public abstract void sshRx(String str);
}

abstract class ShellRx {

    public abstract void sshRx(String str);
}

abstract class IctRx {

    public abstract void sshRx(String str);
}

class SipData {

    public String sipName = "";
    public String sipNo = "";
    public int nowLine = 0;
    public int reDirection_f = 0;
    public String reDirectNumber = "";
    public int phoneSta = 0;  //0 no raspberryPi,1:raspberry pi ready,2:linphonec load,3:pbx registed
    public String status = "";
    public String action = "";
    public int[] lineFlagA = new int[]{0, 0};   //hold,mute,dtmf
    public int[] lineStaA = new int[]{0, 0};     //0:ready, 1: ring out, 2:ring in, 3:connect, 4:hold 
    public int[] handStaA = new int[]{0, 0};     //0:ready, 1: earphone, 2:epeaker 

    public int[] handTimeA = new int[]{0, 0};     //0:ready, 1: earphone, 2:epeaker 
    public String[] lineNoA = new String[]{"", ""};
    public String[] lineNameA = new String[]{"", ""};
    public String[] lineMessageA = new String[]{"", ""};
    public long[] lineConnectTimeA = new long[]{0, 0};

    public String sipServerIp = "";

    void ready() {
        nowLine = 0;
        lineFlagA[0] = 0;
        lineFlagA[1] = 0;
        lineStaA[0] = 0;
        lineStaA[0] = 0;
        lineNoA[0] = "";
        lineNoA[1] = "";
        lineNameA[0] = "";
        lineNameA[1] = "";
    }

}


class TrxPack {

    int lenLim = 2000;
    int format = 0xf0;
    int amt = 0;
    int idBase = 0;
    int[] txLen;
    byte[][] txData;
    int txDataPt = 0;
    int nowPack = 0;

    TrxPack(int _amt, int _idBase) {
        amt = _amt;
        txLen = new int[amt];
        txData = new byte[amt][];
        for (int i = 0; i < amt; i++) {
            txData[i] = new byte[4096];
        }
    }

    void loadStart() {
        txDataPt = 0;
    }

    void setTxDataPt(int pt) {
        txDataPt = pt;
    }

    public void loadWord(int ib) {
        txData[nowPack][txDataPt++] = (byte) (ib & 255);
        txData[nowPack][txDataPt++] = (byte) ((ib >> 8) & 255);

    }

    public void loadInt(int ib) {
        txData[nowPack][txDataPt++] = (byte) (ib & 255);
        txData[nowPack][txDataPt++] = (byte) ((ib >> 8) & 255);
        txData[nowPack][txDataPt++] = (byte) ((ib >> 16) & 255);
        txData[nowPack][txDataPt++] = (byte) ((ib >> 24) & 255);
    }

    public void loadByte(int ib) {
        txData[nowPack][txDataPt++] = (byte) (ib & 255);
    }

}
