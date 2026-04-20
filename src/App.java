import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class App {

    public static App scla;// = new Base3();
    public static final Logger log = Logger.getLogger(App.class);

    App() {
        App.scla = this;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=====================================================================");
        System.out.println(GB.appName + " process start....");
        System.out.println("=====================================================================");
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" + currentDir);
        System.out.println("Java Version: " + System.getProperty("java.version"));
        String fileName = GB.logSetPath + "/log4j.properties";
        String wstr = "";
        wstr += "\nlog4j.rootLogger=DEBUG, consoleout, fileout";
        wstr += "\nlog4j.appender.consoleout=org.apache.log4j.ConsoleAppender";
        wstr += "\nlog4j.appender.consoleout.Target=System.out";
        wstr += "\nlog4j.appender.consoleout.layout=org.apache.log4j.PatternLayout";
        wstr += "\nlog4j.appender.consoleout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %p %c:%L - %m%n";
        wstr += "\nlog4j.appender.fileout=org.apache.log4j.RollingFileAppender";
        wstr += "\nlog4j.appender.fileout.File=" + GB.logPath + "/" + GB.appName + ".log";
        wstr += "\nlog4j.appender.fileout.MaxFileSize=5MB";
        wstr += "\nlog4j.appender.fileout.MaxBackupIndex=10";
        wstr += "\nlog4j.appender.fileout.layout=org.apache.log4j.PatternLayout";
        wstr += "\nlog4j.appender.fileout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n";

        BufferedWriter outf = null;
        try {
            outf = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), "UTF-8"));
            outf.write(wstr);
            outf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PropertyConfigurator.configure(fileName);
        // ===============================================
        GB.initGB();
        loadParaSet();
        Path file = Paths.get(GB.paraSetPath);
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        GB.preParaSetTime = attr.lastModifiedTime().toString();

        netInf(0);

        // log.info(GB.appName + " process start ...");
        // log.warn("test warnning");
        // log.error("test error");
        App.act(0);// read setdata.xml
        App.act(1);// read setdata.db
        String str;
        while (true) {
            Scanner input = new Scanner(System.in);
            str = input.nextLine();
            if (str.equals("exit")) {
                System.exit(0);
                break;
            }
            input.close();
        }

    }

    public static void act(int index) {
        System.out.println("Action " + index);
        switch (index) {
            case 0: // read setdata.xml to GB.paraName[],GB.paraValue[],
                try {
                    FileReader reader = new FileReader(GB.setdataXml);
                    BufferedReader br = new BufferedReader(reader);
                    String line;
                    String paraN;
                    String paraV;
                    App.clrPara();
                    while ((line = br.readLine()) != null) {
                        String sline = line.trim();
                        if (sline.length() == 0) {
                            continue;
                        }
                        if (sline.charAt(0) != '[') {
                            continue;
                        }
                        if (Lib.search(sline, "[", "]") == 1) {
                            paraN = Lib.retstr.trim();
                            if (Lib.search(sline, "<", ">") == 1) {
                                paraV = Lib.retstr;
                                byte[] bb;
                                bb = paraV.getBytes("UTF-8");
                                paraV = "";
                                for (int h = 0; h < bb.length; h++) {
                                    paraV += (char) bb[h];
                                }
                                App.newPara(paraN, paraV);
                            }
                        }
                    }
                    br.close();
                } catch (IOException e2) {
                    System.out.println(e2);
                }
                break;
            case 1:
                App.readDatabase();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
            case 16:
                break;
            case 17:
                break;

        }

    }

    public static void readDatabase() {
        Connection c = null;
        String dbPath = GB.setdataDb;
        try {
            System.out.println("dbPath: " + dbPath);
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            // ==============================================
            java.sql.Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM paraTable;");
            String paraName = "", paraValue = "";
            while (rs.next()) {
                paraName = rs.getString("paraName");
                paraValue = rs.getString("paraValue");
                App.editNewPara(paraName, paraValue);

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static boolean checkDb(String paraName) {
        String dbPath = GB.setdataDb;
        Connection con = null;
        String pName;
        String sbuf;
        boolean ret = false;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            con.setAutoCommit(false);
            // ==============================================
            java.sql.Statement stmt = con.createStatement();
            sbuf = "SELECT * FROM paraTable;";
            ResultSet rs = stmt.executeQuery(sbuf);
            while (rs.next()) {
                pName = rs.getString("paraName");
                if (pName.equals(paraName)) {
                    ret = true;
                    break;
                }
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return ret;
    }

    public static int editDb(String paraName, String paraValue) {
        String dbPath = GB.setdataDb;
        Connection con;
        String sql;
        int chgLine = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            con.setAutoCommit(false);
            java.sql.Statement stmt = con.createStatement();
            // UPDATE paraTable set
            sql = "UPDATE paraTable set paraValue = \"";
            sql = sql + paraValue;
            sql = sql + "\" where paraName=\"";
            sql = sql + paraName;
            sql = sql + "\";";
            chgLine = stmt.executeUpdate(sql);
            con.commit();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return chgLine;
    }

    public static int insertDb(String paraName, String paraValue) {
        String dbPath = GB.setdataDb;
        Connection con = null;
        String sql;
        int chgLine = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            con.setAutoCommit(false);
            java.sql.Statement stmt = con.createStatement();
            sql = "INSERT INTO paraTable VALUES ('";
            sql += paraName;
            sql += "','";
            sql += paraValue;
            sql += "');";
            chgLine = stmt.executeUpdate(sql);
            con.commit();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return chgLine;
    }

    static void clrPara() {
        for (int i = 0; i < GB.paraLen; i++) {
            GB.paraName[i] = null;
            GB.paraValue[i] = null;
            GB.paraLen = 0;
        }
    }

    static int newPara(String name, String value) {
        if (GB.paraLen >= GB.MAX_PARA_LEN) {
            return 0;
        }
        GB.paraName[GB.paraLen] = name;
        GB.paraValue[GB.paraLen] = value;
        GB.paraLen++;
        return 1;
    }

    static int editPara(String name, String value) {
        int i;
        for (i = 0; i < GB.paraLen; i++) {
            if (GB.paraName[i].equals(name)) {
                GB.paraValue[i] = value;
                return 1;
            }
        }
        return 0;
    }

    static int editNewPara(String name, String value) {
        if (editPara(name, value) == 0) {
            return newPara(name, value);
        }
        return 1;
    }

    static String getPara(String name) {
        int i;
        for (i = 0; i < GB.paraLen; i++) {
            if (GB.paraName[i].equals(name)) {
                return GB.paraValue[i];
            }
        }
        return null;
    }

    static int deletePara(String name) {
        int i;
        for (i = 0; i < GB.paraLen; i++) {
            if (GB.paraName[i].equals(name)) {
                i++;
                for (; i < GB.paraLen; i++) {
                    GB.paraName[i - 1] = GB.paraName[i];
                    GB.paraValue[i - 1] = GB.paraValue[i];
                }
                GB.paraLen--;
                return 1;
            }
        }
        return 0;
    }

    public static void loadParaSet() {
        try {
            String content = Lib.readFile(GB.paraSetPath);
            GB.paraSetMap.clear();
            JSONObject jsPara = new JSONObject(content);
            @SuppressWarnings("unchecked")
            Iterator<String> it = jsPara.keys();
            while (it.hasNext()) {
                String key = it.next();
                GB.paraSetMap.put(key, jsPara.get(key));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void saveParaSet() {
        if (GB.paraSaveMap.size() == 0)
            return;
        try {
            Gson gson = new Gson();
            String content = Lib.readFile(GB.paraSetFullName);
            JsonObject jsPara = JsonParser.parseString(content).getAsJsonObject();
            // =======================================================================
            String[] strA;
            String keyName = "";
            // String dataType="str";
            for (String key : GB.paraSaveMap.keySet()) {
                strA = key.split("~");
                keyName = strA[0];
                // if(strA.length==2){
                // dataType=strA[1];
                // }
                Object value = GB.paraSaveMap.get(key);
                jsPara.add(keyName, gson.toJsonTree(value));
                System.out.println("Save paraSet: " + keyName + "  " + value);
            }
            GB.paraSaveMap.clear();
            // =======================================================================
            content = jsPara.toString();
            BufferedWriter outf = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(GB.paraSetFullName), "UTF-8"));
            try {
                outf.write(content);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                outf.close();
                // Path file = Paths.get(GB.paraSetPath);
                // BasicFileAttributes attr = Files.readAttributes(file,
                // BasicFileAttributes.class);
                // GB.preParaSetTime = attr.lastModifiedTime().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static ArrayList<String> checkParaSet() {
        Map<String, Object> paraCheckMap = new HashMap<>();
        ArrayList<String> editA=new ArrayList<String>();             
        try {
            String content = Lib.readFile(GB.paraSetPath);
            paraCheckMap.clear();
            JSONObject jsPara = new JSONObject(content);
            @SuppressWarnings("unchecked")
            Iterator<String> it = jsPara.keys();
            while (it.hasNext()) {
                String key = it.next();
                paraCheckMap.put(key, jsPara.get(key));
            }
            
            for(String key:GB.paraSetMap.keySet()){
                Object nobj= paraCheckMap.get(key);
                Object oobj= GB.paraSetMap.get(key);
                String[] strA=key.split("~");
                if(strA[0].equals("dsc"))
                    continue;
                if(oobj!= null && nobj !=null ){
                    if(!oobj.equals(nobj)){
                        String className=oobj.getClass().getSimpleName();
                        if(className.equals("JSONArray"))
                            continue;
                        editA.add(key);
                    }
                }
            }
            
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return editA;
        
        
        

    }

    public static String netInf(int ww) {
        InetAddress ip;
        int i;
        int sti;
        String str = null;
        String localIp = null;
        GB.realIpAddress = Lib.rdInterfaces("address");
        GB.realNetMask = Lib.rdInterfaces("netmask");
        GB.realGateWay = Lib.rdInterfaces("gateway");

        System.out.println("*** Network Set **************************************************");
        System.out.println("IP address : " + GB.realIpAddress);
        System.out.println("IP mask: " + GB.realNetMask);
        System.out.println("Gateway address: " + GB.realGateWay);

        try {
            int yes_f = 0;
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            System.out.println("*** Exist Network **************************************************");
            while (e.hasMoreElements()) {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress ia = ee.nextElement();
                    str = ia.getHostAddress();
                    System.out.println(str);
                    sti = str.indexOf("192.168.");
                    if (sti >= 0) {
                        localIp = str;
                        yes_f = 1;
                        break;
                    }
                }
                if (yes_f == 1)
                    break;
            }

            if (localIp == null) {
                return str;
            }
            // ip = InetAddress.getLocalHost();
            // ip = InetAddress.getByName("192.168.0.57");
            ip = InetAddress.getByName(localIp);
            System.out.println("Current IP address : " + ip.getHostAddress());
            GB.realIpAddress = ip.getHostAddress();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            // if(network==null)
            // return;
            byte[] mac = network.getHardwareAddress();

            System.out.println("*** Current Network **************************************************");

            StringBuilder sb = new StringBuilder();
            for (i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println("IP:" + GB.realIpAddress + ", MAC: " + sb.toString());

            str = "";
            if (mac.length < 6) {
                GB.syssec_f = 1;
            } else {
                GB.macStr = "" + (mac[0] & 255);
                GB.macStr += "." + (mac[1] & 255);
                GB.macStr += "." + (mac[2] & 255);
                GB.macStr += "." + (mac[3] & 255);
                GB.macStr += "." + (mac[4] & 255);
                GB.macStr += "." + (mac[5] & 255);
                // str = encSyssec(mac);
            }
            if (GB.syssec.equals(str)) {
                GB.syssec_f = 1;
            } else {
                if (ww != 0) {
                    // editNewDb("syssec", str);
                    GB.syssec = str;

                }
            }

        } catch (Exception ex) {
        }
        return str;
    }

}
