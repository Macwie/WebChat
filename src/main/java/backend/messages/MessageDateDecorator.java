package backend.messages;

import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MessageDateDecorator extends MessageDecorator {

    private String date;

    public MessageDateDecorator(iMessage message) {
        super(message);
    }

    @Override
    public void show(TextFlow chatBox, TextFlow activeUsers) {
        super.show(chatBox, activeUsers);
        setDate();
        Text t_date = new Text("\t\t\t\t\t"+date);
        t_date.setStyle(" -fx-fill: white;");

        Platform.runLater(() -> {
            chatBox.getChildren().addAll(t_date);

        });
    }

    private void setDate() {

        Date date_D = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date_D);

        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = String.valueOf(calendar.get(Calendar.MINUTE));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

        switch(Integer.parseInt(day)-1) {
            case 1:
                day = "Monday";
                break;
            case 2:
                day = "Tuesday";
                break;
            case 3:
                day = "Wednesday";
                break;
            case 4:
                day = "Thursday";
                break;
            case 5:
                day = "Friday";
                break;
            case 6:
                day = "Saturday";
                break;
            case 7:
                day = "Sunday";
                break;
        }

        date = day+" at "+hour+":"+minutes+"\n";
    }

}
