package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant.Fundamentals.Dish;
import com.example.restaurant.Fundamentals.Part;
import com.example.restaurant.Fundamentals.Product;
import com.example.restaurant.Fundamentals.User;
import com.example.restaurant.login.ui.ui.LoginActivity;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.restaurant.MainActivity.db;

public class CookActivity extends AppCompatActivity {


    Button logOutButton;

    Button showStock;
    Button getOrders;
    Button cookOrder;

    TextView text;
    EditText id_;

    int userID;
    int selectedSongId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

        AtomicInteger hash = new AtomicInteger();
        Bundle bundle = getIntent().getExtras();
        User mainUser = null;
        if (bundle != null) {
            hash.set(bundle.getInt("id"));
            mainUser = db.getUserByID(hash.get());
        }
        assert mainUser != null;
        userID = mainUser.getIdUser();


        logOutButton = findViewById(R.id.button_log_out);
        cookOrder = findViewById(R.id.button_goodOrder);
        showStock = findViewById(R.id.button_getOrders4);
        getOrders = findViewById(R.id.button_getOrders);

        text = findViewById(R.id.itemText);
        id_ = findViewById(R.id.id_part);



        logOutButton.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });


        getOrders.setOnClickListener(v -> {
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


        showStock.setOnClickListener(v -> {
            String content = "";
            Cursor cursor = db.selectFromProducts();
            Product product = new Product();
            while (cursor.moveToNext()) {
                product.setIdProduct(cursor.getInt(cursor.getColumnIndex("ID_PRODUCT")));
                product.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex("QUANTITY")));

                content += "ProductID: " + product.getIdProduct() + "\n" +
                        "Name: " + product.getName() + "\n" +
                        "Quantity: " + product.getQuantity()+ "\n" + "\n";
                content += "\n\n";
            }

            cursor.close();
            text.setText(content);
        });

        cookOrder.setOnClickListener(v -> {
            String debugString = id_.getText().toString();
            if (debugString.equals("")) {
                Toast.makeText(getBaseContext(), "Некорректный идентификатор заказа", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = Integer.parseInt(id_.getText().toString());
            Part part = db.getPartByID(id);
            if (part != null) {

                Dish dish = db.getDishById(part.getDishId());
                String str = dish.getStatus();
                if (str != null) {
                    if (dish.getStatus().equals("Удалено из меню")) {
                        Toast.makeText(getBaseContext(), "Данное блюдо больше не готовиться", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Product product = null;
                Cursor cursor = db.selectIngredientsByDish(part.getDishId());
                while (cursor.moveToNext()) {
                    int idProduct = cursor.getInt(cursor.getColumnIndex("ID_PRODUCT"));
                    product = db.getProductById(idProduct);
                    if (product.getQuantity() < part.getQuantity()) {
                        Toast.makeText(getBaseContext(), "Не хаватает продуктов для данного заказа", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        product.setQuantity(product.getQuantity() - part.getQuantity());
                        db.updateProduct(product);
                    }
                }
                cursor.close();

                part.setIdCook(userID);
                Toast.makeText(getBaseContext(), "Вы приготовили заказ", Toast.LENGTH_SHORT).show();

                part.setStatus("Готов");
                db.updatePart(part);
            } else {
                Toast.makeText(getBaseContext(), "Некорректный идентификатор заказа", Toast.LENGTH_SHORT).show();
            }
            id_.setText("");
        });

    }

}