package ir.maktabsharif.homeservicephase2.util;

import ir.maktabsharif.homeservicephase2.exception.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.regex.Pattern;

@Component
public class Validation {

    public boolean checkBlank(String str) {
        if (str.isBlank()) throw new TextBlankException("the string input is blank!");
        return true;
    }

    public boolean checkText(String text) {
        String textRegex = "^[a-zA-Z]*$";
        if (!Pattern.matches(textRegex, text))
            throw new AlphabetException("the wording of the text is not incorrect!");
        return true;
    }

    public boolean checkPositiveNumber(Long longDigit) {
        if (longDigit <= 0)
            throw new PositiveNumberException("the number is negative!");
        return true;
    }

    public boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email))
            throw new EmailFormatException("the format of the email is incorrect!");
        return true;
    }


    public boolean checkPassword(String password) {
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (!Pattern.matches(passwordRegex, password))
            throw new PasswordFormatException("the format of the password is incorrect!");
        return true;
    }

    public boolean checkImage(File image) {
        String imageName = image.getName();
        checkBlank(imageName);
        int index = imageName.lastIndexOf('.');
        if (!(index > 0 && imageName.substring(index + 1).equalsIgnoreCase("JPG")))
            throw new ImageFormatException("the format of the image is incorrect!");
        if (image.length() > 300000L)
            throw new ImageSizeException("the size of the image is bigger than 300kb!");
        return true;
    }

    public boolean checkScore(int score) {
        if (score < 0 || score > 5)
            throw new PositiveNumberException("the score must be between 0 to 5!");
        return true;
    }
}
