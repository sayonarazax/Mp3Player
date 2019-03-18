package listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SearchListener implements FocusListener {
    private JTextField searchField;
    public SearchListener(JTextField searchField){
        this.searchField = searchField;
    }
    @Override
    public void focusGained(FocusEvent e) {
        if(searchField.getText().trim().equals("Введите название песни")){
            searchField.setText("");
            searchField.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(searchField.getText().trim().equals("")){
            searchField.setText("Введите название песни");
            searchField.setForeground(Color.GRAY);
        }
    }
}
