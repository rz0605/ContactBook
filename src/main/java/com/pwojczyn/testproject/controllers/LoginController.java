package com.pwojczyn.testproject.controllers;

import com.pwojczyn.testproject.models.UserSession;
import com.pwojczyn.testproject.models.Utils;
import com.pwojczyn.testproject.models.dao.UserDao;
import com.pwojczyn.testproject.models.dao.impl.UserDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField textLogin, textRejestracja;

    @FXML
    PasswordField textPassword, textPasswordR, textPasswordRSec;

    @FXML
    Button buttonLogin, buttonR;


    private UserSession userSession = UserSession.getInstance();
    private UserDao userDao = new UserDaoImpl();


    private String login;
    private String password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (userSession.getUsername() != null || userSession.getPassword() != null) {
            login = userSession.getUsername();
            textLogin.setText(login);
            password = userSession.getPassword();
            textPassword.setText(password);
        }

        buttonLogin.setOnMouseClicked(e -> tryLogin());
        buttonR.setOnMouseClicked(e -> tryRegister());


    }

    private void tryRegister() {
        String login = textRejestracja.getText();
        String password = textPasswordR.getText();

        if (!checkRegisterData()) {
            return;
        }
        if (userDao.register(login, password)) {
            Utils.createSimpleDialog("Rejestracja", "", "Zarejestrowałeś się poprawnie");
        } else {
            Utils.createSimpleDialog("Rejestracja", "", "Login jest już zajęty");
        }
    }

    private boolean checkRegisterData() {
        String login = textRejestracja.getText();
        String password = textPasswordR.getText();
        String passwordRepeat = textPasswordRSec.getText();

        if (login.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
            Utils.createSimpleDialog("Logowanie", "", "Pola nie mogą być puste");
            return false;
        }
        if (!password.equals(passwordRepeat)) {
            Utils.createSimpleDialog("Logowanie", "", "Hasło musi się zgadzać");

            return false;
        }
        /*
        if (password.length() < 5) {
            Utils.createSimpleDialog("Logowanie", "", "Hasło musi mieć więcej niż 5 zanków");
            return false;
        }
        */
        return true;
    }

    private boolean checkLoginData() {


        login = textLogin.getText();
        password = textPassword.getText();


        if (login.isEmpty() || password.isEmpty()) {
            Utils.createSimpleDialog("Logowanie", "", "Pola nie mogą być puste");
            return false;
        }
        return true;
    }

    private void tryLogin() {


        checkLoginData();
        if (!checkLoginData()) {
            return;
        }

        if (userDao.login(login, password)) {
            userSession.setUsername(login);
            userSession.setPassword(password);
            userSession.setLogedIn(true);
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainView.fxml"));
                Stage stageRoot = (Stage) buttonLogin.getScene().getWindow();
                stageRoot.setScene(new Scene(root, 600, 400));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            userSession.setLogedIn(false);
            Utils.createSimpleDialog("Logowanie", "Twój prywatny błędzik", "Nie udało się zalogować");

        }
    }
}
