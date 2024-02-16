package tech.c1ph3rj.text_extraction.home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import tech.c1ph3rj.text_extraction.R;

public class FileUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);


        init();
    }

    private final ActivityResultLauncher<Intent> photoPicker  = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

        }
    });
    void init() {
        try {
            WordRectanglesImageView selectedBitmapView = findViewById(R.id.selectedImageView);
            Bitmap bitmapToProcess = loadImageFromAssets(this);
            selectedBitmapView.setImageBitmap(bitmapToProcess);
            TextRecognizer textRecognizer = TextRecognition.getClient(new TextRecognizerOptions.Builder().build());
            if(bitmapToProcess != null) {
                InputImage image = InputImage.fromBitmap(bitmapToProcess,0);
                textRecognizer.process(image).addOnCompleteListener(task -> {
                    List<Text.TextBlock> textBlockList = task.getResult().getTextBlocks();
                    for(Text.TextBlock eachTextBlock : textBlockList) {
                        for(Text.Line eachLine : eachTextBlock.getLines()) {
                            selectedBitmapView.setWords(eachLine.getElements(),bitmapToProcess.getWidth(), bitmapToProcess.getHeight());
                        }
                    }
                    selectedBitmapView.startDraw();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadImageFromAssets(Context context) {
        try {
            // Open the image file from assets folder
            InputStream inputStream = context.getAssets().open("image_1.jpg");
            // Decode the input stream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // Close the input stream
            inputStream.close();
            // Return the Bitmap
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            // Return null if there's an error loading the image
            return null;
        }
    }
}