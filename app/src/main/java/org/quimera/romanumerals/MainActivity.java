package org.quimera.romanumerals;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
        /// Default true = decimal to roman
        static boolean current;
        private AdView mAdView;
        private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button romanToDecimal = findViewById(R.id.romanToDecimal);
        Button decimalToRoman = findViewById(R.id.decimalToRoman);
        Button btnConvert = findViewById(R.id.btnConvert);
        Button copyBtn = findViewById(R.id.copyBtn);
        TextView showVal = findViewById(R.id.showVal);
        current = true;

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        copyBtn.setOnClickListener(this);
        showVal.setOnClickListener(this);
        romanToDecimal.setOnClickListener(this);
        decimalToRoman.setOnClickListener(this);
        btnConvert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextView showVal = findViewById(R.id.showVal);

        if (v == findViewById(R.id.romanToDecimal)) {
            current = false;
        } else if (v == findViewById(R.id.decimalToRoman)) {
            current = true;
        } else if (v == findViewById(R.id.copyBtn)){
            // Code to Copy the content of Text View to the Clip board.
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", showVal.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), getString(R.string.copiedResult),
                    Toast.LENGTH_SHORT).show();
        } else {
            canviar();
        }
    }

//////////////////////////////////////////////// Main
    void canviar () {

        EditText insertVal = findViewById(R.id.insertVal);
        TextView showVal = findViewById(R.id.showVal);

        String[] array = this.getResources().getStringArray(R.array.WrongInt);
        String wrongInt = array[new Random().nextInt(array.length)];

        String[] array2 = this.getResources().getStringArray(R.array.WrongRoman);
        String wrongRoman = array2[new Random().nextInt(array2.length)];


        if (current) {
            try {
                int val = Integer.parseInt(insertVal.getText().toString());
                if (val>3999){
                    Toast.makeText(getApplicationContext(), wrongInt, Toast.LENGTH_SHORT).show();
                } else {
                    showVal.setText(integerToRoman(val) + " ");
                }
            } catch (NumberFormatException e){
//                showVal.setText(getString(R.string.WrongInt));
                Toast.makeText(getApplicationContext(), wrongInt, Toast.LENGTH_SHORT).show();
            }
        } else {
            String str_val = insertVal.getText().toString().toUpperCase();
            str_val= str_val.replaceAll(" ","");

            if (checkingStrings(str_val)){
                Toast.makeText(getApplicationContext(), wrongRoman, Toast.LENGTH_SHORT).show();

            } else {
                int val = romanToDecimal(str_val);

                if (val<=0 || val>3999) {
//                    showVal.setText(getString(R.string.WrongRoman));
                    Toast.makeText(getApplicationContext(), wrongRoman, Toast.LENGTH_SHORT).show();

                } else {
                    showVal.setText(val + " ");
                }
            }
        }
    }

//////////////////////////////////////////////// Just checkin'
    boolean checkingStrings(String str) {
        boolean here = false;
        String[] values = new String[]{"A", "B", "E", "F", "G", "H", "J", "K", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "W", "Y", "Z",
        "/", "$", "@", ".", ",", ":", "!", "Ç", "&"};
        for (String val : values) {
            if (str.contains(val)) {
                here = true;
            }
        }

        return here;
    }

//////////////////////////////////////////////// DECIMAL TO ROMAN


    String integerToRoman(int num) {

        System.out.println("Integer: " + num);
        int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanLiterals = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};

        StringBuilder roman = new StringBuilder();

        for(int i=0;i<values.length;i++) {
            while(num >= values[i]) {
                num -= values[i];
                roman.append(romanLiterals[i]);
            }
        }
        return roman.toString();
    }



//////////////////////////////////////////////// ROMAN TO DECIMAL

    // This function returns
    // value of a Roman symbol
    int value(char r)
    {
        if (r == 'I')
            return 1;
        if (r == 'V')
            return 5;
        if (r == 'X')
            return 10;
        if (r == 'L')
            return 50;
        if (r == 'C')
            return 100;
        if (r == 'D')
            return 500;
        if (r == 'M')
            return 1000;
        return -1;
    }

    // Finds decimal value of a
    // given roman numeral
    int romanToDecimal(String str)
    {
        // Initialize result
        int res = 0;

        for (int i = 0; i < str.length(); i++)
        {
            // Getting value of symbol s[i]
            int s1 = value(str.charAt(i));

            // Getting value of symbol s[i+1]
            if (i + 1 < str.length())
            {
                int s2 = value(str.charAt(i + 1));

                // Comparing both values
                if (s1 >= s2)
                {
                    // Value of current symbol
                    // is greater or equalto
                    // the next symbol
                    res = res + s1;
                }
                else
                {
                    // Value of current symbol is
                    // less than the next symbol
                    res = res + s2 - s1;
                    i++;
                }
            }
            else {
                res = res + s1;
            }
        }

        return res;
    }

}