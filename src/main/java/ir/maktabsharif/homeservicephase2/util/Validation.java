package ir.maktabsharif.homeservicephase2.util;

import ir.maktabsharif.homeservicephase2.exception.EmailFormatException;
import ir.maktabsharif.homeservicephase2.exception.ImageFormatException;
import ir.maktabsharif.homeservicephase2.exception.ImageSizeException;
import ir.maktabsharif.homeservicephase2.exception.PasswordFormatException;

import java.io.File;
import java.util.regex.Pattern;

public class Validation {

    private Validation() {
    }

    public static boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (Pattern.matches(emailRegex, email)) {
            return true;
        } else {
            throw new EmailFormatException("the format of the email is incorrect!");
        }

    }

    public static boolean checkPassword(String password) {
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (Pattern.matches(passwordRegex, password)) {
            return true;
        } else {
            throw new PasswordFormatException("the format of the password is incorrect!");
        }
    }

    public static boolean checkImage(File image) {
        String imageName = image.getName();
        int index = imageName.lastIndexOf('.');
        if (index > 0 && imageName.substring(index + 1).equalsIgnoreCase("JPG")) {
            if (image.length() <= 300000L) {
                return true;
            } else {
                throw new ImageSizeException("the size of the image is bigger than 300kilo byte!");
            }
        } else {
            throw new ImageFormatException("the format of the image is incorrect!");
        }
    }
}
