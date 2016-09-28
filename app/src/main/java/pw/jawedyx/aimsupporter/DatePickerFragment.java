package pw.jawedyx.aimsupporter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created by Antonio on 14.09.2016.
 */
public class DatePickerFragment extends DialogFragment {
    private int mDay;
    private int mMounth;
    private int mYear;
    private String mConcat;
    private DatePicker dp;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
         dp = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.set_aim_date)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConcat = new StringBuilder().append(dp.getDayOfMonth()).append(".").append(dp.getMonth() + 1).append(".").append(dp.getYear()).toString();
                        ((Button)getActivity().findViewById(R.id.aim_time)).setText(mConcat);

                    }
                }).create();
    }
}
