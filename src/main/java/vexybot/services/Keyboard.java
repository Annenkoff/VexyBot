package vexybot.services;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    public static ReplyKeyboardHide hideKeyboard() {
        return new ReplyKeyboardHide().setSelective(true).setHideKeyboard(true);
    }

    public static ReplyKeyboardMarkup getLanguagesKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        List<String> languages = new ArrayList<>();
        languages.add("Русский");
        languages.add("English");
        for (String language : languages) {
            KeyboardRow row = new KeyboardRow();
            row.add(language);
            keyboard.add(row);
        }

        KeyboardRow row = new KeyboardRow();
        row.add("/cancel");
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
