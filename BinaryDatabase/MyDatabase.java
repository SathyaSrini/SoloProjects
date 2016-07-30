
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDatabase
{

    public static final String id = "id";
    public static final String company = "company";
    public static final String drug_id = "drug_id";
    public static final String trials = "trials";
    public static final String patients = "patients";
    public static final String dosage_mg = "dosage_mg";
    public static final String reading = "reading";
    public static final String double_blind = "double_blind";
    public static final String controlled_study = "controlled_study";
    public static final String govt_funded = "govt_funded";
    public static final String fda_approved = "fda_approved";

    public static final String EqualTo = "=";
    public static final String lessThan = "<";
    public static  final String lessThanEqualTo = "<=";
    public static  final String greaterThan = ">";
    public static final String greaterThanEqualTo = ">=";

    public static HashMap<Integer, Long> idMap;
    public static HashMap<String, String> compMap;
    public static HashMap<String, String> drgIdMap;
    public static HashMap<Integer, String> trlsMap;
    public static HashMap<Integer, String> ptntsMap;
    public static HashMap<Integer, String> dsageMap;
    public static HashMap<Float, String> rdingMap;
    public static HashMap<Boolean, String> dblBindMap;
    public static HashMap<Boolean, String> cntrolStdyMap;
    public static HashMap<Boolean, String> govtMap;
    public static HashMap<Boolean, String> fdaMap;

    static String[] indices = {".id.ndx", ".company.ndx", ".drug_id.ndx", ".trials.ndx", ".patients.ndx",
        ".dosage_mg.ndx", ".reading.ndx", ".double_blind.ndx", ".controlled_study.ndx",
        ".govt_funded.ndx", ".fda_approved.ndx"};

    static ArrayList<String> columnArrayList = new ArrayList<>();
    static String tblNme = "PHARMA_TRIALS_1000B";

    final static byte dleteByte = 16;
    final static byte dblBindByte = 8;
    final static byte cntrolStdyByte = 4;
    final static byte gvtByte = 2;
    final static byte fdaByte = 1;

    /////////////////// HELPER METHODS /////////////////////////////////////////
    public static Object readMap(String fileName, Object objMap) {
        File file;
        FileInputStream fileInStream;
        ObjectInputStream objInStream;
        
        file = null;
        fileInStream = null;
        objInStream = null;

        try {
            file = new File(fileName);
            fileInStream = new FileInputStream(file);
            objInStream = new ObjectInputStream(fileInStream);

            objMap = objInStream.readObject();

            objInStream.close();
            fileInStream.close();

        } catch (IOException | ClassNotFoundException e) {
        }
        return objMap;
    }

    public static void writeMaps() {
        outputMapsToFile(tblNme + indices[0], idMap);
        outputMapsToFile(tblNme + indices[1], compMap);
        outputMapsToFile(tblNme + indices[2], drgIdMap);
        outputMapsToFile(tblNme + indices[3], trlsMap);
        outputMapsToFile(tblNme + indices[4], ptntsMap);
        outputMapsToFile(tblNme + indices[5], dsageMap);
        outputMapsToFile(tblNme + indices[6], rdingMap);
        outputMapsToFile(tblNme + indices[7], dblBindMap);
        outputMapsToFile(tblNme + indices[8], cntrolStdyMap);
        outputMapsToFile(tblNme + indices[9], govtMap);
        outputMapsToFile(tblNme + indices[10], fdaMap);
    }

    public static void outputMapsToFile(String fileName, Object obj) {
        File file = null;
        FileOutputStream fOut = null;
        ObjectOutputStream oOut = null;
        try {
            file = new File(fileName);
            fOut = new FileOutputStream(file);
            oOut = new ObjectOutputStream(fOut);

            oOut.writeObject(obj);
            oOut.flush();
            oOut.close();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readData(String columnNames, String tableName, String filePointer, boolean deleteFlag) {
        RandomAccessFile rnd_File = null;
        String[] colArray;
        String[] pntr;
        long bool_ptr;
        int count;
        int id_Local;
        int comp_Len;
        byte[] comp_local;
        byte[] drugId_Byte;
        int trials_Local;
        int patients_Local;
        int dosage_Local;
        float reading_Local;
        byte bool;
        boolean doubleBlind_Local;
        boolean controlledStudy_Local;
        boolean govtFunded_Local;
        boolean fdaApproved_Local;
        
        
        count = 0;
        
        try {
            if (deleteFlag) {
                rnd_File = new RandomAccessFile(tableName + ".db", "rws");
            } else {
                rnd_File = new RandomAccessFile(tableName + ".db", "r");
            }
            if (filePointer.contains(",")) {
                pntr = filePointer.split(Pattern.quote(","));
            } else {
                pntr = new String[]{filePointer};
            }

            for (String ptr : pntr) {
                ptr = ptr.trim();
                rnd_File.seek(Long.parseLong(ptr));
                while (true) {

                    id_Local = rnd_File.readShort();
                    comp_Len = rnd_File.read();
                    comp_local = new byte[comp_Len];
                    rnd_File.read(comp_local, 0, comp_Len);
                    drugId_Byte = new byte[6];
                    rnd_File.read(drugId_Byte, 0, 6);
                    trials_Local = rnd_File.readShort();
                    patients_Local = rnd_File.readShort();
                    dosage_Local = rnd_File.readShort();
                    reading_Local = rnd_File.readFloat();
                    bool_ptr = rnd_File.getFilePointer();
                    bool = rnd_File.readByte();
                    doubleBlind_Local = false;
                    controlledStudy_Local = false;
                    govtFunded_Local = false;
                    fdaApproved_Local = false;

                    if ((bool & dblBindByte) == dblBindByte) {
                        doubleBlind_Local = true;
                    }

                    if ((bool & cntrolStdyByte) == cntrolStdyByte) {
                        controlledStudy_Local = true;
                    }

                    if ((bool & gvtByte) == gvtByte) {
                        govtFunded_Local = true;
                    }

                    if ((bool & fdaByte) == fdaByte) {
                        fdaApproved_Local = true;
                    }

                    if (!deleteFlag) {
                        if ((bool & dleteByte) != dleteByte) {

                            if (columnNames.equals("*")) {
                                System.out.println(id_Local + "," + new String(comp_local) + "," + new String(drugId_Byte) + "," + trials_Local + "," + patients_Local + "," + dosage_Local
                                        + "," + reading_Local + "," + doubleBlind_Local + "," + controlledStudy_Local + "," + govtFunded_Local + "," + fdaApproved_Local);
                            } else if (columnNames.contains(",")) {
                                colArray = columnNames.split(Pattern.quote(","));
                                for (String colName : colArray) {
                                    colName = colName.trim();
                                    switch (colName) {
                                        case id:
                                            System.out.print(id_Local);
                                            break;
                                        case company:
                                            System.out.print("," + new String(comp_local));
                                            break;
                                        case drug_id:
                                            System.out.print("," + new String(drugId_Byte));
                                            break;
                                        case trials:
                                            System.out.print("," + trials_Local);
                                            break;
                                        case patients:
                                            System.out.print("," + patients_Local);
                                            break;
                                        case dosage_mg:
                                            System.out.print("," + dosage_Local);
                                            break;
                                        case reading:
                                            System.out.print("," + reading_Local);
                                            break;
                                        case double_blind:
                                            System.out.print("," + doubleBlind_Local);
                                            break;
                                        case controlled_study:
                                            System.out.print("," + controlledStudy_Local);
                                            break;
                                        case govt_funded:
                                            System.out.print("," + govtFunded_Local);
                                            break;
                                        case fda_approved:
                                            System.out.print("," + fdaApproved_Local);
                                            break;
                                    }
                                }
                                System.out.print("\n");
                            } else {
                                columnNames = columnNames.trim();
                                switch (columnNames) {
                                    case id:
                                        System.out.print(id_Local);
                                        break;
                                    case company:
                                        System.out.print("," + new String(comp_local));
                                        break;
                                    case drug_id:
                                        System.out.print("," + new String(drugId_Byte));
                                        break;
                                    case trials:
                                        System.out.print("," + trials_Local);
                                        break;
                                    case patients:
                                        System.out.print("," + patients_Local);
                                        break;
                                    case dosage_mg:
                                        System.out.print("," + dosage_Local);
                                        break;
                                    case reading:
                                        System.out.print("," + reading_Local);
                                        break;
                                    case double_blind:
                                        System.out.print("," + doubleBlind_Local);
                                        break;
                                    case controlled_study:
                                        System.out.print("," + controlledStudy_Local);
                                        break;
                                    case govt_funded:
                                        System.out.print("," + govtFunded_Local);
                                        break;
                                    case fda_approved:
                                        System.out.print("," + fdaApproved_Local);
                                        break;
                                }
                                System.out.print("\n");
                            }
                            count += 1;
                        }
                    } else {
                        if ((bool & dleteByte) == dleteByte) {
                            System.out.println("Record not found : Already deleted");
                        } else {
                            bool = (byte) (bool | dleteByte);
                            count += 1;
                            rnd_File.seek(bool_ptr);
                            rnd_File.write(bool);
                        }
                    }
                    break;
                }
            }
            rnd_File.close();

            if (deleteFlag && count > 0) {
                System.out.println("\n" + count + " Record(s) removed");
            }
        } catch (NumberFormatException | IOException e) {
        }
    }

    public static void outputDataToFile(RandomAccessFile randomAccessFile, String[] line) throws IOException {

        long fP;
        int col0 = Integer.parseInt((line[0]).trim());
        String col1 = line[1].trim();
        String col2 = line[2].trim();
        int col3 = Integer.parseInt((line[3]).trim());
        int col4 = Integer.parseInt((line[4]).trim());
        int col5 = Integer.parseInt((line[5]).trim());
        float col6 = Float.parseFloat((line[6]).trim());
        boolean col7 = Boolean.parseBoolean((line[7]).trim());
        boolean col8 = Boolean.parseBoolean((line[8]).trim());
        boolean col9 = Boolean.parseBoolean((line[9]).trim());
        boolean col10 = Boolean.parseBoolean((line[10]).trim());
      
        @SuppressWarnings("unused")
        boolean deleted = false;
        byte setBool = 0;

        if (col7) {
            setBool = (byte) (setBool | dblBindByte);
        }

        if (col8) {
            setBool = (byte) (setBool | cntrolStdyByte);
        }

        if (col9) {
            setBool = (byte) (setBool | gvtByte);
        }

        if (col10) {
            setBool = (byte) (setBool | fdaByte);
        }

        fP = randomAccessFile.getFilePointer();

        randomAccessFile.writeShort(col0);
        randomAccessFile.write(col1.length());
        randomAccessFile.writeBytes(col1);
        randomAccessFile.writeBytes(col2);
        randomAccessFile.writeShort(col3);
        randomAccessFile.writeShort(col4);
        randomAccessFile.writeShort(col5);
        randomAccessFile.writeFloat(col6);
        randomAccessFile.write(setBool);
        
        idMap.put(col0, fP);

        col1 = col1.toLowerCase();
        if (compMap.containsKey(col1)) {
            compMap.put(col1, compMap.get(col1) + "," + String.valueOf(fP));
        } else {
            compMap.put(col1, String.valueOf(fP));
        }

        col2 = col2.toLowerCase();
        if (drgIdMap.containsKey(col2)) {
            drgIdMap.put(col2, drgIdMap.get(col2) + "," + String.valueOf(fP));
        } else {
            drgIdMap.put(col2, String.valueOf(fP));
        }

        if (trlsMap.containsKey(col3)) {
            trlsMap.put(col3, trlsMap.get(col3) + "," + String.valueOf(fP));
        } else {
            trlsMap.put(col3, String.valueOf(fP));
        }

        if (ptntsMap.containsKey(col4)) {
            ptntsMap.put(col4, ptntsMap.get(col4) + "," + String.valueOf(fP));
        } else {
            ptntsMap.put(col4, String.valueOf(fP));
        }

        if (dsageMap.containsKey(col5)) {
            dsageMap.put(col5, dsageMap.get(col5) + "," + String.valueOf(fP));
        } else {
            dsageMap.put(col5, String.valueOf(fP));
        }

        if (rdingMap.containsKey(col6)) {
            rdingMap.put(col6, rdingMap.get(col6) + "," + String.valueOf(fP));
        } else {
            rdingMap.put(col6, String.valueOf(fP));
        }

        if (dblBindMap.containsKey(col7)) {
            dblBindMap.put(col7, dblBindMap.get(col7) + "," + String.valueOf(fP));
        } else {
            dblBindMap.put(col7, String.valueOf(fP));
        }

        if (cntrolStdyMap.containsKey(col8)) {
            cntrolStdyMap.put(col8, cntrolStdyMap.get(col8) + "," + String.valueOf(fP));
        } else {
            cntrolStdyMap.put(col8, String.valueOf(fP));
        }

        if (govtMap.containsKey(col9)) {
            govtMap.put(col9, govtMap.get(col9) + "," + String.valueOf(fP));
        } else {
            govtMap.put(col9, String.valueOf(fP));
        }

        if (fdaMap.containsKey(col10)) {
            fdaMap.put(col10, fdaMap.get(col10) + "," + String.valueOf(fP));
        } else {
            fdaMap.put(col10, String.valueOf(fP));
        }
    }

    public static void getBinary(String fileName) {
        
        File file;
        CSVReader rdr;
        RandomAccessFile rand_File;
        
        idMap = new HashMap<>();
        compMap = new HashMap<>();
        drgIdMap = new HashMap<>();
        trlsMap = new HashMap<>();
        ptntsMap = new HashMap<>();
        dsageMap = new HashMap<>();
        rdingMap = new HashMap<>();
        dblBindMap = new HashMap<>();
        cntrolStdyMap = new HashMap<>();
        govtMap = new HashMap<>();
        fdaMap = new HashMap<>();
        file = null;
        rdr = null;
        rand_File = null;

        try {
            file = new File(fileName);

            rdr = new CSVReader(new FileReader(file));

            rand_File = new RandomAccessFile(tblNme + ".db", "rw");

            rdr.readNext();
            String[] line = rdr.readNext();

            while (line != null) {
                outputDataToFile(rand_File, line);

                line = rdr.readNext();
            }

            writeMaps();

            rdr.close();
            rand_File.close();
        } catch (Exception e) {
        } finally {

        }
    }

    /////////////////// HELPER METHODS /////////////////////////////////////////





    /////////////////// MAIN METHODS ///////////////////////////////////////////

    @SuppressWarnings("unchecked")
    public static void firstCreate() {
        try {
            String[] t;
            File fObj = null;
            Boolean tblExist;
            
            tblExist = new File(tblNme + ".db").exists();
            
            for (int i = 0; i < 11; i++) {
               t  = (indices[i]).split(Pattern.quote("."));
                columnArrayList.add(t[1]);
            }

            if (tblExist) {
                for (int i = 0; i < 11; i++) {
                    fObj = new File(tblNme + indices[i]);
                    tblExist = fObj.exists();
                    if (tblExist == false) {
                        break;
                    }
                }
            }

            if (tblExist) {
                Object Map = new HashMap<>();
                idMap = (HashMap<Integer, Long>) readMap(tblNme + indices[0], Map);
                compMap = (HashMap<String, String>) readMap(tblNme + indices[1], Map);
                drgIdMap = (HashMap<String, String>) readMap(tblNme + indices[2], Map);
                trlsMap = (HashMap<Integer, String>) readMap(tblNme + indices[3], Map);
                ptntsMap = (HashMap<Integer, String>) readMap(tblNme + indices[4], Map);
                dsageMap = (HashMap<Integer, String>) readMap(tblNme + indices[5], Map);
                rdingMap = (HashMap<Float, String>) readMap(tblNme + indices[6], Map);
                dblBindMap = (HashMap<Boolean, String>) readMap(tblNme + indices[7], Map);
                cntrolStdyMap = (HashMap<Boolean, String>) readMap(tblNme + indices[8], Map);
                govtMap = (HashMap<Boolean, String>) readMap(tblNme + indices[9], Map);
                fdaMap = (HashMap<Boolean, String>) readMap(tblNme + indices[10], Map);
            } else {
                System.out.println("\nPHARMA_TRIALS_1000B.csv - Not Found ; Import csv first, using 1.");
            }
        } catch (Exception e) {

        }
    }

    public static int mainMenu(BufferedReader bReader) {
        int choice = 0;
        while (true) {
            System.out.println("\n");
            System.out.println("Menu");
            System.out.println("1 - Import");
            System.out.println("2 - Query");
            System.out.println("3 - Insert");
            System.out.println("4 - Delete");
            System.out.println("5 - Exit");

            System.out.print("\nEnter option : ");
            try {
                choice = Integer.parseInt(bReader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println("Select the correct option");
                choice = 0;
            }

            if (choice == 5) {
                System.out.println("\nABORT");
                System.exit(0);
            }
            if (choice >= 1 && choice <= 5) {
                break;
            } else {
                System.out.println("Invalid option entered - Please reenter");
            }
        }

        return choice;
    }

    public static String getQuery(BufferedReader br, String type) {

        String temp_Query = "";
        String tot_Query = "";
        boolean end = false;

        try {
            do {
                System.out.print("SQL : ");
                temp_Query = br.readLine();
                if (temp_Query.indexOf(';') != -1) {
                    end = true;
                }
                tot_Query += " " + temp_Query.trim();
                tot_Query = tot_Query.trim();
            } while (!end);
        } catch (Exception e) {
        }
        return tot_Query;
    }

    public static void searchForInteger(String clnName, String tblName,String fldName, boolean notFlg, String comp, String inputVal, boolean dltFlag) {

        String fl_Ptr = "";
        int val_int = 0;
        boolean less = false;
        boolean great = false;
        boolean lessEqual = false;
        boolean greatEqual = false;
        boolean equals = false;
        float val_float = 0;

        try {
            switch (comp) {
                case EqualTo:
                    switch (fldName) {
                        case id:
                            val_int = Integer.parseInt(inputVal);
                            if (notFlg) {
                                for (Integer key : idMap.keySet()) {
                                    if (key == val_int) {
                                        continue;
                                    }

                                    fl_Ptr = String.valueOf(idMap.get(key));
                                    readData(clnName, tblName, fl_Ptr, dltFlag);

                                }
                            } else {
                                if (idMap.containsKey(val_int)) {
                                    fl_Ptr = String.valueOf(idMap.get(val_int));
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                } else {
                                    System.out.println("\nNone Found");
                                }
                            }
                            break;
                        case trials:
                            val_int = Integer.parseInt(inputVal);
                            if (notFlg) {
                                for (Integer key : trlsMap.keySet()) {
                                    if (key == val_int) {
                                        continue;
                                    }

                                    fl_Ptr = trlsMap.get(key);
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                }
                            } else {
                                if (trlsMap.containsKey(val_int)) {
                                    fl_Ptr = trlsMap.get(val_int);
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                } else {
                                    System.out.println("\nNone found");
                                }
                            }
                            break;
                        case patients:
                            val_int = Integer.parseInt(inputVal);
                            if (notFlg) {
                                for (Integer key : ptntsMap.keySet()) {
                                    if (key == val_int) {
                                        continue;
                                    }

                                    fl_Ptr = ptntsMap.get(key);
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                }
                            } else {
                                if (ptntsMap.containsKey(val_int)) {
                                    fl_Ptr = ptntsMap.get(val_int);
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                } else {
                                    System.out.println("\nNone found");
                                }
                            }
                            break;
                        case dosage_mg:
                            val_int = Integer.parseInt(inputVal);
                            if (notFlg) {
                                for (Integer key : dsageMap.keySet()) {
                                    if (key == val_int) {
                                        continue;
                                    }

                                    fl_Ptr = dsageMap.get(key);
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                }
                            } else {
                                if (dsageMap.containsKey(val_int)) {
                                    fl_Ptr = String.valueOf(dsageMap.get(val_int));
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                } else {
                                    System.out.println("\nNone found");
                                }
                            }
                            break;
                        case reading:
                            val_float = Float.parseFloat(inputVal);
                            if (notFlg) {
                                for (Float key : rdingMap.keySet()) {
                                    if (key == val_float) {
                                        continue;
                                    }

                                    fl_Ptr = rdingMap.get(key);
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                }
                            } else {
                                if (rdingMap.containsKey(val_float)) {
                                    fl_Ptr = String.valueOf(rdingMap.get(val_float));
                                    readData(clnName, tblName, fl_Ptr, dltFlag);
                                } else {
                                    System.out.println("\nNone found");
                                }
                            }
                            break;
                    }
                    equals = true;
                    break;

                case lessThan:
                    if (notFlg) {
                        greatEqual = true;
                    } else {
                        less = true;
                    }
                    break;
                case greaterThan:
                    if (notFlg) {
                        lessEqual = true;
                    } else {
                        great = true;
                    }
                    break;
                case lessThanEqualTo:
                    if (notFlg) {
                        great = true;
                    } else {
                        lessEqual = true;
                    }
                    break;
                case greaterThanEqualTo:
                    if (notFlg) {
                        less = true;
                    } else {
                        greatEqual = true;
                    }
                    break;
            }

            if (equals == false && (less || great || lessEqual || greatEqual)) {
                switch (fldName) {
                    case id:
                        val_int = Integer.parseInt(inputVal);
                        for (Integer key : idMap.keySet()) {
                            if (less || lessEqual) {
                                if ((key < val_int) || (lessEqual && key == val_int)) {
                                    readData(clnName, tblName, String.valueOf(idMap.get(key)), dltFlag);
                                }
                            } else if (great || greatEqual) {
                                if ((key > val_int) || (greatEqual && key == val_int)) {
                                    readData(clnName, tblName, String.valueOf(idMap.get(key)), dltFlag);
                                }
                            }
                        }
                        break;
                    case trials:
                        val_int = Integer.parseInt(inputVal);
                        for (Integer key : trlsMap.keySet()) {
                            if (less || lessEqual) {
                                if ((key < val_int) || (lessEqual && key == val_int)) {
                                    readData(clnName, tblName, trlsMap.get(key), dltFlag);
                                }
                            } else if (great || greatEqual) {
                                if ((key > val_int) || (greatEqual && key == val_int)) {
                                    readData(clnName, tblName, trlsMap.get(key), dltFlag);
                                }
                            }
                        }   break;
                    case patients:
                        val_int = Integer.parseInt(inputVal);
                        for (Integer key : ptntsMap.keySet()) {
                            if (less || lessEqual) {
                                if ((key < val_int) || (lessEqual && key == val_int)) {
                                    readData(clnName, tblName, ptntsMap.get(key), dltFlag);
                                }
                            } else if (great || greatEqual) {
                                if ((key > val_int) || (greatEqual && key == val_int)) {
                                    readData(clnName, tblName, ptntsMap.get(key), dltFlag);
                            }
                            }
                        }   break;
                    case dosage_mg:
                        val_int = Integer.parseInt(inputVal);
                        for (Integer key : dsageMap.keySet()) {
                            if (less || lessEqual) {
                                if ((key < val_int) || (lessEqual && key == val_int)) {
                                    readData(clnName, tblName, dsageMap.get(key), dltFlag);
                                }
                            } else if (great || greatEqual) {
                                if ((key > val_int) || (greatEqual && key == val_int)) {
                                    readData(clnName, tblName, dsageMap.get(key), dltFlag);
                                }
                            }
                        }   break;
                    case reading:
                        val_float = Float.parseFloat(inputVal);
                        for (Float key : rdingMap.keySet()) {
                            if (less || lessEqual) {
                                if ((key < val_float) || (lessEqual && key == val_float)) {
                                    readData(clnName, tblName, rdingMap.get(key), dltFlag);
                                }
                            } else if (great || greatEqual) {
                                if ((key > val_float) || (greatEqual && key == val_float)) {
                                    readData(clnName, tblName, rdingMap.get(key), dltFlag);
                            }
                        }
                    }   break;
                }
            }
        } catch (Exception e) {
        }
    }

    public static void searchForText(String colnName, String tblName,String fldName, boolean notFlg, String comp, String inputVal, boolean dltFlag)
    {

        boolean didRead = false;
        boolean keyBool;
        int first;
        int last;

        try {

            switch (comp) {
                case EqualTo:
                    didRead = true;
                    break;
                case lessThan:
                case lessThanEqualTo:
                case greaterThan:
                case greaterThanEqualTo:
                    didRead = false;
                    System.out.println("Syntax error in WHERE clause");
                    System.out.println("Cannot compare text with given operator");
                    break;
            }

            if (didRead) {
                if (inputVal.contains("'")) {
                    first = inputVal.indexOf('\'');
                    last = inputVal.lastIndexOf('\'');
                    if (first + 1 < last) {
                        inputVal = inputVal.substring(first + 1, last);
                    }
                }

                switch (fldName) {
                    case company:
                        if (notFlg) {
                            for (String keyValue : compMap.keySet()) {
                                if (keyValue.equalsIgnoreCase(inputVal)) {
                                    continue;
                                }
                                readData(colnName, tblName, compMap.get(keyValue), dltFlag);
                            }
                        } else {
                            if (compMap.containsKey(inputVal)) {
                                readData(colnName, tblName, compMap.get(inputVal), dltFlag);
                            } else {
                                System.out.println("\nNone found");
                            }
                        }
                        break;
                    case drug_id:
                        if (notFlg) {
                            for (String keyValue : drgIdMap.keySet()) {
                                if (keyValue.equalsIgnoreCase(inputVal)) {
                                    continue;
                                }
                                readData(colnName, tblName, drgIdMap.get(keyValue), dltFlag);
                            }
                        } else {
                            if (drgIdMap.containsKey(inputVal)) {
                                readData(colnName, tblName, drgIdMap.get(inputVal), dltFlag);
                            } else {
                                System.out.println("\nNone found");
                            }
                        }
                        break;
                    case double_blind:
                        if (notFlg) {
                            keyBool = !Boolean.parseBoolean(inputVal);
                        } else {
                            keyBool = Boolean.parseBoolean(inputVal);
                        }

                        readData(colnName, tblName, dblBindMap.get(keyBool), dltFlag);
                        break;
                    case controlled_study:
                        if (notFlg) {
                            keyBool = !Boolean.parseBoolean(inputVal);
                        } else {
                            keyBool = Boolean.parseBoolean(inputVal);
                        }

                        readData(colnName, tblName, cntrolStdyMap.get(keyBool), dltFlag);
                        break;
                    case govt_funded:
                        if (notFlg) {
                            keyBool = !Boolean.parseBoolean(inputVal);
                        } else {
                            keyBool = Boolean.parseBoolean(inputVal);
                        }

                        readData(colnName, tblName, govtMap.get(keyBool), dltFlag);
                        break;
                    case fda_approved:
                        if (notFlg) {
                            keyBool = !Boolean.parseBoolean(inputVal);
                        } else {
                            keyBool = Boolean.parseBoolean(inputVal);
                        }

                        readData(colnName, tblName, fdaMap.get(keyBool), dltFlag);
                        break;
                }
            }
        } catch (Exception e) {
        }
    }

    public static void resultValue(String clnNames, String tblNames, String fldName, boolean notFlg, String comp, String inputVal, boolean dlteFlg) {
        try {

            if (!fldName.equals("")) {
                switch (fldName) {
                    case id:
                        searchForInteger(clnNames, tblNames, fldName, notFlg, comp, inputVal, dlteFlg);
                        break;
                    case company:
                    case drug_id:
                        searchForText(clnNames, tblNames, fldName, notFlg, comp, inputVal, dlteFlg);
                        break;
                    case trials:
                    case patients:
                    case dosage_mg:
                    case reading:
                        searchForInteger(clnNames, tblNames, fldName, notFlg, comp, inputVal, dlteFlg);
                        break;
                    case double_blind:
                    case controlled_study:
                    case govt_funded:
                    case fda_approved:
                        searchForText(clnNames, tblNames, fldName, notFlg, comp, inputVal, dlteFlg);
                        break;
                }
            }
        } catch (Exception e) {
        }
    }

    /////////////////// MAIN METHODS ///////////////////////////////////////////
    public static void main(String[] args) throws IOException {

            firstCreate();

            BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
            String quer = "";
            String[] queryArray;
            int inp;


            while (true) {

                inp = mainMenu(rdr);
                if (inp == 1) {
                    System.out.println("Sample : import file_name.csv ");
                    System.out.print("SQL :");
                    quer = rdr.readLine();

                    queryArray = quer.split(" ");

                    if (!(queryArray[0]).equalsIgnoreCase("import")) {
                        System.out.println("Syntax Error:");
                        System.out.print("Press enter to continue");
                        rdr.readLine();
                        continue;
                    } else {
                        tblNme = (queryArray[1]).trim().substring(0, (queryArray[1]).indexOf('.'));
                        getBinary((queryArray[1]).trim());
                        System.out.println("Import successful");
                    }

                }

                if (inp == 2) {

                    System.out.println("Sample : SELECT * FROM PHARMA_TRIALS_1000B WHERE id = 897;");

                    String tot_query = getQuery(rdr, "").toLowerCase();
                    String tblName = "";
                    String colmName = "";
                    String where;
                    String fld_name = "";
                    String comp = "";
                    String val = "";
                    String[] columns;
                    String[] t;

                    int sel_ind;
                    int frm_ind;
                    int wh_ind;
                    int not_ind;
                    boolean queryExec = true;

                    String pattern1 = "select\\s.*from\\s.*where\\s.*";

                    Pattern r1 = Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE);

                    Matcher m1 = r1.matcher(tot_query);

                    sel_ind = tot_query.indexOf("select");
                    frm_ind = tot_query.indexOf("from");

                    if (m1.matches()) {

                        wh_ind = tot_query.indexOf("where");
                        not_ind = tot_query.indexOf("not");

                        if (wh_ind == -1) {
                            tblName = tot_query.substring(frm_ind + 4, tot_query.length() - 1).trim();
                        } else {
                            tblName = tot_query.substring(frm_ind + 4, wh_ind).trim();
                        }

                        if (!new File(tblName + ".db").exists()) {
                            System.out.println("Table does not exists. Please import using 1"
                                    + "");
                            queryExec = false;
                        } else {
                            colmName = tot_query.substring(sel_ind + 6, frm_ind).trim();

                            if (colmName.contains(",")) {
                                columns = colmName.split(Pattern.quote(","));
                                for (String s : columns) {
                                    s = s.trim();
                                    if (!columnArrayList.contains(s)) {
                                        System.out.println(s + "' in Select clause does not exists");
                                        queryExec = false;
                                    }
                                }
                            } else {
                                if (!colmName.equals("*")) {
                                    if (!columnArrayList.contains(colmName)) {
                                        System.out.println(colmName + "' in Select clause does not exists");
                                        queryExec = false;
                                    }
                                }

                            }
                        }

                        if (wh_ind != -1) {
                            where = tot_query.substring(wh_ind);
                            t = where.split(Pattern.quote(" "));
                            if (t.length < 4) {
                                System.out.println("Syntax error in WHERE clause");
                                queryExec = false;
                            } else {
                                fld_name = (t[1]).trim();
                                if (!columnArrayList.contains(fld_name)) {
                                    System.out.println(fld_name + " ' in where clause does not exists");
                                    queryExec = false;
                                }
                                if (not_ind != -1) {
                                    comp = (t[3]).trim();
                                } else {
                                    comp = (t[2]).trim();
                                }
                                val = where.substring(where.indexOf(comp) + comp.length(),
                                        where.length() - 1).trim();
                            }

                        }

                        if (queryExec) {

                            boolean notFlag = (not_ind != -1);
                            resultValue(colmName, tblName, fld_name, notFlag, comp, val, false);
                        }
                    } else {
                        System.out.println("Correct Query, Please");
                    }

                }

                if (inp == 3) {
                    System.out.println("Sample : INSERT into PHARMA_TRIALS_1000B values (898,ACORP,LE-111,16,2029,479,95.2,true,false,true,false);");
                    String tot_Query = getQuery(rdr, "").toLowerCase();
                    String tblNm = "";
                    String value = "";
                    String[] values;
                    boolean canExecute = true;
                    File f = null;
                    RandomAccessFile rndmFile = null;
                    int first = 0;
                    int last = 0;
                    String p1 = "insert\\s.*into\\s.*values\\s.*";
                    Pattern r1 = Pattern.compile(p1, Pattern.CASE_INSENSITIVE);
                    Matcher m1 = r1.matcher(tot_Query);

                    if (m1.matches()) {
                        tblNm = tot_Query.substring(tot_Query.indexOf("into") + 4, tot_Query.indexOf("values")).trim();

                        f = new File(tblNm + ".db");
                        if (!f.exists()) {
                            System.out.println("Table does not exists. Please import using 1");
                            canExecute = false;
                        } else {
                            value = tot_Query.substring(tot_Query.indexOf("values") + 6, tot_Query.indexOf(";")).trim().toUpperCase();
                            if (value.contains("(") && value.contains(")")) {
                                first = value.indexOf("(");
                                last = value.indexOf(")");
                                value = value.substring(first + 1, last).trim();
                            }
                            values = value.split(Pattern.quote(","));

                            if (values.length != columnArrayList.size()) {
                                canExecute = false;
                                System.out.println("No of values less than required");
                            }

                            if (canExecute) {
                                rndmFile = new RandomAccessFile(f, "rw");
                                outputDataToFile(rndmFile, values);
                                writeMaps();
                                System.out.println("1 row successfully inserted");
                            }
                        }
                    } else {
                        System.out.println("Syntax error");

                    }
                }

                if (inp == 4) {

                    System.out.println("Sample : DELETE FROM PHARMA_TRIALS_1000B WHERE id = 897;");
                    String fullQuery;
                    String tableName = "";
                    String where = "";
                    String fld_nm = "";
                    String comp = "";
                    String value = "";
                    int wh_Ind;
                    int not_ind;
                    boolean canExecute = true;
                    String[] temp;
                    File file = null;
                    
                    fullQuery = getQuery(rdr, "").toLowerCase();
                    
                    String pattern1 = "delete\\sfrom\\s.*where\\s.*";
                    
                    Pattern r1 = Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE);

                    Matcher m1 = r1.matcher(fullQuery);

                    if (m1.matches()) {
                        wh_Ind = fullQuery.indexOf("where");
                        not_ind = fullQuery.indexOf("not");

                        tableName = fullQuery.substring(fullQuery.indexOf("from") + 4, wh_Ind).trim();

                        file = new File(tableName + ".db");
                        if (!file.exists()) {
                            System.out.println("Table does not exist. Please import using 1");
                            canExecute = false;
                        } else {
                            if (wh_Ind != -1) {
                                where = fullQuery.substring(wh_Ind, fullQuery.indexOf(";"));
                                temp = where.split(Pattern.quote(" "));
                                if (temp.length < 4) {
                                    System.out.println("Syntax error in WHERE clause");
                                    canExecute = false;
                                } else {
                                    fld_nm = (temp[1]).trim();
                                    if (!columnArrayList.contains(fld_nm)) {
                                        System.out.println(fld_nm + "' in where clause does not exists");
                                        canExecute = false;
                                    }
                                    if (not_ind != -1) {
                                        comp = (temp[3]).trim();
                                    } else {
                                        comp = (temp[2]).trim();
                                    }

                                    value = where.substring(where.indexOf(comp) + comp.length(),
                                            where.length()).trim();
                                }
                            }

                            if (canExecute) {
                                boolean flag = (not_ind != -1);
                                resultValue("", tableName, fld_nm, flag, comp, value, true);
                            }
                        }
                    }
                }

            }

        }


}

