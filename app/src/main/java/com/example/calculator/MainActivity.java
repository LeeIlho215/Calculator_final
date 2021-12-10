package com.example.calculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView text_Result; //결과값을 띄울 텍스트뷰
    private TextView text_Exp; //수식을 띄울 텍스트뷰
    private List<Integer> checkList; // -1 : equal, 0 : 연산자, 1 : 숫자, 2 : . / 예외발생 방지리스트
    private Stack<String> operatorStack; //연산자를 위한 스택
    private List<String> infixList; // 중위 표기
    private List<String> postfixList; // 후위 표기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init(); // 이니셜라이징
    }

    void init() { //이니셜라이징 함수
        text_Result = findViewById(R.id.text_Result); //결과값 텍스트뷰
        text_Exp = findViewById(R.id.text_Exp); //수식 텍스트뷰
        checkList = new ArrayList<>(); //예외발생 방지 리스트
        operatorStack = new Stack<>(); //연산자를 담을 스택
        infixList = new ArrayList<>(); //중위 표기 리스트
        postfixList = new ArrayList<>(); //후위 표기 리스트

        ActionBar actionBar = getSupportActionBar(); //액션바 호출
        assert actionBar != null;
        actionBar.hide(); //액션바 숨기기
    }

    public void buttonClick(View v) { //버튼누름 인식함수
        if (!checkList.isEmpty() && checkList.get(checkList.size() - 1) == -1) { //연산자가 들어왔는지 확인
            text_Exp.setText(text_Result.getText().toString());
            checkList.clear();
            checkList.add(1); //정수
            checkList.add(2); //.
            checkList.add(3); //소수점
            text_Result.setText("");
        }
        switch (v.getId()) { //버튼의 ID값을 받아와 switch문 구동
            case R.id.btn1: addNumber("1"); break;
            case R.id.btn2: addNumber("2"); break;
            case R.id.btn3: addNumber("3"); break;
            case R.id.btn4: addNumber("4"); break;
            case R.id.btn5: addNumber("5"); break;
            case R.id.btn6: addNumber("6"); break;
            case R.id.btn7: addNumber("7"); break;
            case R.id.btn8: addNumber("8"); break;
            case R.id.btn9: addNumber("9"); break;
            case R.id.btn0: addNumber("0"); break;
            case R.id.btnDot: addDot("."); break;
            case R.id.btnD: addOperator("÷"); break;
            case R.id.btnPercent: addOperator("%"); break;
            case R.id.btnX: addOperator("X"); break;
            case R.id.btnP: addOperator("+"); break;
            case R.id.btnM: addOperator("-"); break;
        }
    }

    public void clearClick(View v) { //C 버튼을 눌렀을때 작동할 함수
        // 표기리스트, 체크리스트, 결과값, 수식, 스택 등 초기화
        infixList.clear();
        checkList.clear();
        text_Exp.setText("");
        text_Result.setText("");
        operatorStack.clear();
        postfixList.clear();
    }

    public void deleteClick(View v) { //DEL 버튼을 눌렀을 때 작동할 함수
        if (text_Exp.length() != 0) { //수식의 길이가 0이 아닐때
            checkList.remove(checkList.size() - 1); //체크리스트의 마지막 개체 삭제
            String[] ex = text_Exp.getText().toString().split(" "); //수식텍스트뷰를 String 으로 받아와서 ""을 기준으로 나눠서 ex에 배열로 저장
            List<String> li = new ArrayList<String>();
            Collections.addAll(li, ex);
            li.remove(li.size() - 1);

            if(li.size() > 0 && !isNumber(li.get(li.size() - 1))) {
                li.add(li.remove(li.size() - 1) + " ");
            }
            text_Exp.setText(TextUtils.join(" ", li));
        }
        text_Result.setText("");
    }

    public void addNumber(String str) {
        checkList.add(1);
        text_Exp.append(str);
    }

    void addDot(String str) {
        if (checkList.isEmpty()) {
            Toast.makeText(getApplicationContext(), ".을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if (checkList.get(checkList.size() - 1) != 1) {
            Toast.makeText(getApplicationContext(), ".을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = checkList.size() - 2; i >= 0; i--) {
            int check = checkList.get(i);
            if (check == 2) {
                Toast.makeText(getApplicationContext(), ".을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (check == 0)
                break;
            if (check == 1)
                continue;
        }
        checkList.add(2);
        text_Exp.append(str);
    }

    void addOperator(String str) {
        try {
            if (checkList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "연산자가 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (checkList.get(checkList.size() - 1) == 0 || checkList.get(checkList.size() - 1) == 2) {
                Toast.makeText(getApplicationContext(), "연산자가 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            checkList.add(0);
            text_Exp.append(" " + str + " ");
        } catch(Exception e) {
            Log.e("addOperator", e.toString());
        }
    }

    public void equalClick(View v) {
        if (text_Exp.length() == 0)
            return;
        if (checkList.get(checkList.size() - 1) != 1) {
            Toast.makeText(getApplicationContext(), "입력된 숫자가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Collections.addAll(infixList, text_Exp.getText().toString().split(" "));
        checkList.add(-1);
        result();
    }

    int getWeight(String operator) {
        int weight = 0;
        switch (operator) {
            case "X":
            case "/":
                weight = 5;
                break;
            case "%":
                weight = 3;
                break;
            case "+":
            case "-":
                weight = 1;
                break;
        }
        return weight;
    }

    boolean isNumber(String str) {
        boolean result = true;
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;

    }

    void infixToPostfix() {
        for (String item : infixList) {
            if (isNumber(item))
                postfixList.add(String.valueOf(Double.parseDouble(item)));
            else {
                if(operatorStack.isEmpty())
                    operatorStack.push(item);
                else {
                    if (getWeight(operatorStack.peek()) >= getWeight(item))
                        postfixList.add(operatorStack.pop());
                }
            }
        }
        while (!operatorStack.isEmpty())
            postfixList.add(operatorStack.pop());
    }

    String calculate(String num1, String num2, String op) {
        double first = Double.parseDouble(num1);
        double second = Double.parseDouble(num2);
        double result = 0.0;
        try {
            switch (op) {
                case "X": result = first * second; break;
                case "÷": result = first / second; break;
                case "%": result = first % second; break;
                case "+": result = first + second; break;
                case "-": result = first - second; break;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "연산할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        return String.valueOf(result);
    }

    void result() {
        try {
            int i = 0;
            infixToPostfix();
            while (postfixList.size() != 1) {
                if (!isNumber(postfixList.get(i))) {
                    postfixList.add(i - 2, calculate(postfixList.remove(i - 2), postfixList.remove(i - 2), postfixList.remove(i - 2)));
                    i = -1;
                }
                i++;
            }
            text_Result.setText(postfixList.remove(0));
            infixList.clear();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void swapClick(View v) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
        finish();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog_NoActionBar);

        builder.setMessage("앱을 종료하시겠습니까?                                   ")
                .setTitle("앱 종료")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Dialog", "취소");
            }
        })
        .setNeutralButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
        .show();
    }

    @Override
    public void onBackPressed() {
        show();
    }
}