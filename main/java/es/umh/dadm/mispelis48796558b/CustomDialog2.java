package es.umh.dadm.mispelis48796558b;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CustomDialog2 extends DialogFragment {
    private DialogInterface.OnClickListener positiveButtonListener;
    private DialogInterface.OnClickListener negativeButtonListener;

    /**
     * Crea el dialogo para personalizarlo posteriormente
     * @param title a lanzar
     * @param message a lanzar
     * @param positiveButtonText a recibir y setear
     * @param negativeButtonText a recibir y setear
     * @param positiveListener a recibir y setear
     * @param negativeListener a recibir y setear
     * @return el dialog
     */
    public static CustomDialog2 newInstance(String title, String message, String positiveButtonText, String negativeButtonText,
                                           DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        CustomDialog2 frag = new CustomDialog2();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        args.putString("negativeButtonText", negativeButtonText);

        frag.setArguments(args);
        frag.positiveButtonListener = positiveListener;
        frag.negativeButtonListener = negativeListener;
        return frag;
    }

    /**
     *  Configura un dialogo personalizado dentro de unf ragment y se infla el diseño
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return dialogo creado
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.YourCustomStyle);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.my_alert_dialog_custom, null); // infla el diseño
        builder.setView(view); // aplica el diseño inflado


        TextView messageView = view.findViewById(R.id.tvMensaje);
        messageView.setText(getArguments().getString("message"));


        Button positiveButton = view.findViewById(R.id.btnEditar);
        positiveButton.setText(getArguments().getString("positiveButtonText"));
        positiveButton.setOnClickListener(v -> {
            positiveButtonListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
        });

        Button negativeButton = view.findViewById(R.id.btnCerrar);
        negativeButton.setText(getArguments().getString("negativeButtonText"));
        negativeButton.setOnClickListener(v -> {
            negativeButtonListener.onClick(getDialog(), DialogInterface.BUTTON_NEGATIVE);
        });

        return builder.create();
    }
}
