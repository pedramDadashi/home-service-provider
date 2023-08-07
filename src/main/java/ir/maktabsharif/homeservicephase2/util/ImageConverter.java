package ir.maktabsharif.homeservicephase2.util;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ImageConverter {

    private ImageConverter() {
    }

    public static String getStringImage(File image) throws IOException {
        byte[] imageArray = FileUtils.readFileToByteArray(image);
        return Base64.getEncoder().encodeToString(imageArray);
    }

    public static File getFileImage(String imageString) throws IOException {
        byte[] imageArray = Base64.getDecoder().decode(imageString);
        File image = new File("workerImage");
        FileUtils.writeByteArrayToFile(image, imageArray);
        return image;
    }
}
