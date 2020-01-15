package dbm.photo.scanner.util;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Exiftool {

    private static String exifPath = "exiftool";

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.SS");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ssZZZZZ");
    private static Pattern date1 = Pattern.compile("^[0-9]{4}:[0-9]{2}:[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]*$");
    private static Pattern date2 = Pattern.compile("^[0-9]{4}:[0-9]{2}:[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]*$");

    public static void setExiftoolPath(String exifPath) { Exiftool.exifPath = exifPath; }

    public static Map<String, Object> getMetadata(File file) throws IOException, ParseException {
        HashMap<String, Object> data = new HashMap<>();

        ProcessBuilder pb = new ProcessBuilder(exifPath, file.getAbsolutePath());
        Process process = pb.start();
        InputStream is = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String sLine;

        while ((sLine = br.readLine()) != null) {
            String[] pair = sLine.split(":", 2);
            String key = pair[0].trim();

            if (key.equals("Aperture") ||
                key.equals("Red Balance") ||
                key.equals("Max Aperture At Min Focal") ||
                key.equals("Scale Factor To 35 mm Equivalent") ||
                key.equals("Exposure Difference") ||
                key.equals("F Number") ||
                key.equals("Flash Exposure Bracket Value")) {
                data.put(key, Double.parseDouble(pair[1].trim()));
            }
            else if (key.equals("X Resolution") ||
                key.equals("Y Resolution") ||
                key.equals("ISO") ||
                key.equals("ISO2") ||
                key.equals("Sub Sec Time Digitized") ||
                key.equals("Exif Image Width") ||
                key.equals("Image Height") ||
                key.equals("Memory Card Number") ||
                key.equals("Shutter Count")) {
                data.put(key, Integer.parseInt(pair[1].trim()));
            }
            else if (key.equals("Create Date") ||
                key.equals("Date/Time Original") ||
                key.equals("Modify Date")) {
                String v = pair[1].trim();
                if (v.matches("^[0-9]{4}:[0-9]{2}:[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]*$")) {
                    data.put(key, sdf2.parse(v));
                }
                else if (!v.matches("^[0-9]{4}:[0-9]{2}:[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]*$")) {
                    v += ".00";
                }
                data.put(key, sdf1.parse(v));
            }
            else if (key.equals("File Access Date/Time") ||
                key.equals("File Creation Date/Time") ||
                key.equals("File Modification Date/Time")) {
                String v = pair[1].trim();
                v = v.substring(0, v.length() - 3) + v.substring(v.length() - 2);
                data.put(key, sdf2.parse(v));
            }
            else if (key.equals("Thumbnail Image") ||
                key.equals("Thumbnail Length") ||
                key.equals("Preview Image") ||
                key.equals("MP Image 3")) {
                // Ignore
            }
            else {
                data.put(key, pair[1].trim());
            }
        }

        return data;
    }
}
