package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant.Fundamentals.User;
import com.example.restaurant.login.ui.ui.StringXOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.restaurant.MainActivity.db;

public class StaffManagementActivity extends AppCompatActivity {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);

        AtomicInteger hash = new AtomicInteger();


        TextView text = findViewById(R.id.itemText);

        EditText name = findViewById(R.id.name);
        EditText role = findViewById(R.id.role);
        EditText phone = findViewById(R.id.phone);
        EditText email = findViewById(R.id.email);
        EditText salary = findViewById(R.id.salary);
        EditText date_birth = findViewById(R.id.date_birth);
        EditText login = findViewById(R.id.login);
        EditText pass = findViewById(R.id.pass);
        EditText id = findViewById(R.id.masterID);

        Button addUser = findViewById(R.id.addMaster);
        Button showLoginPass = findViewById(R.id.button_getMasters2);
        Button updateUser = findViewById(R.id.showAvg);
        Button deleteMaster = findViewById(R.id.deleteMaster);
        Button getStuffButton = findViewById(R.id.button_getMasters);



        addUser.setOnClickListener(v -> {
            String name_ = name.getText().toString();
            String role_ = role.getText().toString();
            String phone_ = phone.getText().toString();
            String email_ = email.getText().toString();
            String salary_ = salary.getText().toString();
            String date_birth_ = date_birth.getText().toString();
            String login_ = login.getText().toString();
            String pass_ = pass.getText().toString();


            if (!(role_.equals("Waiter") || role_.equals("Admin") || role_.equals("Cook"))) {
                Toast.makeText(getApplicationContext(),
                        "Неправильно введена роль.", Toast.LENGTH_LONG).show();
                return;
            }


            if (login_.equals("") || pass_.equals("") || phone_.equals("") || email_.equals("") || salary_.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Заполните недостающие поля.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!db.isAlreadyUser(login_, phone_, email_).equals("OK")) {
                Toast.makeText(getApplicationContext(),
                        "Пользователь уже существует.", Toast.LENGTH_LONG).show();
                return;
            }


            String encoded1 = StringXOR.encode(pass_);
            //String encoded1 = StringXOR.encode("123");
            //String password1 = StringXOR.decode(encoded1);

            int tmpSalary = Integer.parseInt(salary_);

            User user = new User(role_, name_, phone_, email_, tmpSalary, date_birth_, login_, encoded1);
            db.insertUser(user);

            Toast.makeText(getApplicationContext(), "Пользователь успешно добавлен!", Toast.LENGTH_LONG).show();

            name.setText("");
            role.setText("");
            phone.setText("");
            salary.setText("");
            date_birth.setText("");
            login.setText("");
            pass.setText("");
            email.setText("");

        });

        updateUser.setOnClickListener(v -> {
            String idStr = id.getText().toString();
            if (idStr.equals("")) {
                Toast.makeText(getBaseContext(), "Введите ID", Toast.LENGTH_SHORT).show();
                return;
            }


            User tmp = db.getUserByID(Integer.parseInt(idStr));
            if (tmp == null) {
                Toast.makeText(getBaseContext(), "Некорректный ID", Toast.LENGTH_SHORT).show();
                return;
            }


            String name_ = name.getText().toString();
            String phone_ = phone.getText().toString();
            String email_ = email.getText().toString();
            String salary_ = salary.getText().toString();
            String date_birth_ = date_birth.getText().toString();
            String login_ = login.getText().toString();
            String pass_ = pass.getText().toString();

            //String encoded1 = StringXOR.encode("123");
            //String password1 = StringXOR.decode(encoded1);


            if (!db.isAlreadyUser(login_, phone_, email_).equals("OK")) {
                Toast.makeText(getApplicationContext(),
                        "Пользователь уже существует.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!salary_.equals("")) {
                int tmpSalary = Integer.parseInt(salary_);
                tmp.setSalary(tmpSalary);
            }

            if (!pass_.equals("")) {
                String encoded1 = StringXOR.encode(pass_);
                tmp.setPassword(encoded1);
            }
            if (!login_.equals("")) {
                tmp.setLogin(login_);
            }
            if (!date_birth_.equals("")) {
                tmp.setBirthDate(date_birth_);
            }
            if (!name_.equals("")) {
                tmp.setName(name_);
            }
            if (!phone_.equals("")) {
                tmp.setPhone(phone_);
            }
            if (!email_.equals("")) {
                tmp.setEmail(email_);
            }

            db.updateUser(tmp);

            Toast.makeText(getApplicationContext(), "Пользователь успешно изменен!", Toast.LENGTH_LONG).show();

            name.setText("");
            role.setText("");
            phone.setText("");
            salary.setText("");
            date_birth.setText("");
            login.setText("");
            pass.setText("");
            email.setText("");
        });

        deleteMaster.setOnClickListener(v -> {
            String preID = id.getText().toString();
            if (preID.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Заполните ID", Toast.LENGTH_LONG).show();
                return;
            }
            int id_ = Integer.parseInt(preID);

            db.deleteUser(id_);
            Toast.makeText(getApplicationContext(), "Пользователь успешно удален!", Toast.LENGTH_LONG).show();
        });

        getStuffButton.setOnClickListener(v -> {
            String content = "";
            Cursor cursor = db.selectFromUser();
            User user;
            while (cursor.moveToNext()) {
                hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                user = db.getUserByHashCode(hash.get());
                String salary1 = user.getSalary() + "";
                if (user.getSalary() == 0) salary1 = "-";
                content += "UserID: " + user.getIdUser() + "\n" +
                        "Role: " + user.getRole() + "\n" +
                        "Name: " + user.getName()+ "\n" +
                        "Phone: " + user.getPhone() + "\n" +
                        "Salary: " + salary1 + "\n" +
                        "Birth Date: " + user.getBirthDate() + "\n";
                content += "\n\n";
            }
            cursor.close();
            text.setText(content);
        });

        showLoginPass.setOnClickListener(v -> {
            String content = "";

            String idStr = id.getText().toString();
            if (idStr.equals("")) {
                Toast.makeText(getBaseContext(), "Введите ID официанта", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = db.getUserByID(Integer.parseInt(idStr));
            if (user == null) {
                Toast.makeText(getBaseContext(), "Некорректный ID", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!user.getRole().equals("Waiter")) {
                Toast.makeText(getBaseContext(), "Введите ID официанта", Toast.LENGTH_SHORT).show();
                return;
            }

            String salary1 = user.getSalary() + "";
            if (user.getSalary() == 0) salary1 = "-";

            String password = StringXOR.decode(user.getPassword());

            content += "UserID: " + user.getIdUser() + "\n" +
                    "Role: " + user.getRole() + "\n" +
                    "Name: " + user.getName()+ "\n" +
                    "Login: " + user.getLogin() + "\n" +
                    "Pass: " + password + "\n";
            content += "\n\n";
            text.setText(content);
        });




    }
}