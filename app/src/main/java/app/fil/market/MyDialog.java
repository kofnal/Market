package app.fil.market;
import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.TextView;

public class MyDialog {
    Activity activity;
    TextView txt_Message;
    public Dialog dialog;
    public MyDialog(Activity activity) {
        this.activity = activity;
    }
    public void showDialog(String message){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_dialog);
        txt_Message = dialog.findViewById(R.id.tvDialogBolee70rub);
        txt_Message.setText(message);
        //if you want to dimiss the dialog
        //dialog.dimiss()
        dialog.show();
    }
    public void dimiss(){
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}