package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant.Fundamentals.Dish;
import com.example.restaurant.Fundamentals.Part;
import com.example.restaurant.Fundamentals.User;
import com.example.restaurant.db.DataBaseHandler;
import com.example.restaurant.login.ui.ui.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK;

public class MainActivity extends AppCompatActivity {


    public static DataBaseHandler db;
    public static Integer DEFAULT_QUANTITY = 30;
    Button logOutButton;
    Button soldDishesBetweenButton;
    Button getOrdersButton;
    Button getStuffButton;
    Button mastersManagement;
    Button dishManagement;

    Button moneyInRange;
    Button allMoney;
    Button averageDishMoney;
    Button soldDishes;

    Button dateFrom;
    Button dateTo;

    TextView text;


    Calendar dateAndTime = Calendar.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseHandler(this);

        /*User userByID = db.getUserByID(2);
        String encoded1 = StringXOR.encode("123");
        userByID.setPassword(encoded1);
        db.updateUser(userByID);*/

        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {
            AtomicInteger hash = new AtomicInteger();

            logOutButton = findViewById(R.id.button_log_out);
            getOrdersButton = findViewById(R.id.button_getOrders);
            getStuffButton = findViewById(R.id.button_acceptOrder);
            soldDishesBetweenButton = findViewById(R.id.button_ordersByDate);
            mastersManagement = findViewById(R.id.masters_management);
            dishManagement = findViewById(R.id.masters_management2);

            moneyInRange = findViewById(R.id.button_getOrders9);
            soldDishes = findViewById(R.id.button_getOrders11);
            allMoney = findViewById(R.id.button_allMoney);
            averageDishMoney = findViewById(R.id.button_averageSum);

            dateFrom = findViewById(R.id.button_getOrders5);
            dateTo = findViewById(R.id.button_getOrders3);

            text = findViewById(R.id.itemText);


            soldDishes.setOnClickListener(v -> {
                String content = "";
                Cursor cursor = db.selectFromPart();
                Part part = null;
                while (cursor.moveToNext()) {
                    hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                    part = db.getPartByHashCode(hash.get());
                    if (!part.getStatus().equals("Готов")) continue;


                    User waiter = db.getUserByID(part.getIdWaiter());
                    User cook = db.getUserByID(part.getIdCook());

                    String waiterName;
                    String cookName;


                    if (waiter != null)  waiterName = waiter.getName();
                    else waiterName = "-";

                    if (cook != null)  cookName = cook.getName();
                    else cookName = "-";

                    content += "OrderID: " + part.getIdPart() + "\n" +
                            "Dish name: " + part.getDishName() + "\n" +
                            "Quantity: " + part.getQuantity() + "\n" +
                            "Price: " + part.getPrice() + "\n" +
                            "Status: " + part.getStatus() + "\n" +
                            "Date: " + part.getDate() + "\n" +
                            "Comment: " + part.getComment() + "\n" +
                            "Cook Name: " + cookName + "\n" +
                            "Waiter Name: " + waiterName + "\n";
                    content += "=====================\n";
                }
                cursor.close();

                text.setText(content);
            });

            moneyInRange.setOnClickListener(v -> {
                if (dateFrom.getText().toString().equals("-") || dateTo.getText().toString().equals("-")) {
                Toast.makeText(getBaseContext(), "Некорректные даты", Toast.LENGTH_SHORT).show();
                return;
            }








                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Date strDateFrom = null;
                Date strDateTo = null;

                try {
                    strDateFrom = sdf.parse(dateFrom.getText().toString());
                    strDateTo = sdf.parse(dateTo.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                assert strDateTo != null;
                assert strDateFrom != null;
                if (strDateTo.getTime() <= strDateFrom.getTime()) {
                    Toast.makeText(getBaseContext(), "Дата 'До:' должна быть больше чем 'От' ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String content = "";
                Cursor cursor = db.selectFromPart();
                Part part = null;
                int i = 0;
                while (cursor.moveToNext()) {
                    hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                    part = db.getPartByHashCode(hash.get());

                    Date strDate = null;
                    try {
                        strDate = sdf.parse(part.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (strDate == null) {
                        Toast.makeText(getBaseContext(), "Некорректная дата в БД", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (strDate.getTime() >= strDateFrom.getTime() && strDate.getTime() <= strDateTo.getTime()) {
                        i += part.getPrice();
                    }

                }
                cursor.close();
                text.setText("Общая сумма заказов за промежуток: " + i);

            });

            allMoney.setOnClickListener(v -> {
                String content = "";
                Cursor cursor = db.selectFromPart();
                Part part = null;
                int sum = 0;
                while (cursor.moveToNext()) {
                    hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                    part = db.getPartByHashCode(hash.get());
                    sum += part.getPrice();
                }
                cursor.close();

                content += "Общая выручка магазина: " + sum;
                text.setText(content);

            });

            averageDishMoney.setOnClickListener(v -> {
                String content = "";
                Cursor cursor = db.selectFromPart();
                Part part = null;
                double sum = 0;
                int quantity = 0;
                while (cursor.moveToNext()) {
                    hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                    part = db.getPartByHashCode(hash.get());
                    if (!part.getStatus().equals("Готов")) continue;
                    sum += part.getPrice();
                    quantity += part.getQuantity();
                }
                cursor.close();

                content += "Средняя стоимость заказа: " + sum/quantity;
                text.setText(content);
            });

            dateFrom.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, THEME_DEVICE_DEFAULT_DARK, d,
                        //получаем текущую дату
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            });

            dateTo.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, THEME_DEVICE_DEFAULT_DARK, b,
                        //получаем текущую дату
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            });

            soldDishesBetweenButton.setOnClickListener(v -> {
                if (dateFrom.getText().toString().equals("-") || dateTo.getText().toString().equals("-")) {
                Toast.makeText(getBaseContext(), "Некорректные даты", Toast.LENGTH_SHORT).show();
                return;
                }

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Date strDateFrom = null;
                Date strDateTo = null;

                try {
                    strDateFrom = sdf.parse(dateFrom.getText().toString());
                    strDateTo = sdf.parse(dateTo.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                assert strDateTo != null;
                assert strDateFrom != null;
                if (strDateTo.getTime() <= strDateFrom.getTime()) {
                    Toast.makeText(getBaseContext(), "Дата 'До:' должна быть больше чем 'От' ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String content = "";
                Cursor cursor = db.selectFromPart();
                Part part = null;
                int i = 0;
                while (cursor.moveToNext()) {
                    hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                    part = db.getPartByHashCode(hash.get());

                    Date strDate = null;
                    try {
                        strDate = sdf.parse(part.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (strDate == null) {
                        Toast.makeText(getBaseContext(), "Некорректная дата в БД", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (strDate.getTime() >= strDateFrom.getTime() && strDate.getTime() <= strDateTo.getTime()) {
                        Dish dish = db.getDishById(part.getDishId());
                        content += "DishID: " + dish.getIdDish() + "\n" +
                                "Name: " + dish.getName() + "\n" +
                                "Status: " + dish.getStatus() + "\n" +
                                "Price: " + dish.getPrice()+ "\n";
                        content += "\n\n";
                    }

                }
                cursor.close();
                text.setText(content);
            });

            logOutButton.setOnClickListener(v -> {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            });

            mastersManagement.setOnClickListener(v -> {
                Intent i = new Intent(MainActivity.this, StaffManagementActivity.class);
                startActivity(i);
            });

            dishManagement.setOnClickListener(v -> {
                Intent i = new Intent(MainActivity.this, StockActivity.class);
                startActivity(i);
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

            getOrdersButton.setOnClickListener(v -> {
                String content = "";
                Cursor cursor = db.selectFromPart();
                Part part = null;
                while (cursor.moveToNext()) {
                    hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                    part = db.getPartByHashCode(hash.get());



                    User waiter = db.getUserByID(part.getIdWaiter());
                    User cook = db.getUserByID(part.getIdCook());
                    
                    String waiterName;
                    String cookName;
                    
                    
                    if (waiter != null)  waiterName = waiter.getName();
                    else waiterName = "-";
                    
                    if (cook != null)  cookName = cook.getName();
                    else cookName = "-";

                    content += "OrderID: " + part.getIdPart() + "\n" +
                            "Dish name: " + part.getDishName() + "\n" +
                            "Quantity: " + part.getQuantity() + "\n" +
                            "Price: " + part.getPrice() + "\n" +
                            "Status: " + part.getStatus() + "\n" +
                            "Date: " + part.getDate() + "\n" +
                            "Comment: " + part.getComment() + "\n" +
                            "Cook Name: " + cookName + "\n" +
                            "Waiter Name: " + waiterName + "\n";
                    content += "=====================\n";
                }
                cursor.close();

                text.setText(content);
            });



        }
    }

    @SuppressLint("SetTextI18n")
    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        String monthsString = getNumeric(monthOfYear + 1);
        String dayString = getNumeric(dayOfMonth);
        dateFrom.setText(dayString + "." + monthsString + "." + year);
    };

    @SuppressLint("SetTextI18n")
    DatePickerDialog.OnDateSetListener b = (view, year, monthOfYear, dayOfMonth) -> {
        String monthsString = getNumeric(monthOfYear + 1);
        String dayString = getNumeric(dayOfMonth);
        dateTo.setText(dayString + "." + monthsString + "." + year);
    };

    public String getNumeric(int val) {
        String valString;
        if (val < 10) {
            valString = "0" + val;
        } else {
            valString = String.valueOf(val);
        }
        return valString;
    }


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
}