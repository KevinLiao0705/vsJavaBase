
import java.util.HashMap;
import java.util.Map;

public class GB {
    static int emuMeterStatus_f = 0;
    static int emuPowerValue_f = 0;
    static int emuSspaValue_f = 0;
    static int debug_f=0;
    static String version = "1.2";
    static int emulate = 0;
    public static String webRootPath = "./";
    public static final int MAX_PARA_LEN = 8192;
    static int lang = 1;//0:english 1:chinese 
    static String paraSetPath = "./paraSet.json";
 
    public static String logSetPath = ".";
    public static String logPath = "./log";
    public static String appName = "MyAppName";
    public static String webSocketAddr = "";
    public static int webSocketPort = 8899;
    public static String winParaSetFullName = "e:/kevin/myCode/webSet/syncSet/paraSet.json";
    public static String linuxParaSetFullName = "/home/admintx/syncSetExe/paraSet.json";
    public static String paraSetFullName ="";
    
    public static String laPath="C:/Program Files/Logic";
    public static String laAppName="Logic.exe";
    public static String chromePath="C:/Program Files/Google/Chrome/Application";
    public static String chromeAppName="chrome.exe";
    public static String chromeAddress="http://localhost/webBase/index.jsp";
    
    //=====================================================
    static int processInx = 1;   //0:console sipph,1:desktop sipph,2:PhoneUi,3:Phone6in1 
    static int osInx = 1;     //0:windows 1://linux    
    static String asteriskConfPath = "./asteridkConfPath";
    static String osName="win";

    //===============================
    static String realIpAddress = ""; 
    static String realNetMask = "";
    static String realGateWay = "";


    static int cursorOff_f = 0;
    //================================================
    static String[] icsPhnos = new String[256];
    static int icsPhnos_amt = 0;



    static int fullScr_f = 0;
    static int frameOn_f = 0;
    static int syssec_f = 1;
    static int syssec_xor = 0;
    static String macStr;
    //================================================
    static int winFrame_bm = 30;
    static int winFrame_wm = 8;
    static int winFrame_hm = 38;
    static String syssec = "123-125-222-456-111-123";
    static String web_password = "1234";

    static int appId = 0;
    //================================================
    //sipmd ui use
    //================================================

    static String netName = "eth0";
    static String maskStr = "255.255.0.0";
    static String gatewayStr = "192.168.0.1";
    static String startTime = "";

    //
    //sipmd_ui tx "sipphone information" to sipui_ui over socket
    static String sipui_ui_ip = "192.168.0.181";
    static int sipui_ui_port = 1336;
    //================================================
    //sipui ui use
    //================================================exit

    //sipui_ui tx "command data" to sipmd_ui over socket
    static String sipmd_ui_ip = "192.168.0.45";
    static int sipmd_ui_port = 1236;

    //==============================================================================
    //windows debug use===================================================================
    static String setdataXml = "./setdata.xml";
    static String setdataDb = "./setdata.db";
    static String interfacesPath = "./interfaces";
    //==============================================================================
    static String real_ip_str = "";
    static String set_ip_str = "";
    static String set_ipmask_str = "";
    static String set_gateway_str = "";
    static byte[] realIp = new byte[]{0, 0, 0, 0};

    //==============================================================================
    static String[] paraName = new String[MAX_PARA_LEN];
    static String[] paraValue = new String[MAX_PARA_LEN];
    static int paraLen = 0;
    //==============================================================================
    static String ret_str;
    static int action_inx = 0;
    static int action_step = 0;
    static int action_tim = 0;
    static String preParaSetTime = "";


    static int pbxConOn_f = 0;
    static int sipToUiSocketPort = 1336;
    static String alueIcsUserName = "mtcl";
    static String alueIcsPassword = "mtcl";
    static String twinkleCfgPath = "./twinkle.cfg";
    public static int earMicSens = 6;
    public static int phsetMicSens = 6;
    static int ngrepOn_f = 0;
    static String twinkleDeviceIp = "127.0.0.1";
    static String twinkleDeviceName = "pi";
    static String twinkleDevicePassword = "123456789";
    static String ntpConfPathName = "/etc/systemd/timesyncd.conf";


    //==================================================================
    public static Map<String, Object> paraSetMap = new HashMap<>();
    public static HashMap<String, ConnectCla> connectMap = new HashMap<>();
    public static HashMap<String, Object> paraSaveMap = new HashMap<>();

    //=================================================================
    static void initGB() {
        Lib.getOs();
        
        if (GB.osName.equals("win")) {
            GB.processInx = 0; //0:consoleMain, 
            GB.setdataXml = "./setdata.xml";
            GB.setdataDb = "./setdata.db";
            GB.interfacesPath = "./interfaces";
            GB.paraSetFullName = GB.winParaSetFullName;
            GB.logSetPath = ".";
            GB.logPath ="e:/kevin/myCode/webServletBase/web/log";
            GB.laPath="C:/Program Files/Logic";
            GB.laAppName="Logic.exe";
            GB.chromePath="C:/Program Files/Google/Chrome/Application";
            GB.chromeAppName="chrome.exe";
            GB.chromeAddress="http://localhost:8080/webBase/index.jsp";
            //GB.logPath = "./log";
        }
        if (GB.osName.equals("linux")) {        
            GB.processInx = 0; //0:consoleMain
            GB.setdataXml = "./setdata.xml";
            GB.setdataDb = "./setdata.db";
            GB.interfacesPath = "/etc/network/interfaces";
            GB.paraSetFullName = GB.linuxParaSetFullName;
            GB.logSetPath = "/home/admintx/syncSetExe";
            GB.logPath = "/home/admintx/syncSetExe/log";
            GB.laPath="/home/admintx/syncSetExe";
            GB.laAppName="la.sh";
            GB.chromePath="/usr/bin";
            GB.chromeAppName="google-chrome-stable --disable-session-crashed-bubble --no-sandbox";
            GB.chromeAddress="http://127.0.0.1";
            

        }
        //=============================================================
    }


}
