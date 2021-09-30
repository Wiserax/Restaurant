package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant.Fundamentals.Composition;
import com.example.restaurant.Fundamentals.Dish;
import com.example.restaurant.Fundamentals.Product;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.restaurant.MainActivity.db;

public class StockActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        AtomicInteger hash = new AtomicInteger();

        TextView text = findViewById(R.id.itemText);

        EditText productName = findViewById(R.id.name);
        EditText price = findViewById(R.id.date_birth);
        EditText dishName = findViewById(R.id.salary);
        EditText idProduct = findViewById(R.id.masterID2);
        EditText idDish = findViewById(R.id.masterID);
        EditText quantityProd = findViewById(R.id.date_birth2);


        Button addDish = findViewById(R.id.addMaster);
        Button addProduct = findViewById(R.id.addMaster2);
        Button delete = findViewById(R.id.deleteMaster);
        Button update = findViewById(R.id.deleteMaster2);

        Button getProducts = findViewById(R.id.popular_beer);
        Button getDishes = findViewById(R.id.button_getMasters);
        Button getIngredientsByDish = findViewById(R.id.showYearAmount);

        Button buyProducts = findViewById(R.id.button_getMasters3);
        Button addToComp = findViewById(R.id.button_getMasters4);
        Button removeFromComp = findViewById(R.id.button_getMasters5);


        addDish.setOnClickListener(v -> {
            String name_ = dishName.getText().toString();
            String price_ = price.getText().toString();

            if (price_.equals("") || name_.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Заполните недостающие поля имени и цены блюда.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!db.isAlreadyHaveDish(name_).equals("OK")) {
                Toast.makeText(getApplicationContext(),
                        "Элемент уже существует.", Toast.LENGTH_LONG).show();
                return;
            }

            int price1 = Integer.parseInt(price_);

            Dish dish = new Dish(name_, price1);
            db.insertDish(dish);

            destroyASS(productName, price, dishName, idProduct, idDish);

            Toast.makeText(getApplicationContext(), "Элемент успешно добавлен!", Toast.LENGTH_LONG).show();
        });


        addProduct.setOnClickListener(v -> {
            String num = quantityProd.getText().toString();
            String name_ = productName.getText().toString();

            if (name_.equals("") || num.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Заполните недостающее поле имени и кол-ва ингредиента.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!db.isAlreadyHaveProduct(name_).equals("OK")) {
                Toast.makeText(getApplicationContext(),
                        "Элемент уже существует.", Toast.LENGTH_LONG).show();
                return;
            }

            int quantity = Integer.parseInt(num);

            Product product = new Product(quantity, name_);
            db.insertProduct(product);

            destroyASS(productName, price, dishName, idProduct, idDish);

            Toast.makeText(getApplicationContext(), "Элемент успешно добавлен! Количество: " + quantity, Toast.LENGTH_LONG).show();
        });

        update.setOnClickListener(v -> {
            String prName = productName.getText().toString();
            String diName = dishName.getText().toString();

            String price_ = price.getText().toString();
            String quantity = quantityProd.getText().toString();

            String idDish_ = idDish.getText().toString();
            String idProduct_ = idProduct.getText().toString();

            boolean isProductFalse = quantity.equals("") || prName.equals("") || idProduct_.equals("");
            boolean isDishFalse = price_.equals("") || diName.equals("") || idDish_.equals("");


            if ((!isDishFalse && isProductFalse) || (isDishFalse && !isProductFalse)) {
                if (!isDishFalse) {
                    int debugVal = Integer.parseInt(idDish_);
                    Dish item = db.getDishById(debugVal);
                    if (item == null) {
                        Toast.makeText(getApplicationContext(),
                                "Элемента с таким ID не существует.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    int num = Integer.parseInt(price_);

                    item.setName(diName);
                    item.setPrice(num);

                    db.updateDish(item);

                    destroyASS(productName, price, dishName, idProduct, idDish);

                    Toast.makeText(getApplicationContext(),
                            "Элемент успешно изменён!", Toast.LENGTH_LONG).show();
                } else {
                    int debugVal = Integer.parseInt(idProduct_);
                    Product item = db.getProductById(debugVal);
                    if (item == null) {
                        Toast.makeText(getApplicationContext(),
                                "Элемента с таким ID не существует.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    int num = Integer.parseInt(idProduct_);

                    item.setName(diName);
                    item.setQuantity(num);

                    db.updateProduct(item);

                    destroyASS(productName, price, dishName, idProduct, idDish);

                    Toast.makeText(getApplicationContext(),
                            "Элемент успешно изменён!", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Заполните недостающие поля или выберите" +
                                " либо блюдо либо продукт к изменению.", Toast.LENGTH_LONG).show();
            }
        });

        delete.setOnClickListener(v -> {
            String idDish_ = idDish.getText().toString();
            String idProduct_ = idProduct.getText().toString();

            if (!idProduct_.equals("") && idDish_.equals("")) {
                int id = Integer.parseInt(idProduct_);
                Product item = db.getProductById(id);
                if (item == null) {
                    Toast.makeText(getApplicationContext(),
                            "Элемента с таким ID не существует.", Toast.LENGTH_LONG).show();
                    return;
                }

                Cursor cursor = db.selectIngredientsByProduct(id);
                while (cursor.moveToNext()) {
                    int idComp = cursor.getInt(cursor.getColumnIndex("ID_COMPOSITION"));
                    db.deleteComp(idComp);
                }
                cursor.close();
                db.deleteProduct(id);
            } else if (idProduct_.equals("") && !idDish_.equals("")) {

                int id = Integer.parseInt(idDish_);
                Dish item = db.getDishById(id);
                if (item == null) {
                    Toast.makeText(getApplicationContext(),
                            "Элемента с таким ID не существует.", Toast.LENGTH_LONG).show();
                    return;
                }
                Cursor cursor = db.selectFromPartByDish(id);
                if (cursor.moveToNext()) {
                    item.setStatus("Удалено из меню");
                    db.updateDish(item);
                } else {
                    db.deleteDish(id);
                    Cursor cursor1 = db.selectIngredientsByDish(id);
                    while (cursor.moveToNext()) {
                        int idComp = cursor1.getInt(cursor1.getColumnIndex("ID_COMPOSITION"));
                        db.deleteComp(idComp);
                    }
                    cursor1.close();
                }
                cursor.close();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Заполните недостающие поля либо выберите что-то одно к удалению.", Toast.LENGTH_LONG).show();
                return;
            }

            destroyASS(productName, price, dishName, idProduct, idDish);
            Toast.makeText(getApplicationContext(),
                    "Элемент успешно удалён!", Toast.LENGTH_LONG).show();
        });

        getDishes.setOnClickListener(v -> {
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
                        "Price: " + dish.getPrice() + "\n";
                content += "\n\n";
            }
            cursor.close();
            text.setText(content);
        });

        getProducts.setOnClickListener(v -> {
            String content = "";
            Cursor cursor = db.selectFromProducts();
            Product product = new Product();
            while (cursor.moveToNext()) {
                product.setIdProduct(cursor.getInt(cursor.getColumnIndex("ID_PRODUCT")));
                product.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex("QUANTITY")));

                content += "ProductID: " + product.getIdProduct() + "\n" +
                        "Name: " + product.getName() + "\n" +
                        "Quantity: " + product.getQuantity() + "\n" + "\n";
                content += "\n\n";
            }

            cursor.close();
            text.setText(content);
        });


        getIngredientsByDish.setOnClickListener(v -> {
                String dishID = idDish.getText().toString();

                if (dishID.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Заполните недостающие поля.", Toast.LENGTH_LONG).show();
                    return;

                }

                int id = Integer.parseInt(dishID);
                Dish item = db.getDishById(id);
                if (item == null) {
                    Toast.makeText(getApplicationContext(),
                            "Элемента с таким ID не существует.", Toast.LENGTH_LONG).show();
                    return;
                }

                String content = "";
                Cursor cursor = db.selectIngredientsByDish(id);

                while (cursor.moveToNext()){
                    int idProduct_ = cursor.getInt(cursor.getColumnIndex("ID_PRODUCT"));
                    Product product = db.getProductById(idProduct_);
                    content += "ProductID: " + product.getIdProduct() + "\n" +
                            "Name: " + product.getName() + "\n" +
                            "Price: " + product.getQuantity() + "\n" + "\n";
                    content += "\n\n";
                }
            cursor.close();
            text.setText(content);

            destroyASS(productName, price, dishName, idProduct, idDish);
        });

        buyProducts.setOnClickListener(v -> {
            String content = "";
            Cursor cursor = db.selectFromProducts();
            Product product = null;
            while (cursor.moveToNext()) {
                int id = (cursor.getInt(cursor.getColumnIndex("ID_PRODUCT")));
                product = db.getProductById(id);
                product.setQuantity(product.getQuantity() + 10);
                db.updateProduct(product);
            }
            cursor.close();
            Toast.makeText(getApplicationContext(),
                    "Покупка совершена!", Toast.LENGTH_SHORT).show();
            destroyASS(productName, price, dishName, idProduct, idDish);
        });


        addToComp.setOnClickListener(v -> {
            String idDish_ = idDish.getText().toString();
            String idProduct_ = idProduct.getText().toString();

            if (idDish_.equals("") || idProduct_.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Заполните недостающие поля.", Toast.LENGTH_LONG).show();
                return;
            }

            int idPr = Integer.parseInt(idProduct_);
            Product pr = db.getProductById(idPr);
            if (pr == null) {
                Toast.makeText(getApplicationContext(),
                        "Элемента с таким ID не существует.", Toast.LENGTH_SHORT).show();
                return;
            }

            int idDi = Integer.parseInt(idDish_);
            Dish di = db.getDishById(idDi);
            if (di == null) {
                Toast.makeText(getApplicationContext(),
                        "Элемента с таким ID не существует.", Toast.LENGTH_SHORT).show();
                return;
            }

            Composition comp = new Composition(idPr, idDi);

            if (!db.isAlreadyHaveComposition(comp).equals("OK")) {
                Toast.makeText(getApplicationContext(),
                        "Такой продукт уже есть в составе этого блюда.", Toast.LENGTH_SHORT).show();
                return;
            }

            db.insertComp(comp);
            Toast.makeText(getApplicationContext(),
                    "Успешно!", Toast.LENGTH_SHORT).show();
        });


        removeFromComp.setOnClickListener(v -> {
            String idDish_ = idDish.getText().toString();
            String idProduct_ = idProduct.getText().toString();

            if (idDish_.equals("") || idProduct_.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Заполните недостающие поля.", Toast.LENGTH_LONG).show();
                return;
            }

            int idPr = Integer.parseInt(idProduct_);
            Product pr = db.getProductById(idPr);
            if (pr == null) {
                Toast.makeText(getApplicationContext(),
                        "Элемента с таким ID не существует.", Toast.LENGTH_SHORT).show();
                return;
            }

            int idDi = Integer.parseInt(idDish_);
            Dish di = db.getDishById(idDi);
            if (di == null) {
                Toast.makeText(getApplicationContext(),
                        "Элемента с таким ID не существует.", Toast.LENGTH_SHORT).show();
                return;
            }

            Composition comp = db.getCompByProductAndDish(idPr, idDi);
            db.deleteComp(comp.getIdComp());
            Toast.makeText(getApplicationContext(),
                    "Успешно!", Toast.LENGTH_SHORT).show();
        });



        /*showYear.setOnClickListener(v -> {
            String content = "";

            List<Part> parts = new ArrayList<>();
            Cursor cursor = db.selectFromPart();
            while (cursor.moveToNext()) {
                int j = (cursor.getInt(cursor.getColumnIndex("ID_PART")));
                Part part = db.getPartByID(j);
                parts.add(part);
            }
            cursor.close();


            for (int i = 0; i < 12; i++) {
                content += "Выручка в течении " + getMonth(i);
                int money = 0;

                for (Part part : parts) {
                    String input = part.getDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd.MM.yyyy" );
                    LocalDate localDate = LocalDate.parse( input , formatter );
                    int month = localDate.getMonthValue() - 1;

                    if (month == i) {
                        money += part.getPrice();
                    }
                }

                content += ": " + money + "\n\n";
            }
            text.setText(content);
        });
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }*/

    }

    private void destroyASS(EditText productName, EditText price, EditText dishName, EditText idProduct, EditText idDish) {
        price.setText("");
        idProduct.setText("");
        idDish.setText("");
        productName.setText("");
        dishName.setText("");
    }
}