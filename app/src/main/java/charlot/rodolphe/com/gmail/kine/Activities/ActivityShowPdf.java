package charlot.rodolphe.com.gmail.kine.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import charlot.rodolphe.com.gmail.kine.R;

public class ActivityShowPdf extends Activity {

    public int idPatient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pdf);
        // filePath represent path of Pdf document on storage
        Intent intent = getIntent();

        if (intent != null) {
            idPatient=intent.getIntExtra("idPatient",0);
            String filePath = intent.getStringExtra("filepath");

            File file = new File(filePath);
            // FileDescriptor for file, it allows you to close file when you are
            // done with it
            ParcelFileDescriptor mFileDescriptor = null;
            try {
                mFileDescriptor = ParcelFileDescriptor.open(file,
                        ParcelFileDescriptor.MODE_READ_ONLY);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // PdfRenderer enables rendering a PDF document
            PdfRenderer mPdfRenderer = null;
            try {
                mPdfRenderer = new PdfRenderer(mFileDescriptor);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Open page with specified index
            PdfRenderer.Page mCurrentPage = mPdfRenderer.openPage(0);
            Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(),
                    mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);

            // Pdf page is rendered on Bitmap
            mCurrentPage.render(bitmap, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            // Set rendered bitmap to ImageView (pdfView in my case)

            ImageView pdfView=(ImageView)findViewById(R.id.pdfView);
            pdfView.setImageBitmap(bitmap);

            mCurrentPage.close();
            mPdfRenderer.close();
            try {
                mFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBackPressed(){
        Intent new_intent=new Intent(ActivityShowPdf.this,ActivityListResultat.class);
        new_intent.putExtra("idPatient",idPatient);
        startActivity(new_intent);
    }
}
