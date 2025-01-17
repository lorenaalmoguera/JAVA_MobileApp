package es.umh.dadm.mispelis48796558b;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CustomDialog extends DialogFragment {
    private DialogInterface.OnClickListener positiveButtonListener;
    private DialogInterface.OnClickListener negativeButtonListener;

    /**
     * Crea el dialogo para personalizarlo posteriormente
     * @param title titulo a lanzar
     * @param message mensaje a lanzar
     * @param positiveButtonText boton positivo
     * @param negativeButtonText boton negativo
     * @param positiveListener listener del boton positivo
     * @param negativeListener listener del boton negativo
     * @param imageData imagen a mandar
     * @return devuelve el CustomDialog creado
     */
    public static CustomDialog newInstance(String title, String message, String positiveButtonText, String negativeButtonText,
                                           DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, byte[] imageData) {
        CustomDialog frag = new CustomDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        args.putString("negativeButtonText", negativeButtonText);
        args.putByteArray("image", imageData);

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
        View view = inflater.inflate(R.layout.my_dialog_layout, null); // se infla el diseño aquí
        builder.setView(view); // Se declara el diseño


        TextView messageView = view.findViewById(R.id.tvMensaje);
        messageView.setText(getArguments().getString("message"));

        ImageView imageView = view.findViewById(R.id.imageViewDialog);
        byte[] imageBytes = getArguments().getByteArray("image");
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        }

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
