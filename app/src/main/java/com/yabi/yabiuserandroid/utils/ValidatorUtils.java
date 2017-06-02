package com.yabi.yabiuserandroid.utils;


import com.yabi.yabiuserandroid.ui.uiutils.palette.CustomFontEditTextView;

/**
 * Created by yogeshmadaan on 16/11/15.
 */
public class ValidatorUtils {
    // Dialog Texts
    public static boolean validateName(CustomFontEditTextView editTextName)
    {
        String name = editTextName.getText().toString();
        if(name.trim().length()==0)
        {
            editTextName.setError("Enter correct name");
            editTextName.invalidate();
            return false;
        }
        String expression = "^[a-zA-Z\\s]+";
        return name.matches(expression);

    }
    public static boolean validateEmail(CustomFontEditTextView editTextEmail)
    {
        String email = editTextEmail.getText().toString();
        if (email.trim().length()==0) {
            {
                editTextEmail.setError("Enter valid email");
                editTextEmail.invalidate();
                return false;
            }
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
    public static boolean validatePhone(CustomFontEditTextView editTextPhone)
    {
        String phone = editTextPhone.getText().toString();
        try{
            if(phone.trim().length()==0)
            {
                editTextPhone.setError("Enter valid phone number");
                editTextPhone.invalidate();
                return false;
            }
            double n = Double.parseDouble(phone);
            if(!(phone.trim().startsWith("7")|| phone.trim().startsWith("8")|| phone.trim().startsWith("9")))
                return false;
            if(phone.trim().length()==10)
                return true;

        }catch(Exception e)
        {
            editTextPhone.setError("Enter valid phone number");
            editTextPhone.invalidate();
            return false;
        }
        return false;
    }
    public static boolean validateAddress(CustomFontEditTextView editTextAddress){
        String address = editTextAddress.getText().toString();
        address = editTextAddress.getText().toString();
        if(address.trim().length()==0)
        {
            editTextAddress.setError("Enter Valid Address");
            editTextAddress.invalidate();
            return false;
        }
        return true;
    }
}
