package com.example.calculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView text_Result; //결과값을 띄울 텍스트뷰
    private TextView text_Exp; //수식을 띄울 텍스트뷰
    private List<Integer> checkList; // -1 : equal, 0 : 연산자, 1 : 숫자, 2 : . , 3 : 부호결정중/ 예외발생 방지리스트
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

    @SuppressLint("NonConstantResourceId")
    public void buttonClick(View v) { //버튼누름 인식함수
        if (!checkList.isEmpty() && checkList.get(checkList.size() - 1) == -1) { //연산자가 들어왔는지 확인
            text_Exp.setText(text_Result.getText().toString());
            checkList.clear();
            checkList.add(1); //정수
            checkList.add(2); //.
            checkList.add(3); //소수점
            text_Result.setText("");
        }
        switch (v.getId()) { //버튼의 id를 받아와 switch 구동
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
            List<String> li = new ArrayList<String>(); //삭제작업을 진행할 리스트 생성
            Collections.addAll(li, ex); //리스트에 ex의 값을 추가
            li.remove(li.size() - 1); //리스트의 마지막 값을 제거

            if(li.size() > 0 && !isNumber(li.get(li.size() - 1))) { //마지막이 연산자일 때 " " 빈칸 추가
                li.add(li.remove(li.size() - 1) + " ");
            }
            text_Exp.setText(TextUtils.join(" ", li)); //수식을 " " 공백을 간격으로 대입
        }
        text_Result.setText(""); //결과창 초기화
    }

    public void addNumber(String str) { //수식에 숫자 추가
        checkList.add(1); //숫자가 들어갔음을 알림
        text_Exp.append(str); //기존 수식 뒤에 숫자를 추가
    }

    void addDot(String str) {
        if (checkList.isEmpty()) { //맨처음 .을 찍는거 방지
            Toast.makeText(getApplicationContext(), ".을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if (checkList.get(checkList.size() - 1) != 1) { //. 뒤에 . 방지
            Toast.makeText(getApplicationContext(), ".을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = checkList.size() - 2; i >= 0; i--) { //checkList 훑어보기
            int check = checkList.get(i); //checkList의 i번째 값을 정수 check에 저장(임시)
            if (check == 2) { //checkList의 값이 2(.)일 경우 연속 . 방지
                Toast.makeText(getApplicationContext(), ".을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (check == 0) //checkList의 값이 0(연산자)일 경우 연산자 뒤에 . 오는것 방지
                break;
            if (check == 1) { //checkList의 값이 1(숫자)일 경우 진행
                continue;
            }
        }
        checkList.add(2); //.이 들어왔음을 알림
        text_Exp.append(str); //수식 뒤에 .을 붙이기
    }

    void addOperator(String str) { //연산자 추가 메서드
        try {
            if (checkList.isEmpty()) { //제일 첫번째에 연산자가 오는 것 방지
                Toast.makeText(getApplicationContext(), "연산자가 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (checkList.get(checkList.size() - 1) == 0 || checkList.get(checkList.size() - 1) == 2) { //연속된 연산자나 . 뒤에 연산자 오는것을 방지
                Toast.makeText(getApplicationContext(), "연산자가 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            checkList.add(0); //연산자가 왔음을 알림
            text_Exp.append(" " + str + " "); //앞뒤로 한칸씩의 공백을 두고 연산자 추가
        } catch(Exception e) { //예외발생시
            Log.e("addOperator", e.toString());
        }
    }

    /*
    public void swapSign(View v) { // +/- 부호 바꾸는 메서드(제작중)
        if(checkList.get(checkList.size() - 1) == 1 || checkList.isEmpty()) {
            if(text_Exp.get) {

            }
        }
    }*/

    public void equalClick(View v) { // = 버튼을 눌렀을때 작동할 메서드
        if (text_Exp.length() == 0) //수식의 길이가 0이라면 계산을 실행하지 않음
            return;
        if (checkList.get(checkList.size() - 1) != 1) { //마지막이 연산자나 .으로 끝날경우 계산을 실행하지 않음
            Toast.makeText(getApplicationContext(), "입력된 숫자가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Collections.addAll(infixList, text_Exp.getText().toString().split(" ")); //infixList(중위표기)에 수식을 " " 공백을 기준으로 나누어 집어넣음
        checkList.add(-1); // 계산을 시도할 것임을 알림
        result(); //계산 및 결과표출 메서드 실행
    }

    int getWeight(String operator) { //연산자에 가중치를 부여하는 메서드
        int weight = 0; //연산자의 가중치를 담을 정수
        switch (operator) { //switch 가동
            case "X": // 사칙연산에서 가장 우선순위가 높은 X/÷ 가 올 경우에 가장 높은 가중치 부여
            case "÷":
                weight = 5;
                break;
            case "%": // 그 다음으로 우선순위가 높은 나머지값 구하는 %에 중간만큼 가중치 부여
                weight = 3;
                break;
            case "+": // 가장 마지막으로 우선시되는 +-에 가장 낮은 가중치 부여
            case "-":
                weight = 1;
                break;
        }
        return weight; // 가중치를 반환
    }

    boolean isNumber(String str) { //str이 숫자인지 아닌지 판별(숫자 : true, 숫자 아님 : false)
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    void infixToPostfix() { //중위표기를 후위표기로 바꾸는 메서드
        for (String item : infixList) { //infixList의 item을 뽑아내기(item이 없을때까지)
            if (isNumber(item)) //받아온 item이 숫자라면
                postfixList.add(String.valueOf(Double.parseDouble(item))); //후위표기식에 숫자를 추가
            else {
                if(operatorStack.isEmpty()) { //연산자 스택이 비어있다면
                    operatorStack.push(item); //연산자를 집어넣는다
                } else {
                    if (getWeight(operatorStack.peek()) >= getWeight(item)) //원래 들어와있던 연산자의 가중치가 새로 들어온 연산자의 가중치보다 낮다면
                        postfixList.add(operatorStack.pop()); //스택 가장 위에 연산자를 빼내고
                        operatorStack.push(item); //가중치가 높은 연산자를 집어넣는다
                }
            }
        }
        while (!operatorStack.isEmpty()) //연산자 스택이 비어있지 않다면
            postfixList.add(operatorStack.pop()); //계속해서 연산자들을 스택에서 빼낸다
    }

    String calculate(String num1, String num2, String op) { //계산을 담당하는 메서드
        double first = Double.parseDouble(num1); //수식의 왼쪽값(a(연산)b의 a)
        double second = Double.parseDouble(num2); //수식의 오른쪽값(a(연산)b의 b)
        double result = 0.0; //결과값을 담을 실수
        try {
            switch (op) { //switch 가동
                case "X": result = first * second; break; //곱셈
                case "÷": result = first / second; break; //나눗셈
                case "%": result = first % second; break; //나머지
                case "+": result = first + second; break; //더하기
                case "-": result = first - second; break; //빼기
            }
        } catch (Exception e) { //예외발생시
            Toast.makeText(getApplicationContext(), "연산할 수 없습니다.", Toast.LENGTH_SHORT).show(); //안내메시지 출력
        }
        return String.valueOf(result); //결과값을 String 으로 반환
    }

    void result() { //계산 및 결과표출을 담당하는 메서드
        try {
            int i = 0; //index 정수 생성
            infixToPostfix(); //중위표기를 후위표기로 바꾸기
            while (postfixList.size() != 1) { //postfixList의 개체들이 비어있지 않다면
                if (!isNumber(postfixList.get(i))) { //i번째 개체가 숫자가 아닐시
                    postfixList.add(i - 2, calculate(postfixList.remove(i - 2), postfixList.remove(i - 2), postfixList.remove(i - 2))); //i번째 개체를 기준으로 앞뒤의 숫자들과 연산실행
                    i = -1;
                }
                i++;
            }
            text_Result.setText(postfixList.remove(0)); //postfixList의 첫번째 개체 삭제 후 결과값에 세팅
            infixList.clear(); //중위표기 리스트 초기화
        } catch(IllegalStateException e) { //예외처리
            e.printStackTrace();
        } catch(IndexOutOfBoundsException e) { //예외처리
            e.printStackTrace();
        }
    }

    public void swapClick(View v) { //방정식 계산기로 넘어가기 위한 메서드
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
        finish(); //메모리절약(?)을 위해 현재 액티비티는 종료
    }

    public void show() { //뒤로가기 버튼을 눌렀을 때 앱을 종료할건지 물어보기 위한 메서드
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
    public void onBackPressed() { //show()의 실질적 실행부분
        show();
    }
}