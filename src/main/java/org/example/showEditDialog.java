package org.example;

import javax.swing.*;
import java.awt.*;

public class showEditDialog extends JDialog {

    public showEditDialog(){

        setTitle("Редактирование работы");
        setSize(300, 150);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            // Получите новые данные из полей редактирования
            // ...

            // Обновите данные задачи в базе данных
            // ...

            // Закройте диалоговое окно
            setLocationRelativeTo(null);
            setVisible(true);
        });

    }

}
