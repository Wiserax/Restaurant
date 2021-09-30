package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant.Fundamentals.Dish;
import com.example.restaurant.Fundamentals.Part;
import com.example.restaurant.Fundamentals.Product;
import com.example.restaurant.Fundamentals.User;
import com.example.restaurant.login.ui.ui.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.restaurant.MainActivity.db;

public class WaiterActivity extends AppCompatActivity {

    Button logOutButton;
    Button showDishes;
    Button whoAdminButton;
    Button partsByWaiter;
    Button acceptOrder;
    Button partsByTable;
    Button showWaiters;

    EditText quantity_;
    EditText id_;

    List<Dish> dishes;
    List<Part> parts;
    List<String> strings;

    int selectedDishId;
    int userID;
    AtomicInteger hash;
    TextView text;

    User mainUser;

    int NUMBER_OF_TABLES = MainActivity.DEFAULT_QUANTITY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        hash = new AtomicInteger();
        Bundle bundle = getIntent().getExtras();
        mainUser = null;
        if (bundle != null) {
            hash.set(bundle.getInt("id"));
            mainUser = db.getUserByID(hash.get());
        }
        if (mainUser == null) {
            Toast.makeText(getBaseContext(), "Ошибка входа", Toast.LENGTH_SHORT).show();
            return;
        }
        userID = mainUser.getIdUser();

        logOutButton = findViewById(R.id.button_log_out);
        showDishes = findViewById(R.id.button_getOrders7);
        whoAdminButton = findViewById(R.id.button_getOrders8);
        partsByWaiter = findViewById(R.id.button_acceptOrder);
        acceptOrder = findViewById(R.id.button_goodOrder);
        partsByTable = findViewById(R.id.button_acceptOrder2);
        showWaiters = findViewById(R.id.button_getOrders10);

        quantity_ = findViewById(R.id.id_part);
        id_ = findViewById(R.id.id_part2);
        text = findViewById(R.id.itemText);

        dishes = new ArrayList<>();
        strings = new ArrayList<>();

        Spinner spinner = findViewById(R.id.spinner);
        // адаптер

        Cursor tmpCursor = db.selectFromDish();
        while (tmpCursor.moveToNext()) {
            strings.add(tmpCursor.getString(tmpCursor.getColumnIndex("NAME")) + " " +
                    tmpCursor.getInt(tmpCursor.getColumnIndex("PRICE")));
        }
        tmpCursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                selectedDishId = position + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        acceptOrder.setOnClickListener(v -> {
            Calendar time = Calendar.getInstance();
            String date = time.get(Calendar.DAY_OF_MONTH) + "." +
                    (time.get(Calendar.MONTH) + 1) +  "." +
                    time.get(Calendar.YEAR);

            Part part = new Part();
            part.setIdDish(selectedDishId);
            part.setIdWaiter(userID);
            part.setStatus("Оформляется");
            part.setDate(date);


            if (quantity_.getText().equals("")){
                Toast.makeText(getBaseContext(), "Введите количество шт", Toast.LENGTH_SHORT).show();
                return;
            }

            int debugVal = Integer.parseInt(quantity_.getText().toString());
            if (debugVal != 0)
                part.setQuantity(debugVal);
            quantity_.setText("");



            int id = Integer.parseInt(id_.getText().toString());
            if (id <= NUMBER_OF_TABLES && id != 0)
                part.setIdTable(id);
            else if (id > NUMBER_OF_TABLES) {
                Toast.makeText(getBaseContext(), "В ресторане всего " + NUMBER_OF_TABLES +
                        "столов!", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(getBaseContext(), "Введите ID стола", Toast.LENGTH_SHORT).show();
                return;
            }
            id_.setText("");

            Dish dish = db.getDishById(part.getDishId());
            String str = dish.getStatus();
            if (str != null) {
                if (dish.getStatus().equals("Удалено из меню")) {
                    Toast.makeText(getBaseContext(), "Данное блюдо больше не готовиться", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //проверочка на лоха
            Cursor cursor = db.selectIngredientsByDish(selectedDishId);
            while (cursor.moveToNext()) {
                int idProduct = cursor.getInt(cursor.getColumnIndex("ID_PRODUCT"));
                Product product = db.getProductById(idProduct);
                if (product.getQuantity() < part.getQuantity()) {
                    Toast.makeText(getBaseContext(), "Не хаватает продуктов для данного заказа", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            cursor.close();

            db.insertPart(part);

            Toast.makeText(getBaseContext(), "Заказ принят в обработку!", Toast.LENGTH_SHORT).show();
        });

        logOutButton.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });


        partsByWaiter.setOnClickListener(v -> {
            String idStr = id_.getText().toString();
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



            String content = "";
            Cursor cursor = db.selectFromPartByWaiter(user.getIdUser());
            Part part = null;
            while (cursor.moveToNext()) {
                hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                part = db.getPartByHashCode(hash.get());

                User cook = db.getUserByID(part.getIdCook());
                String cookName;

                if (cook != null)  cookName = cook.getName();
                else cookName = "-";

                content += "OrderID: " + part.getIdPart() + "\n" +
                        "Dish name: " + part.getDishName() + "\n" +
                        "Quantity: " + part.getQuantity() + "\n" +
                        "Price: " + part.getPrice() + "\n" +
                        "Status: " + part.getStatus() + "\n" +
                        "Date: " + part.getDate() + "\n" +
                        "Comment: " + part.getComment() + "\n" +
                        "Waiter Name: " + user.getName() + "\n" +
                        "Cook Name: " + cookName + "\n";
                content += "=====================\n";
            }
            cursor.close();

            text.setText(content);
            id_.setText("");


            /*AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);

            edittext.setText(part.getComment());

            alert.setTitle("Ваш отзыв к заказу");
            alert.setView(edittext);

            alert.setPositiveButton("OK", (dialog, whichButton) -> {
                //What ever you want to do with the value
                String comment = edittext.getText().toString();
                if (!comment.equals("")) {
                    part.setComment(comment);
                    db.updatePart(part);
                    Toast.makeText(getBaseContext(), "Вы оставили отзыв к заказу!", Toast.LENGTH_SHORT).show();
                }
            });

            alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
                // what ever you want to do with No option.
            });

            alert.show();*/

        });

        partsByTable.setOnClickListener(v -> {
            String idStr = id_.getText().toString();
            if (idStr.equals("")) {
                Toast.makeText(getBaseContext(), "Введите номер стола", Toast.LENGTH_SHORT).show();
                return;
            }

            String content = "";
            Cursor cursor = db.selectFromPartByTable(Integer.parseInt(idStr));
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

        whoAdminButton.setOnClickListener(v -> {
            String content = "";
            User user = db.getAdmin();

            String salary1 = user.getSalary() + "";
            if (user.getSalary() == 0) salary1 = "-";
            content += "UserID: " + user.getIdUser() + "\n" +
                    "Role: " + user.getRole() + "\n" +
                    "Name: " + user.getName()+ "\n" +
                    "Phone: " + user.getPhone() + "\n" +
                    "Salary: " + salary1 + "\n" +
                    "Birth Date: " + user.getBirthDate() + "\n";
            content += "\n\n";

            text.setText(content);
        });


        showDishes.setOnClickListener(v -> {
            String content = "";
            Cursor cursor = db.selectFromDish();
            Dish dish = new Dish();
            while (cursor.moveToNext()) {
                dish.setIdDish(cursor.getInt(cursor.getColumnIndex("ID_DISH")));
                dish.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                dish.setPrice(cursor.getInt(cursor.getColumnIndex("PRICE")));
                dish.setStatus(cursor.getString(cursor.getColumnIndex("STATUS")));

                content += "DishID: " + dish.getIdDish() + "\n" +
                        "Name: " + dish.getName() + "\n" +
                        "Status: " + dish.getStatus() + "\n" +
                        "Price: " + dish.getPrice()+ "\n";
                content += "\n\n";
            }
            cursor.close();
            text.setText(content);
        });

        showWaiters.setOnClickListener(v -> {
            String content = "";
            Cursor cursor = db.selectFromUser();
            User user;
            while (cursor.moveToNext()) {
                hash.set(cursor.getInt(cursor.getColumnIndex("HASH_CODE")));
                user = db.getUserByHashCode(hash.get());

                if (!user.getRole().equals("Waiter")) continue;

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


    }


}