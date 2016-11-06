package su.vexy.vexybot.services;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    public static ReplyKeyboardHide hideKeyboard() {
        return new ReplyKeyboardHide().setSelective(true);
    }

    public static ReplyKeyboardMarkup getLanguagesKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Русский");
        keyboardRow.add("English");

        KeyboardRow keyboardLastRow = new KeyboardRow();
        keyboardLastRow.add("/cancel");
        keyboard.add(keyboardRow);
        keyboard.add(keyboardLastRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
