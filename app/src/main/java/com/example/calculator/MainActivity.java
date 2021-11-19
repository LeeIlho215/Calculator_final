package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnP, btnM, btnX, btnD, btnR, btnC, btnDel, btnCE, btnPercent,
            btnPM, btnDot;
    private int a;
    private EditText edit;
    private int where = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "Calcultor", Toast.LENGTH_SHORT).show();

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        btnP = (Button)findViewById(R.id.btnP);
        btnM = (Button)findViewById(R.id.btnM);
        btnX = (Button)findViewById(R.id.btnX);
        btnD = (Button)findViewById(R.id.btnD);
        btnR = (Button)findViewById(R.id.btnR);
        btnC = (Button)findViewById(R.id.btnC);
        btnDel = (Button)findViewById(R.id.btnDEL);
        btnCE = (Button)findViewById(R.id.btnCE);
        btnPercent = (Button)findViewById(R.id.btnPercent);
        btnPM = (Button)findViewById(R.id.btnPM);
        btnDot = (Button)findViewById(R.id.btnDot);


        edit = (EditText)findViewById(R.id.edit1);

        View.OnClickListener cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==btn1) {
                    edit.setText(edit.getText().toString()+1);
                }
                else if(v==btn2) {
                    edit.setText(edit.getText().toString()+2);
                }
                else if(v==btn3) {
                    edit.setText(edit.getText().toString()+3);
                }
                else if(v==btn4) {
                    edit.setText(edit.getText().toString()+4);
                }
                else if(v==btn5) {
                    edit.setText(edit.getText().toString()+5);
                }
                else if(v==btn6) {
                    edit.setText(edit.getText().toString()+6);
                }
                else if(v==btn7) {
                    edit.setText(edit.getText().toString()+7);
                }
                else if(v==btn8) {
                    edit.setText(edit.getText().toString()+8);
                }
                else if(v==btn9) {
                    edit.setText(edit.getText().toString()+9);
                }
                else if(v==btn0) {
                    edit.setText(edit.getText().toString()+0);
                }
                else if(v==btnP) {
                    if(edit.getText().toString().equals("") || edit.getText().toString().equals(null)) {

                    }
                    else {
                        a = Integer.valueOf(edit.getText().toString().trim());
                        edit.setText("");
                        where = 1;
                    }
                }
                else if(v==btnX) {
                    if(edit.getText().toString().equals("") || edit.getText().toString().equals(null)) {

                    }
                    else {
                        a = Integer.valueOf(edit.getText().toString().trim());
                        edit.setText("");
                        where = 2;
                    }
                }
                else if(v==btnD) {
                    if(edit.getText().toString().equals("") || edit.getText().toString().equals(null)) {

                    }
                    else {
                        a = Integer.valueOf(edit.getText().toString().trim());
                        edit.setText("");
                        where = 3;
                    }
                }
                else if(v==btnM) {
                    if(edit.getText().toString().equals("") || edit.getText().toString().equals(null)) {

                    }
                    else {
                        a = Integer.valueOf(edit.getText().toString().trim());
                        edit.setText("");
                        where = 4;
                    }
                }
                else if(v==btnC) {
                    edit.setText("");
                }
                else if(v==btnR) {
                    if (where == 1) {
                        if(edit.getText().toString() == "") {

                        }
                        else {
                            a = a + Integer.valueOf(edit.getText().toString().trim());
                            edit.setText(Integer.toString(a));
                        }
                    }
                    else if (where == 2) {
                        if(edit.getText().toString() == "") {

                        }
                        else {
                            a = a * Integer.valueOf(edit.getText().toString().trim());
                            edit.setText(Integer.toString(a));
                        }
                    }
                    else if (where == 3) {
                        if(edit.getText().toString() == "") {

                        }
                        else {
                            a = a / Integer.valueOf(edit.getText().toString().trim());
                            edit.setText(Integer.toString(a));
                        }
                    }
                    else if (where == 4) {
                        if(edit.getText().toString() == "") {

                        }
                        else {
                            a = a + Integer.valueOf(edit.getText().toString().trim());
                            edit.setText(Integer.toString(a));
                        }
                    }
                }
            }
        };
        btn1.setOnClickListener(cl);
        btn2.setOnClickListener(cl);
        btn3.setOnClickListener(cl);
        btn4.setOnClickListener(cl);
        btn5.setOnClickListener(cl);
        btn6.setOnClickListener(cl);
        btn7.setOnClickListener(cl);
        btn8.setOnClickListener(cl);
        btn9.setOnClickListener(cl);
        btn0.setOnClickListener(cl);
        btnP.setOnClickListener(cl);
        btnM.setOnClickListener(cl);
        btnX.setOnClickListener(cl);
        btnD.setOnClickListener(cl);
        btnR.setOnClickListener(cl);
        btnC.setOnClickListener(cl);
        btnPercent.setOnClickListener(cl);
        btnDel.setOnClickListener(cl);
        btnPM.setOnClickListener(cl);
        
    }
}