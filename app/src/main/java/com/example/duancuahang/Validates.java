package com.example.duancuahang;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Validates {
    public static boolean validPhone(String phone) {
        String strRegex = "^0[0-9]{9}$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }
    public static boolean validFullname(String fullname) {
        String strRegex = "^([a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]{8,70})$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(fullname);
        return matcher.find();
    }
    public static boolean validShopName(String shopName) {
        String strRegex = "^([a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ0-9\\s]{8,100})$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(shopName);
        return matcher.find();
    }
    public static boolean validShopAddress(String shopAddress) {
        String strRegex = "^([a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ0-9\\s]{8,300})$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(shopAddress);
        return matcher.find();
    }
    public static boolean validEmail(String email) {
        String strRegex = "^([a-zA-z0-9]+@gmail.com)$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
    public static boolean validMaSoThue(String maSoThue) {
        String strRegex = "^\\d{13}$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(maSoThue);
        return matcher.find();
    }

    public static boolean getCheckValueString_Normal(String value){
        String regex = "^[a-zA-Z0-9\\p{L} ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()){
            return true;
        }
        return  false;
    }

    public static boolean getCheckValueNumber(String value){
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (value != null){
            if (matcher.matches()){
                if (Integer.parseInt(value) > 0){
                    return true;
                }
                return false;
            }
            return  false;
        }
        return false;
    }


}
