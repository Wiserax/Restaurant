package com.example.restaurant.login.ui.ui;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.restaurant.Fundamentals.Composition;
import com.example.restaurant.Fundamentals.Dish;
import com.example.restaurant.Fundamentals.Part;
import com.example.restaurant.Fundamentals.Product;
import com.example.restaurant.CookActivity;
import com.example.restaurant.WaiterActivity;
import com.example.restaurant.Fundamentals.User;
import com.example.restaurant.MainActivity;
import com.example.restaurant.R;

import static com.example.restaurant.MainActivity.db;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button test = findViewById(R.id.test);
        final Button clear = findViewById(R.id.clear);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        test.setEnabled(true);
        clear.setEnabled(true);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }

        });

        clear.setOnClickListener(v -> {
            db.clearDB();
            Toast.makeText(getApplicationContext(),
                    "База данных очищена", Toast.LENGTH_LONG).show();

        });

        test.setOnClickListener(v -> {
            String encoded1 = StringXOR.encode("123");
            User user1 = new User("Admin",
                    "Дмитрий Нагиев",
                    "111111",
                    "mail1@mail.com",
                    200000,
                    "15.04.1975",
                    "admin",
                    encoded1);
            User user2 = new User("Waiter",
                    "Артемий Лебедев",
                    "454545",
                    "mail2@mail.com",
                    20000,
                    "02.06.1989",
                    "waiter1",
                    encoded1);
            User user3 = new User("Cook",
                    "Николай Радионов",
                    "789789",
                    "mail3@mail.com",
                    60000,
                    "07.12.1972",
                    "cook1",
                    encoded1);
            User user4 = new User("Waiter",
                    "Стивен Памп",
                    "123456",
                    "mail4@mail.com",
                    22000,
                    "12.08.1988",
                    "waiter2",
                    encoded1);

            db.insertUser(user1);
            db.insertUser(user2);
            db.insertUser(user3);
            db.insertUser(user4);

            Product product1 = new Product(30, "Фасоль");
            Product product2 = new Product(30, "Майонез");
            Product product3 = new Product(30, "Свинина");
            Product product4 = new Product(30, "Баклажан");
            Product product5 = new Product(30, "Кетчуп");
            Product product6 = new Product(30, "Тесто");

            db.insertProduct(product1);
            db.insertProduct(product2);
            db.insertProduct(product3);
            db.insertProduct(product4);
            db.insertProduct(product5);
            db.insertProduct(product6);

            Dish dish1 = new Dish("Бабурек в фасоли",
                    340);
            Dish dish2 = new Dish("Ткемали с майонезом",
                    220);
            Dish dish3 = new Dish("Сацибели с нежной свининой",
                    520);

            db.insertDish(dish1);
            db.insertDish(dish2);
            db.insertDish(dish3);


            Composition comp1 = new Composition(1, 1);
            Composition comp2 = new Composition(3, 1);
            Composition comp3 = new Composition(6, 1);

            Composition comp4 = new Composition(2, 2);
            Composition comp5 = new Composition(4, 2);

            Composition comp6 = new Composition(3, 3);
            Composition comp7 = new Composition(5, 3);

            db.insertComp(comp1);
            db.insertComp(comp2);
            db.insertComp(comp3);
            db.insertComp(comp4);
            db.insertComp(comp5);
            db.insertComp(comp6);
            db.insertComp(comp7);


            Part part1 = new Part(2, 3, 2,
                    2, 2, "Готов", "23.02.2021");
            Part part2 = new Part(2, 3, 20,
                    3, 1, "Готов", "19.01.2021");
            Part part3 = new Part(4, 3, 17,
                    1, 1, "Оформляется", "12.02.2021");

            db.insertPart(part1);
            db.insertPart(part2);
            db.insertPart(part3);


            Toast.makeText(getApplicationContext(),
                    "Введены тестовые данные. Попробуйте log: admin pass: 123", Toast.LENGTH_LONG).show();
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String login = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();

                String encodedPass = StringXOR.encode(pass);

                User user = MainActivity.db.findUserLoginPass(login, encodedPass);
                if (user != null) {
                    String role = user.getRole();
                    switch (role) {
                        case "Admin": {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("result", 1);
                            startActivity(i);
                            break;
                        }
                        case "Waiter": {
                            Intent i = new Intent(LoginActivity.this, WaiterActivity.class);
                            startActivity(i);
                            break;
                        }
                        case "Cook": {
                            Intent i = new Intent(LoginActivity.this, CookActivity.class);
                            startActivity(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid login/password", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();

                String encodedPass = StringXOR.encode(pass);
                User user = MainActivity.db.findUserLoginPass(login, encodedPass);
                if (user != null) {
                    String role = user.getRole();
                    switch (role) {
                        case "Admin": {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("result", 1);
                            startActivity(i);
                            break;
                        }
                        case "Waiter": {
                            Intent i = new Intent(LoginActivity.this, WaiterActivity.class);
                            i.putExtra("id", user.getIdUser());
                            startActivity(i);
                            break;
                        }
                        case "Cook": {
                            Intent i = new Intent(LoginActivity.this, CookActivity.class);
                            i.putExtra("id", user.getIdUser());
                            startActivity(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid login/password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}