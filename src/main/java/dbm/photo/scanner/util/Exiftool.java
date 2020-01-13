package dbm.photo.scanner.util;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Exiftool {

    private static String exifPath = "exiftool";

    public static void setExiftoolPath(String exifPath) { Exiftool.exifPath = exifPath; }

    public static Map<String, String> getMetadata(File file) throws IOException {
        HashMap<String, String> data = new HashMap<>();

        ProcessBuilder pb = new ProcessBuilder(exifPath, file.getAbsolutePath());
        Process process = pb.start();
        InputStream is = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String sLine;

        while ((sLine = br.readLine()) != null) {
            String[] pair = sLine.split(":", 2);
            data.put(pair[0].trim(), pair[1].trim());
        }

        return data;
    }
}
